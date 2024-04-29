import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class Server {
    private static Model model = new Model();

    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(5000);
            System.out.println("Server> waiting for client to connect...");
            while (true) {
                Socket client = server.accept();
                System.out.println("Server> connected to client Socket");
                System.out.println("Server> waiting for client to send data...");

                BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintWriter writer = new PrintWriter(client.getOutputStream(), true);
                String credentials = reader.readLine();
                System.out.println("server> credentials:" + credentials);
                String[] userDetails = credentials.split(" ");
                System.out.println("server> userdetails:" + Arrays.toString(userDetails));
                String username = userDetails[1];
                String password = userDetails[2];

                // Authenticate or create user
                if (model.authenticateUser(username, password)) {
                    int balance = model.getBalance(username); // Assume getBalance returns int balance for a user
                    System.out.println("server> user balance:" + balance);
                    System.out.println("Authenticated user: " + username + ", Balance: " + balance); // Debug output
                    writer.println("valid " + username + " " + balance);
                } else {
                    model.createUser(username, password);
                    int balance = model.getBalance(username); // Balance after user creation
                    System.out.println("New user created: " + username + ", Initial Balance: " + balance); // Debug output
                    writer.println("valid " + username + " " + balance); // Send valid after creating user
                }

                reader.close();
                writer.close();
                client.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
