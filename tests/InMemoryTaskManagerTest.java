import manager.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;

class InMemoryTaskManagerTest extends TaskManagerTest {
    @BeforeEach
    public void beforeEach(){
        taskManager = new InMemoryTaskManager();
    }
}