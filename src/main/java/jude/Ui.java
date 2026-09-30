package jude;

import java.util.Scanner;

public class Ui {
    private Scanner in;
    private static final String DIVIDER = "____________________________________________________________";

    public Ui() {
        this.in = new Scanner(System.in);
    }

    public void showWelcome() {
        String banner = """
                     _           _
                    | |_   _  __| | ___
                 _  | | | | |/ _` |/ _ \\
                | |_| | |_| | (_| |  __/
                 \\___/ \\__,_|\\__,_|\\___|
                """;
        System.out.println(banner);
        showLine();
        System.out.println("Hello! I'm Jude\nWhat can I do for you?");
        showLine();
        System.out.println();
    }

    public String readCommand() {
        return in.nextLine();
    }

    public void showLine() {
        System.out.println(DIVIDER);
    }

    public void showMessage(String message) {
        showLine();
        System.out.println(message);
        showLine();
        System.out.println();
    }

    public void showError(String message) {
        showMessage(message);
    }

    public void close() {
        in.close();
    }
}