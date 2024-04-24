package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class View {

    public JTextField inputUsernameField;
    public JTextField inputPasswordField;
    public View(//actionlistener
    ){
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JTabbedPane tabPanel = new JTabbedPane();

        JPanel page1 = new JPanel();

        inputUsernameField = new JTextField(15);
        inputPasswordField = new JTextField(15);
        JButton computeButton = new JButton("LOGIN");

        page1.add(new JLabel("Username"));
        page1.add(inputUsernameField);

        page1.add(new JLabel("Password"));
        page1.add(inputPasswordField);
        page1.add(computeButton);

        tabPanel.addTab("Login", page1);

        //computeButton.addActionListener(actionListener);

        frame.add(tabPanel);

        frame.getContentPane().add(tabPanel);
        frame.setSize(350, 350);
        frame.setVisible(true);
    }
}
