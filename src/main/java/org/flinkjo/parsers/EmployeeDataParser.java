package main.java.org.flinkjo.parsers;

import main.java.org.flinkjo.models.Employee;
import main.java.org.flinkjo.validation.Validator;
import se.unlogic.standardutils.xml.XMLParser;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

import org.xml.sax.SAXException;

import java.util.ArrayList;
import java.util.List;

public class EmployeeDataParser {

    private XMLParser xmlParser;
    private List<Employee> employees = new ArrayList<>();
    private Validator validator;

    public EmployeeDataParser(String filePath) throws SAXException, IOException, ParserConfigurationException {
        File xmlFile = new File(filePath);
        this.xmlParser = new XMLParser(xmlFile);
        this.validator = new Validator();
    }

    public List<Employee> parseEmployeeData() throws IOException {
        List<XMLParser> employeeNodes = xmlParser.getNodes("/NordicPeak/Employees/Employee");

        if (employeeNodes != null) {
            for (XMLParser employeeNode : employeeNodes) {
                String employeeIDStr = employeeNode.getString("employeeID");
                String firstName = employeeNode.getString("firstname");
                String lastName = employeeNode.getString("lastname");
                String citizenIdentifier = employeeNode.getString("citizenIdentifier");
                String employmentDate = employeeNode.getString("employmentDate");
                String departmentIDStr = employeeNode.getString("departmentID");
                String profileImage = employeeNode.getString("profileImage");


                boolean isValid = validator.validateEmployeeID(employeeIDStr) &&
                        validator.validateString20Character(firstName, "First name") &&
                        validator.validateString20Character(lastName, "Last name") &&
                        validator.validateCitizenIdentifier(citizenIdentifier) &&
                        validator.validateEmploymentDate(employmentDate) &&
                        validator.validateDepartmentID(departmentIDStr) &&
                        validator.validateImage(profileImage);

                if (!isValid) {
                    continue;
                }

                int employeeID = Integer.parseInt(employeeIDStr);
                int departmentID = Integer.parseInt(departmentIDStr);
                Employee employee = new Employee(employeeID, firstName, lastName, citizenIdentifier, employmentDate, departmentID, profileImage);

                if (validator.validateEmployeeUniqueness(employee, employees)) {
                    employees.add(employee);
                }
            }
        } else {
            System.out.println("No employees found in the XML.");
        }
        return employees;
    }
}
