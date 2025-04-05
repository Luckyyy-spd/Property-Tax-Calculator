package machineprob;

import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Database {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "property_tax_db";
    private static final String USER = "root";
    private static final String PASS = "";
    
    private Connection conn = null;
    private PreparedStatement pst;
    private Statement stmt;
    public ResultSet rs;
    
    public Database() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            try {
                conn = DriverManager.getConnection(DB_URL + DB_NAME, USER, PASS);
                stmt = conn.createStatement();
            } catch (SQLException e) {
                createDatabase();
                conn = DriverManager.getConnection(DB_URL + DB_NAME, USER, PASS);
                stmt = conn.createStatement();
                createTables();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                "Database connection error: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void createDatabase() {
        try (Connection tempConn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = tempConn.createStatement()) {
            
            String sql = "CREATE DATABASE IF NOT EXISTS " + DB_NAME;
            stmt.executeUpdate(sql);
            
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                "Error creating database: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void createTables() {
        try {
            String sql = """
                CREATE TABLE IF NOT EXISTS property_records (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    property_type VARCHAR(20) NOT NULL,
                    location VARCHAR(20) NOT NULL,
                    land_fmv DECIMAL(15,2) NOT NULL,
                    improvement_fmv DECIMAL(15,2) NOT NULL,
                    total_rpt DECIMAL(15,2) NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """;
            stmt.executeUpdate(sql);
            
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                "Error creating tables: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    public void addRecord(String propertyType, String location, double landFMV, 
                         double improvementFMV, double totalRPT) throws SQLException {
        String sql = """
            INSERT INTO property_records 
            (property_type, location, land_fmv, improvement_fmv, total_rpt)
            VALUES (?, ?, ?, ?, ?)
        """;
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, propertyType);
            pstmt.setString(2, location);
            pstmt.setDouble(3, landFMV);
            pstmt.setDouble(4, improvementFMV);
            pstmt.setDouble(5, totalRPT);
            
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                "Error saving record: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
            throw e;
        }
    }
    
    public void loadRecords(DefaultTableModel tableModel) {
        String sql = "SELECT * FROM property_records ORDER BY created_at DESC";
        
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (tableModel.getRowCount() > 0) {
                tableModel.removeRow(0);
            }
            
            while (rs.next()) {
                Object[] row = {
                    rs.getString("property_type"),
                    rs.getString("location"),
                    String.format("%.2f", rs.getDouble("land_fmv")),
                    String.format("%.2f", rs.getDouble("improvement_fmv")),
                    String.format("%.2f", rs.getDouble("total_rpt"))
                };
                tableModel.addRow(row);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                "Error loading records: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void searchRecords(DefaultTableModel tableModel, String searchTerm) {
        String sql = """
            SELECT * FROM property_records 
            WHERE property_type LIKE ? 
            OR location LIKE ? 
            OR land_fmv LIKE ? 
            OR improvement_fmv LIKE ? 
            OR total_rpt LIKE ?
            ORDER BY created_at DESC
        """;
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String searchPattern = "%" + searchTerm + "%";
            for (int i = 1; i <= 5; i++) {
                pstmt.setString(i, searchPattern);
            }
            
            ResultSet rs = pstmt.executeQuery();
            
            while (tableModel.getRowCount() > 0) {
                tableModel.removeRow(0);
            }
            
            while (rs.next()) {
                Object[] row = {
                    rs.getString("property_type"),
                    rs.getString("location"),
                    String.format("%.2f", rs.getDouble("land_fmv")),
                    String.format("%.2f", rs.getDouble("improvement_fmv")),
                    String.format("%.2f", rs.getDouble("total_rpt"))
                };
                tableModel.addRow(row);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                "Error searching records: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void clearAllRecords() throws SQLException {
        String sql = "DELETE FROM property_records";
        try {
            pst = conn.prepareStatement(sql);
            pst.executeUpdate();
        } finally {
            if (pst != null) {
                pst.close();
            }
        }
    }

    public void deleteRecord(String propertyType, String location, double landFMV, 
                           double improvementFMV, double totalRPT) throws SQLException {
        String sql = """
            DELETE FROM property_records 
            WHERE property_type = ? 
            AND location = ? 
            AND land_fmv = ? 
            AND improvement_fmv = ? 
            AND total_rpt = ?
        """;
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, propertyType);
            pstmt.setString(2, location);
            pstmt.setDouble(3, landFMV);
            pstmt.setDouble(4, improvementFMV);
            pstmt.setDouble(5, totalRPT);
            
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                "Error deleting record: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
            throw e;
        }
    }
}
    
  