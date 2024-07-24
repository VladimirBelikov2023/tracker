package exception;

import java.util.Scanner;

public class WrongIdEpicException extends WrongIdException{

    public WrongIdEpicException(String message, Scanner scanner) {
        super(message, scanner);
    }
}
