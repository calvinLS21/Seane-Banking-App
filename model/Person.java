package model;

public class Person {
    private int nationalID;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String address;
    private String emailAddress;
    private int contact;

    public Person(int nationalID, String firstName, String lastName, String dateOfBirth, String address, String emailAddress, int contact) {
        this.nationalID = nationalID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.emailAddress = emailAddress;
        this.contact = contact;
    }

    public int getNationalID() {
        return nationalID;
    }

    public void setNationalID(int nationalID) {
        this.nationalID = nationalID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public int getContact() {
        return contact;
    }

    public void setContact(int contact) {
        this.contact = contact;
    }

    public boolean login(int id, String email) {
        return this.emailAddress.equals(email) && this.nationalID == id;
    }

    public static Person signUp(int nationalID, String firstName, String lastName, String dateOfBirth, String address, String emailAddress, int contact) {
        if (nationalID <= 0 || firstName.isEmpty() || lastName.isEmpty() || emailAddress.isEmpty()) {
            throw new IllegalArgumentException("Invalid signup details");
        }
        return new Person(nationalID, firstName, lastName, dateOfBirth, address, emailAddress, contact);
    }

    @Override
    public String toString() {
        return "Person{" +
                "nationalID=" + nationalID +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", address='" + address + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", contact=" + contact +
                '}';
    }
}
