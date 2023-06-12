/**
 * Project 5 - Discussion forum, Learning Management System
 * <p>
 * Person class, super class for student and teacher.
 *
 * @author Noah Mundy, Paul Kelson, Raj Lunia, Sarah Nicholson, Aryan Samantaray L11
 * @version 04/06/2022
 */
public class Person {
    private String username;
    private String password;

    public Person() {
        this.username = "";
        this.password = "";
    }

    public Person(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean equals(Object o) {

        if (!(o instanceof Person)) {
            return false;
        }

        if (this == o) {
            return true;
        }

        Person temp = (Person) o; // CASTING OBJECT o TO Person OBJECT FOR COMPARISON

        return (temp.getUsername().equals(this.username) && temp.getPassword().equals(this.password));
    }

    @Override
    public String toString() {
        String output = username + ":" + password;
        return output;
    }
}