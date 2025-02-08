package hyperskill.entity;

public class Course {
    private String name;
    private int points;
    private int enrolledStudents;
    private int submissions;
    private int pointsScored;
    private double averageTaskScore;

    public Course(String name, int points, int enrolledStudents, double averageTaskScore) {
        this.name = name;
        this.points = points;
        this.enrolledStudents = enrolledStudents;
        this.averageTaskScore = averageTaskScore;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
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

    public void setAverageTaskScore(double averageTaskScore) {
        this.averageTaskScore = averageTaskScore;
    }

    public int getPointsScored() {
        return pointsScored;
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

}
