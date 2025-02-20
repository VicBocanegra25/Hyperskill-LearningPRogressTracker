package tracker;

import java.util.Objects;

public class Student {
    private String id = "";
    private String firstName = "";
    private String lastName = "";
    private String email = "";
    private int courseScore = 0;

    public Student(String firstName, String lastName, String email) {
        setId();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    // A Method that sets a unique ID (must be random) for a student
    public void setId() {
        this.id = Integer.toString((int) (Math.random() * 1000));
    }

    public String getId() {
        return id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
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
}