package main.java.org.flinkjo.validation;

import main.java.org.flinkjo.models.Department;
import main.java.org.flinkjo.models.Employee;
import main.java.org.flinkjo.models.EmployeeCardData;
import se.unlogic.standardutils.date.DateUtils;
import se.unlogic.standardutils.date.PooledSimpleDateFormat;
import se.unlogic.standardutils.validation.Birthdate8DigitValidator;
import se.unlogic.standardutils.validation.StringIntegerValidator;
import se.unlogic.standardutils.validation.StringLengthValidator;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class Validator {

    private static final StringIntegerValidator ID_VALIDATOR = new StringIntegerValidator(1, 9999);
    private static final StringLengthValidator STRING_LENGTH_VALIDATOR = new StringLengthValidator(1, 20);
    private static final Birthdate8DigitValidator DATE_VALIDATOR = new Birthdate8DigitValidator();
    private static final Pattern CITIZEN_IDENTIFIER_PATTERN = Pattern.compile("[0-9]{8}-[0-9]{4}");
    private static final PooledSimpleDateFormat YYYYMMDD_FORMATTER = new PooledSimpleDateFormat("yyyyMMdd");
    private static final Date MIN_EMPLOYMENT_DATE;

    static {
        try {
            MIN_EMPLOYMENT_DATE = YYYYMMDD_FORMATTER.parse("20100721");
        } catch (ParseException e) {
            throw new RuntimeException("Failed to parse minimum date", e);
        }
    }

    public boolean validateDepartmentID(String departmentIDStr) {
        if (!ID_VALIDATOR.validateFormat(departmentIDStr)) {
            logError("Invalid Department ID: " + departmentIDStr + "\nDepartment ID must be numeric and between 1-9999.");
            return false;
        }
        return true;
    }

    public boolean validateEmployeeID(String employeeIDStr) {
        if (!ID_VALIDATOR.validateFormat(employeeIDStr)) {
            logError("Invalid Employee ID: " + employeeIDStr + "\nEmployee ID must be numeric, and between 1-9999.");
            return false;
        }
        return true;
    }

    public boolean validateString20Character(String string, String typeOfString) {
        if (!STRING_LENGTH_VALIDATOR.validateFormat(string)) {
            logError("Invalid " + typeOfString + ": " + string + "\n" + typeOfString + " must be between 1-20 characters.");
            return false;
        }
        return true;
    }

    public boolean validateCitizenIdentifier(String citizenIdentifier) {
        if (!CITIZEN_IDENTIFIER_PATTERN.matcher(citizenIdentifier).matches()) {
            logError("Invalid Citizen Identifier: " + citizenIdentifier + "\nCitizen Identifier must be in the format YYYYMMDD-XXXX.");
            return false;
        }
        String birthdatePart = citizenIdentifier.substring(0, 8);
        if (!DATE_VALIDATOR.validateFormat(birthdatePart)) {
            logError("Invalid birthdate in Citizen Identifier: " + birthdatePart);
            return false;
        }

        return true;
    }

    public boolean validateEmploymentDate(String employmentDate) {
        if (!DateUtils.isValidDate(YYYYMMDD_FORMATTER, employmentDate)) {
            logError("Invalid employment date: " + employmentDate + "\nEmployment Date must be a valid date, in the format YYYYMMDD.");
            return false;
        }

        Date parsedDate = DateUtils.getDate(YYYYMMDD_FORMATTER, employmentDate);
        if (parsedDate == null) {
            logError("Failed to parse employment date: " + employmentDate);
            return false;
        }

        if (parsedDate.after(new Date())) {
            logError("Invalid employment date: " + employmentDate + "\nEmployment date cannot be in the future.");
            return false;
        }

        if (parsedDate.before(MIN_EMPLOYMENT_DATE)) {
            logError("Invalid employment date: " + employmentDate + "\nEmployment date cannot be earlier than 2010-07-21.");
            return false;
        }
        return true;
    }

    public boolean validateImage(String base64Image) {
        base64Image = base64Image.trim();

        while (base64Image.length() % 4 != 0) {
            base64Image += "=";
        }

        byte[] imageBytes;
        try {
            imageBytes = Base64.getDecoder().decode(base64Image);
        } catch (IllegalArgumentException e) {
            logError("Profile picture has invalid Base64 encoding: " + e.getMessage());
            return false;
        }

        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes)) {
            BufferedImage image = ImageIO.read(byteArrayInputStream);
            if (image == null) {
                logError("Profile picture is not set.");
                return false;
            }
        } catch (IOException e) {
            logError("Error reading profile image data: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean validateDepartmentUniqueness(Department newDepartment, List<Department> existingDepartments) {

        if (existingDepartments.isEmpty()) {
            return true;
        }
        for (Department existingDepartment : existingDepartments) {
            if (newDepartment.getDepartmentID() == existingDepartment.getDepartmentID()) {
                logError("Invalid Department ID: " + newDepartment.getDepartmentID() + "\nDepartment ID must be unique.");
                return false;
            }
        }
        return true;
    }

    public boolean validateEmployeeUniqueness(Employee newEmployee, List<Employee> existingEmployees) {

        if (existingEmployees.isEmpty()) {
            return true;
        }
        for (Employee existingEmployee : existingEmployees) {
            if (newEmployee.getEmployeeID() == existingEmployee.getEmployeeID()) {
                logError("Invalid Employee ID: " + newEmployee.getEmployeeID() + "\nEmployee ID must be unique.");
                return false;
            }

            if (newEmployee.getCitizenIdentifier().equals(existingEmployee.getCitizenIdentifier())) {
                logError("Invalid Citizen Identifier: " + newEmployee.getCitizenIdentifier() + "\nCitizen Identifier must be unique.");
                return false;
            }
        }
        return true;
    }

    public EmployeeCardData validateEmployeeDepartmentRelationship(List<Department> departments, List<Employee> employees) {
        int departmentSize = departments.size();
        int employeeSize = employees.size();
        departments = validateDepartmentManager(departments, employees);
        employees = validateEmployeeDepartment(employees, departments);

        if (departmentSize != departments.size() || employeeSize != employees.size()) {
            return validateEmployeeDepartmentRelationship(departments, employees);
        }
        return new EmployeeCardData(departments, employees);
    }

    public List<Department> validateDepartmentManager(List<Department> departments, List<Employee> employees) {

        List<Department> departmentsWithManager = new ArrayList<>();
        boolean departmentHasManager = false;

        for (Department department : departments) {
            int managerID = department.getManagerID();
            departmentHasManager = false;
            for (Employee employee : employees) {
                if (employee.getEmployeeID() == managerID) {
                    departmentHasManager = true;
                    departmentsWithManager.add(department);
                    break;
                }
            }
            if (!departmentHasManager) {
                logError("Department " + department.getDepartmentID() + " has no valid manager id: " + department.getManagerID());
            }
        }

        return departmentsWithManager;

    }

    public List<Employee> validateEmployeeDepartment(List<Employee> employees, List<Department> departments) {

        List<Employee> employeesWithDepartment = new ArrayList<>();
        boolean employeeHasDepartment = false;

        for (Employee employee : employees) {
            int departmentID = employee.getDepartmentID();
            employeeHasDepartment = false;
            for (Department department : departments) {
                if (department.getDepartmentID() == departmentID) {
                    employeeHasDepartment = true;
                    employeesWithDepartment.add(employee);
                    break;
                }
            }
            if (!employeeHasDepartment) {
                logError("Employee " + employee.getEmployeeID() + " has no valid department: " + employee.getDepartmentID());
            }
        }
        return employeesWithDepartment;
    }

    private void logError(String message) {
        Logger logger = Logger.getLogger(Validator.class.getName());
        logger.warning(message);
    }
}
