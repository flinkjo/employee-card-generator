package main.java.org.flinkjo.parsers;

import main.java.org.flinkjo.models.Department;
import main.java.org.flinkjo.validation.Validator;
import org.xml.sax.SAXException;
import se.unlogic.standardutils.xml.XMLParser;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDataParser {

    private XMLParser xmlParser;
    private List<Department> departments = new ArrayList<>();
    private Validator validator;

    public DepartmentDataParser(String filePath) throws SAXException, IOException, ParserConfigurationException {
        File xmlFile = new File(filePath);
        this.xmlParser = new XMLParser(xmlFile);
        this.validator = new Validator();
    }

    public List<Department> parseDepartmentData() {
        List<XMLParser> departmentNodes = xmlParser.getNodes("/NordicPeak/Departments/Department");

        if (departmentNodes != null) {
            for (XMLParser departmentNode : departmentNodes) {
                String departmentIDStr = departmentNode.getString("departmentID");
                String name = departmentNode.getString("name");
                String managerIDStr = departmentNode.getString("managerID");
                String location = departmentNode.getString("location");

                boolean isValid = validator.validateDepartmentID(departmentIDStr) &&
                        validator.validateString20Character(name, "Name") &&
                        validator.validateEmployeeID(managerIDStr) &&
                        validator.validateString20Character(location, "Location");

                if (!isValid) {
                    continue;
                }

                int departmentID = Integer.parseInt(departmentIDStr);
                int managerID = Integer.parseInt(managerIDStr);
                Department department = new Department(departmentID, name, managerID, location);

                if (validator.validateDepartmentUniqueness(department, departments)) {
                    departments.add(department);
                }
            }
        } else {
            System.out.println("No departments found in the XML.");
        }
        return departments;
    }
}
