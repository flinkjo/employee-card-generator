package main.java.org.flinkjo.parsers;

import main.java.org.flinkjo.models.Department;
import main.java.org.flinkjo.models.Employee;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

public class DataLoader {

    private String xmlFilePath;

    public DataLoader(String xmlFilePath) {
        this.xmlFilePath = xmlFilePath;
    }

    public List<Employee> loadEmployees() throws IOException, ParserConfigurationException, SAXException {
        EmployeeDataParser employeeParser = new EmployeeDataParser(xmlFilePath);
        return employeeParser.parseEmployeeData();
    }

    public List<Department> loadDepartments() throws IOException, ParserConfigurationException, SAXException {
        DepartmentDataParser departmentDataParser = new DepartmentDataParser(xmlFilePath);
        return departmentDataParser.parseDepartmentData();
    }
}
