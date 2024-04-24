import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Controller implements ActionListener {
    private View view;
    private Model model;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public Controller() {
        view = new View(this);
        model = new Model();
        try {
            clientSocket = new Socket("localhost", 5000);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        view.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String username = view.inputUsernameField.getText();
        String password = view.inputPasswordField.getText();
        try {
            out.println(username + " " + password); // Sends data to the server
            String response = in.readLine(); // Reads response from the server
            if (response.equals("valid")) {
                view.showGameTab();
            } else {
                view.showLoginError();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            in.close();
            out.close();
            clientSocket.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void finalize() {
        closeConnection();
    }
}
