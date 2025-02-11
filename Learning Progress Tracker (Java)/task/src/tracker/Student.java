package tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Student {
    private String id = "";
    private String firstName = "";
    private String lastName = "";
    private String email = "";
    // Map to track the progress (points) for each course
    Map<String, Integer> courseProgress = new HashMap<>();
    // Map to track whether notification has been sent for each course
    private Map<String, Boolean> notificationsSent = new HashMap<>();


    public Student(String firstName, String lastName, String email) {
        setId();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;

        this.courseProgress.put("Java", 0);
        this.courseProgress.put("DSA", 0);
        this.courseProgress.put("Databases", 0);
        this.courseProgress.put("Spring", 0);

        this.notificationsSent.put("Java", false);
        this.notificationsSent.put("DSA", false);
        this.notificationsSent.put("Databases", false);
        this.notificationsSent.put("Spring", false);
    }

    // A Method that sets a unique ID (must be random) for a student
    public void setId() {
        this.id = Integer.toString((int) (Math.random() * 1000));
    }

    public void setCourseProgress(ArrayList<Integer> courseProgressArray) {
        this.courseProgress.put("Java", courseProgress.get("Java") + courseProgressArray.get(0));
        this.courseProgress.put("DSA", courseProgress.get("DSA") + courseProgressArray.get(1));
        this.courseProgress.put("Databases", courseProgress.get("Databases") + courseProgressArray.get(2));
        this.courseProgress.put("Spring", courseProgress.get("Spring") + courseProgressArray.get(3));

    }
    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(id, student.id) && Objects.equals(email, student.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }

    @Override
    public String toString() {
        return "Student{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    /**
     * A method that prints a student's information if the ID is found.
     * @param id_: The ID of the student to print
     * @return: A String containing the student's information.
     */
    public String printStudentCourseInfo (String id_) {
        StringBuilder studentInfo = new StringBuilder();
        studentInfo.append("%s points: Java=%d, DSA=%d, Databases=%d, Spring=%d\n".formatted(this.id,
                this.courseProgress.get("Java"), this.courseProgress.get("DSA"), this.courseProgress.get("Databases"),
                this.courseProgress.get("Spring")));
        return studentInfo.toString();

    }

    // Returns the points accumulated for the specified course.
    public int getPointsForCourse(String courseName) {
        return courseProgress.getOrDefault(courseName, 0);
    }

    // Checks if the notification has already been sent for a given course.
    public boolean isNotifiedForCourse(String courseName) {
        return notificationsSent.getOrDefault(courseName, false);
    }

    // Sets the notification status for a specific course as sent.
    public void markNotifiedForCourse(String courseName) {
        if (notificationsSent.containsKey(courseName)) {
            notificationsSent.put(courseName, true);
        }
    }


}