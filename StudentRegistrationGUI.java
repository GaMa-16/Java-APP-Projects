import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StudentRegistrationGUI extends JFrame implements ActionListener {

    // GUI Components
    private JLabel nameLabel, courseLabel, phoneLabel, addressLabel;
    private JTextField nameField, courseField, phoneField;
    private JTextArea addressArea;
    private JButton submitButton;

    public StudentRegistrationGUI() {
        super("Student Registration Form (GUI)");
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // --- 1. Initialize Components ---
        nameLabel = new JLabel("Name:");
        courseLabel = new JLabel("Course:");
        phoneLabel = new JLabel("Phone (10 digits):");
        addressLabel = new JLabel("Address:");

        nameField = new JTextField(20);
        courseField = new JTextField(20);
        phoneField = new JTextField(20);
        addressArea = new JTextArea(3, 20);
        addressArea.setBorder(BorderFactory.createLineBorder(Color.gray));

        submitButton = new JButton("Submit Registration");
        submitButton.addActionListener(this);

        // --- 2. Layout Management (GridBagLayout) ---
        
        // Name Row
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST; add(nameLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST; add(nameField, gbc);

        // Course Row
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST; add(courseLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.anchor = GridBagConstraints.WEST; add(courseField, gbc);

        // Phone Row
        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST; add(phoneLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.anchor = GridBagConstraints.WEST; add(phoneField, gbc);

        // Address Row
        gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.NORTHWEST; add(addressLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.anchor = GridBagConstraints.WEST; add(new JScrollPane(addressArea), gbc);

        // Submit Button Row
        gbc.gridx = 1; gbc.gridy = 4; gbc.anchor = GridBagConstraints.CENTER; add(submitButton, gbc);

        // --- 3. Frame Setup ---
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack(); // Adjusts window size based on components
        setLocationRelativeTo(null); // Center the frame
        setVisible(true);
    }
    
    // --- 4. Event Handling (Button Click) ---
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitButton) {
            if (validateInput()) {
                // Since no database is required, we just display success
                displaySuccess();
            }
        }
    }

    // --- 5. Input Validation ---
    private boolean validateInput() {
        String name = nameField.getText().trim();
        String course = courseField.getText().trim();
        String phone = phoneField.getText().trim();

        // Check 1: Mandatory fields (Name and Course)
        if (name.isEmpty() || course.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name and Course fields are mandatory.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Check 2: Simple Phone number validation (exactly 10 digits)
        if (!phone.isEmpty() && !phone.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(this, "Phone number must be exactly 10 digits.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    // --- 6. Success Message and Clear Fields ---
    private void displaySuccess() {
        String name = nameField.getText().trim();
        String course = courseField.getText().trim();
        
        // Display confirmation
        JOptionPane.showMessageDialog(this, 
            "Successfully Registered:\nName: " + name + "\nCourse: " + course, 
            "Registration Complete", 
            JOptionPane.INFORMATION_MESSAGE
        );
        
        // Clear fields
        nameField.setText("");
        courseField.setText("");
        phoneField.setText("");
        addressArea.setText("");
    }

    public static void main(String[] args) {
        // Run the GUI creation on the Event Dispatch Thread (Swing best practice)
        SwingUtilities.invokeLater(() -> new StudentRegistrationGUI());
    }
}