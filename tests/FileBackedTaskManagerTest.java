import manager.FileBackedTaskManager;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;
import java.nio.file.Path;
import java.util.Scanner;

class FileBackedTaskManagerTest extends TaskManagerTest {
    public static final String HOME = System.getProperty("user.dir");
    public static final Path PATH = Path.of(HOME + File.separator + "resources" + File.separator + "data.txt");
    @BeforeEach
    public void beforeEach(){
        taskManager = new FileBackedTaskManager(PATH, new Scanner(System.in));
    }

}