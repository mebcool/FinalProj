import javax.imageio.IIOParam;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class View {
    public JTextField inputUsernameField, inputPasswordField;
    JFrame frame;
    JTabbedPane tabPanel;
    private JPanel loginPanel, coinFlipPanel, diceGamePanel;
    public JLabel userInfoLabel; // This label is used across panels and should not be re-added.
    private ActionListener actionListener;
    private Controller controller;

    public View(ActionListener actionListener) {
        this.actionListener = actionListener;
        this.controller = (Controller) actionListener;
        frame = new JFrame("Game Client");
        frame.setLayout(new BorderLayout());  // Set BorderLayout for the frame

        userInfoLabel = new JLabel("User: "); // Initialize with default text.
        frame.add(userInfoLabel, BorderLayout.NORTH); // Keep userInfoLabel always at the top

        tabPanel = new JTabbedPane();
        createLoginTab();

        frame.add(tabPanel, BorderLayout.CENTER); // Ensure tabPanel is only in the center
        frame.setSize(600,600);
        frame.setVisible(true);

    }

    private void createLoginTab() {
        loginPanel = new JPanel();
        inputUsernameField = new JTextField(15);
        inputPasswordField = new JTextField(15);
        JButton loginButton = new JButton("LOGIN");
        loginButton.addActionListener(actionListener);

        loginPanel.add(new JLabel("Username:"));
        loginPanel.add(inputUsernameField);
        loginPanel.add(new JLabel("Password:"));
        loginPanel.add(inputPasswordField);
        loginPanel.add(loginButton);

        tabPanel.addTab("Login", loginPanel);
    }

    public void showGameTab(String username, int balance, ArrayList<String> leaderboardData) {
        tabPanel.removeAll(); // Clear previous tabs
        updateUserInfo(username, balance); // Update user info immediately after login

        coinFlipPanel = createCoinFlipPanel();
        diceGamePanel = createDiceGamePanel();

        tabPanel.addTab("Coin Flip", coinFlipPanel);
        tabPanel.addTab("Dice", diceGamePanel);

        // Add the leaderboard tab
        tabPanel.addTab("Leaderboard", new JScrollPane(new JTextArea()));

        // Add a ChangeListener to the tabPanel to update the leaderboard when the leaderboard tab is selected
        tabPanel.addChangeListener(e -> {
            if (tabPanel.getSelectedIndex() == tabPanel.indexOfTab("Leaderboard")) {
                updateLeaderboardTab(leaderboardData);
            }
        });

        createLogoutTab();
    }


    private JPanel createCoinFlipPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Options Panel for Heads or Tails
        JPanel optionsPanel = new JPanel(new FlowLayout());
        ButtonGroup group = new ButtonGroup();

        // Create image icons for the buttons
        ImageIcon origIcon = new ImageIcon(getClass().getResource("/heads.jpg")); // Adjust path as needed
        Image img = origIcon.getImage();
        Image resizedImg = img.getScaledInstance(94, 66, Image.SCALE_SMOOTH); // Specify desired width and height
        ImageIcon headsIcon = new ImageIcon(resizedImg);
        ImageIcon origIcon2 = new ImageIcon(getClass().getResource("/tails.jpg")); // Adjust path as needed
        Image img2 = origIcon2.getImage();
        Image resizedImg2 = img2.getScaledInstance(94, 66, Image.SCALE_SMOOTH); // Specify desired width and height
        ImageIcon tailsIcon = new ImageIcon(resizedImg2);

        JToggleButton headsButton = new JToggleButton(headsIcon);
        headsButton.setActionCommand("Heads");

        JToggleButton tailsButton = new JToggleButton(tailsIcon);
        tailsButton.setActionCommand("Tails");


        group.add(headsButton);
        group.add(tailsButton);
        optionsPanel.add(headsButton);
        optionsPanel.add(tailsButton);
        optionsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 50)); // Adjust horizontal and vertical gaps


        // Bet Panel for amount input and bet action
        JPanel betPanel = new JPanel(new FlowLayout());
        JTextField betAmountField = new JTextField(5);
        JButton placeBetButton = new JButton("Flip Coin");
        placeBetButton.addActionListener(e -> {
            String betType = headsButton.isSelected() ? "Heads" : "Tails";
            String betAmount = betAmountField.getText();
            controller.placeCoinBet(betType, betAmount);
        });

        betPanel.add(new JLabel("Bet Amount:"));
        betPanel.add(betAmountField);
        betPanel.add(placeBetButton);

        panel.add(optionsPanel, BorderLayout.NORTH);
        panel.add(betPanel, BorderLayout.SOUTH);

        return panel;
    }


    private JPanel createDiceGamePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Options Panel for dice numbers
        JPanel optionsPanel = new JPanel(new FlowLayout());
        ButtonGroup group = new ButtonGroup();
        JToggleButton[] diceButtons = new JToggleButton[6];
        for (int i = 1; i <= 6; i++) {
            ImageIcon icon = new ImageIcon(getClass().getResource("/" + i + ".jpg"));
            diceButtons[i - 1] = new JToggleButton(icon);
            diceButtons[i - 1].setActionCommand(String.valueOf(i)); // Set action command to the dice number
            group.add(diceButtons[i - 1]);
            optionsPanel.add(diceButtons[i - 1]);
        }
        optionsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 50)); // Adjust horizontal and vertical gaps
        // Bet Panel
        JPanel betPanel = new JPanel(new FlowLayout());
        JTextField betAmountField = new JTextField(5);
        JButton placeBetButton = new JButton("Roll Dice");
        placeBetButton.addActionListener(e -> {
            // Get selected dice number from the group
            String selectedDiceNumber = null;
            for (JToggleButton btn : diceButtons) {
                if (btn.isSelected()) {
                    selectedDiceNumber = btn.getText();
                    break;
                }
            }

            // Get the bet amount from the text field
            String betAmount = betAmountField.getText();

            // Call controller method to handle the bet
            controller.placeDiceBet(selectedDiceNumber, betAmount);
        });

        betPanel.add(new JLabel("Bet Amount:"));
        betPanel.add(betAmountField);
        betPanel.add(placeBetButton);

        panel.add(optionsPanel);
        panel.add(betPanel);

        return panel;
    }


    public void updateGameResult(String result, int winnings) {
        JOptionPane.showMessageDialog(frame, "Result: " + result + " | Winnings: " + winnings);
    }

    public void updateUserInfo(String username, int balance) {
            userInfoLabel.setText("User: " + username + " | Balance: $" + balance);
    }

    private void createLogoutTab() {
        JPanel logoutPanel = new JPanel();
        JButton logoutButton = new JButton("LOGOUT");
        logoutButton.addActionListener(e -> {
            frame.dispose();
            new Controller ();
        });
        logoutPanel.add(logoutButton);
        tabPanel.addTab("Logout", logoutPanel);
    }

    public void showLoginError() {
        JOptionPane.showMessageDialog(frame, "Invalid username or password!");
    }

    public void setVisible(boolean visible) {
        frame.setVisible(visible);
    }

    public JFrame getFrame() {
        return frame;
    }

    public void updateLeaderboardTab(ArrayList<String> leaderboardData) {
        JTextArea leaderboardTextArea = new JTextArea();
        for (String entry : leaderboardData) {
            leaderboardTextArea.append(entry + "\n");
        }
        JScrollPane scrollPane = new JScrollPane(leaderboardTextArea);
        tabPanel.setComponentAt(tabPanel.indexOfTab("Leaderboard"), scrollPane);
    }
    public boolean isLeaderboardTabSelected() {
        return tabPanel.getSelectedIndex() == tabPanel.indexOfTab("Leaderboard");
    }
    public String getUsername() {
        return inputUsernameField.getText();
    }
}
