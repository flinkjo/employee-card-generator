package main.java.org.flinkjo.models;

public class Department {

    private int departmentID;
    private String name;
    private int managerID;
    private String location;

    public Department(int departmentID, String name, int managerID, String location) {
        this.departmentID = departmentID;
        this.name = name;
        this.managerID = managerID;
        this.location = location;
    }

    public int getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(int departmentID) {
        this.departmentID = departmentID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getManagerID() {
        return managerID;
    }

    public void setManagerID(int managerID) {
        this.managerID = managerID;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void printDepartment() {
        System.out.println("Department ID: " + departmentID);
        System.out.println("Name: " + name);
        System.out.println("ManagerID: " + managerID);
        System.out.println("Location: " + location);
        System.out.println("---------------------------------");
    }
}
