package main.java.org.flinkjo.models;

import main.java.org.flinkjo.utils.ImageUtils;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class Employee {

    private int employeeID;
    private String firstName;
    private String lastName;
    private String citizenIdentifier;
    private String employmentDate;
    private int departmentID;
    private BufferedImage profileImage;

    public Employee(int employeeID, String firstName, String lastName, String citizenIdentifier, String employmentDate, int departmentID, String profileImage) {
        this.employeeID = employeeID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.citizenIdentifier = citizenIdentifier;
        this.employmentDate = employmentDate;
        this.departmentID = departmentID;
        this.profileImage = ImageUtils.decodeBase64ToBufferedImage(profileImage);
    }


    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
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

    public String getCitizenIdentifier() {
        return citizenIdentifier;
    }

    public void setCitizenIdentifier(String citizenIdentifier) {
        this.citizenIdentifier = citizenIdentifier;
    }

    public String getEmploymentDate() {
        return employmentDate;
    }

    public void setEmploymentDate(String employmentDate) {
        this.employmentDate = employmentDate;
    }

    public int getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(int departmentID) {
        this.departmentID = departmentID;
    }

    public BufferedImage getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) throws IOException {
        this.profileImage = ImageUtils.decodeBase64ToBufferedImage(profileImage);
    }

    public void printEmployee() {
        System.out.println("Employee ID: " + employeeID);
        System.out.println("First Name: " + firstName);
        System.out.println("Last Name: " + lastName);
        System.out.println("Citizen Identifier: " + citizenIdentifier);
        System.out.println("Employment Date: " + employmentDate);
        System.out.println("Department ID: " + departmentID);
        System.out.println("---------------------------------");
    }
}
