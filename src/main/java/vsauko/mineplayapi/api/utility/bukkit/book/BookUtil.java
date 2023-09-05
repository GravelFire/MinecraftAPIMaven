package vsauko.mineplayapi.api.utility.bukkit.book;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import vsauko.mineplayapi.api.customevents.CustomBookOpenEvent;
import vsauko.mineplayapi.api.customevents.CustomBookOpenEvent.Hand;

public final class BookUtil {
    private static final boolean canTranslateDirectly;

    static {
        boolean success = true;
        try {
            ChatColor.BLACK.asBungee();
        } catch (NoSuchMethodError e) {
            success = false;
        }
        canTranslateDirectly = success;
    }


    @SuppressWarnings("deprecation")
    public static void openPlayer(Player p, ItemStack book) {
        CustomBookOpenEvent event = new CustomBookOpenEvent(p, Hand.MAIN_HAND, book);
        Bukkit.getPluginManager().callEvent(event);
        if(event.isCancelled())
            return;
        p.closeInventory();
        ItemStack hand = p.getItemInHand();

        p.setItemInHand(event.getBook());
        p.updateInventory();

        NmsBookHelper.openBook(p, event.getBook(), event.getHand() == CustomBookOpenEvent.Hand.OFF_HAND);

        p.setItemInHand(hand);
        p.updateInventory();
    }

    public static BookBuilder writtenBook() {
        return new BookBuilder(new ItemStack(Material.WRITTEN_BOOK));
    }

    public static class BookBuilder {
        private final BookMeta meta;
        private final ItemStack book;

        public BookBuilder(ItemStack book) {
            this.book = book;
            this.meta = (BookMeta)book.getItemMeta();
        }

        public BookBuilder title(String title) {
            meta.setTitle(title);
            return this;
        }

        public BookBuilder author(String author) {
            meta.setAuthor(author);
            return this;
        }

        public BookBuilder pagesRaw(String... pages) {
            meta.setPages(pages);
            return this;
        }

        public BookBuilder pagesRaw(List<String> pages) {
            meta.setPages(pages);
            return this;
        }

        public BookBuilder pages(BaseComponent[]... pages) {
            NmsBookHelper.setPages(meta, pages);
            return this;
        }

        public BookBuilder pages(List<BaseComponent[]> pages) {
            NmsBookHelper.setPages(meta, pages.toArray(new BaseComponent[0][]));
            return this;
        }

        public BookBuilder generation(BookMeta.Generation generation) {
            meta.setGeneration(generation);
            return this;
        }

        public ItemStack build() {
            book.setItemMeta(meta);
            return book;
        }
    }
    public static class PageBuilder {
        private final List<BaseComponent> text = new ArrayList<>();

        public PageBuilder add(String text) {
            this.text.add(TextBuilder.of(text).build());
            return this;
        }

        public PageBuilder add(BaseComponent component) {
            this.text.add(component);
            return this;
        }

        public PageBuilder add(BaseComponent... components) {
            this.text.addAll(Arrays.asList(components));
            return this;
        }

        public PageBuilder add(Collection<BaseComponent> components) {
            this.text.addAll(components);
            return this;
        }

        public PageBuilder newLine() {
            this.text.add(new TextComponent("\n"));
            return this;
        }

        public BaseComponent[] build() {
            return text.toArray(new BaseComponent[0]);
        }

        public static PageBuilder of(String text) {
            return new PageBuilder().add(text);
        }

        public static PageBuilder of(BaseComponent text) {
            return new PageBuilder().add(text);
        }

        public static PageBuilder of(BaseComponent... text) {
            PageBuilder res = new PageBuilder();
            for(BaseComponent b : text)
                res.add(b);
            return res;
        }
    }

    @Setter
    @Getter
    @Accessors(fluent = true, chain = true)
    public static class TextBuilder {
        private String text = "";
        private ClickAction onClick = null;
        private HoverAction onHover = null;
        private ChatColor color = ChatColor.BLACK;

        @Setter(AccessLevel.NONE)//We're overwriting it
        private ChatColor[] style;

        public TextBuilder color(ChatColor color) {
            if(color != null && !color.isColor())
                throw new IllegalArgumentException("Argument isn't a color!");
            this.color = color;
            return this;
        }

        public TextBuilder style(ChatColor... style) {
            for(ChatColor c : style)
                if(!c.isFormat())
                    throw new IllegalArgumentException("Argument isn't a style!");
            this.style = style;
            return this;
        }

        public BaseComponent build() {
            TextComponent res = new TextComponent(text);
            if(onClick != null)
                res.setClickEvent(new ClickEvent(onClick.action(), onClick.value()));
            if(onHover != null)
                res.setHoverEvent(new HoverEvent(onHover.action(), onHover.value()));
            if(color != null) {
                if (canTranslateDirectly)
                    res.setColor(color.asBungee());
                else
                    res.setColor(net.md_5.bungee.api.ChatColor.getByChar(color.getChar()));
            }
            if(style != null) {
                for(ChatColor c : style) {
                    switch (c) {
                        case MAGIC:
                            res.setObfuscated(true);
                            break;
                        case BOLD:
                            res.setBold(true);
                            break;
                        case STRIKETHROUGH:
                            res.setStrikethrough(true);
                            break;
                        case UNDERLINE:
                            res.setUnderlined(true);
                            break;
                        case ITALIC:
                            res.setItalic(true);
                            break;
                    }
                }
            }
            return res;
        }

        public static TextBuilder of(String text) {
            return new TextBuilder().text(text);
        }
    }

    /**
     * A class representing the actions a client can do when a component is clicked
     */
    public interface ClickAction {
        ClickEvent.Action action();

        String value();

        static ClickAction runCommand(String command) {
            return new SimpleClickAction(ClickEvent.Action.RUN_COMMAND, command);
        }

        @Deprecated
        static ClickAction suggestCommand(String command) {
            return new SimpleClickAction(ClickEvent.Action.SUGGEST_COMMAND, command);
        }

        static ClickAction openUrl(String url) {
            if(url.startsWith("http://") || url.startsWith("https://"))
                return new SimpleClickAction(ClickEvent.Action.OPEN_URL, url);
            else
                throw new IllegalArgumentException("Invalid url: \"" + url + "\", it should start with http:// or https://");
        }

        static ClickAction changePage(int page) {
            return new SimpleClickAction(ClickEvent.Action.CHANGE_PAGE, Integer.toString(page));
        }

        @Getter
        @Accessors(fluent = true)
        @RequiredArgsConstructor
        class SimpleClickAction implements ClickAction {
            private final ClickEvent.Action action;
            private final String value;
        }
    }

    public interface HoverAction {
        HoverEvent.Action action();
        BaseComponent[] value();

        static HoverAction showText(BaseComponent... text) {
            return new SimpleHoverAction(HoverEvent.Action.SHOW_TEXT, text);
        }

        static HoverAction showText(String text) {
            return new SimpleHoverAction(HoverEvent.Action.SHOW_TEXT, new TextComponent(text));
        }

        static HoverAction showItem(BaseComponent... item) {
            return new SimpleHoverAction(HoverEvent.Action.SHOW_ITEM, item);
        }

        static HoverAction showItem(ItemStack item) {
            return new SimpleHoverAction(HoverEvent.Action.SHOW_ITEM, NmsBookHelper.itemToComponents(item));
        }

        static HoverAction showEntity(BaseComponent... entity) {
            return new SimpleHoverAction(HoverEvent.Action.SHOW_ENTITY, entity);
        }

        static HoverAction showEntity(UUID uuid, String type, String name) {
            return new SimpleHoverAction(HoverEvent.Action.SHOW_ENTITY,
                    NmsBookHelper.jsonToComponents(
                            "{id:\"" + uuid + "\",type:\"" + type + "\"name:\"" + name + "\"}"
                    )
            );
        }

        static HoverAction showEntity(Entity entity) {
            return showEntity(entity.getUniqueId(), entity.getType().getName(), entity.getName());
        }

        static HoverAction showAchievement(String achievementId) {
            return new SimpleHoverAction(HoverEvent.Action.SHOW_ACHIEVEMENT, new TextComponent("achievement." + achievementId));
        }

        static HoverAction showStatistic(String statisticId) {
            return new SimpleHoverAction(HoverEvent.Action.SHOW_ACHIEVEMENT, new TextComponent("statistic." + statisticId));
        }

        @Getter
        @Accessors(fluent = true)
        class SimpleHoverAction implements HoverAction {
            private final HoverEvent.Action action;
            private final BaseComponent[] value;

            public SimpleHoverAction(HoverEvent.Action action, BaseComponent... value) {
                this.action = action;
                this.value = value;
            }
        }
    }

}
