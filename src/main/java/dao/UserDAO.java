/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import com.disasterresponse.DatabaseConnection;
import com.disasterresponse.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Lenovo
 */
public class UserDAO {
    // Method to update the user's role
    public void updateUserRoleInDatabase(User updatedUser) {
        String query = "UPDATE users SET AccessLevel = ? WHERE Username = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, updatedUser.getRole());
            stmt.setString(2, updatedUser.getUsername());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to retrieve a user by username
    public User getUserByUsername(String username) {
        String query = "SELECT * FROM users WHERE Username = ?";
        User user = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setUsername(rs.getString("Username"));
                user.setRole(rs.getString("AccessLevel"));
                // Populate other user fields as needed
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    // Method to get all users
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                User user = new User();
                user.setUsername(rs.getString("Username"));
                user.setRole(rs.getString("AccessLevel"));
                // Populate other user fields as needed
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    // Method to delete a user by username
    public void deleteUserByUsername(String username) {
        String query = "DELETE FROM users WHERE Username = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
