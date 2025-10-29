package model.dao;

import datasource.MariaDbJpaConnection;
import util.PasswordUtil;
import model.entity.AppUsers;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppUsersDao {


    /**
     *
     * @return returns list of every user in database
     * @throws SQLException
     */
    public List<AppUsers> getAllUsers() throws SQLException {
        List<AppUsers> users = new ArrayList<>();
        String sql = "SELECT * FROM app_users";

        try (Connection conn = MariaDbJpaConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int user_id = rs.getInt("user_id");
                String username = rs.getString("username");
                String email = rs.getString("email");
                String password_hash = rs.getString("password_hash");
                String role = rs.getString("role");
                int is_active = rs.getInt("is_active");
                Timestamp created_at = rs.getTimestamp("created_at");

                AppUsers user = new AppUsers(username, email, password_hash, role, is_active, created_at);
                user.setUser_id(user_id);
                users.add(user);
            }
        }

        return users;
    }

    /**
     *
     * @param value     Boolean
     * @param id        User ID
     * @throws SQLException
     */
    // switches app_users value to active or not active 
    public void setActive(boolean value, int id) throws SQLException {
        String sql = "UPDATE app_users SET is_active = ? WHERE user_id = ?";

        try (Connection conn = MariaDbJpaConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, value ? 1 : 0);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    /**
     * inserts user into database
     * @param user
     * @throws SQLException
     */
    public void persist(AppUsers user) throws SQLException {
        String sql = "INSERT INTO app_users (username, email, password_hash, role, is_active, created_at) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = MariaDbJpaConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, PasswordUtil.hashPassword(user.getPassword_hash()));
            ps.setString(4, user.getRole());
            ps.setInt(5, user.getIs_active());
            ps.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
            ps.executeUpdate();
        }
    }

    /** Searches and returns user from database by username
     *
     * @param SearchName
     * @return User from database
     * @throws SQLException
     */
    public AppUsers getUserByUsername(String SearchName) throws SQLException {
        String sql = "SELECT * FROM app_users WHERE username = ?";
        try (Connection conn = MariaDbJpaConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, SearchName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int user_id = rs.getInt("user_id");
                    String username = rs.getString("username");
                    String email = rs.getString("email");
                    String password_hash = rs.getString("password_hash");
                    String role = rs.getString("role");
                    int is_active = rs.getInt("is_active");
                    Timestamp created_at = rs.getTimestamp("created_at");

                    AppUsers user = new AppUsers(username, email, password_hash, role, is_active, created_at);
                    user.setUser_id(user_id);
                    return user;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * Updates user information in the database
     * @param user AppUsers object with updated information
     * @throws SQLException
     */
    public void updateUser(AppUsers user) throws SQLException {
        String sql = "UPDATE app_users SET username = ?, email = ?, role = ? WHERE user_id = ?";
        try (Connection conn = MariaDbJpaConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getRole());
            ps.setInt(4, user.getUser_id());
            ps.executeUpdate();
        }
    }
}
