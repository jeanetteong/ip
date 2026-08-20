import java.util.Arrays;
import java.util.Scanner;

public class Jude {
    public static void main(String[] args) {
        String line;
        Task[] list = new Task[] {};
        System.out.print("""
                    ____________________________________________________________
                    Hello! I'm Jude
                    What can I do for you?
                    ____________________________________________________________\n
                """);

        while (true) {
            Scanner in = new Scanner(System.in);
            line = in.nextLine();

            // exit
            if (line.equals("bye")) {
                break;
            }

            // print list
            if (line.equals("list")) {
                System.out.println("""
                            ____________________________________________________________
                            Here are the tasks in your list:
                        """);
                int count = 1;
                for (Task task : list) {
                    System.out.printf("    %d.[%s] %s\n", count, task.getStatusIcon(), task.description);
                    count++;
                }
                System.out.println("    ____________________________________________________________");
                continue;
            }

            // unmark tasks
            else if (line.contains("unmark")) {
                int taskNo = Integer.parseInt(line.split(" ")[1]);
                Task selectedTask = list[taskNo - 1];
                selectedTask.unMark();
                System.out.printf("""
                            ____________________________________________________________
                            OK, I've marked this task as not done yet:
                                [%s] %s
                            ____________________________________________________________\n
                        """, selectedTask.getStatusIcon(), selectedTask.description);
                continue;
            }

            // mark tasks as done
            else if (line.contains("mark")) {
                int taskNo = Integer.parseInt(line.split(" ")[1]);
                Task selectedTask = list[taskNo - 1];
                selectedTask.markAsDone();
                System.out.printf("""
                            ____________________________________________________________
                            Nice! I've marked this task as done:
                                [%s] %s
                            ____________________________________________________________\n
                        """, selectedTask.getStatusIcon(), selectedTask.description);
            }

            // add items to list
            else {
                list = Arrays.copyOf(list, list.length + 1);
                list[list.length - 1] = new Task(line);
                System.out.printf("""
                            ____________________________________________________________
                            added: %s
                            ____________________________________________________________\n
                        """, line);
            }
        }

        System.out.print("""
                    ____________________________________________________________
                    Bye. Hope to see you again soon!
                    ____________________________________________________________

                """);
    }
}
