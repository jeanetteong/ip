import java.util.Scanner;
public class Jude {
    public static void main(String[] args) {
        String line;
        System.out.print("""
                ____________________________________________________________
                Hello! I'm Jude
                What can I do for you?
                ____________________________________________________________\n
            """);
        
        while (true){
        Scanner in = new Scanner(System.in);
        line = in.nextLine();
        if (line.equals("bye")){
            break;
        }
        System.out.printf("""
                    ____________________________________________________________
                    %s
                    ____________________________________________________________\n
                """,line);
        }
        
        System.out.print("""
                    ____________________________________________________________
                    Bye. Hope to see you again soon!
                    ____________________________________________________________    
            
                """);
    }
}
