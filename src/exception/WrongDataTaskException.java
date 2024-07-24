package exception;

import java.util.Scanner;

public class WrongDataTaskException extends Exception {
    Scanner scanner;

    public WrongDataTaskException(String message, Scanner scanner) {
        super(message);
        this.scanner = scanner;
    }

    public String changeName() {
        System.out.println("Введите правильый название");
        return scanner.nextLine();
    }

    public String changeDescription() {
        System.out.println("Введите правильый описание");
        return scanner.nextLine();
    }
}
