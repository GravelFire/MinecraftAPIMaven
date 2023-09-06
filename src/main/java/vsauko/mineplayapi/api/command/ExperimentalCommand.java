package vsauko.mineplayapi.api.command;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import vsauko.mineplayapi.MinePlayAPI;
import vsauko.mineplayapi.api.BukkitAPI;
import vsauko.mineplayapi.api.command.BaseCommand;
import vsauko.mineplayapi.api.command.BaseMegaCommand;

public abstract class ExperimentalCommand<S extends CommandSender>
        extends BaseMegaCommand<S>
        implements TabCompleter {

    @SuppressWarnings("all")
    public ExperimentalCommand(String command, String... aliases) {
        super(command, aliases);

        commandArguments.clear();
        Arrays.stream(getClass().getDeclaredMethods())
                .filter(method -> method.getDeclaredAnnotation(CommandArgument.class) != null)
                .filter(method -> method.getParameterCount() == 1 && (Arrays.equals(method.getParameterTypes(), new Class<?>[]{CommandContext.class})))

                .forEach(method -> {
                    method.setAccessible(true);

                    commandArguments.put(method.getName().toLowerCase(), method);

                    for (String alias : method.getDeclaredAnnotation(CommandArgument.class).aliases()) {
                        commandArguments.put(alias.toLowerCase(), method);
                    }
                });

        setNoArgumentMessage(this::onUsage);

        // Костыль, но ворк же ))0
        Bukkit.getScheduler().runTaskLater(MinePlayAPI.getPlugin(MinePlayAPI.class),
                () -> ((BaseCommand<S>) BukkitAPI.COMMAND_MANAGER.COMMAND_MAP.getCommand(command)).setTabCompleter(this), 20);
    }

    protected abstract TabCompleteContext onTabComplete(@NonNull S sender);

    @SneakyThrows
    @Override
    public void onExecute(S commandSender, String[] args) {
        if (args.length == minimalArgsCount) {
            onUsage(commandSender);
            return;
        }

        String label = args[minimalArgsCount].toLowerCase();
        Method argumentMethodMethod = commandArguments.get(args[minimalArgsCount].toLowerCase());

        if (argumentMethodMethod != null) {
            CommandContext<S> context = CommandContext.create(commandSender, label, Arrays.copyOfRange(args, minimalArgsCount + 1, args.length));
            argumentMethodMethod.invoke(this, context);

        } else {

            noArgumentMessage.accept(commandSender);
        }
    }

    @SuppressWarnings("all")
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length <= 1) {
            return commandArguments.keySet()
                    .stream()
                    .filter(s -> s.toLowerCase().startsWith(args.length > 0 ? args[0].toLowerCase() : ""))
                    .collect(Collectors.toList());
        }

        // Initialize context.
        TabCompleteContext context = onTabComplete((S) sender);
        Method currentMethod = commandArguments.get(args[0].toLowerCase());

        if (currentMethod == null) {
            return null;
        }

        TabCompleteContext.Suggestions<S> suggestions = context.getSuggestionsMap(currentMethod.getName(), args.length - 2);

        if (suggestions.suggestionsFunction != null && ((suggestions.canComplete != null && suggestions.canComplete.test((S) sender)) || suggestions.canComplete == null)) {
            return new ArrayList<>(suggestions.suggestionsFunction.apply( args[args.length - 1] ));
        }

        return null;
    }


    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    public static class CommandContext<S extends CommandSender> {

        public static <S extends CommandSender> CommandContext<S> create(S sender, String executedLabel, String[] arguments) {
            return new CommandContext<>(sender, executedLabel, arguments);
        }


        @Getter
        @NonNull
        S sender;

        @Getter
        @NonNull
        String executedLabel;

        @NonNull
        String[] arguments;


        public boolean argumentsIsEmpty() {
            return arguments.length == 0;
        }
        
        public int argumentsLength() {
            return arguments.length;
        }

        public Stream<String> arguments() {
            return Arrays.stream(arguments);
        }


        public String firstArgument() {
            return argument(0);
        }

        public String secondArgument() {
            return argument(1);
        }

        public String argument(int index) {
            return arguments[index];
        }


        public <T> T map(int argumentIndex, Function<String, T> mappingFunction) {
            return mappingFunction.apply(argument(argumentIndex));
        }
    }

    @Value(staticConstructor = "newContext")
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    public static class TabCompleteContext {

        @NonNull
        Table<String, Integer, Suggestions<?>> suggestionsByArgumentsTable = HashBasedTable.create();

        @SuppressWarnings("all")
        public <S extends CommandSender> Suggestions<S> getSuggestionsMap(@NonNull String method, int argument) {

            if (suggestionsByArgumentsTable.contains(method.toLowerCase(), argument)) {
                return (Suggestions<S>) suggestionsByArgumentsTable.get(method.toLowerCase(), argument);
            }

            Suggestions<S> suggestions = new Suggestions<>(this);
            suggestionsByArgumentsTable.put(method.toLowerCase(), argument, suggestions);

            return suggestions;
        }

        @FieldDefaults(level = AccessLevel.PRIVATE)
        @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
        public static class Suggestions<S extends CommandSender> {

            @NonNull
            TabCompleteContext context;

            Function<String, Collection<String>> suggestionsFunction;
            Predicate<S> canComplete;


            public Suggestions<S> setCanComplete(@NonNull Predicate<S> canComplete) {
                this.canComplete = canComplete;
                return this;
            }

            public Suggestions<S> setSuggestions(@NonNull Function<String, Collection<String>> suggestionsFunction) {
                this.suggestionsFunction = suggestionsFunction;
                return this;
            }

            public Suggestions<S> setSuggestion(@NonNull Function<String, String> suggestion) {
                this.suggestionsFunction = (input -> Collections.singletonList(suggestion.apply(input)));
                return this;
            }


            public final TabCompleteContext toContext() {
                return context;
            }
        }
    }

}
