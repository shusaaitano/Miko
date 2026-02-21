package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import net.proteanit.sql.DbUtils;

public class configclass {
    
    // Single Connection Method to ensure consistency
    public Connection connectDB() {
        try {
            Class.forName("org.sqlite.JDBC");
            // Fixed the typo in the file name to gymsystem.db
            Connection con = DriverManager.getConnection("jdbc:sqlite:gymsystem.db");
            return con;
        } catch (Exception e) {
            System.out.println("Connection Failed: " + e);
            return null;
        }
    }

    // The method your register.java is looking for
    public int insertData(String sql, Object... values) {
        try (Connection conn = connectDB(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < values.length; i++) {
                pstmt.setObject(i + 1, values[i]);
            }

            pstmt.executeUpdate();
            System.out.println("Record added successfully!");
            return 1;
        } catch (SQLException e) {
            System.out.println("Insert Error: " + e.getMessage());
            return 0;
        }
    }

    public String authenticate(String sql, Object... values) {
        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < values.length; i++) {
                pstmt.setObject(i + 1, values[i]);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("u_status"); // Updated to match your column name
                }
            }
        } catch (SQLException e) {
            System.out.println("Login Error: " + e.getMessage());
        }
        return null;
    }
    
    public void displayData(String sql, javax.swing.JTable table) {
        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            table.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            System.out.println("Error displaying data: " + e.getMessage());
        }
    }
}