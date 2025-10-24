import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

public class BankGUI extends JFrame implements ActionListener {

    // --- Frutiger Aero Colors ---
    private static final Color FA_LIGHT_BLUE = new Color(180, 220, 255);      // Background
    private static final Color FA_DISPLAY_BG = new Color(220, 255, 220);      // Balance Display Background
    private static final Color FA_DEPOSIT_GREEN = new Color(100, 200, 0);     // Deposit button
    private static final Color FA_WITHDRAW_RED = new Color(255, 100, 100);    // Withdraw button

    private BankAccount account;
    private JLabel balanceLabel;
    private JTextField amountField;

    // Currency formatter
    private final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);

    /**
     * Inner Class: The OOP Core for Bank Account Management
     */
    private static class BankAccount {
        private final String accountHolder;
        private final String accountNumber;
        private BigDecimal balance;

        public BankAccount(String holder, String number, double initialBalance) {
            this.accountHolder = holder;
            this.accountNumber = number;
            this.balance = new BigDecimal(initialBalance).setScale(2, RoundingMode.HALF_UP);
        }

        public boolean deposit(double amount) {
            if (amount > 0) {
                BigDecimal depositAmount = new BigDecimal(amount).setScale(2, RoundingMode.HALF_UP);
                balance = balance.add(depositAmount);
                return true;
            }
            return false;
        }

        public boolean withdraw(double amount) {
            if (amount > 0) {
                BigDecimal withdrawalAmount = new BigDecimal(amount).setScale(2, RoundingMode.HALF_UP);
                if (balance.compareTo(withdrawalAmount) >= 0) {
                    balance = balance.subtract(withdrawalAmount);
                    return true;
                }
            }
            return false; 
        }

        public BigDecimal getBalance() {
            return balance;
        }
        
        public String getAccountHolder() {
            return accountHolder;
        }
        
        public String getAccountNumber() {
            return accountNumber;
        }
    }

    /**
     * Constructor sets up the entire GUI.
     */
    public BankGUI(BankAccount account) {
        this.account = account;
        setTitle("Frutiger Aero Bank Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(15, 15)); 

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createBalancePanel(), BorderLayout.CENTER);
        add(createTransactionPanel(), BorderLayout.SOUTH);

        getContentPane().setBackground(FA_LIGHT_BLUE);
        getRootPane().setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        pack();
        setSize(450, 480);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    // --- GUI Panel Creation Methods (Frutiger Aero Styling) ---

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new GridLayout(2, 1));
        header.setOpaque(false);
        
        JLabel titleLabel = new JLabel("Welcome, " + account.getAccountHolder());
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(0, 50, 150));

        String accNum = account.getAccountNumber();
        JLabel numberLabel = new JLabel("Account: ****" + accNum.substring(accNum.length() - 4));
        numberLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        numberLabel.setForeground(new Color(0, 100, 200));

        header.add(titleLabel);
        header.add(numberLabel);
        return header;
    }

    private JPanel createBalancePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        
        // Display Container with Glassy Look
        JPanel displayContainer = new JPanel(new BorderLayout());
        displayContainer.setBackground(FA_DISPLAY_BG);
        displayContainer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 190, 150), 3), // 3D/Shadow border
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel staticLabel = new JLabel("Current Balance:");
        staticLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        staticLabel.setHorizontalAlignment(SwingConstants.CENTER);
        staticLabel.setForeground(new Color(0, 100, 0));

        balanceLabel = new JLabel(currencyFormatter.format(account.getBalance()));
        balanceLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        balanceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        balanceLabel.setForeground(new Color(0, 150, 0));

        displayContainer.add(staticLabel, BorderLayout.NORTH);
        displayContainer.add(balanceLabel, BorderLayout.CENTER);
        
        panel.add(displayContainer, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createTransactionPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setOpaque(false);

        // Amount Input Field
        amountField = new JTextField();
        amountField.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        amountField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 190, 255), 2), 
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        amountField.setBackground(Color.WHITE);
        amountField.setForeground(Color.BLACK);
        amountField.setText("0.00");
        mainPanel.add(amountField, BorderLayout.NORTH);

        // Buttons
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        buttonPanel.setOpaque(false);

        JButton depositButton = new JButton("Deposit");
        depositButton.setActionCommand("deposit");
        GlassButtonPanel depositGlassPanel = new GlassButtonPanel(depositButton, FA_DEPOSIT_GREEN);
        buttonPanel.add(depositGlassPanel);
        depositButton.addActionListener(this);

        JButton withdrawButton = new JButton("Withdraw");
        withdrawButton.setActionCommand("withdraw");
        GlassButtonPanel withdrawGlassPanel = new GlassButtonPanel(withdrawButton, FA_WITHDRAW_RED);
        buttonPanel.add(withdrawGlassPanel);
        withdrawButton.addActionListener(this);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        return mainPanel;
    }
    
    // --- Custom JPanel for Frutiger Aero Button Aesthetics ---
    private class GlassButtonPanel extends JPanel {
        private final JButton button;
        private final Color baseColor;

        public GlassButtonPanel(JButton btn, Color color) {
            setLayout(new BorderLayout());
            setOpaque(false);
            this.button = btn;
            this.baseColor = color;
            
            button.setFont(new Font("Segoe UI", Font.BOLD, 18));
            button.setForeground(Color.WHITE); 
            button.setBorder(BorderFactory.createEmptyBorder());
            button.setContentAreaFilled(false);
            button.setFocusPainted(false);
            
            add(button, BorderLayout.CENTER);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int w = getWidth();
            int h = getHeight();
            int arc = 20;

            // 1. Paint the Main Gradient (Glossy 'Gel' Effect)
            Color topColor = baseColor.brighter();
            Color bottomColor = baseColor.darker();
            
            GradientPaint gp = new GradientPaint(0, 0, topColor, 0, h, bottomColor);
            g2.setPaint(gp);
            g2.fill(new RoundRectangle2D.Float(0, 0, w - 1, h - 1, arc, arc));

            // 2. Paint the Reflection/Highlight
            g2.setPaint(new Color(255, 255, 255, 120)); 
            g2.fill(new java.awt.geom.Ellipse2D.Double(w * 0.1, 0, w * 0.8, h * 0.3));

            // 3. Paint the Border/Shadow
            g2.setColor(bottomColor.darker());
            g2.draw(new RoundRectangle2D.Float(0, 0, w - 1, h - 1, arc, arc));

            g2.dispose();
        }
    }


    // --- Action Handling ---

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        String amountText = amountField.getText().trim();

        if (amountText.isEmpty() || amountText.equals("0.00") || amountText.equals("0")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid amount.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double amount = Double.parseDouble(amountText);
            boolean success = false;
            String message;

            if (command.equals("deposit")) {
                success = account.deposit(amount);
                message = success ? "Deposit successful!" : "Invalid deposit amount.";
            } else if (command.equals("withdraw")) {
                success = account.withdraw(amount);
                if (success) {
                    message = "Withdrawal successful!";
                } else {
                    message = "Withdrawal failed: Insufficient funds.";
                }
            } else {
                return;
            }

            updateBalanceDisplay();
            JOptionPane.showMessageDialog(this, message, success ? "Success" : "Failure", success ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE);
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number format for amount.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        
        amountField.setText("0.00");
    }

    private void updateBalanceDisplay() {
        balanceLabel.setText(currencyFormatter.format(account.getBalance()));
    }

    /**
     * Main entry point to run the application.
     */
    public static void main(String[] args) {
        // Create the BankAccount object and launch the GUI
        BankAccount myAccount = new BankAccount("Alex F. Aero", "1234567890", 5000.75);
        SwingUtilities.invokeLater(() -> new BankGUI(myAccount));
    }
}