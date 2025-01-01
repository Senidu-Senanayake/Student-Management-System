/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.studentmanagementsystemproject;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.HashMap;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.swing.JRViewer;
import net.sf.jasperreports.view.JasperViewer;

public class HomeUI extends JFrame {

    private JPanel mainContent;
    private CardLayout cardLayout;
    private int totalStudents = 0;
    private int enrolledMale = 0;
    private int enrolledFemale = 0;
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private JTextField txtSearch, txtStudentId, txtFirstName, txtLastName, txtBirthDate;
    private JComboBox<String> comboYear, comboCourse, comboGender;
    private JTextField courseField, descriptionField, degreeField;
    private JTable courseTable;
    private DefaultTableModel tableCourse;

    public HomeUI() {
        initComponents();
        
    }
    private void initComponents(){
    
        fetchStudentData();

        // Frame settings
        setTitle("Student Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Sidebar Panel
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(44, 62, 80));
        sidebar.setPreferredSize(new Dimension(250, getHeight()));
        sidebar.setLayout(null);

        // user icon image
        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/images/boy.png"));
        Image scaledIcon = originalIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        JLabel userIcon = new JLabel(new ImageIcon(scaledIcon));
        userIcon.setBounds(90, 30, 80, 80);
        sidebar.add(userIcon);

        JLabel lblWelcome = new JLabel("Welcome,");
        lblWelcome.setBounds(90, 120, 150, 20);
        lblWelcome.setForeground(Color.WHITE);
        sidebar.add(lblWelcome);

        JLabel lblAdmin = new JLabel("admin");
        lblAdmin.setBounds(100, 140, 150, 30);
        lblAdmin.setFont(new Font("Arial", Font.BOLD, 20));
        lblAdmin.setForeground(Color.WHITE);
        sidebar.add(lblAdmin);

        String[] menuItems = {"Dashboard", "Add Students", "Available Courses", "Grades of Students", "Report", "Sign Out"};

        int yOffset = 200;
        for (String item : menuItems) {
            JButton button = new JButton(item);
            button.setBounds(30, yOffset, 190, 40);
            button.setForeground(Color.WHITE);
            button.setBackground(new Color(52, 73, 94));
            button.setFocusPainted(false);
            button.setHorizontalAlignment(SwingConstants.LEFT);
            sidebar.add(button);

            button.addActionListener(new SidebarActionListener(item));
            yOffset += 50;
        }

        // Main Content Panel with CardLayout
        mainContent = new JPanel();
        cardLayout = new CardLayout();
        mainContent.setLayout(cardLayout);

        JPanel dashboardPanel = createDashboardPanel();
        mainContent.add(dashboardPanel, "Dashboard");

        JPanel addStudentPanel = createAddStudentPanel();
        mainContent.add(addStudentPanel, "AddStudents");

        JPanel availableCoursesPanel = createAvailableCoursePanel();
        mainContent.add(availableCoursesPanel, "AvailableCourses");

        JPanel gradesPanel = createGradePanel();
        mainContent.add(gradesPanel, "GradesofStudents");
        
        JPanel ReportPanel = createReportPanel();
        mainContent.add(ReportPanel, "Report");

        add(sidebar, BorderLayout.WEST);
        add(mainContent, BorderLayout.CENTER);

        cardLayout.show(mainContent, "Dashboard");
    }

    private void fetchStudentData() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/StudentManagementSystemDB", "root", "200369")) {
            String totalQuery = "SELECT COUNT(*) FROM tbStudent";
            String maleQuery = "SELECT COUNT(*) FROM tbStudent WHERE Gender = 'Male'";
            String femaleQuery = "SELECT COUNT(*) FROM tbStudent WHERE Gender = 'Female'";

            try (PreparedStatement totalStmt = conn.prepareStatement(totalQuery);
                 PreparedStatement maleStmt = conn.prepareStatement(maleQuery);
                 PreparedStatement femaleStmt = conn.prepareStatement(femaleQuery)) {

                ResultSet totalResult = totalStmt.executeQuery();
                if (totalResult.next()) {
                    totalStudents = totalResult.getInt(1);
                }

                ResultSet maleResult = maleStmt.executeQuery();
                if (maleResult.next()) {
                    enrolledMale = maleResult.getInt(1);
                }

                ResultSet femaleResult = femaleStmt.executeQuery();
                if (femaleResult.next()) {
                    enrolledFemale = femaleResult.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private JPanel createDashboardPanel() {
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BorderLayout());

        JPanel topStats = new JPanel();
        topStats.setLayout(new GridLayout(1, 3, 20, 0));
        topStats.setPreferredSize(new Dimension(getWidth(), 120));
        topStats.setBackground(Color.WHITE);

        String[] statLabels = {"Total Enrolled Students", "Enrolled Female Students", "Enrolled Male Students"};
        int[] statNumbers = {totalStudents, enrolledFemale, enrolledMale};
        Color[] statColors = {new Color(52, 73, 94), new Color(52, 73, 94), new Color(52, 73, 94)};

        for (int i = 0; i < statLabels.length; i++) {
            JPanel statCard = new JPanel();
            statCard.setBackground(statColors[i]);
            statCard.setLayout(new BorderLayout());

            JLabel statNumber = new JLabel(String.valueOf(statNumbers[i]), SwingConstants.CENTER);
            statNumber.setFont(new Font("Arial", Font.BOLD, 40));
            statNumber.setForeground(Color.WHITE);

            JLabel statLabel = new JLabel(statLabels[i], SwingConstants.CENTER);
            statLabel.setForeground(Color.WHITE);
            statLabel.setFont(new Font("Arial", Font.PLAIN, 16));

            statCard.add(statNumber, BorderLayout.CENTER);
            statCard.add(statLabel, BorderLayout.SOUTH);

            topStats.add(statCard);
        }

               // Announcement and Event Section
        JPanel infoSection = new JPanel();
        infoSection.setLayout(new GridLayout(1, 2, 20, 0));
        infoSection.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Announcements Panel
        JPanel announcements = new JPanel();
        announcements.setLayout(new BorderLayout());
        announcements.setBackground(Color.WHITE);
        announcements.setBorder(BorderFactory.createTitledBorder("Announcements"));
        
        ImageIcon announcementImg = new ImageIcon(getClass().getResource("/images/test.png"));
        Image scaledAnnouncement = announcementImg.getImage().getScaledInstance(300, 250, Image.SCALE_SMOOTH);
        JLabel announcementImage = new JLabel(new ImageIcon(scaledAnnouncement));
        announcementImage.setHorizontalAlignment(SwingConstants.CENTER);
        announcements.add(announcementImage, BorderLayout.CENTER);
        
        JLabel announcementText = new JLabel("Semester exams will begin on January 15th.", SwingConstants.CENTER);
        announcementText.setFont(new Font("Arial", Font.PLAIN, 14));
        announcements.add(announcementText, BorderLayout.SOUTH);

        // Upcoming Events Panel
        JPanel events = new JPanel();
        events.setLayout(new BorderLayout());
        events.setBackground(Color.WHITE);
        events.setBorder(BorderFactory.createTitledBorder("Upcoming Events"));
        
        ImageIcon eventImg = new ImageIcon(getClass().getResource("/images/games.png"));
        Image scaledEvent = eventImg.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
        JLabel eventImage = new JLabel(new ImageIcon(scaledEvent));
        eventImage.setHorizontalAlignment(SwingConstants.CENTER);
        events.add(eventImage, BorderLayout.CENTER);
        
        JLabel eventText = new JLabel("Annual sports meet - February 10th.", SwingConstants.CENTER);
        eventText.setFont(new Font("Arial", Font.PLAIN, 14));
        events.add(eventText, BorderLayout.SOUTH);

        infoSection.add(announcements);
        infoSection.add(events);

        // Add components to main content
        mainContent.add(topStats, BorderLayout.NORTH);
        mainContent.add(infoSection, BorderLayout.CENTER);

        return mainContent;
    }

    private JPanel createAddStudentPanel() {
         JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel lblSearch = new JLabel("Search:");
        lblSearch.setBounds(30, 20, 100, 30);
        panel.add(lblSearch);

        JTextField txtSearch = new JTextField();
        txtSearch.setBounds(90, 20, 400, 30);
        panel.add(txtSearch);

        JButton btnSearch = new JButton("Search");
        btnSearch.setBounds(500, 20, 100, 30);
        btnSearch.setBackground(new Color(52, 73, 94)); 
        btnSearch.setForeground(Color.WHITE);  
        btnSearch.setFocusPainted(false);
        panel.add(btnSearch);

        tableModel = new DefaultTableModel();
        studentTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setBounds(30, 70, 870, 200);
        panel.add(scrollPane);

        tableModel.addColumn("Student ID");
        tableModel.addColumn("Year");
        tableModel.addColumn("Course");
        tableModel.addColumn("First Name");
        tableModel.addColumn("Last Name");
        tableModel.addColumn("Gender");
        tableModel.addColumn("Birth Date");

        JLabel lblStudentId = new JLabel("Student #:");
        lblStudentId.setBounds(30, 300, 100, 30);
        panel.add(lblStudentId);

        txtStudentId = new JTextField();
        txtStudentId.setBounds(130, 300, 150, 30);
        panel.add(txtStudentId);

        JLabel lblYear = new JLabel("Year:");
        lblYear.setBounds(400, 350, 100, 30);
        panel.add(lblYear);

        comboYear = new JComboBox<>(new String[]{"First Year", "Second Year", "Third Year", "Fourth Year"});
        comboYear.setBounds(470, 350, 150, 30);
        panel.add(comboYear);

        JLabel lblCourse = new JLabel("Course:");
        lblCourse.setBounds(400, 300, 100, 30);
        panel.add(lblCourse);

        comboCourse = new JComboBox<>(new String[]{"DSE", "DNE", "DCSD"});
        comboCourse.setBounds(470, 300, 150, 30);
        panel.add(comboCourse);

        JLabel lblFirstName = new JLabel("First Name:");
        lblFirstName.setBounds(30, 350, 100, 30);
        panel.add(lblFirstName);

        txtFirstName = new JTextField();
        txtFirstName.setBounds(130, 350, 150, 30);
        panel.add(txtFirstName);

        JLabel lblLastName = new JLabel("Last Name:");
        lblLastName.setBounds(30, 400, 100, 30);
        panel.add(lblLastName);

        txtLastName = new JTextField();
        txtLastName.setBounds(130, 400, 150, 30);
        panel.add(txtLastName);

        JLabel lblGender = new JLabel("Gender:");
        lblGender.setBounds(400, 400, 100, 30);
        panel.add(lblGender);

        comboGender = new JComboBox<>(new String[]{"Male", "Female"});
        comboGender.setBounds(470, 400, 150, 30);
        panel.add(comboGender);

        JLabel lblBirthDate = new JLabel("Birth Date:");
        lblBirthDate.setBounds(30, 450, 100, 30);
        panel.add(lblBirthDate);

        txtBirthDate = new JTextField();
        txtBirthDate.setBounds(130, 450, 150, 30);
        panel.add(txtBirthDate);

        JButton btnAdd = new JButton("Add");
        btnAdd.setBounds(30, 550, 100, 30);
        btnAdd.setBackground(new Color(52, 73, 94)); 
        btnAdd.setForeground(Color.WHITE);  
        btnAdd.setFocusPainted(false);
        panel.add(btnAdd);

        JButton btnUpdate = new JButton("Update");
        btnUpdate.setBounds(190, 550, 100, 30);
        btnUpdate.setBackground(new Color(52, 73, 94)); 
        btnUpdate.setForeground(Color.WHITE);  
        btnUpdate.setFocusPainted(false);
        panel.add(btnUpdate);

        JButton btnDelete = new JButton("Delete");
        btnDelete.setBounds(350, 550, 100, 30);
        btnDelete.setBackground(new Color(52, 73, 94)); 
        btnDelete.setForeground(Color.WHITE);  
        btnDelete.setFocusPainted(false);
        panel.add(btnDelete);

        JButton btnClear = new JButton("Clear");
        btnClear.setBounds(510, 550, 100, 30);
        btnClear.setBackground(new Color(52, 73, 94)); 
        btnClear.setForeground(Color.WHITE);  
        btnClear.setFocusPainted(false);
        panel.add(btnClear);

        loadStudentData();
        
        btnClear.addActionListener(e -> clearInputFields());
        btnAdd.addActionListener(e -> addStudent());
        btnUpdate.addActionListener(e -> updateStudent());
        btnDelete.addActionListener(e -> deleteStudent());
        btnSearch.addActionListener(e -> {
            String searchQuery = txtSearch.getText();
            searchStudent(searchQuery);
         });

        return panel;
    }
    
    private void searchStudent(String studentId) {
    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/StudentManagementSystemDB", "root", "200369")) {
        String query = "SELECT * FROM tbStudent WHERE studentId = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, studentId);
            ResultSet rs = stmt.executeQuery();
            tableModel.setRowCount(0);  // Clear the table

            if (rs.next()) {
                // Update the input fields with the student data
                txtStudentId.setText(rs.getString("StudentId"));
                txtFirstName.setText(rs.getString("First_Name"));
                txtLastName.setText(rs.getString("Last_Name"));
                txtBirthDate.setText(rs.getString("Birth_Day"));
                comboYear.setSelectedItem(rs.getString("Year"));
                comboCourse.setSelectedItem(rs.getString("Course"));
                comboGender.setSelectedItem(rs.getString("Gender"));

                // Add the found student data to the table
                String[] row = new String[7];
                row[0] = rs.getString("StudentId");
                row[1] = rs.getString("Year");
                row[2] = rs.getString("Course");
                row[3] = rs.getString("First_Name");
                row[4] = rs.getString("Last_Name");
                row[5] = rs.getString("Gender");
                row[6] = rs.getString("Birth_Day");
                tableModel.addRow(row);
            } else {
                JOptionPane.showMessageDialog(this, "No student found with ID: " + studentId);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    private void loadStudentData() {
    tableModel.setRowCount(0); // Clear table before loading new data

    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/StudentManagementSystemDB", "root", "200369")) {
        String query = "SELECT * FROM tbStudent";
        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Object[] row = {
                    rs.getString("StudentId"),
                    rs.getString("Year"),
                    rs.getString("Course"),
                    rs.getString("First_Name"),
                    rs.getString("Last_Name"),
                    rs.getString("Gender"),
                    rs.getDate("Birth_Day")
                };
                tableModel.addRow(row);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Failed to load student data.", "Error", JOptionPane.ERROR_MESSAGE);
    }
    }
    
    private void addStudent() {
    String studentId = txtStudentId.getText();
    String year = (String) comboYear.getSelectedItem();
    String course = (String) comboCourse.getSelectedItem();
    String firstName = txtFirstName.getText();
    String lastName = txtLastName.getText();
    String gender = (String) comboGender.getSelectedItem();
    String birthDate = txtBirthDate.getText();

    if (studentId.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || birthDate.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Validation Error", JOptionPane.WARNING_MESSAGE);
        return;
    }

    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/StudentManagementSystemDB", "root", "200369")) {
        String query = "INSERT INTO tbStudent VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, studentId);
            stmt.setString(2, year);
            stmt.setString(3, course);
            stmt.setString(4, firstName);
            stmt.setString(5, lastName);
            stmt.setString(6, gender);
            stmt.setString(7, birthDate);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Student added successfully!");
            loadStudentData();
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Failed to add student. Check if ID already exists.", "Error", JOptionPane.ERROR_MESSAGE);
    }
    }

    private void updateStudent() {
    int selectedRow = studentTable.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Please select a student to update.", "Error", JOptionPane.WARNING_MESSAGE);
        return;
    }

    String studentId = txtStudentId.getText();
    String year = (String) comboYear.getSelectedItem();
    String course = (String) comboCourse.getSelectedItem();
    String firstName = txtFirstName.getText();
    String lastName = txtLastName.getText();
    String gender = (String) comboGender.getSelectedItem();
    String birthDate = txtBirthDate.getText();

    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/StudentManagementSystemDB", "root", "200369")) {
        String query = "UPDATE tbStudent SET Year=?, Course=?, First_Name=?, Last_Name=?, Gender=?, Birth_Day=? WHERE StudentId=?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, year);
            stmt.setString(2, course);
            stmt.setString(3, firstName);
            stmt.setString(4, lastName);
            stmt.setString(5, gender);
            stmt.setString(6, birthDate);
            stmt.setString(7, studentId);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Student updated successfully!");
            loadStudentData();
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Failed to update student.", "Error", JOptionPane.ERROR_MESSAGE);
    }
    }

    private void deleteStudent() {
    int selectedRow = studentTable.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Please select a student to delete.", "Error", JOptionPane.WARNING_MESSAGE);
        return;
    }

    String studentId = (String) tableModel.getValueAt(selectedRow, 0);

    int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this student?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
    if (confirm != JOptionPane.YES_OPTION) {
        return;
    }

    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/StudentManagementSystemDB", "root", "200369")) {
        String query = "DELETE FROM tbStudent WHERE StudentId=?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, studentId);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Student deleted successfully!");
            loadStudentData();
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Failed to delete student.", "Error", JOptionPane.ERROR_MESSAGE);
    }
    }
    
    private void clearInputFields() {
    txtStudentId.setText("");
    txtFirstName.setText("");
    txtLastName.setText("");
    txtBirthDate.setText("");
    comboYear.setSelectedIndex(0);  
    comboCourse.setSelectedIndex(0);  
    comboGender.setSelectedIndex(0); 
    }

    private JPanel createAvailableCoursePanel() {
        JPanel course = new JPanel(new BorderLayout());

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputPanel.add(new JLabel("Course:"));
        courseField = new JTextField();
        inputPanel.add(courseField);

        inputPanel.add(new JLabel("Description:"));
        descriptionField = new JTextField();
        inputPanel.add(descriptionField);

        inputPanel.add(new JLabel("Degree:"));
        degreeField = new JTextField();
        inputPanel.add(degreeField);

        course.add(inputPanel, BorderLayout.NORTH);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 100, 30));
        JButton addButton = new JButton("Add");
        addButton.setPreferredSize(new Dimension(100, 30));
        addButton.setBackground(new Color(52, 73, 94));
        addButton.setForeground(Color.WHITE);  
        addButton.setFocusPainted(false);
        addButton.setPreferredSize(new Dimension(100, 30));
        JButton updateButton = new JButton("Update");
        updateButton.setPreferredSize(new Dimension(100, 30));
        updateButton.setBackground(new Color(52, 73, 94));
        updateButton.setForeground(Color.WHITE);  
        updateButton.setFocusPainted(false);
        updateButton.setPreferredSize(new Dimension(100, 30));
        JButton deleteButton = new JButton("Delete");
        deleteButton.setPreferredSize(new Dimension(100, 30));
        deleteButton.setBackground(new Color(52, 73, 94));
        deleteButton.setForeground(Color.WHITE);  
        deleteButton.setFocusPainted(false);
        deleteButton.setPreferredSize(new Dimension(100, 30));
        JButton clearButton = new JButton("Clear");
        clearButton.setPreferredSize(new Dimension(100, 30));
        clearButton.setBackground(new Color(52, 73, 94));
        clearButton.setForeground(Color.WHITE);  
        clearButton.setFocusPainted(false);
        clearButton.setPreferredSize(new Dimension(100, 30));

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        course.add(buttonPanel, BorderLayout.CENTER);

        // Table Panel
        tableCourse = new DefaultTableModel(new String[]{"Course", "Description", "Degree"}, 0);
        courseTable = new JTable(tableCourse);
        JScrollPane tableScrollPane = new JScrollPane(courseTable);
        course.add(tableScrollPane, BorderLayout.SOUTH);

        // Load data into table
        loadCourseData();

        // Add Listeners
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addCourse();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateCourse();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteCourse();
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFields();
            }
        });

        courseTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = courseTable.getSelectedRow();
                if (selectedRow != -1) {
                    courseField.setText(tableCourse.getValueAt(selectedRow, 0).toString());
                    descriptionField.setText(tableCourse.getValueAt(selectedRow, 1).toString());
                    degreeField.setText(tableCourse.getValueAt(selectedRow, 2).toString());
                }
            }
        });

        return course;
    }

    private void loadCourseData() {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM tbCourse");
             ResultSet rs = stmt.executeQuery()) {

            tableCourse.setRowCount(0);
            while (rs.next()) {
                tableCourse.addRow(new Object[]{
                        rs.getString("Course_Name"),
                        rs.getString("Description"),
                        rs.getString("Degree")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addCourse() {
        String course = courseField.getText();
        String description = descriptionField.getText();
        String degree = degreeField.getText();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO tbCourse (Course_Name, Description, Degree) VALUES (?, ?, ?)");) {

            stmt.setString(1, course);
            stmt.setString(2, description);
            stmt.setString(3, degree);
            stmt.executeUpdate();
            loadCourseData();
            clearFields();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateCourse() {
        int selectedRow = courseTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a course to update.");
            return;
        }

        String course = courseField.getText();
        String description = descriptionField.getText();
        String degree = degreeField.getText();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE tbCourse SET Description = ?, Degree = ? WHERE Course_Name = ?")) {

            stmt.setString(1, description);
            stmt.setString(2, degree);
            stmt.setString(3, course);
            stmt.executeUpdate();
            loadCourseData();
            clearFields();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteCourse() {
        int selectedRow = courseTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a course to delete.");
            return;
        }

        String course = tableCourse.getValueAt(selectedRow, 0).toString();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM tbCourse WHERE Course_Name = ?")) {

            stmt.setString(1, course);
            stmt.executeUpdate();
            loadCourseData();
            clearFields();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        courseField.setText("");
        descriptionField.setText("");
        degreeField.setText("");
    }

    private Connection getConnection() throws Exception {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/StudentManagementSystemDB", "root", "200369");
    }

    public JPanel createGradePanel() {
        JPanel gradePanel = new JPanel(new BorderLayout(10, 10));

        // Sidebar and Input Fields Panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));

        // Input Fields
        JLabel studentIdLabel = new JLabel("Student ID:");
        JTextField studentIdField = new JTextField(20);
        studentIdField.setMaximumSize(studentIdField.getPreferredSize());
        studentIdField.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel yearLabel = new JLabel("Year:");
        // JComboBox for Year selection
        String[] years = {"First Year", "Second Year", "Third Year", "Fourth Year"};
        JComboBox<String> yearComboBox = new JComboBox<>(years);
        yearComboBox.setMaximumSize(new Dimension(230, yearComboBox.getPreferredSize().height));
        
         yearComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setHorizontalAlignment(SwingConstants.CENTER); // Center align the text
                return label;
            }
        });

        JLabel courseLabel = new JLabel("Course:");
        JTextField courseField = new JTextField(20);
        courseField.setMaximumSize(courseField.getPreferredSize()); 
        courseField.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel firstSemLabel = new JLabel("First Sem:");
        JTextField firstSemField = new JTextField(20);
        firstSemField.setMaximumSize(firstSemField.getPreferredSize()); 
        firstSemField.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel secondSemLabel = new JLabel("Second Sem:");
        JTextField secondSemField = new JTextField(20);
        secondSemField.setMaximumSize(secondSemField.getPreferredSize()); 
        secondSemField.setHorizontalAlignment(SwingConstants.CENTER);

        // Buttons
        JButton clearButton = new JButton("Clear");
        clearButton.setPreferredSize(new Dimension(100, 30));
        clearButton.setBackground(new Color(52, 73, 94));
        clearButton.setForeground(Color.WHITE);

        JButton updateButton = new JButton("Update");
        updateButton.setPreferredSize(new Dimension(100, 30));
        updateButton.setBackground(new Color(52, 73, 94));
        updateButton.setForeground(Color.WHITE);

        JButton addButton = new JButton("Add");
        addButton.setPreferredSize(new Dimension(100, 50));
        addButton.setBackground(new Color(52, 73, 94));
        addButton.setForeground(Color.WHITE);

        // Add components to input panel
        inputPanel.add(studentIdLabel);
        inputPanel.add(studentIdField);
        inputPanel.add(Box.createVerticalStrut(10));

        inputPanel.add(yearLabel);
        inputPanel.add(yearComboBox); 
        inputPanel.add(Box.createVerticalStrut(10));

        inputPanel.add(courseLabel);
        inputPanel.add(courseField);
        inputPanel.add(Box.createVerticalStrut(10));

        inputPanel.add(firstSemLabel);
        inputPanel.add(firstSemField);
        inputPanel.add(Box.createVerticalStrut(10));

        inputPanel.add(secondSemLabel);
        inputPanel.add(secondSemField);
        inputPanel.add(Box.createVerticalStrut(20));

        inputPanel.add(clearButton);
        inputPanel.add(Box.createVerticalStrut(10));
        inputPanel.add(updateButton);
        inputPanel.add(Box.createVerticalStrut(10));
        inputPanel.add(addButton);

        // Table Panel
        String[] columnNames = {"Student ID", "Year", "Course", "First Sem", "Second Sem"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable gradeTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(gradeTable);

        // Add listeners
        gradeTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && gradeTable.getSelectedRow() != -1) {
                int selectedRow = gradeTable.getSelectedRow();
                studentIdField.setText(tableModel.getValueAt(selectedRow, 0).toString());
                yearComboBox.setSelectedItem(tableModel.getValueAt(selectedRow, 1).toString()); // Set the selected year
                courseField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                firstSemField.setText(tableModel.getValueAt(selectedRow, 3).toString());
                secondSemField.setText(tableModel.getValueAt(selectedRow, 4).toString());
            }
        });

        clearButton.addActionListener((ActionEvent e) -> {
            studentIdField.setText("");
            yearComboBox.setSelectedIndex(0); 
            courseField.setText("");
            firstSemField.setText("");
            secondSemField.setText("");
        });

        updateButton.addActionListener(e -> {
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/StudentManagementSystemDB", "root", "200369")) {
                String query = "UPDATE tbGrade SET Year = ?, Course = ?, First_Sem = ?, Second_Sem = ? WHERE Sid = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, yearComboBox.getSelectedItem().toString()); 
                stmt.setString(2, courseField.getText());
                stmt.setDouble(3, Double.parseDouble(firstSemField.getText()));
                stmt.setDouble(4, Double.parseDouble(secondSemField.getText()));
                stmt.setString(5, studentIdField.getText());
                stmt.executeUpdate();

                // Refresh table
                int selectedRow = gradeTable.getSelectedRow();
                tableModel.setValueAt(yearComboBox.getSelectedItem().toString(), selectedRow, 1);
                tableModel.setValueAt(courseField.getText(), selectedRow, 2);
                tableModel.setValueAt(firstSemField.getText(), selectedRow, 3);
                tableModel.setValueAt(secondSemField.getText(), selectedRow, 4);

                JOptionPane.showMessageDialog(null, "Grade updated successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            }
        });

        addButton.addActionListener(e -> {
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/StudentManagementSystemDB", "root", "200369")) {
                String query = "INSERT INTO tbGrade (Sid, Year, Course, First_Sem, Second_Sem) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, studentIdField.getText());
                stmt.setString(2, yearComboBox.getSelectedItem().toString()); // Get selected year from JComboBox
                stmt.setString(3, courseField.getText());
                stmt.setDouble(4, Double.parseDouble(firstSemField.getText()));
                stmt.setDouble(5, Double.parseDouble(secondSemField.getText()));
                stmt.executeUpdate();

                // Refresh table
                tableModel.addRow(new Object[]{
                    studentIdField.getText(),
                    yearComboBox.getSelectedItem().toString(),
                    courseField.getText(),
                    firstSemField.getText(),
                    secondSemField.getText()
                });

                JOptionPane.showMessageDialog(null, "Grade added successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            }
        });

        // Fetch data from the database and populate the table
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/StudentManagementSystemDB", "root", "200369")) {
            String query = "SELECT Sid, Year, Course, First_Sem, Second_Sem FROM tbGrade";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getString("Sid"),
                    rs.getString("Year"),
                    rs.getString("Course"),
                    rs.getDouble("First_Sem"),
                    rs.getDouble("Second_Sem")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error fetching data: " + ex.getMessage());
        }

        // Add components to the grade panel
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(inputPanel, BorderLayout.WEST);
        rightPanel.add(tableScrollPane, BorderLayout.CENTER);

        // Add the panel to the main frame
        gradePanel.add(rightPanel, BorderLayout.CENTER);

        return gradePanel;
    }
    
    private JPanel createReportPanel() {
    JPanel reportPanel = new JPanel();
    reportPanel.setLayout(new BorderLayout());

    Connection conn = null;

    try {
        // Load database driver
        Class.forName("com.mysql.cj.jdbc.Driver");

        // Establish connection
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/StudentManagementSystemDB", "root", "200369");

        // Load report file (use relative path or resource location)
        String reportPath = "C:\\Users\\ASUS\\Documents\\NetBeansProjects\\StudentManagementSystemProject\\src\\main\\resources\\Reports\\Students.jrxml";
        JasperReport jr = JasperCompileManager.compileReport(reportPath);

        // Fill the report
        JasperPrint jp = JasperFillManager.fillReport(jr, null, conn);

        // Create a viewer and add it to the panel
        JRViewer viewer = new JRViewer(jp);
        reportPanel.add(viewer, BorderLayout.CENTER);

    } catch (ClassNotFoundException e) {
        JOptionPane.showMessageDialog(reportPanel, "Database driver not found: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(reportPanel, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(reportPanel, "Error generating report: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    } finally {
        // Close the connection
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(reportPanel, "Failed to close connection: " + e.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    return reportPanel;
}
    
    private class SidebarActionListener implements ActionListener {
        private final String menuItem;

        public SidebarActionListener(String menuItem) {
            this.menuItem = menuItem;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (menuItem) {
                case "Dashboard":
                    cardLayout.show(mainContent, "Dashboard");
                    break;
                case "Add Students":
                    cardLayout.show(mainContent, "AddStudents");
                    break;
                case "Available Courses":
                    cardLayout.show(mainContent, "AvailableCourses");
                    break;
                case "Grades of Students":
                    cardLayout.show(mainContent, "GradesofStudents");
                    break;
                case "Report":
                    cardLayout.show(mainContent, "Report");
                    break;
                case "Sign Out":
                    dispose();  // Close the login window
                    LoginForm login = new LoginForm();
                    login.setVisible(true);
                    break;
            }
        }
    }
}
