package presentation;

import java.util.Optional;

public class ErrorHandling {
    private ErrorHandling() {}

    public static void tryOrShowExceptionMessage(CheckedRunnable code, ApplicationWindow window) {
        try {
            code.run();
        } catch (Exception e) {
            showMessage(e.getMessage(), window);
        }
    }

    public static void tryOrShowCustomMessage(CheckedRunnable code, String message, ApplicationWindow window) {
        try {
            code.run();
        } catch (Exception e) {
            showMessage(message, window);
        }
    }

    public static <T> Optional<T> tryReturnOrShowCustomMessage(CheckedSupplier<T> code, String message, ApplicationWindow window) {
        try {
            return Optional.ofNullable(code.get());
        } catch (Exception e) {
            showMessage(message, window);
            return Optional.empty();
        }
    }

    public static void showMessage(String message, ApplicationWindow window) {
        window.showError(message);
    }

    @FunctionalInterface
    public static interface CheckedRunnable {
        public void run() throws Exception;
    }

    @FunctionalInterface
    public static interface CheckedSupplier<T> {
        public T get() throws Exception;
    }

}
