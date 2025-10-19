package dao;

import model.systemAdmin;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SystemAdminDAO {

    public void addSystemAdmin(systemAdmin admin) {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // Start transaction
            PreparedStatement psPerson = conn.prepareStatement(
                    "INSERT INTO Person (nationalID, firstName, lastName, dateOfBirth, address, emailAddress, contact) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?)"
            );
            psPerson.setInt(1, admin.getNationalID());
            psPerson.setString(2, admin.getFirstName());
            psPerson.setString(3, admin.getLastName());
            psPerson.setString(4, admin.getDateOfBirth());
            psPerson.setString(5, admin.getAddress());
            psPerson.setString(6, admin.getEmailAddress());
            psPerson.setInt(7, admin.getContact());
            psPerson.executeUpdate();

            PreparedStatement psAdmin = conn.prepareStatement(
                    "INSERT INTO SystemAdmin (adminID, adminPassword, nationalID) VALUES (?, ?, ?)"
            );
            psAdmin.setInt(1, admin.getAdminID());
            psAdmin.setString(2, admin.getAdminPassword());
            psAdmin.setInt(3, admin.getNationalID());
            psAdmin.executeUpdate();

            conn.commit(); // Commit transaction
        } catch (SQLException e) {
            try (Connection conn = DBConnection.getConnection()) {
                conn.rollback(); // Rollback on error
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
            throw new RuntimeException("Failed to add system admin: " + e.getMessage());
        }
    }

    public systemAdmin getSystemAdminByID(int adminID) {
        systemAdmin admin = null;
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM SystemAdmin a JOIN Person p ON a.nationalID = p.nationalID WHERE a.adminID = ?"
            );
            ps.setInt(1, adminID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                admin = new systemAdmin(
                        rs.getInt("nationalID"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("dateOfBirth"),
                        rs.getString("address"),
                        rs.getString("emailAddress"),
                        rs.getInt("contact"),
                        rs.getInt("adminID"),
                        rs.getString("adminPassword")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to retrieve system admin: " + e.getMessage());
        }
        return admin;
    }

    public List<systemAdmin> getAllSystemAdmins() {
        List<systemAdmin> admins = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM SystemAdmin a JOIN Person p ON a.nationalID = p.nationalID"
            );
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                systemAdmin admin = new systemAdmin(
                        rs.getInt("nationalID"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("dateOfBirth"),
                        rs.getString("address"),
                        rs.getString("emailAddress"),
                        rs.getInt("contact"),
                        rs.getInt("adminID"),
                        rs.getString("adminPassword")
                );
                admins.add(admin);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to retrieve system admins: " + e.getMessage());
        }
        return admins;
    }

    public boolean updateSystemAdmin(systemAdmin admin) {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // Start transaction
            PreparedStatement psPerson = conn.prepareStatement(
                    "UPDATE Person SET firstName = ?, lastName = ?, dateOfBirth = ?, address = ?, emailAddress = ?, contact = ? WHERE nationalID = ?"
            );
            psPerson.setString(1, admin.getFirstName());
            psPerson.setString(2, admin.getLastName());
            psPerson.setString(3, admin.getDateOfBirth());
            psPerson.setString(4, admin.getAddress());
            psPerson.setString(5, admin.getEmailAddress());
            psPerson.setInt(6, admin.getContact());
            psPerson.setInt(7, admin.getNationalID());
            psPerson.executeUpdate();

            PreparedStatement psAdmin = conn.prepareStatement(
                    "UPDATE SystemAdmin SET adminPassword = ? WHERE adminID = ?"
            );
            psAdmin.setString(1, admin.getAdminPassword());
            psAdmin.setInt(2, admin.getAdminID());
            psAdmin.executeUpdate();

            conn.commit(); // Commit transaction
            return true;
        } catch (SQLException e) {
            try (Connection conn = DBConnection.getConnection()) {
                conn.rollback(); // Rollback on error
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteSystemAdmin(int adminID) {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // Start transaction
            PreparedStatement psGet = conn.prepareStatement(
                    "SELECT nationalID FROM SystemAdmin WHERE adminID = ?"
            );
            psGet.setInt(1, adminID);
            ResultSet rs = psGet.executeQuery();
            if (rs.next()) {
                int nationalID = rs.getInt("nationalID");

                PreparedStatement psAdmin = conn.prepareStatement(
                        "DELETE FROM SystemAdmin WHERE adminID = ?"
                );
                psAdmin.setInt(1, adminID);
                psAdmin.executeUpdate();

                PreparedStatement psPerson = conn.prepareStatement(
                        "DELETE FROM Person WHERE nationalID = ?"
                );
                psPerson.setInt(1, nationalID);
                psPerson.executeUpdate();

                conn.commit(); // Commit transaction
                return true;
            }
            return false;
        } catch (SQLException e) {
            try (Connection conn = DBConnection.getConnection()) {
                conn.rollback(); // Rollback on error
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
            return false;
        }
    }
}