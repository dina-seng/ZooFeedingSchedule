package com.zoo.main;

import com.zoo.exceptions.InvalidNameException;
import com.zoo.exceptions.ZooException;
import com.zoo.models.Animal;
import com.zoo.models.Food;
import com.zoo.models.Schedule;
import com.zoo.models.habitat_types.Habitat;
import com.zoo.models.staff_roles.Manager;
import com.zoo.models.staff_roles.Staff;
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
    private static final Font  FONT_TITLE   = new Font("Segoe UI Emoji", Font.BOLD, 26);
    private static final Font  FONT_LABEL   = new Font("Segoe UI Emoji", Font.PLAIN, 14);
    private static final Font  FONT_BUTTON  = new Font("Segoe UI Emoji", Font.BOLD, 14);
    private static final Font  FONT_SMALL   = new Font("Segoe UI Emoji", Font.PLAIN, 12);

    // ENTRY POINT
    public static void main(String[] args) {
        // Run on the Swing Event Dispatch Thread (EDT) — best practice
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new ZooLoginGUI().initApp();
        });
    }

    // INIT — creates Zoo OOP object then shows login window
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
            System.out.println("[System] Zoo initialization attempt complete.");
        }
        buildLoginScreen();
    }

    // LOGIN SCREEN
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
        card.add(wrapCenter(iconLabel));
        iconLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(iconLabel);
        card.add(Box.createVerticalStrut(8));

        // ── Title ─────────────────────────────────────────────────────────────
        JLabel title = new JLabel("Safari Zoo", SwingConstants.CENTER);
        title.setFont(FONT_TITLE);
        title.setForeground(ACCENT_GREEN);
        card.add(wrapCenter(title));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(title);

        JLabel subtitle = new JLabel("Staff Management System", SwingConstants.CENTER);
        subtitle.setFont(FONT_SMALL);
        subtitle.setForeground(TEXT_MUTED);
        card.add(wrapCenter(subtitle));
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(subtitle);
        card.add(Box.createVerticalStrut(28));

        // ── Email field ───────────────────────────────────────────────────────
        card.add(makeFieldLabel("Email Address"));
        card.add(Box.createVerticalStrut(6));
        emailField = makeTextField("e.g. admin@zoo.com");
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        emailField.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ADD THIS — validate email on every keystroke
        emailField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e)  { validateEmailLive(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e)  { validateEmailLive(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { validateEmailLive(); }
        });

        card.add(emailField);
        card.add(Box.createVerticalStrut(16));

        // ── Password field ────────────────────────────────────────────────────
        card.add(makeFieldLabel("Password"));
        card.add(Box.createVerticalStrut(6));
        passwordField = new JPasswordField();
        styleField(passwordField);
        passwordField.setEnabled(false);  // disabled until email is valid
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ADD THIS — validate password on every keystroke
        passwordField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e)  { validatePasswordLive(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e)  { validatePasswordLive(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { validatePasswordLive(); }
        });

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
        loginButton.setAlignmentX(Component.LEFT_ALIGNMENT);

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
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(statusLabel);

        root.add(card);
        loginFrame.setVisible(true);
    }

    // LOGIN HANDLER
    private void handleLogin() {

        String email    = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        loginButton.setEnabled(false);
        setStatus("Authenticating...", TEXT_MUTED);

        try {
            validateLoginInput(email, password);
            zoo.login(email, password);

            setStatus("✓ Welcome, " + zoo.getLoggedInStaff().getName() + "!", ACCENT_GREEN);
            Timer timer = new Timer(800, evt -> {
                loginFrame.setVisible(false);
                buildDashboard();
            });
            timer.setRepeats(false);
            timer.start();

        } catch (ZooException e) {
            setStatus("✗ " + e.getMessage(), ACCENT_RED);
            passwordField.setText("");
            passwordField.requestFocus();

        } catch (Exception e) {
            setStatus("✗ Unexpected error: " + e.getMessage(), ACCENT_RED);
            e.printStackTrace();

        } finally {
            loginButton.setEnabled(true);
            System.out.println("[Login] Attempt finished for: " + email);
        }
    }

    private void validateLoginInput(String email, String password) throws ZooException {
        if (email == null || email.isEmpty())
            throw new ZooException("Email cannot be blank.");

        if (!email.matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$"))
            throw new ZooException("Please enter a valid email address.");

        if (email.length() > 254)
            throw new ZooException("Email address is too long.");

        if (password == null || password.isEmpty())
            throw new ZooException("Password cannot be blank.");

        if (password.length() < 8)
            throw new ZooException("Password must be at least 8 characters.");

        if (password.length() > 128)
            throw new ZooException("Password is too long.");
    }

    // LO11 — OOP classes (Zoo, Staff, Animal, Habitat, Food) drive the content
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
        welcomeLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 15));
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

        if ( zoo.getLoggedInStaff().can(Zoo.ANIMAL_MANAGE)) {
            tabs.addTab("🐾  Animals", buildAnimalsTab());
        }

        if ( zoo.getLoggedInStaff().can(Zoo.HABITAT_MANAGE)) {
            tabs.addTab("🐾  Habitats", buildHabitatsTab());
        }

        if (zoo.getLoggedInStaff().can(Zoo.SCHEDULE_MANAGE)) {
            tabs.addTab("⌛  Schedules", ScheduleBuild());
        }

        if (zoo.getLoggedInStaff().can(Zoo.STAFF_MANAGE)) {
            tabs.addTab("👥  Staffs", buildStaffTab());
        }

        if (zoo.getLoggedInStaff().can(Zoo.FOOD_MANAGE)) {
            tabs.addTab("🍖  Foods", buildFoodTab());
        }

        if (zoo.getLoggedInStaff().can(Zoo.VIEW_REPORT)) {
            tabs.addTab("📊  Report", ReportBuild());
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

        try {
            for (Animal a : zoo.getAnimals()) {
                model.addRow(new Object[]{
                        a.getName(), a.getSpecies(), a.getAge(), a.getWeight(),
                        a.getHabitatName() != null ? a.getHabitatName() : "—"
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(dashboardFrame, "Error loading animals: " + e.getMessage(), "Data Error", JOptionPane.ERROR_MESSAGE);
        }

        JTable table = makeStyledTable(model);
        JScrollPane scroll = new JScrollPane(table);
        styleScrollPane(scroll);

        // ── Button panel ──────────────────────────────────────────────────────
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        btnPanel.setBackground(BG_DARK);

        if (zoo.getLoggedInStaff() instanceof Manager) {
            JButton addBtn = makeSmallButton("+ Add Animal", ACCENT_GREEN);
            addBtn.addActionListener(e -> {
                JTextField nameF    = new JTextField();
                JTextField speciesF = new JTextField();
                JTextField ageF     = new JTextField();
                JTextField weightF  = new JTextField();

                // Build habitat dropdown
                String[] habitatNames = zoo.getHabitats().stream()
                        .map(Habitat::getName).toArray(String[]::new);
                JComboBox<String> habitatBox = new JComboBox<>(habitatNames);

                Object[] fields = {
                        "Name:", nameF, "Species:", speciesF,
                        "Age:", ageF, "Weight (kg):", weightF,
                        "Habitat:", habitatBox
                };

                int result = JOptionPane.showConfirmDialog(dashboardFrame, fields,
                        "Add New Animal", JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION) {
                    try {
                        String name    = nameF.getText().trim();
                        String species = speciesF.getText().trim();
                        String ageS    = ageF.getText().trim();
                        String weightS = weightF.getText().trim();

                        // ── Blank checks ──────────────────────────────────────
                        if (name.isEmpty())    throw new ZooException("Animal name cannot be blank.");
                        if (species.isEmpty()) throw new ZooException("Species cannot be blank.");
                        if (ageS.isEmpty())    throw new ZooException("Age cannot be blank.");
                        if (weightS.isEmpty()) throw new ZooException("Weight cannot be blank.");

                        // ── Name / species must not be numbers ────────────────
                        zoo.validateName("Animal Name", name);
                        zoo.validateName("Species", species);

                        // ── Parse with friendly errors ────────────────────────
                        int age;
                        double weight;
                        try { age = Integer.parseInt(ageS); }
                        catch (NumberFormatException ex) { throw new ZooException("Age must be a whole number (e.g. 5)."); }

                        try { weight = Double.parseDouble(weightS); }
                        catch (NumberFormatException ex) { throw new ZooException("Weight must be a number (e.g. 120.5)."); }

                        // ── Range checks ──────────────────────────────────────
                        if (age <= 0 || age > 100)      throw new ZooException("Age must be between 1 and 100.");
                        if (weight <= 0)                 throw new ZooException("Weight must be greater than 0.");
                        if (zoo.getHabitats().isEmpty()) throw new ZooException("No habitats available. Add a habitat first.");

                        int habitatIdx   = habitatBox.getSelectedIndex();
                        Habitat selected = zoo.getHabitats().get(habitatIdx);

                        zoo.addAnimalToHabitat(new Animal(name, age, species, weight), selected);
                        model.addRow(new Object[]{name, species, age, weight, selected.getName()});
                        JOptionPane.showMessageDialog(dashboardFrame, "Animal added successfully!");

                    } catch (InvalidNameException ex) {
                        JOptionPane.showMessageDialog(dashboardFrame, ex.getMessage(), "Invalid Name", JOptionPane.WARNING_MESSAGE);
                    } catch (ZooException ex) {
                        JOptionPane.showMessageDialog(dashboardFrame, ex.getMessage(), "Validation Error", JOptionPane.WARNING_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(dashboardFrame, "Unexpected error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            JButton delBtn = makeSmallButton("- Remove Animal", ACCENT_RED);
            delBtn.addActionListener(e -> {
                int row = table.getSelectedRow();
                if (row == -1) {
                    JOptionPane.showMessageDialog(dashboardFrame, "Please select an animal to remove.");
                    return;
                }
                String animalName = (String) model.getValueAt(row, 0);
                String habitatName = (String) model.getValueAt(row, 4);

                int confirm = JOptionPane.showConfirmDialog(dashboardFrame,
                        "Remove " + animalName + "?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        Animal target = zoo.getAnimals().stream()
                                .filter(a -> a.getName().equals(animalName)).findFirst().orElse(null);
                        Habitat habitat = zoo.getHabitats().stream()
                                .filter(h -> h.getName().equals(habitatName)).findFirst().orElse(null);
                        if (target != null && habitat != null) {
                            zoo.removeAnimalFromHabitat(target, habitat);
                            model.removeRow(row);
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(dashboardFrame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            btnPanel.add(addBtn);
            btnPanel.add(delBtn);
        }

        JLabel header = makeTabHeader("Animals in Safari Zoo  (" + model.getRowCount() + " total)");
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(BG_DARK);
        top.add(header, BorderLayout.WEST);
        top.add(btnPanel, BorderLayout.EAST);

        panel.add(top, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    // ── Habitats tab ──────────────────────────────────────────────────────────
    private JPanel buildHabitatsTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 0));
        panel.setBackground(BG_DARK);
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));

        String[] cols = {"Habitat Name", "Type", "Food", "Capacity", "Animals Inside", "Performance"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        try {
            for (Habitat h : zoo.getHabitats()) {
                model.addRow(new Object[]{
                        h.getName(),
                        h.getClass().getSimpleName(),
                        h.getFood().getName(),
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

        // ── Button panel ──────────────────────────────────────────────────────
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        btnPanel.setBackground(BG_DARK);

        if (zoo.getLoggedInStaff() instanceof Manager) {

            // ── Add Habitat ───────────────────────────────────────────────────
            JButton addBtn = makeSmallButton("+ Add Habitat", ACCENT_GREEN);
            addBtn.addActionListener(e -> {
                JTextField nameF     = new JTextField();
                JTextField capacityF = new JTextField();
                String[] types       = {"Forest", "Ocean", "Savannah"};
                JComboBox<String> typeBox = new JComboBox<>(types);

                String[] foodNames = zoo.getFoodInventory().stream()
                        .map(Food::getName).toArray(String[]::new);
                JComboBox<String> foodBox = new JComboBox<>(foodNames);

                Object[] fields = {
                        "Habitat Name:", nameF,
                        "Type:", typeBox,
                        "Food:", foodBox,
                        "Capacity:", capacityF
                };

                int result = JOptionPane.showConfirmDialog(dashboardFrame, fields,
                        "Add New Habitat", JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION) {
                    try {
                        String name      = nameF.getText().trim();
                        String capacityS = capacityF.getText().trim();
                        String type      = ((String) typeBox.getSelectedItem()).toLowerCase();
                        int foodIdx      = foodBox.getSelectedIndex();

                        // ── Blank checks ──────────────────────────────────────
                        if (name.isEmpty())      throw new ZooException("Habitat name cannot be blank.");
                        if (capacityS.isEmpty()) throw new ZooException("Capacity cannot be blank.");
                        if (zoo.getFoodInventory().isEmpty())
                            throw new ZooException("No food in inventory. Add food first.");

                        zoo.validateName("Habitat Name", name);

                        // ── Parse capacity ────────────────────────────────────
                        int capacity;
                        try { capacity = Integer.parseInt(capacityS); }
                        catch (NumberFormatException ex) {
                            throw new ZooException("Capacity must be a whole number (e.g. 10).");
                        }
                        if (capacity <= 0) throw new ZooException("Capacity must be greater than 0.");

                        // ── Duplicate check ───────────────────────────────────
                        boolean exists = zoo.getHabitats().stream()
                                .anyMatch(h -> h.getName().equalsIgnoreCase(name));
                        if (exists) throw new ZooException("A habitat with that name already exists.");

                        Food selectedFood = zoo.getFoodInventory().get(foodIdx);
                        zoo.createHabitat(name, type, capacity, selectedFood);

                        Habitat added = zoo.getHabitats().get(zoo.getHabitats().size() - 1);
                        model.addRow(new Object[]{
                                added.getName(),
                                added.getClass().getSimpleName(),
                                added.getFood().getName(),
                                added.getCapacity(),
                                0,
                                added.getFeedingPerformance() + "%"
                        });
                        JOptionPane.showMessageDialog(dashboardFrame, "Habitat added successfully!");

                    } catch (InvalidNameException ex) {
                        JOptionPane.showMessageDialog(dashboardFrame,
                                ex.getMessage(), "Invalid Name", JOptionPane.WARNING_MESSAGE);
                    } catch (ZooException ex) {
                        JOptionPane.showMessageDialog(dashboardFrame,
                                ex.getMessage(), "Validation Error", JOptionPane.WARNING_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(dashboardFrame,
                                "Unexpected error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            // ── Remove Habitat ────────────────────────────────────────────────
            JButton delBtn = makeSmallButton("- Remove Habitat", ACCENT_RED);
            delBtn.addActionListener(e -> {
                int row = table.getSelectedRow();
                if (row == -1) {
                    JOptionPane.showMessageDialog(dashboardFrame,
                            "Please select a habitat to remove.");
                    return;
                }

                String habitatName = (String) model.getValueAt(row, 0);
                int animalsInside  = (int)    model.getValueAt(row, 4);

                if (animalsInside > 0) {
                    JOptionPane.showMessageDialog(dashboardFrame,
                            "Cannot remove \"" + habitatName + "\": " + animalsInside +
                                    " animal(s) still inside. Relocate them first.",
                            "Removal Blocked", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int confirm = JOptionPane.showConfirmDialog(dashboardFrame,
                        "Remove habitat \"" + habitatName + "\"?",
                        "Confirm Removal", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        zoo.removeHabitat(habitatName);
                        model.removeRow(row);
                        JOptionPane.showMessageDialog(dashboardFrame,
                                "\"" + habitatName + "\" removed successfully.");
                    } catch (ZooException ex) {
                        JOptionPane.showMessageDialog(dashboardFrame,
                                ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(dashboardFrame,
                                "Unexpected error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            btnPanel.add(addBtn);
            btnPanel.add(delBtn);
        }

        JLabel header = makeTabHeader("Active Habitats  (" + model.getRowCount() + " total)");
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(BG_DARK);
        top.add(header,   BorderLayout.WEST);
        top.add(btnPanel, BorderLayout.EAST);

        panel.add(top,    BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    // ── Food tab ──────────────────────────────────────────────────────────────
    private JPanel buildFoodTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 0));
        panel.setBackground(BG_DARK);
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));

        String[] cols = {"ID", "Food Name", "Stock(kg)", "Expiry Date", "Cost/Unit($)"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        try {
            for (Food f : zoo.getFoodInventory()) {
                model.addRow(new Object[]{
                        f.getId(), f.getName(),
                        String.format("%.2f", f.getStock()),
                        f.getExpiryDate(),
                        String.format("$%.2f", f.getCostPerUnit())
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(dashboardFrame, "Error loading food: " + e.getMessage(), "Data Error", JOptionPane.ERROR_MESSAGE);
        }

        JTable table = makeStyledTable(model);
        JScrollPane scroll = new JScrollPane(table);
        styleScrollPane(scroll);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        btnPanel.setBackground(BG_DARK);

        if (zoo.getLoggedInStaff() instanceof Manager) {

            // ── Add Food ──────────────────────────────────────────────────────────
            JButton addBtn = makeSmallButton("+ Add Food", ACCENT_GREEN);
            addBtn.addActionListener(e -> {
            JTextField nameF   = new JTextField();
            JTextField stockF  = new JTextField();
            JTextField expiryF = new JTextField("2026-12-31");
            JTextField costF   = new JTextField();

            Object[] fields = {
                    "Food Name:", nameF, "Stock (kg):", stockF,
                    "Expiry (YYYY-MM-DD):", expiryF, "Cost per Unit ($):", costF
            };

            int result = JOptionPane.showConfirmDialog(dashboardFrame, fields,
                    "Add Food to Inventory", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    String name   = nameF.getText().trim();
                    String stockS = stockF.getText().trim();
                    String expiry = expiryF.getText().trim();
                    String costS  = costF.getText().trim();

                    // ── Blank checks ──────────────────────────────────────────
                    if (name.isEmpty())   throw new ZooException("Food name cannot be blank.");
                    if (stockS.isEmpty()) throw new ZooException("Stock cannot be blank.");
                    if (expiry.isEmpty()) throw new ZooException("Expiry date cannot be blank.");
                    if (costS.isEmpty())  throw new ZooException("Cost cannot be blank.");

                    // ── Name must not be a number ─────────────────────────────
                    zoo.validateName("Food Name", name);

                    // ── Parse numbers ─────────────────────────────────────────
                    double stock, cost;
                    try {
                        stock = Double.parseDouble(stockS);
                    } catch (NumberFormatException ex) {
                        throw new ZooException("Stock must be a valid number (e.g. 10 or 5.5).");
                    }
                    try {
                        cost = Double.parseDouble(costS);
                    } catch (NumberFormatException ex) {
                        throw new ZooException("Cost must be a valid number (e.g. 2.99).");
                    }

                    // ── Range checks ──────────────────────────────────────────
                    if (stock <= 0) throw new ZooException("Stock must be greater than 0.");
                    if (cost  <= 0) throw new ZooException("Cost must be greater than 0.");

                    // ── Date format check ─────────────────────────────────────
                    if (!expiry.matches("\\d{4}-\\d{2}-\\d{2}"))
                        throw new ZooException("Expiry must be in YYYY-MM-DD format (e.g. 2027-06-30).");

                    // ── All good → persist ────────────────────────────────────
                    Food newFood = new Food(name, stock, expiry, cost);
                    zoo.addFoodToInventory(newFood);

                    model.addRow(new Object[]{
                            newFood.getId(), name,
                            String.format("%.2f", stock), expiry,
                            String.format("$%.2f", cost)
                    });
                    JOptionPane.showMessageDialog(dashboardFrame,
                            "Food added successfully! (ID: " + newFood.getId() + ")");

                } catch (InvalidNameException ex) {
                    JOptionPane.showMessageDialog(dashboardFrame,
                            ex.getMessage(), "Invalid Name", JOptionPane.WARNING_MESSAGE);
                } catch (ZooException ex) {
                    JOptionPane.showMessageDialog(dashboardFrame,
                            ex.getMessage(), "Validation Error", JOptionPane.WARNING_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dashboardFrame,
                            "Unexpected error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

            // ── Remove Food ───────────────────────────────────────────────────────
            JButton delBtn = makeSmallButton("- Remove Food", ACCENT_RED);
            delBtn.addActionListener(e -> {
                int row = table.getSelectedRow();
                if (row == -1) {
                    JOptionPane.showMessageDialog(dashboardFrame, "Please select a food item to remove.");
                    return;
                }
                String foodName = (String) model.getValueAt(row, 1);
                int foodId      = (int)   model.getValueAt(row, 0);

                int confirm = JOptionPane.showConfirmDialog(dashboardFrame,
                        "Remove \"" + foodName + "\" from inventory?",
                        "Confirm Removal", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        zoo.removeFoodFromInventory(foodId);
                        model.removeRow(row);
                        JOptionPane.showMessageDialog(dashboardFrame, "\"" + foodName + "\" removed.");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(dashboardFrame,
                                "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            btnPanel.add(addBtn);
            btnPanel.add(delBtn);
        }

        JLabel header = makeTabHeader("Food Inventory  (" + model.getRowCount() + " items)");
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(BG_DARK);
        top.add(header, BorderLayout.WEST);
        top.add(btnPanel, BorderLayout.EAST);

        panel.add(top, BorderLayout.NORTH);
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
                        s.getId(), s.getName(), s.getClass().getSimpleName(),
                        s.getUsername(), s.isActive() ? "✓" : "✗"
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(dashboardFrame, "Error loading staff: " + e.getMessage(), "Data Error", JOptionPane.ERROR_MESSAGE);
        }

        JTable table = makeStyledTable(model);
        JScrollPane scroll = new JScrollPane(table);
        styleScrollPane(scroll);

        // ── Button panel ──────────────────────────────────────────────────────
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        btnPanel.setBackground(BG_DARK);

        JButton addBtn = makeSmallButton("+ Add Staff", ACCENT_GREEN);
        addBtn.addActionListener(e -> {
            JTextField nameF     = new JTextField();
            JTextField emailF    = new JTextField();
            JTextField passF     = new JPasswordField();
            JTextField salaryF   = new JTextField();
            String[] roles       = {"Keeper", "Manager"};
            JComboBox<String> roleBox = new JComboBox<>(roles);

            Object[] fields = {
                    "Full Name:", nameF, "Email:", emailF,
                    "Password (min 8):", passF, "Salary:", salaryF,
                    "Role:", roleBox
            };

            int result = JOptionPane.showConfirmDialog(dashboardFrame, fields,
                    "+ Add New Staff", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    String name   = nameF.getText().trim();
                    String email  = emailF.getText().trim();
                    String pass   = passF.getText().trim();
                    String salaryS = salaryF.getText().trim();
                    String role   = (String) roleBox.getSelectedItem();

                    // ── Blank checks ──────────────────────────────────────
                    if (name.isEmpty())    throw new ZooException("Full name cannot be blank.");
                    if (email.isEmpty())   throw new ZooException("Email cannot be blank.");
                    if (pass.isEmpty())    throw new ZooException("Password cannot be blank.");
                    if (salaryS.isEmpty()) throw new ZooException("Salary cannot be blank.");

                    // ── Name must not be numeric ──────────────────────────
                    zoo.validateName("Full Name", name);

                    // ── Email format ──────────────────────────────────────
                    if (!email.matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$"))
                        throw new ZooException("Please enter a valid email address.");

                    // ── Password strength ─────────────────────────────────
                    if (pass.length() < 8)
                        throw new ZooException("Password must be at least 8 characters.");

                    // ── Parse salary ──────────────────────────────────────
                    float salary;
                    try { salary = Float.parseFloat(salaryS); }
                    catch (NumberFormatException ex) { throw new ZooException("Salary must be a valid number (e.g. 1500.00)."); }

                    if (salary <= 0) throw new ZooException("Salary must be greater than 0.");

                    zoo.createStaff(name, role, email, pass, salary);
                    Staff added = zoo.getUsers().get(zoo.getUsers().size() - 1);
                    model.addRow(new Object[]{added.getId(), name, role, email, "✓"});
                    JOptionPane.showMessageDialog(dashboardFrame, role + " added successfully!");

                } catch (InvalidNameException ex) {
                    JOptionPane.showMessageDialog(dashboardFrame, ex.getMessage(), "Invalid Name", JOptionPane.WARNING_MESSAGE);
                } catch (ZooException ex) {
                    JOptionPane.showMessageDialog(dashboardFrame, ex.getMessage(), "Validation Error", JOptionPane.WARNING_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dashboardFrame, "Unexpected error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton delBtn = makeSmallButton("- Remove Staff", ACCENT_RED);
        delBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(dashboardFrame, "Please select a staff member to remove.");
                return;
            }
            int staffId = (int) model.getValueAt(row, 0);
            String name = (String) model.getValueAt(row, 1);
            int confirm = JOptionPane.showConfirmDialog(dashboardFrame,
                    "Remove " + name + "?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    zoo.removeStaff(staffId);
                    model.removeRow(row);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dashboardFrame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnPanel.add(addBtn);
        btnPanel.add(delBtn);

        JLabel header = makeTabHeader("Staff Directory  (" + model.getRowCount() + " members)");
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(BG_DARK);
        top.add(header, BorderLayout.WEST);
        top.add(btnPanel, BorderLayout.EAST);

        panel.add(top, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    // ── Schedule tab (Manager only) ──────────────────────────────────────────────
    public JPanel ScheduleBuild() {
        JPanel panel = new JPanel(new BorderLayout(0, 0));
        panel.setBackground(BG_DARK);
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));

        // ── Table ─────────────────────────────────────────────────────────────
        String[] cols = {"ID", "Staff ID", "Habitat", "Food ID",
                "Feeding Time", "Qty (kg)", "Notes", "Status"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        try {
            for (Schedule s : zoo.getSchedules()) {
                model.addRow(new Object[]{
                        s.getId(),
                        s.getAssignedKeeper(),
                        s.getAnimalId(),    // used as habitatId in your schema
                        s.getFoodId(),
                        s.getFeedingTime(),
                        String.format("%.2f", s.getQuantityKg()),
                        s.getNotes() != null ? s.getNotes() : "—",
                        s.isCompleted() ? "✓ Done" : "Pending"
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(dashboardFrame,
                    "Error loading schedules: " + e.getMessage(),
                    "Data Error", JOptionPane.ERROR_MESSAGE);
        }

        JTable table = makeStyledTable(model);
        JScrollPane scroll = new JScrollPane(table);
        styleScrollPane(scroll);

        // ── Button panel ──────────────────────────────────────────────────────
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        btnPanel.setBackground(BG_DARK);

        // Mark Done — all roles
        JButton doneBtn = makeSmallButton("✓ Mark Done", ACCENT_GREEN);
        doneBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(dashboardFrame,
                        "Please select a schedule to mark as done.");
                return;
            }
            if ("✓ Done".equals(model.getValueAt(row, 7))) {
                JOptionPane.showMessageDialog(dashboardFrame, "Already marked as done.");
                return;
            }
            // Match by schedule ID (col 0), not row index — safe against reordering
            int scheduleId = (int) model.getValueAt(row, 0);
            zoo.getSchedules().stream()
                    .filter(s -> s.getId() == scheduleId)
                    .findFirst()
                    .ifPresent(Schedule::markCompleted);
            model.setValueAt("✓ Done", row, 7);
        });
        btnPanel.add(doneBtn);

        // Add / Remove — Manager only
        if (zoo.getLoggedInStaff().can(Zoo.HABITAT_MANAGE)) {
            btnPanel.add(makeAddButton(model));
            btnPanel.add(makeRemoveButton(table, model));
        }

        // ── Header row ────────────────────────────────────────────────────────
        JLabel header = new JLabel("Feeding Schedules  (" + model.getRowCount() + " items)");
        header.setFont(new Font("Segoe UI Emoji", Font.BOLD, 15));
        header.setForeground(TEXT_WHITE);
        header.setBorder(new EmptyBorder(0, 0, 12, 0));

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(BG_DARK);
        top.add(header,   BorderLayout.WEST);
        top.add(btnPanel, BorderLayout.EAST);

        panel.add(top,    BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    // ── Report tab (Manager only) ──────────────────────────────────────────────
    public JPanel ReportBuild() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setBackground(BG_DARK);
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));

        panel.add(makeNorth(),        BorderLayout.NORTH);
        panel.add(makeTable(),        BorderLayout.CENTER);
        panel.add(makeSouth(panel),   BorderLayout.SOUTH);
        return panel;
    }

    // LOGOUT HANDLER
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

    // UI HELPER METHODS
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

    // Helper method — add this to your UI helpers
    private JPanel wrapCenter(JComponent component) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(BG_CARD);
        wrapper.add(component, BorderLayout.CENTER);
        return wrapper;
    }

    private void highlightFieldError(JTextField field) {
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_RED, 2),
                new EmptyBorder(8, 12, 8, 12)
        ));
        field.setBackground(new Color(80, 40, 40));
    }

    private void validateEmailLive() {
        String email = emailField.getText().trim();
        if (email.isEmpty() || !email.matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$") || email.length() > 254) {
            highlightFieldError(emailField);
            passwordField.setEnabled(false);
            loginButton.setEnabled(false);
            return;
        }
        resetFieldStyle(emailField);
        resetFieldStyle(passwordField);
    }

    private void validatePasswordLive() {
        String password = new String(passwordField.getPassword()).trim();

        if (password.length() < 8 || password.length() > 128) {
            highlightFieldError(passwordField);
            loginButton.setEnabled(false);  // lock login button
            return;
        }

        // Password is valid — unlock login button
        resetFieldStyle(passwordField);
        loginButton.setEnabled(true);
    }

    private void resetFieldStyle(JTextField field) {
        field.setEnabled(true);
        styleField(field);
    }

    private JPanel makeStatCard(String label, String value, Color accent) {
        JPanel card = new JPanel(new BorderLayout(0, 6));
        card.setBackground(BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 3, 0, 0, accent),
                new EmptyBorder(14, 16, 14, 16)
        ));
        JLabel valLabel = new JLabel(value);
        valLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 22));
        valLabel.setForeground(accent);

        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(FONT_SMALL);
        lblLabel.setForeground(TEXT_MUTED);

        card.add(valLabel, BorderLayout.CENTER);
        card.add(lblLabel, BorderLayout.SOUTH);
        return card;
    }

    // NORTH — header label + two rows of stat cards
    private JPanel makeNorth() {
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setBackground(BG_DARK);

        JLabel header = new JLabel("Zoo Summary Report");
        header.setFont(new Font("Segoe UI Empji", Font.BOLD, 15));
        header.setForeground(TEXT_WHITE);
        header.setBorder(new EmptyBorder(0, 0, 12, 0));
        wrapper.add(header);

        // Row 1: Staff | Habitats | Animals
        JPanel row1 = new JPanel(new GridLayout(1, 3, 12, 0));
        row1.setBackground(BG_DARK);
        row1.add(makeStatCard("👥  Total Staff",
                String.valueOf(zoo.getUsers().size()),    new Color(33, 150, 243)));
        row1.add(makeStatCard("🌿  Habitats",
                String.valueOf(zoo.getHabitats().size()), new Color(76, 175, 80)));
        row1.add(makeStatCard("🐾  Animals",
                String.valueOf(zoo.getAnimals().size()),  new Color(255, 152, 0)));

        // Row 2: Food items | Schedules summary
        JPanel row2 = new JPanel(new GridLayout(1, 2, 12, 0));
        row2.setBackground(BG_DARK);
        long done = zoo.getSchedules().stream().filter(Schedule::isCompleted).count();
        row2.add(makeStatCard("🍖  Food Items",
                String.valueOf(zoo.getFoodInventory().size()), new Color(233, 30, 99)));
        row2.add(makeStatCard("⌛  Schedules",
                zoo.getSchedules().size() + " total  |  " + done + " done",
                new Color(156, 39, 176)));

        JLabel sectionLabel = new JLabel("Habitat Breakdown");
        sectionLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        sectionLabel.setForeground(TEXT_MUTED);
        sectionLabel.setBorder(new EmptyBorder(14, 0, 6, 0));

        wrapper.add(row1);
        wrapper.add(Box.createVerticalStrut(10));
        wrapper.add(row2);
        wrapper.add(sectionLabel);
        return wrapper;
    }

    // CENTER — per-habitat breakdown table
    private JScrollPane makeTable() {
        String[] cols = {"Habitat", "Type", "Animals", "Capacity", "Food", "Performance"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        try {
            for (Habitat h : zoo.getHabitats()) {
                model.addRow(new Object[]{
                        h.getName(),
                        h.getClass().getSimpleName(),
                        h.getAnimals().size(),
                        h.getCapacity(),
                        h.getFood().getName(),
                        h.getFeedingPerformance() + "%"
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(dashboardFrame,
                    "Error building report: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        JTable table = makeStyledTable(model);
        JScrollPane scroll = new JScrollPane(table);
        styleScrollPane(scroll);
        return scroll;
    }

    // SOUTH — generated-by footer + refresh button
    private JPanel makeSouth(JPanel panel) {
        JLabel footer = new JLabel(
                "Generated by: " + zoo.getLoggedInStaff().getName()
                        + "  [" + zoo.getLoggedInStaff().getClass().getSimpleName() + "]"
                        + "   •   " + zoo.getZooName() + ", " + zoo.getAddress()
        );
        footer.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        footer.setForeground(TEXT_MUTED);
        footer.setBorder(new EmptyBorder(8, 0, 0, 0));

        JButton refreshBtn = makeSmallButton("↻ Refresh", TEXT_WHITE);
        refreshBtn.addActionListener(e -> {
            JTabbedPane tabs = (JTabbedPane) panel.getParent();
            if (tabs != null) {
                for (int i = 0; i < tabs.getTabCount(); i++) {
                    if (tabs.getComponentAt(i) == panel) {
                        tabs.setComponentAt(i, ReportBuild());
                        tabs.setSelectedIndex(i);
                        break;
                    }
                }
            }
        });

        JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        btnBar.setBackground(BG_DARK);
        btnBar.add(refreshBtn);

        JPanel south = new JPanel(new BorderLayout());
        south.setBackground(BG_DARK);
        south.add(footer,  BorderLayout.WEST);
        south.add(btnBar,  BorderLayout.EAST);
        return south;
    }

    // ADD SCHEDULE BUTTON
    private JButton makeAddButton(DefaultTableModel model) {
        JButton addBtn = makeSmallButton("+ Add Schedule", ACCENT_GREEN);

        addBtn.addActionListener(e -> {
            JTextField staffIdF = new JTextField();
            JTextField foodIdF  = new JTextField();
            JTextField timeF    = new JTextField("08:00:00");
            JTextField qtyF     = new JTextField();
            JTextField notesF   = new JTextField();

            // Habitat dropdown — replaces raw ID field from the original code
            String[] habitatNames = zoo.getHabitats().stream()
                    .map(Habitat::getName).toArray(String[]::new);
            JComboBox<String> habitatBox = new JComboBox<>(habitatNames);

            Object[] fields = {
                    "Staff ID:",                staffIdF,
                    "Habitat:",                 habitatBox,
                    "Food ID:",                 foodIdF,
                    "Feeding Time (HH:MM:SS):", timeF,
                    "Quantity (kg):",           qtyF,
                    "Notes:",                   notesF
            };

            int result = JOptionPane.showConfirmDialog(dashboardFrame, fields,
                    "Add New Schedule", JOptionPane.OK_CANCEL_OPTION);

            if (result != JOptionPane.OK_OPTION) return;

            // LO3 try-catch on every button click
            try {
                String staffIdS = staffIdF.getText().trim();
                String foodIdS  = foodIdF.getText().trim();
                String time     = timeF.getText().trim();
                String qtyS     = qtyF.getText().trim();
                String notes    = notesF.getText().trim();

                // ── Blank checks ──────────────────────────────────────────────
                if (staffIdS.isEmpty()) throw new ZooException("Staff ID cannot be blank.");
                if (foodIdS.isEmpty())  throw new ZooException("Food ID cannot be blank.");
                if (time.isEmpty())     throw new ZooException("Feeding time cannot be blank.");
                if (qtyS.isEmpty())     throw new ZooException("Quantity cannot be blank.");
                if (zoo.getHabitats().isEmpty())
                    throw new ZooException("No habitats available. Create a habitat first.");

                // ── Parse IDs ─────────────────────────────────────────────────
                int staffId, foodId;
                try { staffId = Integer.parseInt(staffIdS); }
                catch (NumberFormatException ex) {
                    throw new ZooException("Staff ID must be a whole number.");
                }
                try { foodId = Integer.parseInt(foodIdS); }
                catch (NumberFormatException ex) {
                    throw new ZooException("Food ID must be a whole number.");
                }

                // ── Parse quantity ────────────────────────────────────────────
                float qty;
                try { qty = Float.parseFloat(qtyS); }
                catch (NumberFormatException ex) {
                    throw new ZooException("Quantity must be a number (e.g. 2.5).");
                }

                // ── Range / format checks ─────────────────────────────────────
                if (staffId <= 0) throw new ZooException("Staff ID must be a positive number.");
                if (foodId  <= 0) throw new ZooException("Food ID must be a positive number.");
                if (qty     <= 0) throw new ZooException("Quantity must be greater than 0.");
                if (notes.length() > 200)
                    throw new ZooException("Notes must be 200 characters or fewer.");
                if (!time.matches("^([01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d$"))
                    throw new ZooException("Feeding time must be HH:MM:SS (e.g. 08:30:00).");

                // ── Resolve habitat ───────────────────────────────────────────
                Habitat selectedHab = zoo.getHabitats().get(habitatBox.getSelectedIndex());

                // ── Duplicate check ───────────────────────────────────────────
                boolean duplicate = selectedHab.getFeedingTimes().stream()
                        .anyMatch(s -> s.getAssignedKeeper() == staffId
                                && time.equals(s.getFeedingTime())); // null-safe: time is never null here
                if (duplicate)
                    throw new ZooException("Duplicate: this staff already has a schedule "
                            + "for " + selectedHab.getName() + " at " + time + ".");

                // ── Build Schedule object ─────────────────────────────────────
                Schedule newSch = new Schedule(0, 0, staffId, foodId, time, qty, notes, false);

                // ── zoo.addScheduleToHabitat() ────────────────────────────────
                // Checks SCHEDULE_MANAGE permission + links to habitat.feedingTimes
                zoo.addScheduleToHabitat(newSch, selectedHab);

                model.addRow(new Object[]{
                        0, staffId, selectedHab.getName(), foodId,
                        time, String.format("%.2f", qty),
                        notes.isEmpty() ? "—" : notes, "Pending"
                });

                JOptionPane.showMessageDialog(dashboardFrame,
                        "Schedule added to " + selectedHab.getName() + "!");

            } catch (ZooException ex) {
                // LO4 multiple catch — permission denied, duplicate, validation
                JOptionPane.showMessageDialog(dashboardFrame,
                        ex.getMessage(), "Validation Error", JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dashboardFrame,
                        "Unexpected error: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return addBtn;
    }

    // REMOVE SCHEDULE BUTTON
    private JButton makeRemoveButton(JTable table, DefaultTableModel model) {
        JButton delBtn = makeSmallButton("- Remove Schedule", ACCENT_RED);

        delBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(dashboardFrame,
                        "Please select a schedule to remove.");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(dashboardFrame,
                    "Remove selected schedule?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                // Match by schedule ID (col 0) — safe against reordering
                int scheduleId = (int) model.getValueAt(row, 0);
                try {
                    zoo.removeSchedule(scheduleId);
                    model.removeRow(row);
                } catch (ZooException ex) {
                    JOptionPane.showMessageDialog(dashboardFrame,
                            "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        return delBtn;
    }
}

