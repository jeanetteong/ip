import java.util.Arrays;
import java.util.Scanner;

public class Jude {
    public static void main(String[] args) {
        String line;
        Task[] tasks = new Task[] {};
        System.out.println("""
                    ____________________________________________________________
                    Hello! I'm Jude
                    What can I do for you?
                    ____________________________________________________________
                """);

        while (true) {
            Scanner in = new Scanner(System.in);
            line = in.nextLine();
            String action = line.split(" ")[0];

            // exit
            if (action.equals("bye")) {
                break;
            }

            // print tasks
            if (action.equals("list")) {
                System.out.println("""
                            ____________________________________________________________
                            Here are the tasks in your list:
                        """);
                int count = 1;
                for (Task task : tasks) {
                    System.out.printf("    %d. %s\n", count, task.toString());
                    count++;
                }
                System.out.println("    ____________________________________________________________");
                continue;
            }

            // unmark tasks
            else if (action.equals("unmark")) {
                int taskNo = Integer.parseInt(line.split(" ")[1]);
                Task selectedTask = tasks[taskNo - 1];
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
            else if (action.equals("mark")) {
                int taskNo = Integer.parseInt(line.split(" ")[1]);
                Task selectedTask = tasks[taskNo - 1];
                selectedTask.markAsDone();
                System.out.printf("""
                            ____________________________________________________________
                            Nice! I've marked this task as done:
                                [%s] %s
                            ____________________________________________________________\n
                        """, selectedTask.getStatusIcon(), selectedTask.description);
            }

            // todo, event, deadline tasks
            else {
                tasks = Arrays.copyOf(tasks, tasks.length + 1);

                // add todo items to tasks
                if (action.equals("todo")) {
                    String description = line.substring(5);
                    tasks[tasks.length - 1] = new Todo(description);
                }

                // add deadline items to tasks
                else if (action.equals("deadline")) {
                    String description = line.substring(9, line.indexOf(" /by"));
                    String by = line.substring(line.indexOf("/by ") + 4);
                    tasks[tasks.length - 1] = new Deadline(description, by);
                }

                System.out.printf("""
                            ____________________________________________________________
                            Got it. I've added this task:
                                %s
                            Now you have %d tasks in the list.
                            ____________________________________________________________\n
                        """, tasks[tasks.length - 1].toString(), tasks.length);

            }
        }

        System.out.println("""
                    ____________________________________________________________
                    Bye. Hope to see you again soon!
                    ____________________________________________________________

                """);
    }
}
