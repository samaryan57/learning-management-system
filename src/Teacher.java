/**
 * Project 4 - Discussion forum, Learning Management System
 * <p>
 * Teacher class, to handle teacher login credentials.
 *
 * @author Noah Mundy, Paul Kelson, Raj Lunia, Sarah Nicholson, Aryan Samantaray L11
 * @version 04/06/2022
 */

public class Teacher extends Person {
    public Teacher(String username, String password) {
        super(username, password);
    }

    public boolean equals(Object o) {

        if (!(o instanceof Teacher)) {
            return false;
        }

        if (this == o) {
            return true;
        }
        Teacher temp = (Teacher) o; // CASTING OBJECT o TO Teacher OBJECT FOR COMPARISON

        return (temp.getUsername().equals(this.getUsername()) && temp.getPassword().equals(this.getPassword()));
    }

    @Override
    public String toString() {
        return super.toString() + ":T";
    }
}