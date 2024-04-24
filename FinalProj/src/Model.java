
import java.sql.*;
import java.util.ArrayList;


public class Model {
    private String url;
    public Model(){
        url = "jdbc:sqlite:database.db";
        try {
            Connection conn = DriverManager.getConnection(url);
            String createTableCmd = "CREATE TABLE IF NOT EXISTS users (" +
                    "user_id INTEGER PRIMARY KEY," +
                    "username TEXT UNIQUE NOT NULL,"+
                    "password TEXT NOT NULL," +
                    "balance INTEGER NOT NULL);";
            conn.createStatement().executeUpdate(createTableCmd);
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createUser(String username, String password){
        try {
            Connection conn = DriverManager.getConnection(url);
            String addUserCmd = """
                        INSERT INTO users (username, password, balance) 
                        VALUES(? ,? ,?);
                    """;
            PreparedStatement preparedStatement = conn.prepareStatement(addUserCmd);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setInt(3, 500);
            preparedStatement.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<String> getUser(String username){
        ArrayList<String> user = new ArrayList<String>();
        try {

            Connection conn = DriverManager.getConnection(url);
            String getUserCmd = """
                     SELECT * FROM users WHERE username = ?;
                    """;
            PreparedStatement preparedStatement = conn.prepareStatement(getUserCmd);
            preparedStatement.setString(1, username);
            ResultSet rs = preparedStatement.executeQuery();


            String id = Integer.toString(rs.getInt("user_id"));
            user.add(id);
            user.add(username);
            String password = rs.getString("password");
            user.add(password);
            String balance = Integer.toString(rs.getInt("balance"));
            user.add(balance);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    public void setBalance(int newBalance, int userId){
        try {
            Connection conn = DriverManager.getConnection(url);
            String updateBalance = """
                    UPDATE users
                    SET balance = ?
                    WHERE user_id = ?;
                    """;
            PreparedStatement preparedStatement = conn.prepareStatement(updateBalance);
            preparedStatement.setInt(1, newBalance);
            preparedStatement.setInt(2, userId);
            preparedStatement.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getBalance(String userID){
        try {
            Connection conn = DriverManager.getConnection(url);
            String selectBalance = """
                    SELECT balance FROM users WHERE user_id = ?;
                    """;
            PreparedStatement preparedStatement = conn.prepareStatement(selectBalance);
            preparedStatement.setString(1, userID);
            ResultSet rs = preparedStatement.executeQuery();

            int balance = rs.getInt("balance");
            conn.close();
            return balance;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public boolean authenticateUser(String username, String password) {
        try {
            Connection conn = DriverManager.getConnection(url);
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet rs = preparedStatement.executeQuery();
            boolean exists = rs.next();
            conn.close();
            return exists;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
