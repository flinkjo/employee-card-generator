package main.java.org.flinkjo.models;

import java.util.List;

public class EmployeeCardData {
    public List<Department> departments;
    public List<Employee> employees;

    public EmployeeCardData(List<Department> departments, List<Employee> employees) {
        this.departments = departments;
        this.employees = employees;
    }

    public List<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
}
