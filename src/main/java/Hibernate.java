import javax.persistence.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.util.List;

@Entity
@Table(name = "Employee")

class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "Last_Name")
    private String lastName;

    @Column(name = "First_Name")
    private String firstName;

    @Column(name = "Middle_Name")
    private String middleName;

    @Column(name = "Gender")
    private String gender;

    @Column(name = "Date_of_Birth")
    private Date dateOfBirth;

    @Column(name = "Address")
    private String address;

    @Column(name = "Position")
    private String position;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}

public class Hibernate extends JFrame {
    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private JTextArea employeeTextArea;

    public Hibernate() {
        initializeUI();
        connectToDatabase();
    }

    private void initializeUI() {
        setTitle("Employee Management App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());

        employeeTextArea = new JTextArea();
        employeeTextArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(employeeTextArea);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton viewButton = new JButton("View Employees");
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewEmployees();
            }
        });
        buttonPanel.add(viewButton);

        JButton editButton = new JButton("Edit Employee");
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editEmployee();
            }
        });
        buttonPanel.add(editButton);

        JButton addButton = new JButton("Add Employee");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addEmployee();
            }
        });
        buttonPanel.add(addButton);

        JButton deleteButton = new JButton("Delete Employee");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteEmployee();
            }
        });
        buttonPanel.add(deleteButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void connectToDatabase() {
        try {
            String url = "jdbc:sqlserver://localhost:1433;database=employee;integratedSecurity=true;trustServerCertificate=true";
            Connection connection = DriverManager.getConnection(url);
            entityManagerFactory = Persistence.createEntityManagerFactory("EmployeeManagementApp");
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.setProperty("javax.persistence.jdbc.connection", connection);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to connect to the database.");
            System.exit(1);
        }
    }

    private void viewEmployees() {
        try {
            TypedQuery<Employee> query = entityManager.createQuery("SELECT e FROM Employee e", Employee.class);
            List<Employee> employees = query.getResultList();

            StringBuilder employeeData = new StringBuilder();
            for (Employee employee : employees) {
                String employeeInfo = String.format("ID: %d, Last Name: %s, First Name: %s, Middle Name: %s, Gender: %s, " +
                                "Date of Birth: %s, Address: %s, Position: %s", employee.getId(),
                        employee.getLastName(), employee.getFirstName(), employee.getMiddleName(), employee.getGender(),
                        employee.getDateOfBirth().toString(), employee.getAddress(), employee.getPosition());
                employeeData.append(employeeInfo).append("\n");
            }

            employeeTextArea.setText(employeeData.toString());
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to connect to the database or execute the query.");
        }
    }

    private void editEmployee() {
        String employeeId = JOptionPane.showInputDialog(this, "Enter the ID of the employee to edit:");
        try {
            int id = Integer.parseInt(employeeId);
            Employee employee = entityManager.find(Employee.class, id);
            if (employee != null) {
                String lastName = JOptionPane.showInputDialog(this, "Enter the last name:");
                employee.setLastName(lastName);

                String firstName = JOptionPane.showInputDialog(this, "Enter the first name:");
                employee.setFirstName(firstName);

                String middleName = JOptionPane.showInputDialog(this, "Enter the middle name:");
                employee.setMiddleName(middleName);

                String gender = JOptionPane.showInputDialog(this, "Enter the gender:");
                employee.setGender(gender);

                String dateOfBirth = JOptionPane.showInputDialog(this, "Enter the date of birth (YYYY-MM-DD):");
                employee.setDateOfBirth(Date.valueOf(dateOfBirth));

                String address = JOptionPane.showInputDialog(this, "Enter the address:");
                employee.setAddress(address);

                String position = JOptionPane.showInputDialog(this, "Enter the position:");
                employee.setPosition(position);

                entityManager.getTransaction().begin();
                entityManager.merge(employee);
                entityManager.getTransaction().commit();

                JOptionPane.showMessageDialog(this, "Employee details updated successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Employee not found with the provided ID.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid employee ID format.");
        }
    }

    private void addEmployee() {
        String lastName = JOptionPane.showInputDialog(this, "Enter the last name:");
        String firstName = JOptionPane.showInputDialog(this, "Enter the first name:");
        String middleName = JOptionPane.showInputDialog(this, "Enter the middle name:");

                String gender = JOptionPane.showInputDialog(this, "Enter the gender:");
        String dateOfBirth = JOptionPane.showInputDialog(this, "Enter the date of birth (YYYY-MM-DD):");
        String address = JOptionPane.showInputDialog(this, "Enter the address:");
        String position = JOptionPane.showInputDialog(this, "Enter the position:");

        Employee employee = new Employee();
        employee.setLastName(lastName);
        employee.setFirstName(firstName);
        employee.setMiddleName(middleName);
        employee.setGender(gender);
        employee.setDateOfBirth(Date.valueOf(dateOfBirth));
        employee.setAddress(address);
        employee.setPosition(position);

        entityManager.getTransaction().begin();
        entityManager.persist(employee);
        entityManager.getTransaction().commit();

        JOptionPane.showMessageDialog(this, "Employee added successfully.");
    }

    private void deleteEmployee() {
        String employeeId = JOptionPane.showInputDialog(this, "Enter the ID of the employee to delete:");
        try {
            int id = Integer.parseInt(employeeId);
            Employee employee = entityManager.find(Employee.class, id);
            if (employee != null) {
                int confirmation = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to delete the employee?\nID: " + employee.getId() +
                                "\nLast Name: " + employee.getLastName() +
                                "\nFirst Name: " + employee.getFirstName(),
                        "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirmation == JOptionPane.YES_OPTION) {
                    entityManager.getTransaction().begin();
                    entityManager.remove(employee);
                    entityManager.getTransaction().commit();

                    JOptionPane.showMessageDialog(this, "Employee deleted successfully.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Employee not found with the provided ID.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid employee ID format.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Hibernate().setVisible(true);
            }
        });
    }
}