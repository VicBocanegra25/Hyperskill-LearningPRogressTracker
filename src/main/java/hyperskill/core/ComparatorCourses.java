package hyperskill.core;

import hyperskill.entity.Course;

import java.util.Comparator;

class CourseNameComparator implements Comparator<Course> {
    @Override
    public int compare(Course course1, Course course2) {
        return course1.getName().compareTo(course2.getName());
    }

}

class CoursePopularComparator implements Comparator<Course> {
    @Override
    public int compare(Course course1, Course course2) {
        return course2.getEnrolledStudents() - course1.getEnrolledStudents();
    }
}

class CourseSubmissionsComparator implements Comparator<Course> {
    @Override
    public int compare(Course course1, Course course2) {
        return course2.getSubmissions() - course1.getSubmissions();
    }
}

class CourseDifficultyComparator implements Comparator<Course> {
    @Override
    public int compare(Course course1, Course course2) {
        return Double.compare(course1.getAverageTaskScore(), course2.getAverageTaskScore());
    }
}
