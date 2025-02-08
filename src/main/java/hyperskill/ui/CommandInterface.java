package hyperskill.ui;

import hyperskill.core.CredentialsValidator;
import hyperskill.entity.Student;

import java.lang.reflect.Array;
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
        } else if (this.command.equals("add points")) {
            System.out.println("Enter an id and points or 'back' to return:");
            while (true) {
                String studentPoints = scanner.nextLine();
                if (studentPoints.equals("back")) {
                    return "";
                }

                List<String> courseProgressArray = List.of(studentPoints.split(" "));
                if (courseProgressArray.size() != 5) {
                    System.out.println("Incorrect points format");
                    continue;
                }
                boolean isIdFound = false;
                outerloop:
                for (Student student : students) {
                    if (student.getId().equals(courseProgressArray.get(0))) {
                        isIdFound = true;
                        ArrayList<Integer> courseProgress = new ArrayList<>(4);
                        for (int i = 1; i < courseProgressArray.size(); i++) {

                            try {
                                if (Integer.parseInt(courseProgressArray.get(i)) < 0) {
                                    System.out.println("Incorrect points format");
                                    continue outerloop;
                                }
                                courseProgress.add(Integer.parseInt(courseProgressArray.get(i)));
                            } catch (NumberFormatException e) {
                                System.out.println("Incorrect points format");
                                continue outerloop;
                            }
                        }
                        student.setCourseProgress(courseProgress);
                        System.out.println("Points updated");
                    }
                }
                if (!isIdFound) {
                    System.out.printf("No student is found for id=%s\n", courseProgressArray.get(0));
                }
            }
        } else if (this.command.equals("find")) {
            System.out.println("Enter an id or 'back' to return:");
            while (true) {
                String id = scanner.nextLine();
                if (id.equals("back")) {
                    return "";
                }
                Student studentFound = null;
                for (Student student : students) {
                    if (student.getId().equals(id)) {
                        studentFound = student;
                    }
                }
                if (studentFound == null) {
                    System.out.printf("No student is found for id=%s\n", id);
                } else {
                    System.out.println(studentFound.printStudentCourseInfo(id));
                }

            }
        } else if (this.command.equals("statistics")) {
            System.out.println("Type the name of a course to see details or 'back' to quit:\n");
            String courseName;
            while (true) {
                courseName = scanner.nextLine();
                if (courseName.equals("back")) {
                    return "";
                }
                getStatistics(courseName);
            }
        } else {
            return "Error: unknown command!";
        }
    }


    public boolean getStatistics(String courseName) {
        System.out.printf("\nMost Popular: %s\n" +
                "Least popular: %s\n)" +
                "Highest activity: %s\n" +
                "Lowest activity: %s\n",
                "Easiest course: %s\n" +
                "Hardest course: %s\n");
        return false;
    }
}
