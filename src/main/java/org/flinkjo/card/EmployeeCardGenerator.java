package main.java.org.flinkjo.card;

import main.java.org.flinkjo.models.Department;
import main.java.org.flinkjo.models.Employee;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;
import java.util.List;

public class EmployeeCardGenerator {

    public void generateEmployeeCard(List<Employee> employees, List<Department> departments) {
        employees.forEach(employee -> {

            Department department = null;
            for (Department d : departments) {
                if (d.getDepartmentID() == employee.getDepartmentID()) {
                    department = d;
                }
            }

            try {
                BufferedImage image = drawEmployeeCard(employee, department);

                File cardsFolder = new File("employee-cards/");
                if (!cardsFolder.exists()) {
                    cardsFolder.mkdir();
                }
                File imageFile = new File(cardsFolder, "employee-card-" + employee.getEmployeeID() + ".png");
                ImageIO.write(image, "png", imageFile);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private BufferedImage drawEmployeeCard(Employee employee, Department department) throws IOException {

        BufferedImage templateImage = ImageIO.read(new File("src/main/resources/Passerkort.png"));
        BufferedImage profileImage = employee.getProfileImage();

        Graphics2D g2d = templateImage.createGraphics();

        int imageX = 152;
        int imageY = 105;
        int imageWidth = 295;
        int imageHeight = 320;
        g2d.drawImage(profileImage, imageX, imageY, imageWidth, imageHeight, null);

        g2d.setFont(new Font("Arial", Font.PLAIN, 22));
        g2d.setColor(Color.decode("#121b1f"));

        g2d.drawString(employee.getFirstName(), 160, 530);
        g2d.drawString(employee.getLastName(), 160, 600);
        g2d.drawString(department.getName(), 160, 672);
        g2d.drawString(department.getLocation(), 160, 745);

        g2d.setFont(new Font("Arial", Font.PLAIN, 20));
        drawVerticalText(g2d, String.valueOf(employee.getEmployeeID()), imageX - 10, imageY + (imageHeight / 2), -90);
        drawVerticalText(g2d, employee.getEmploymentDate(), imageX + imageWidth + 20, imageY + (imageHeight / 2), -90);

        g2d.dispose();
        return templateImage;
    }

    private void drawVerticalText(Graphics2D g2d, String text, int x, int y, int angle) {
        g2d.setFont(new Font("Arial", Font.BOLD, 17));
        g2d.setColor(Color.decode("#121b1f"));

        g2d.rotate(Math.toRadians(angle), x, y);
        g2d.drawString(text, x, y);
        g2d.rotate(Math.toRadians(-angle), x, y);
    }
}
