package tracker;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CommandInterface {
    private Scanner scanner = new Scanner(System.in);
    private String command;
    private static List<Student> students = new ArrayList<>();
    CredentialsValidator validator = new CredentialsValidator();

    public CommandInterface() {
    }

    public static List<Student> getStudents() {
        return students;
    }

    public void askForCommand() {
        this.command = scanner.nextLine().toLowerCase();
    }
    
    public String evaluateCommand() {
        if (this.command.isBlank()) {
            return "No input";
        } else if (this.command.equals("back")) {
            return "Enter 'exit' to exit the program.";
        } else if (this.command.equals("exit")) {
            return "Bye!";
        } else if (this.command.equals("add students")){
            System.out.println("Enter student credentials or 'back' to return:");
            while (true) {
                String studentCredentials = scanner.nextLine();
                if (studentCredentials.equals("back")) {
                    break;
                }
                switch (validator.validateCredentials(studentCredentials)) {
                    case CredentialsValidator.CredentialsStatus.INCORRECT_CREDENTIALS:
                        System.out.println("Incorrect credentials.");
                        continue;
                    case CredentialsValidator.CredentialsStatus.INCORRECT_FIRST_NAME:
                        System.out.println("Incorrect first name.");
                        continue;
                    case CredentialsValidator.CredentialsStatus.INCORRECT_LAST_NAME:
                        System.out.println("Incorrect last name.");
                        continue;
                    case CredentialsValidator.CredentialsStatus.INCORRECT_EMAIL:
                        System.out.println("Incorrect email.");
                        continue;
                    case CredentialsValidator.CredentialsStatus.DUPLICATED_EMAIL:
                        System.out.println("This email is already taken.");
                        continue;
                    case CredentialsValidator.CredentialsStatus.SUCCESS:
                        System.out.println("The student has been added");
                        String[] credentialsToEvaluate = validator.splitCredentials(studentCredentials);
                        String firstName = credentialsToEvaluate[0];
                        String lastName = credentialsToEvaluate[1];
                        String email = credentialsToEvaluate[2];
                        Student student = new Student(firstName, lastName, email);
                        this.students.add(student);
                }
            }
            return String.format("Total %d students have been added.", students.size());
        } else if (this.command.equals("list")) {
            if (students.isEmpty()) {
                return "No students found";
            }
            StringBuilder result = new StringBuilder();
            result.append("Students:\n");
            for (Student student : students) {
                result.append(student.getId()).append("\n");
            }
            return result.toString();
        } else {
            return "Error: unknown command!";
        }
    }


}
