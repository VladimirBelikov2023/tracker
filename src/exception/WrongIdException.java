package exception;

import java.util.Scanner;

public class WrongIdException extends Exception {
    Scanner scanner;

    public WrongIdException(String message, Scanner scanner) {
        super(message);
        this.scanner = scanner;
    }

    public int changeId() {
        System.out.println("Введите правильый id");
        return Integer.parseInt(scanner.nextLine());
    }
}
