package main.java.org.flinkjo;

import main.java.org.flinkjo.card.EmployeeCardGenerator;
import main.java.org.flinkjo.models.Department;
import main.java.org.flinkjo.models.Employee;
import main.java.org.flinkjo.models.EmployeeCardData;
import main.java.org.flinkjo.parsers.DataLoader;
import main.java.org.flinkjo.validation.Validator;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        List<Employee> employees;
        List<Department> departments;

        try {
            DataLoader loader = new DataLoader("src/main/resources/NordicPeakEmployees.xml");
            Validator validator = new Validator();
            EmployeeCardGenerator employeeCardGenerator = new EmployeeCardGenerator();

            employees = loader.loadEmployees();
            departments = loader.loadDepartments();

            EmployeeCardData result = validator.validateEmployeeDepartmentRelationship(departments, employees);

            departments = result.getDepartments();
            employees = result.getEmployees();
            employeeCardGenerator.generateEmployeeCard(employees, departments);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
