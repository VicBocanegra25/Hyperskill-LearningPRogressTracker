package tracker;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class CommandInterface {
    private Scanner scanner = new Scanner(System.in);
    private String command;
    private static List<Student> students = new ArrayList<>();
    CredentialsValidator validator = new CredentialsValidator();
    NotificationService notificationService = new NotificationService();

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
            while (true) {
                if (!getStatistics()) {
                    return "";
                }
            }
        } else if (this.command.equals("notify")) {
            notificationService.notifyStudents(students);
            return "";
        } else {
            return "Error: unknown command!";
        }
    }


    /**
     * Displays general statistics about the courses and then
     * allows the user to view top learners for a specific course.
     */
    private boolean getStatistics() {
        System.out.println("Type the name of a course to see details or 'back' to quit:");
        printGeneralStatistics();
        while (true) {
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("back")) {
                return false;
            }
            if (isValidCourseName(input)) {
                printTopLearnersForCourse(input);
            } else {
                System.out.println("Unknown course.");
            }
        }
    }

    /**
     * Prints overall statistics for each course.
     */
    private void printGeneralStatistics() {
        List<Course> courses = getCoursesList();
        // If no data exists (i.e. no enrolled students or submissions) then show "n/a"
        if (courses.isEmpty()) {
            System.out.println("Most popular: n/a");
            System.out.println("Least popular: n/a");
            System.out.println("Highest activity: n/a");
            System.out.println("Lowest activity: n/a");
            System.out.println("Easiest course: n/a");
            System.out.println("Hardest course: n/a");
            return;
        }

        // Most and least popular (by enrolled students)
        int maxEnrolled = courses.stream().mapToInt(Course::getEnrolledStudents).max().orElse(0);
        int minEnrolled = courses.stream().mapToInt(Course::getEnrolledStudents).min().orElse(0);
        List<String> mostPopular = new ArrayList<>();
        List<String> leastPopular = new ArrayList<>();
        for (Course c : courses) {
            if (c.getEnrolledStudents() == maxEnrolled && maxEnrolled > 0) {
                mostPopular.add(c.getName());
            }
        }
        for (Course c : courses) {
            if (c.getEnrolledStudents() == minEnrolled && c.getEnrolledStudents() > 0 && !mostPopular.contains(c.getName())) {
                leastPopular.add(c.getName());
            }
        }
        System.out.println("Most popular: " + (mostPopular.isEmpty() ? "n/a" : String.join(", ", mostPopular)));
        System.out.println("Least popular: " + (leastPopular.isEmpty() ? "n/a" : String.join(", ", leastPopular)));

        // Highest and lowest activity (by submissions)
        int maxSubmissions = courses.stream().mapToInt(Course::getSubmissions).max().orElse(0);
        int minSubmissions = courses.stream().mapToInt(Course::getSubmissions).min().orElse(0);
        List<String> highestActivity = new ArrayList<>();
        List<String> lowestActivity = new ArrayList<>();
        for (Course c : courses) {
            if (c.getSubmissions() == maxSubmissions && maxSubmissions > 0) {
                highestActivity.add(c.getName());
            }
        }
        for (Course c : courses) {
            if (c.getSubmissions() == minSubmissions && c.getSubmissions() > 0 && !highestActivity.contains(c.getName())) {
                lowestActivity.add(c.getName());
            }
        }
        System.out.println("Highest activity: " + (highestActivity.isEmpty() ? "n/a" : String.join(", ", highestActivity)));
        System.out.println("Lowest activity: " + (lowestActivity.isEmpty() ? "n/a" : String.join(", ", lowestActivity)));

        // Easiest and hardest courses (by average task score)
        // Only consider courses with at least one submission.
        double maxAverage = courses.stream().filter(c -> c.getSubmissions() > 0)
                .mapToDouble(Course::getAverageTaskScore).max().orElse(0);
        double minAverage = courses.stream().filter(c -> c.getSubmissions() > 0)
                .mapToDouble(Course::getAverageTaskScore).min().orElse(0);
        List<String> easiest = new ArrayList<>();
        List<String> hardest = new ArrayList<>();
        for (Course c : courses) {
            if (c.getSubmissions() > 0 && Double.compare(c.getAverageTaskScore(), maxAverage) == 0) {
                easiest.add(c.getName());
            }
        }
        for (Course c : courses) {
            if (c.getSubmissions() > 0 && Double.compare(c.getAverageTaskScore(), minAverage) == 0 && !easiest.contains(c.getName())) {
                hardest.add(c.getName());
            }
        }
        System.out.println("Easiest course: " + (easiest.isEmpty() ? "n/a" : String.join(", ", easiest)));
        System.out.println("Hardest course: " + (hardest.isEmpty() ? "n/a" : String.join(", ", hardest)));
    }

    /**
     * Prints the top learners for a specified course.
     */
    private void printTopLearnersForCourse(String courseName) {
        List<Student> enrolled = getEnrolledStudentsForCourse(courseName);

        // Sort by total points for the course (descending) then by student ID (ascending).
        enrolled.sort(Comparator.comparingInt((Student s) -> getCoursePoints(s, courseName))
                .reversed().thenComparing(Student::getId));

        System.out.println(courseName);
        System.out.println("id     points completed");
        for (Student s : enrolled) {
            int points = getCoursePoints(s, courseName);
            double completion = computeCompletionPercentage(courseName, points);
            // Print the student's id, total points and progress percentage (with one decimal)
            System.out.printf("%s %d %.1f%%%n", s.getId(), points, completion);
        }
    }

    private List<Course> getCoursesList() {
        // Initialize courses with the required points and default values.
        Map<String, Course> coursesMap = new HashMap<>();
        coursesMap.put("Java", new Course("Java", 600, 0, 0));
        coursesMap.put("DSA", new Course("DSA", 400, 0, 0));
        coursesMap.put("Databases", new Course("Databases", 480, 0, 0));
        coursesMap.put("Spring", new Course("Spring", 550, 0, 0));

        // Iterate over each student and update course statistics.
        for (Student student : getStudents()) {
            for (String courseName : coursesMap.keySet()) {
                int studentPoints = student.getPointsForCourse(courseName);
                if (studentPoints > 0) {
                    Course course = coursesMap.get(courseName);
                    // We count the student as enrolled even if they have one submission.
                    course.setEnrolledStudents(course.getEnrolledStudents() + 1);
                    // Assuming each student submission is counted as one submission.
                    course.addSubmission();
                    course.addPointsScored(studentPoints);
                    course.recalcAverageTaskScore();
                }
            }
        }
        return new ArrayList<>(coursesMap.values());
    }


    /**
     * Returns true if the course name (case-sensitive) is one of the allowed course names.
     */
    private boolean isValidCourseName(String name) {
        return name.equals("Java") || name.equals("DSA") || name.equals("Databases") || name.equals("Spring");
    }

    /**
     * Returns the list of students enrolled in the specified course.
     * A student is enrolled if the learning progress data contains at least one non-zero submission for that course.
     */
    private List<Student> getEnrolledStudentsForCourse(String courseName) {
        List<Student> enrolledStudents = new ArrayList<>();
        for (Student s : students) {
            if (getCoursePoints(s, courseName) > 0) {
                enrolledStudents.add(s);
            }
        }
        return enrolledStudents;
    }

    /**
     * Retrieves the total points earned by a student for the specified course.
     * (Assumes Student provides a method to get points per course.)
     */
    private int getCoursePoints(Student student, String courseName) {
        return student.getPointsForCourse(courseName);
    }

    /**
     * Computes the completion percentage for the student in a specific course.
     */
    private double computeCompletionPercentage(String courseName, int pointsEarned) {
        int totalPoints;
        switch (courseName) {
            case "Java":
                totalPoints = 600;
                break;
            case "DSA":
                totalPoints = 400;
                break;
            case "Databases":
                totalPoints = 480;
                break;
            case "Spring":
                totalPoints = 550;
                break;
            default:
                totalPoints = 0;
        }
        double percentage = totalPoints > 0 ? (100.0 * pointsEarned / totalPoints) : 0;
        return roundHalfUp(percentage, 1);
    }

    /**
     * Rounds the given value to the specified scale using HALF_UP.
     */
    private double roundHalfUp(double value, int scale) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(scale, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


}
