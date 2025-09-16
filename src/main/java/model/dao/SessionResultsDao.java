package model.dao;

import model.entity.SessionResults;
import datasource.MariaDbJpaConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SessionResultsDao {

    public List<SessionResults> getAllSessionResults() throws SQLException {
        List<SessionResults> results = new ArrayList<>();
        String sql = "SELECT * FROM sessionresults";

        try (Connection conn = MariaDbJpaConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int result_id = rs.getInt("result_id");
                int session_id = rs.getInt("session_id");
                int card_id = rs.getInt("card_id");
                boolean is_correct = rs.getBoolean("is_correct");
                int response_time = rs.getInt("response_time");

                SessionResults result = new SessionResults(session_id, card_id, is_correct, response_time);
                result.setResult_id(result_id);
                results.add(result);
            }
        }

        return results;
    }

    public void persist(SessionResults result) throws SQLException {
        String sql = "INSERT INTO sessionresults (session_id, card_id, is_correct, response_time) VALUES (?, ?, ?, ?)";
        try (Connection conn = MariaDbJpaConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, result.getSession_id());
            ps.setInt(2, result.getCard_id());
            ps.setBoolean(3, result.isIs_correct());
            ps.setInt(4, result.getResponse_time());
            ps.executeUpdate();
        }
    }
}