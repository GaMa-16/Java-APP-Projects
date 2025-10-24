import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.awt.geom.Ellipse2D; // <-- This is the required fix!

public class FrutigerAeroCalculator extends JFrame implements ActionListener {

    // --- Frutiger Aero Color Palette ---
    private static final Color FA_LIGHT_BLUE = new Color(180, 220, 255); // Main background
    private static final Color FA_GLASS_BUTTON = new Color(230, 245, 255); // Button base
    private static final Color FA_ACCENT_BLUE = new Color(0, 150, 255); // Operator accent
    private static final Color FA_ACCENT_GREEN = new Color(100, 200, 0); // Equals accent
    private static final Color FA_DISPLAY_BG = new Color(200, 240, 200); // Display background (optimistic green tint)

    // --- GUI Components ---
    private JTextField display;

    // --- Calculation State Variables ---
    private double currentResult = 0;
    private String lastOperation = "=";
    private boolean isStartingNewNumber = true;

    // --- Button Labels for the Grid ---
    private final String[] buttonLabels = {
            "AC", "+/-", "%", "÷",
            "7", "8", "9", "×",
            "4", "5", "6", "-",
            "1", "2", "3", "+",
            "0", ".", "="
    };

    /**
     * Constructor sets up the entire GUI.
     */
    public FrutigerAeroCalculator() {
        setTitle("Aero Calculator");
        // Use the UIManager to try and give a native OS look, but custom paint is key for FA
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
             // Fallback to default Swing Look and Feel
        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(5, 5)); // Add small gaps for separation

        // 1. Setup Display (North)
        setupDisplay();
        add(display, BorderLayout.NORTH);

        // 2. Setup Buttons (Center)
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.CENTER);

        // 3. Finalize Frame
        getContentPane().setBackground(FA_LIGHT_BLUE);
        // Add a padding border to the content pane for the glass effect to breathe
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 
        pack();
        setSize(320, 500); 
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Configures the FA-style display text field.
     */
    private void setupDisplay() {
        display = new JTextField("0");
        display.setEditable(false);
        display.setBackground(FA_DISPLAY_BG);
        display.setForeground(new Color(0, 50, 0)); // Dark green text
        display.setFont(new Font("Segoe UI", Font.BOLD, 40)); // Segoe UI is often associated with FA
        display.setHorizontalAlignment(JTextField.RIGHT);
        
        // Add a subtle drop shadow/3D effect using borders
        display.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 190, 150), 2), // Darker inner border
            BorderFactory.createEmptyBorder(15, 10, 15, 10) // Padding
        ));
    }

    /**
     * Creates and populates the JPanel for buttons.
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 4, 8, 8)); // Larger gaps for floating buttons
        panel.setBackground(FA_LIGHT_BLUE); 

        for (String label : buttonLabels) {
            // Use the custom panel to handle the glossy paint job
            GlassButtonPanel buttonPanel = new GlassButtonPanel(label);
            panel.add(buttonPanel);
            
            // The actual button component is inside the panel
            JButton button = buttonPanel.getButton();
            button.addActionListener(this); 

            // Add placeholder for the last row layout cheat
            if (label.equals("=")) {
                GlassButtonPanel placeholderPanel = new GlassButtonPanel("");
                placeholderPanel.setVisible(false);
                panel.add(placeholderPanel);
            }
        }
        return panel;
    }
    
    // --- Custom JPanel for Frutiger Aero Button Aesthetics ---
    private class GlassButtonPanel extends JPanel {
        private final JButton button;
        private final Color baseColor;

        public GlassButtonPanel(String label) {
            setLayout(new BorderLayout());
            setOpaque(false); // Crucial to allow the custom paint
            
            // Determine base color based on label type
            if (label.matches("[+\\-×÷]")) {
                baseColor = FA_ACCENT_BLUE;
            } else if (label.equals("=")) {
                baseColor = FA_ACCENT_GREEN;
            } else if (label.matches("AC|\\+/-|%")) {
                baseColor = new Color(150, 200, 255); // Lighter utility blue
            } else {
                baseColor = FA_GLASS_BUTTON; // Numbers & Decimal
            }

            // Create the invisible button component
            button = new JButton(label);
            button.setFont(new Font("Arial", Font.BOLD, 22));
            button.setForeground(Color.BLACK); // Dark text for contrast
            button.setBorder(BorderFactory.createEmptyBorder());
            button.setContentAreaFilled(false); // Make content area invisible
            button.setFocusPainted(false);
            
            // Special styling for '0'
            if (label.equals("0")) {
                button.setHorizontalAlignment(SwingConstants.LEFT);
                button.setMargin(new Insets(0, 20, 0, 0)); 
            }

            add(button, BorderLayout.CENTER);
        }

        public JButton getButton() {
            return button;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int w = getWidth();
            int h = getHeight();
            int arc = 25; // Rounded corners for that soft FA look

            // 1. Paint the Main Gradient (The Glossy 'Gel' Effect)
            // Use a vertical gradient from the base color to a darker shade
            Color topColor = baseColor;
            Color bottomColor = baseColor.darker().darker();
            
            GradientPaint gp = new GradientPaint(
                0, 0, topColor, 
                0, h, bottomColor
            );
            g2.setPaint(gp);
            g2.fill(new RoundRectangle2D.Float(0, 0, w - 1, h - 1, arc, arc));

            // 2. Paint the Reflection/Highlight (Skeuomorphic Shine)
            // A semi-transparent white oval at the top
            g2.setPaint(new Color(255, 255, 255, 180)); // Semi-transparent white
            g2.fill(new Ellipse2D.Double(w * 0.1, 0, w * 0.8, h * 0.3));

            // 3. Paint the Border/Shadow (3D Pop)
            g2.setColor(bottomColor.darker());
            g2.draw(new RoundRectangle2D.Float(0, 0, w - 1, h - 1, arc, arc));

            g2.dispose();
        }
    }


    // --- Action Listener and Core Logic (Unchanged from previous version) ---

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (command.matches("[0-9]")) {
            handleNumber(command);
        } else if (command.equals(".")) {
            handleDecimal(command);
        } else if (command.matches("[+\\-×÷]")) {
            handleOperator(command.replace('×', '*').replace('÷', '/')); 
        } else if (command.equals("=")) {
            handleEquals();
        } else if (command.equals("AC")) {
            handleClear();
        } else if (command.equals("+/-")) {
            handleSignChange();
        } else if (command.equals("%")) {
            handlePercentage();
        }
    }

    private void handleNumber(String digit) {
        if (isStartingNewNumber) {
            display.setText(digit.equals("0") ? "0" : digit);
            isStartingNewNumber = false;
        } else if (display.getText().length() < 12) {
            if (display.getText().equals("0")) {
                display.setText(digit);
            } else {
                display.setText(display.getText() + digit);
            }
        }
    }

    private void handleDecimal(String decimal) {
        if (isStartingNewNumber) {
            display.setText("0.");
            isStartingNewNumber = false;
        } else if (!display.getText().contains(".")) {
            display.setText(display.getText() + decimal);
        }
    }

    private void handleOperator(String operator) {
        if (!isStartingNewNumber || lastOperation.equals("=")) {
            calculate(Double.parseDouble(display.getText()));
        }
        lastOperation = operator;
        isStartingNewNumber = true;
    }

    private void handleEquals() {
        calculate(Double.parseDouble(display.getText()));
        lastOperation = "=";
        isStartingNewNumber = true;
    }

    private void handleClear() {
        currentResult = 0;
        lastOperation = "=";
        isStartingNewNumber = true;
        display.setText("0");
    }

    private void handleSignChange() {
        try {
            double value = Double.parseDouble(display.getText());
            display.setText(formatResult(-value));
        } catch (NumberFormatException ex) { /* Ignore */ }
    }

    private void handlePercentage() {
        try {
            double value = Double.parseDouble(display.getText());
            double percentage = value / 100.0;
            display.setText(formatResult(percentage));
        } catch (NumberFormatException ex) { /* Ignore */ }
    }

    private void calculate(double secondOperand) {
        try {
            switch (lastOperation) {
                case "+": currentResult += secondOperand; break;
                case "-": currentResult -= secondOperand; break;
                case "*": currentResult *= secondOperand; break;
                case "/":
                    if (secondOperand != 0) { currentResult /= secondOperand; } 
                    else { display.setText("Error"); currentResult = 0; lastOperation = "="; return; }
                    break;
                case "=": currentResult = secondOperand; break;
            }
            display.setText(formatResult(currentResult));
        } catch (Exception ex) {
            display.setText("Error");
            currentResult = 0; lastOperation = "=";
        }
    }
    
    private String formatResult(double result) {
        if (result == Math.floor(result) && result < 1e12 && result > -1e12) {
            return String.format("%.0f", result);
        } else {
            return String.valueOf(result);
        }
    }

    /**
     * Main method to run the application.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(FrutigerAeroCalculator::new);
    }
}