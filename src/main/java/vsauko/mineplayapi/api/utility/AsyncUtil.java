package vsauko.mineplayapi.api.utility;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Supplier;

@UtilityClass
public class AsyncUtil {

    public final ScheduledExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor();


    /**
     * Выполнить руннейбл в аснхронном потоке
     *
     * @param runnable - асинхронный руннейбл
     */
    public void submitAsync(Runnable runnable) {
        EXECUTOR_SERVICE.submit(runnable);
    }

    /**
     * Выполнить руннейбл в аснхронном потоке
     */
    public void submitThrowsAsync(ThrowableAsynchronousCommand throwableAsynchronousCommand) {
        submitAsync(() -> {
            try {
                throwableAsynchronousCommand.submitCommand();
            }

            catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    /**
     * Создать объект в асинхронном потоке
     *
     * @param futureSupplier - обработчик создания объекта
     */
    @SneakyThrows
    public <T> T supplyAsyncFuture(Supplier<T> futureSupplier) {
        CompletableFuture<T> completableFuture
                = CompletableFuture.supplyAsync(futureSupplier);

        return completableFuture.get();
    }

    public interface ThrowableAsynchronousCommand {

        void submitCommand() throws Exception;
    }

}
