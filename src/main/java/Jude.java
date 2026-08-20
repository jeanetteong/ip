import java.util.Arrays;
import java.util.Scanner;

public class Jude {
    public static void main(String[] args) {
        String line;
        String[] list = new String[]{};
        System.out.print("""
                    ____________________________________________________________
                    Hello! I'm Jude
                    What can I do for you?
                    ____________________________________________________________\n
                """);

        while (true) {
            Scanner in = new Scanner(System.in);
            line = in.nextLine();
            if (line.equals("bye")) {
                break;
            }
            if (line.equals("list")) {
                System.out.println("    ____________________________________________________________");
                int count = 1;
                for (String task:list){
                    System.out.printf("    %d. %s\n",count,task);
                    count++;
                }
                System.out.println("    ____________________________________________________________");
                continue;
            }
            list = Arrays.copyOf(list, list.length+1);
            list[list.length-1] = line;
            System.out.printf("""
                        ____________________________________________________________
                        added: %s
                        ____________________________________________________________\n
                    """, line);
        }

        System.out.print("""
                    ____________________________________________________________
                    Bye. Hope to see you again soon!
                    ____________________________________________________________

                """);
    }
}
