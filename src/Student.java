/**
 * Project 4 - Discussion forum, Learning Management System
 * <p>
 * Student class, to handle student login credentials.
 *
 * @author Noah Mundy, Paul Kelson, Raj Lunia, Sarah Nicholson, Aryan Samantaray L11
 * @version 04/06/2022
 */

public class Student extends Person {
    public Student(String username, String password) {
        super(username, password);
    }

    public boolean equals(Object o) {

        if (!(o instanceof Student)) {
            return false;
        }

        if (this == o) {
            return true;
        }
        Student temp = (Student) o; // CASTING OBJECT o TO Student OBJECT FOR COMPARISON

        return (temp.getUsername().equals(this.getUsername()) && temp.getPassword().equals(this.getPassword()));
    }

    @Override
    public String toString() {
        return super.toString() + ":S";
    }
}