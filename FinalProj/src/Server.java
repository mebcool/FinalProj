import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

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
                String[] userDetails = credentials.split(" ");
                String username = userDetails[0];
                String password = userDetails[1];

                // Authenticate or create user
                if (model.authenticateUser(username, password)) {
                    writer.println("valid");
                } else {
                    model.createUser(username, password);
                    writer.println("valid"); // Send valid after creating user
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
