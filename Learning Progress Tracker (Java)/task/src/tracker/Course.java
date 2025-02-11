package tracker;

public class Course {
    private String name;
    private int points; // required points to complete the course
    private int enrolledStudents;
    private int submissions;
    private int pointsScored;
    private double averageTaskScore;

    public Course(String name, int points, int enrolledStudents, double averageTaskScore) {
        this.name = name;
        this.points = points;
        this.enrolledStudents = enrolledStudents;
        this.averageTaskScore = averageTaskScore;
        this.pointsScored = 0;
        this.submissions = 0;
    }

    public String getName() {
        return name;
    }

    public int getEnrolledStudents() {
        return enrolledStudents;
    }

    public void setEnrolledStudents(int enrolledStudents) {
        this.enrolledStudents = enrolledStudents;
    }

    public double getAverageTaskScore() {
        return averageTaskScore;
    }

    public void addPointsScored(int pointsScored) {
        this.pointsScored += pointsScored;
    }

    public int getSubmissions() {
        return submissions;
    }

    public void addSubmission() {
        this.submissions++;
    }

    /**
     * Recomputes the average score per assignment.
     * Call this method after updating pointsScored or submissions.
     */
    public void recalcAverageTaskScore() {
        if (submissions > 0) {
            averageTaskScore = (double) pointsScored / submissions;
        } else {
            averageTaskScore = 0.0;
        }
    }
}