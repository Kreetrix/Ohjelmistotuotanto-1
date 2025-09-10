package model.dao;

import model.entity.AppUsers;
import datasource.MariaDbJpaConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppUsersDao {

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

    public void persist(AppUsers user) throws SQLException {
        String sql = "INSERT INTO app_users (username, email, password_hash, role, is_active, created_at) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = MariaDbJpaConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword_hash());
            ps.setString(4, user.getRole());
            ps.setInt(5, user.getIs_active());
            ps.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
            ps.executeUpdate();
        }
    }
}
