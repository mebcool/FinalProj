import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

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
            System.out.println("Error connecting to server: " + e.getMessage());
        }
        view.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Action Performed by: " + e.getSource().toString());
        System.out.println("Action Command: " + e.getActionCommand());
        if (e.getActionCommand().equals("LOGIN")) {
            performLogin();
        } else if (e.getActionCommand().equals("LeaderboardTabClicked")) {
            updateLeaderboard();
        }
    }

    public void placeCoinBet(String selectedBet, String betAmountText) {
        try {
            String username = view.getUsername(); // Assume this method fetches the currently logged-in user
            int betAmount = Integer.parseInt(betAmountText);
            int currentBalance = model.getBalance(username);

            if (betAmount > currentBalance) {
                JOptionPane.showMessageDialog(view.getFrame(), "Insufficient balance to place bet", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            FlipCoinGame game = new FlipCoinGame();
            String gameOutcome = game.playGame();
            String result = game.result(gameOutcome, selectedBet);
            int payout = game.payout(result, betAmount);

            updateBalanceAfterBet(username, result, payout, betAmount);
            view.updateGameResult(result, payout);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view.getFrame(), "Invalid bet amount", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void placeDiceBet(String selectedBet, String betAmountText) {
        try {
            String username = view.getUsername();
            int betAmount = Integer.parseInt(betAmountText);
            int currentBalance = model.getBalance(username);

            if (betAmount > currentBalance) {
                JOptionPane.showMessageDialog(view.getFrame(), "Insufficient balance to place bet", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            DiceGame game = new DiceGame();
            String gameOutcome = game.playGame();
            String result = game.result(gameOutcome, selectedBet);
            int payout = game.payout(result, betAmount);

            updateBalanceAfterBet(username, result, payout, betAmount);
            view.updateGameResult(result, payout);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view.getFrame(), "Invalid bet amount", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void updateBalanceAfterBet(String username, String outcome, int winnings, int betAmount) {
        int currentBalance = model.getBalance(username);
        if (outcome.equals("Win")) {
            currentBalance += winnings- betAmount;
        } else {
            currentBalance -= betAmount;
        }

        if (currentBalance <= 0) {
            JOptionPane.showMessageDialog(view.getFrame(), "You're Broke! Your balance will be reset to $500.", "you bust!", JOptionPane.WARNING_MESSAGE);
            currentBalance = 500;  // Reset balance
        }

        model.setBalance(username, currentBalance);
        view.updateUserInfo(username, currentBalance);  // Update UI
        System.out.println("leaderboard updated");
        updateLeaderboard();

        if (view.isLeaderboardTabSelected()) {
            System.out.println("leaderboard selected");
            updateLeaderboard(); // Refresh the leaderboard if it is currently being viewed
        }
    }

    private void performLogin() {
        String username = view.inputUsernameField.getText();
        String password = view.inputPasswordField.getText();
        try {
            System.out.println("username:" + username);
            out.println("login " + username + " " + password); // Sends login data to the server
            String response = in.readLine(); // Reads response from the server
            System.out.println("Received from server: " + response); // Add this to see what you actually got
            if (response != null && response.startsWith("valid")) {
                String[] parts = response.split(" ");
                if (parts.length >= 3) {
                    String user = parts[1];
                    int balance = Integer.parseInt(parts[2]);
                    // Retrieve the leaderboard data from the Model
                    TreeMap<Integer, String> leaderboardData = model.getAllUserBalances();
                    // Convert TreeMap to ArrayList<String>
                    ArrayList<String> leaderboardList = new ArrayList<>();
                    for (Map.Entry<Integer, String> entry : leaderboardData.entrySet()) {
                        leaderboardList.add(entry.getValue() + ": $" + entry.getKey());
                    }
                    // Pass the leaderboard data along with the username and balance
                    view.showGameTab(user, balance, leaderboardList);
                } else {
                    System.out.println("Invalid response format: " + response);
                    view.showLoginError();
                }
            } else {
                view.showLoginError();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Login failed: " + ex.getMessage());
        }
    }
    private void updateLeaderboard() {
        try {
            TreeMap<Integer, String> leaderboardData = model.getAllUserBalances();
            ArrayList<String> leaderboardEntries = new ArrayList<>();
            for (Map.Entry<Integer, String> entry : leaderboardData.entrySet()) {
                leaderboardEntries.add(entry.getValue() + ": $" + entry.getKey());
            }
            view.updateLeaderboardTab(leaderboardEntries); // Command the view to update the leaderboard
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Failed to update leaderboard: " + ex.getMessage());
        }
    }

}
