import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(5000);
            System.out.println("server> waiting for client to connect...");
            while (true) {
                Socket client = server.accept();
                System.out.println("server> connected to client Socket");
                System.out.println("server> waiting for client to send data...");

                //read bet input from client
                InputStreamReader input = new InputStreamReader(client.getInputStream());
                BufferedReader reader = new BufferedReader(input);
                String msg = reader.readLine();
                System.out.println("server> received: " + msg);

                //do game logic

                //output to client and update balance
                PrintWriter writer = new PrintWriter(client.getOutputStream());
                writer.println("test");
                writer.flush();

                reader.close();
                writer.close();
                client.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
