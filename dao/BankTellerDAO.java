package dao;

import model.bankTeller;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BankTellerDAO {

    public void addBankTeller(bankTeller teller) {
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement psPerson = conn.prepareStatement(
                    "INSERT INTO Person (nationalID, firstName, lastName, dateOfBirth, address, emailAddress, contact) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?)"
            );
            psPerson.setInt(1, teller.getNationalID());
            psPerson.setString(2, teller.getFirstName());
            psPerson.setString(3, teller.getLastName());
            psPerson.setString(4, teller.getDateOfBirth());
            psPerson.setString(5, teller.getAddress());
            psPerson.setString(6, teller.getEmailAddress());
            psPerson.setInt(7, teller.getContact());
            psPerson.executeUpdate();

            PreparedStatement psTeller = conn.prepareStatement(
                    "INSERT INTO BankTeller (bankTellerID, bankTellerPassword, nationalID) VALUES (?, ?, ?)"
            );
            psTeller.setInt(1, teller.getBankTellerID());
            psTeller.setString(2, teller.getBankTellerPassword());
            psTeller.setInt(3, teller.getNationalID());
            psTeller.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public bankTeller getBankTellerByID(int bankTellerID) {
        bankTeller teller = null;
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM BankTeller t JOIN Person p ON t.nationalID = p.nationalID WHERE t.bankTellerID = ?"
            );
            ps.setInt(1, bankTellerID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                teller = new bankTeller(
                        rs.getInt("nationalID"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("dateOfBirth"),
                        rs.getString("address"),
                        rs.getString("emailAddress"),
                        rs.getInt("contact"),
                        rs.getInt("bankTellerID"),
                        rs.getString("bankTellerPassword")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return teller;
    }

    public List<bankTeller> getAllBankTellers() {
        List<bankTeller> tellers = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM BankTeller t JOIN Person p ON t.nationalID = p.nationalID"
            );
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                bankTeller teller = new bankTeller(
                        rs.getInt("nationalID"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("dateOfBirth"),
                        rs.getString("address"),
                        rs.getString("emailAddress"),
                        rs.getInt("contact"),
                        rs.getInt("bankTellerID"),
                        rs.getString("bankTellerPassword")
                );
                tellers.add(teller);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tellers;
    }

    public boolean updateBankTeller(bankTeller teller) {
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement psPerson = conn.prepareStatement(
                    "UPDATE Person SET firstName = ?, lastName = ?, dateOfBirth = ?, address = ?, emailAddress = ?, contact = ? WHERE nationalID = ?"
            );
            psPerson.setString(1, teller.getFirstName());
            psPerson.setString(2, teller.getLastName());
            psPerson.setString(3, teller.getDateOfBirth());
            psPerson.setString(4, teller.getAddress());
            psPerson.setString(5, teller.getEmailAddress());
            psPerson.setInt(6, teller.getContact());
            psPerson.setInt(7, teller.getNationalID());
            psPerson.executeUpdate();

            PreparedStatement psTeller = conn.prepareStatement(
                    "UPDATE BankTeller SET bankTellerPassword = ? WHERE bankTellerID = ?"
            );
            psTeller.setString(1, teller.getBankTellerPassword());
            psTeller.setInt(2, teller.getBankTellerID());
            psTeller.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteBankTeller(int bankTellerID) {
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement psGet = conn.prepareStatement(
                    "SELECT nationalID FROM BankTeller WHERE bankTellerID = ?"
            );
            psGet.setInt(1, bankTellerID);
            ResultSet rs = psGet.executeQuery();
            if (rs.next()) {
                int nationalID = rs.getInt("nationalID");

                PreparedStatement psTeller = conn.prepareStatement(
                        "DELETE FROM BankTeller WHERE bankTellerID = ?"
                );
                psTeller.setInt(1, bankTellerID);
                psTeller.executeUpdate();

                PreparedStatement psPerson = conn.prepareStatement(
                        "DELETE FROM Person WHERE nationalID = ?"
                );
                psPerson.setInt(1, nationalID);
                psPerson.executeUpdate();
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}
