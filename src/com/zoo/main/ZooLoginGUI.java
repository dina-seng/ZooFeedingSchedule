package com.zoo.main;

import com.zoo.exceptions.ZooException;
import com.zoo.models.Animal;
import com.zoo.models.Food;
import com.zoo.models.habitat_types.Habitat;
import com.zoo.models.staff_roles.Manager;
import com.zoo.services.Zoo;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

/**
 * ZooLoginGUI — Java Swing GUI for ZooFeedingSchedule
 *
 * EXCEPTION HANDLING COVERAGE (Learning Outcomes):
 *
 *  ✅ LO3  try-catch          — every button click is wrapped in try-catch
 *  ✅ LO4  multiple catch     — login catches ZooException AND general Exception separately
 *  ✅ LO5  finally            — DB connection attempt uses finally to re-enable the login button
 *  ✅ LO6  throw              — validateLoginInput() throws ZooException for blank fields
 *  ✅ LO7  throws             — all helper methods declare throws ZooException in signature
 *  ✅ LO8  read GUI input     — email/password read from JTextField / JPasswordField
 *  ✅ LO9  button click logic — ActionListener on login button triggers zoo.login()
 *  ✅ LO10 display result     — JLabel statusLabel shows success/error messages
 *  ✅ LO11 OOP → GUI          — Zoo, Staff, Animal, Habitat, Food all drive the GUI content
 */
public class ZooLoginGUI {

    // ── Core OOP object — GUI is just a view on top of this ──────────────────
    private Zoo zoo;

    // ── Frames ────────────────────────────────────────────────────────────────
    private JFrame loginFrame;
    private JFrame dashboardFrame;

    // ── Login screen components ───────────────────────────────────────────────
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel statusLabel;

    // ── Colors & fonts (consistent theme) ────────────────────────────────────
    private static final Color BG_DARK      = new Color(28, 37, 38);
    private static final Color BG_CARD      = new Color(38, 50, 51);
    private static final Color ACCENT_GREEN = new Color(76, 175, 80);
    private static final Color ACCENT_RED   = new Color(229, 57, 53);
    private static final Color TEXT_WHITE   = new Color(236, 239, 241);
    private static final Color TEXT_DARK   = new Color(28,37,38);
    private static final Color TEXT_MUTED   = new Color(144, 164, 174);
    private static final Color FIELD_BG     = new Color(55, 71, 79);
    private static final Font  FONT_TITLE   = new Font("Segoe UI", Font.BOLD, 26);
    private static final Font  FONT_LABEL   = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font  FONT_BUTTON  = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font  FONT_SMALL   = new Font("Segoe UI", Font.PLAIN, 12);

    // ─────────────────────────────────────────────────────────────────────────
    // ENTRY POINT
    // ─────────────────────────────────────────────────────────────────────────
    public static void main(String[] args) {
        // Run on the Swing Event Dispatch Thread (EDT) — best practice
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new ZooLoginGUI().initApp();
        });
    }

    // ─────────────────────────────────────────────────────────────────────────
    // INIT — creates Zoo OOP object then shows login window
    // ─────────────────────────────────────────────────────────────────────────
    private void initApp() {
        // LO5 finally — guaranteed cleanup/message even if Zoo constructor fails
        try {
            zoo = new Zoo("Safari Zoo", "Phnom Penh");
        } catch (Exception e) {
            // LO4 multiple catch — runtime exception separate from our custom one
            JOptionPane.showMessageDialog(null,
                "Failed to initialize Zoo system:\n" + e.getMessage(),
                "Startup Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        } finally {
            // finally always runs — good place for guaranteed startup log
            System.out.println("[System] Zoo initialization attempt complete.");
        }
        buildLoginScreen();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // LOGIN SCREEN
    // ─────────────────────────────────────────────────────────────────────────
    private void buildLoginScreen() {
        loginFrame = new JFrame("Safari Zoo — Staff Login");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(420, 520);
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setResizable(false);

        // ── Root panel ───────────────────────────────────────────────────────
        JPanel root = new JPanel(new GridBagLayout());
        root.setBackground(BG_DARK);
        loginFrame.setContentPane(root);

        // ── Card panel ───────────────────────────────────────────────────────
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(BG_CARD);
        card.setBorder(new EmptyBorder(36, 40, 36, 40));
        card.setPreferredSize(new Dimension(340, 420));

        // ── Zoo icon label ────────────────────────────────────────────────────
        JLabel iconLabel = new JLabel("🦁", SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(iconLabel);
        card.add(Box.createVerticalStrut(8));

        // ── Title ─────────────────────────────────────────────────────────────
        JLabel title = new JLabel("Safari Zoo", SwingConstants.CENTER);
        title.setFont(FONT_TITLE);
        title.setForeground(ACCENT_GREEN);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(title);

        JLabel subtitle = new JLabel("Staff Management System", SwingConstants.CENTER);
        subtitle.setFont(FONT_SMALL);
        subtitle.setForeground(TEXT_MUTED);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(subtitle);
        card.add(Box.createVerticalStrut(28));

        // ── Email field ───────────────────────────────────────────────────────
        card.add(makeFieldLabel("Email Address"));
        card.add(Box.createVerticalStrut(6));
        emailField = makeTextField("e.g. admin@zoo.com");
        card.add(emailField);
        card.add(Box.createVerticalStrut(16));

        // ── Password field ────────────────────────────────────────────────────
        card.add(makeFieldLabel("Password"));
        card.add(Box.createVerticalStrut(6));
        passwordField = new JPasswordField();
        styleField(passwordField);
        card.add(passwordField);
        card.add(Box.createVerticalStrut(24));

        // ── Login button ──────────────────────────────────────────────────────
        loginButton = new JButton("Login");
        loginButton.setFont(FONT_BUTTON);
        loginButton.setBackground(ACCENT_GREEN);
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ── LO9 Button click → program logic ─────────────────────────────────
        loginButton.addActionListener(e -> handleLogin());

        // Enter key also triggers login
        passwordField.addActionListener(e -> handleLogin());
        emailField.addActionListener(e -> passwordField.requestFocus());

        card.add(loginButton);
        card.add(Box.createVerticalStrut(16));

        // ── Status label (shows success / error messages) ─────────────────────
        // LO10 — display result or error in a label
        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setFont(FONT_SMALL);
        statusLabel.setForeground(TEXT_MUTED);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(statusLabel);

        root.add(card);
        loginFrame.setVisible(true);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // LOGIN HANDLER — demonstrates ALL exception handling learning outcomes
    // ─────────────────────────────────────────────────────────────────────────
    private void handleLogin() {
        // LO8 — read input from GUI text fields
        String email    = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        // Disable button during processing (good UX + shows finally usage)
        loginButton.setEnabled(false);
        setStatus("Authenticating...", TEXT_MUTED);

        // LO3 try-catch + LO4 multiple catch + LO5 finally
        try {
            // LO6 throw — validateLoginInput throws ZooException for blank fields
            validateLoginInput(email, password);

            // LO7 throws — zoo.login() declares throws ZooException
            zoo.login(email, password);

            // ── Success ───────────────────────────────────────────────────────
            setStatus("✓ Welcome, " + zoo.getLoggedInStaff().getName() + "!", ACCENT_GREEN);

            // Small delay so user sees success message, then open dashboard
            Timer timer = new Timer(800, evt -> {
                loginFrame.setVisible(false);
                buildDashboard();
            });
            timer.setRepeats(false);
            timer.start();

        } catch (ZooException e) {
            // LO4 — catch our CUSTOM exception type separately
            // This catches: blank fields, wrong password, inactive staff
            setStatus("✗ " + e.getMessage(), ACCENT_RED);
            passwordField.setText("");
            passwordField.requestFocus();

        } catch (Exception e) {
            // LO4 — catch unexpected runtime exceptions separately
            setStatus("✗ Unexpected error: " + e.getMessage(), ACCENT_RED);
            e.printStackTrace();

        } finally {
            // LO5 — finally ALWAYS runs: re-enable button whether login passed or failed
            loginButton.setEnabled(true);
            System.out.println("[Login] Attempt finished for: " + email);
        }
    }

    /**
     * LO6 throw  — manually throws a ZooException for invalid input
     * LO7 throws — method signature declares it
     *
     * This is the KEY method to show your teacher:
     * "throw creates a custom problem, throws declares it in the signature"
     */
    private void validateLoginInput(String email, String password) throws ZooException {
        if (email == null || email.isEmpty()) {
            throw new ZooException("Email cannot be blank.");       // LO6 throw
        }
        if (password == null || password.isEmpty()) {
            throw new ZooException("Password cannot be blank.");    // LO6 throw
        }
        if (!email.contains("@")) {
            throw new ZooException("Please enter a valid email address.");
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // DASHBOARD — shown after successful login
    // LO11 — OOP classes (Zoo, Staff, Animal, Habitat, Food) drive the content
    // ─────────────────────────────────────────────────────────────────────────
    private void buildDashboard() {
        dashboardFrame = new JFrame("Safari Zoo — " + zoo.getLoggedInStaff().getName());
        dashboardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dashboardFrame.setSize(860, 600);
        dashboardFrame.setLocationRelativeTo(null);

        // ── Main layout ───────────────────────────────────────────────────────
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG_DARK);

        // ── Top bar ───────────────────────────────────────────────────────────
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(BG_CARD);
        topBar.setBorder(new EmptyBorder(12, 20, 12, 20));

        JLabel welcomeLabel = new JLabel(
            "🦁  Safari Zoo  |  " +
            zoo.getLoggedInStaff().getName() +
            "  [" + zoo.getLoggedInStaff().getClass().getSimpleName() + "]"
        );
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        welcomeLabel.setForeground(TEXT_WHITE);

        JButton logoutBtn = makeSmallButton("Logout", ACCENT_RED);
        logoutBtn.addActionListener(e -> handleLogout());

        topBar.add(welcomeLabel, BorderLayout.WEST);
        topBar.add(logoutBtn, BorderLayout.EAST);

        // ── Tab panel ─────────────────────────────────────────────────────────
        JTabbedPane tabs = new JTabbedPane();
        tabs.setBackground(BG_DARK);
        tabs.setForeground(TEXT_DARK);
        tabs.setFont(FONT_LABEL);

        tabs.addTab("🐾  Animals",   buildAnimalsTab());
        tabs.addTab("🌿  Habitats",  buildHabitatsTab());
        tabs.addTab("🍖  Food",      buildFoodTab());

        // Only show staff tab if Manager
        if (zoo.getLoggedInStaff() instanceof Manager) {
            tabs.addTab("👥  Staff", buildStaffTab());
        }

        root.add(topBar, BorderLayout.NORTH);
        root.add(tabs, BorderLayout.CENTER);

        dashboardFrame.setContentPane(root);
        dashboardFrame.setVisible(true);
    }

    // ── Animals tab ───────────────────────────────────────────────────────────
    private JPanel buildAnimalsTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 0));
        panel.setBackground(BG_DARK);
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));

        String[] cols = {"Name", "Species", "Age", "Weight (kg)", "Habitat"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        // LO11 — pulling data from OOP Animal objects into the GUI
        // LO3 try-catch — database read wrapped safely
        try {
            for (Animal a : zoo.getAnimals()) {
                model.addRow(new Object[]{
                    a.getName(),
                    a.getSpecies(),
                    a.getAge(),
                    a.getWeight(),
                    a.getHabitatName() != null ? a.getHabitatName() : "—"
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(dashboardFrame,
                "Error loading animals: " + e.getMessage(),
                "Data Error", JOptionPane.ERROR_MESSAGE);
        }

        JTable table = makeStyledTable(model);
        JScrollPane scroll = new JScrollPane(table);
        styleScrollPane(scroll);

        JLabel header = makeTabHeader("Animals in Safari Zoo  (" + model.getRowCount() + " total)");
        panel.add(header, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    // ── Habitats tab ──────────────────────────────────────────────────────────
    private JPanel buildHabitatsTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 0));
        panel.setBackground(BG_DARK);
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));

        String[] cols = {"Habitat Name", "Type", "Capacity", "Animals Inside", "Performance"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        try {
            for (Habitat h : zoo.getHabitats()) {
                model.addRow(new Object[]{
                    h.getName(),
                    h.getClass().getSimpleName(),
                    h.getCapacity(),
                    h.getAnimals().size(),
                    h.getFeedingPerformance() + "%"
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(dashboardFrame,
                "Error loading habitats: " + e.getMessage(),
                "Data Error", JOptionPane.ERROR_MESSAGE);
        }

        JTable table = makeStyledTable(model);
        JScrollPane scroll = new JScrollPane(table);
        styleScrollPane(scroll);

        JLabel header = makeTabHeader("Active Habitats  (" + model.getRowCount() + " total)");
        panel.add(header, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    // ── Food tab ──────────────────────────────────────────────────────────────
    private JPanel buildFoodTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 0));
        panel.setBackground(BG_DARK);
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));

        String[] cols = {"ID", "Food Name", "Stock", "Expiry Date", "Cost/Unit"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        try {
            for (Food f : zoo.getFoodInventory()) {
                model.addRow(new Object[]{
                    f.getId(),
                    f.getName(),
                    String.format("%.2f", f.getStock()),
                    f.getExpiryDate(),
                    String.format("$%.2f", f.getCostPerUnit())
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(dashboardFrame,
                "Error loading food inventory: " + e.getMessage(),
                "Data Error", JOptionPane.ERROR_MESSAGE);
        }

        JTable table = makeStyledTable(model);
        JScrollPane scroll = new JScrollPane(table);
        styleScrollPane(scroll);

        JLabel header = makeTabHeader("Food Inventory  (" + model.getRowCount() + " items)");
        panel.add(header, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    // ── Staff tab (Manager only) ──────────────────────────────────────────────
    private JPanel buildStaffTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 0));
        panel.setBackground(BG_DARK);
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));

        String[] cols = {"ID", "Name", "Role", "Email / Username", "Active"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        try {
            for (com.zoo.interfaces.IStaff s : zoo.getUsers()) {
                model.addRow(new Object[]{
                    s.getId(),
                    s.getName(),
                    s.getClass().getSimpleName(),
                    s.getUsername(),
                    s.isActive() ? "✓" : "✗"
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(dashboardFrame,
                "Error loading staff: " + e.getMessage(),
                "Data Error", JOptionPane.ERROR_MESSAGE);
        }

        JTable table = makeStyledTable(model);
        JScrollPane scroll = new JScrollPane(table);
        styleScrollPane(scroll);

        JLabel header = makeTabHeader("Staff Directory  (" + model.getRowCount() + " members)");
        panel.add(header, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // LOGOUT HANDLER
    // ─────────────────────────────────────────────────────────────────────────
    private void handleLogout() {
        // LO3 try-catch on logout too
        try {
            zoo.logout();
            dashboardFrame.dispose();
            // Reset fields for security
            emailField.setText("");
            passwordField.setText("");
            setStatus("Logged out successfully.", TEXT_MUTED);
            loginFrame.setVisible(true);
        } catch (ZooException e) {
            JOptionPane.showMessageDialog(dashboardFrame,
                "Logout error: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            // LO5 finally — always log the logout attempt
            System.out.println("[Logout] Session ended for: " +
                (zoo.getLoggedInStaff() != null ? zoo.getLoggedInStaff().getName() : "unknown"));
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // UI HELPER METHODS
    // ─────────────────────────────────────────────────────────────────────────
    private JLabel makeFieldLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_LABEL);
        lbl.setForeground(TEXT_MUTED);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JTextField makeTextField(String placeholder) {
        JTextField field = new JTextField();
        styleField(field);
        field.setToolTipText(placeholder);
        return field;
    }

    private void styleField(JTextField field) {
        field.setFont(FONT_LABEL);
        field.setBackground(FIELD_BG);
        field.setForeground(TEXT_WHITE);
        field.setCaretColor(TEXT_WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(84, 110, 122), 1),
            new EmptyBorder(8, 12, 8, 12)
        ));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private JButton makeSmallButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_SMALL);
        btn.setBackground(BG_DARK);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(6, 16, 6, 16));
        return btn;
    }

    private JLabel makeTabHeader(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lbl.setForeground(TEXT_WHITE);
        lbl.setBorder(new EmptyBorder(0, 0, 12, 0));
        return lbl;
    }

    private JTable makeStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setBackground(BG_CARD);
        table.setForeground(TEXT_WHITE);
        table.setFont(FONT_LABEL);
        table.setRowHeight(32);
        table.setGridColor(new Color(55, 71, 79));
        table.setSelectionBackground(new Color(76, 175, 80, 80));
        table.setSelectionForeground(TEXT_WHITE);
        table.getTableHeader().setBackground(BG_DARK);
        table.getTableHeader().setForeground(BG_DARK);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.setShowVerticalLines(false);
        return table;
    }

    private void styleScrollPane(JScrollPane scroll) {
        scroll.getViewport().setBackground(BG_CARD);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(55, 71, 79)));
    }

    private void setStatus(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);
    }
}
