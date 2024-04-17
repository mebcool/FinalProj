import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class View {

    public JTextField inputUsernameField;
    public JTextField inputPasswordField;
    public View(){
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JTabbedPane tabPanel = new JTabbedPane();

        JPanel page1 = new JPanel(new BorderLayout());

        inputUsernameField = new JTextField(15);
        inputPasswordField = new JTextField(15);
        JButton computeButton = new JButton("LOGIN");

        page1.add(inputUsernameField, BorderLayout.NORTH);
        page1.add(inputPasswordField, BorderLayout.CENTER);
        page1.add(computeButton, BorderLayout.SOUTH);

        tabPanel.addTab("Login", page1);

        //computeButton.addActionListener(actionListener);

        frame.add(tabPanel);

        frame.getContentPane().add(tabPanel);
        frame.setSize(400, 300);
        frame.setVisible(true);
    }
}
