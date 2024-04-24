import javax.swing.*;
import java.awt.event.ActionListener;

public class View {

    public JTextField inputUsernameField;
    public JTextField inputPasswordField;
    private JFrame frame;

    public View(ActionListener actionListener) {
        frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JTabbedPane tabPanel = new JTabbedPane();
        JPanel loginPanel = new JPanel();

        inputUsernameField = new JTextField(15);
        inputPasswordField = new JTextField(15);
        JButton loginButton = new JButton("LOGIN");
        loginButton.addActionListener(actionListener); // Set the controller as the listener

        loginPanel.add(new JLabel("Username"));
        loginPanel.add(inputUsernameField);
        loginPanel.add(new JLabel("Password"));
        loginPanel.add(inputPasswordField);
        loginPanel.add(loginButton);

        tabPanel.addTab("Login", loginPanel);

        frame.add(tabPanel);
        frame.setSize(350, 350);
        frame.setVisible(true);
    }

    public void showGameTab() {
        // Switch to game tab
    }

    public void showLoginError() {
        JOptionPane.showMessageDialog(frame, "Invalid username or password!");
    }

    public void setVisible(boolean visible) {
        frame.setVisible(visible);
    }
}
