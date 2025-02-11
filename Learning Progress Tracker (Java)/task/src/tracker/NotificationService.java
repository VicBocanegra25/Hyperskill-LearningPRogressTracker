package tracker;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NotificationService {

    private static final Map<String, Integer> COURSE_THRESHOLDS = new HashMap<>();
    
    static {
        COURSE_THRESHOLDS.put("Java", 600);
        COURSE_THRESHOLDS.put("DSA", 400);
        COURSE_THRESHOLDS.put("Databases", 480);
        COURSE_THRESHOLDS.put("Spring", 550);
    }

    /**
     * Processes the list of students and prints notification messages for each course that the student has
     * completed and has not been notified for already. Afterward, prints the total number of unique students
     * that have been notified.
     *
     * @param students List of Student objects to check.
     */
    public void notifyStudents(List<Student> students) {
        Set<String> notifiedStudentIds = new HashSet<>();

        for (Student student : students) {
            for (Map.Entry<String, Integer> entry : COURSE_THRESHOLDS.entrySet()) {
                String course = entry.getKey();
                int threshold = entry.getValue();

                // Check if points meet threshold and if notification hasn't been sent yet.
                if (student.getPointsForCourse(course) >= threshold && !student.isNotifiedForCourse(course)) {
                    // Generate and print the notification message.
                    System.out.println("To: " + student.getEmail());
                    System.out.println("Re: Your Learning Progress");
                    System.out.println("Hello, " + student.getFullName() + "! You have accomplished our " + course + " course!");
                    // Mark the notification as sent for this course.
                    student.markNotifiedForCourse(course);
                    
                    // Add student id to the notified set.
                    notifiedStudentIds.add(student.getId());
                }
            }
        }
        System.out.println("Total " + notifiedStudentIds.size() + " students have been notified.");
    }
}