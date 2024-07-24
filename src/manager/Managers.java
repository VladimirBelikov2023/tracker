package manager;

import java.nio.file.Path;
import java.util.Scanner;

public class Managers {
    public static TaskManager getDefaultTaskManager(final Path path, final Scanner scanner) {
        return FileBackedTaskManager.load(path, scanner);
    }

    public static HistoryManager getDefaultHistoryManager() {
        return new InMemoryHistoryManager();
    }
}
