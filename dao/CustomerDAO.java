package dao;

import model.Customer;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDAO {

    public void addCustomer(Customer customer) {
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement psPerson = conn.prepareStatement(
                    "INSERT INTO Person (nationalID, firstName, lastName, dateOfBirth, address, emailAddress, contact) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?)"
            );
            psPerson.setInt(1, customer.getNationalID());
            psPerson.setString(2, customer.getFirstName());
            psPerson.setString(3, customer.getLastName());
            psPerson.setString(4, customer.getDateOfBirth());
            psPerson.setString(5, customer.getAddress());
            psPerson.setString(6, customer.getEmailAddress());
            psPerson.setInt(7, customer.getContact());
            psPerson.executeUpdate();

            PreparedStatement psCustomer = conn.prepareStatement(
                    "INSERT INTO Customer (customerID, customerPassword, nationalID) VALUES (?, ?, ?)"
            );
            psCustomer.setString(1, customer.getCustomerID());
            psCustomer.setString(2, customer.getCustomerPassword());
            psCustomer.setInt(3, customer.getNationalID());
            psCustomer.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Customer getCustomerByID(String customerID) {
        Customer customer = null;
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM Customer c JOIN Person p ON c.nationalID = p.nationalID WHERE c.customerID = ?"
            );
            ps.setString(1, customerID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                customer = new Customer(
                        rs.getInt("nationalID"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("dateOfBirth"),
                        rs.getString("address"),
                        rs.getString("emailAddress"),
                        rs.getInt("contact"),
                        rs.getString("customerID"),
                        rs.getString("customerPassword")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customer;
    }

    public boolean updateCustomer(Customer customer) {
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement psPerson = conn.prepareStatement(
                    "UPDATE Person SET firstName = ?, lastName = ?, dateOfBirth = ?, address = ?, emailAddress = ?, contact = ? WHERE nationalID = ?"
            );
            psPerson.setString(1, customer.getFirstName());
            psPerson.setString(2, customer.getLastName());
            psPerson.setString(3, customer.getDateOfBirth());
            psPerson.setString(4, customer.getAddress());
            psPerson.setString(5, customer.getEmailAddress());
            psPerson.setInt(6, customer.getContact());
            psPerson.setInt(7, customer.getNationalID());
            psPerson.executeUpdate();

            PreparedStatement psCustomer = conn.prepareStatement(
                    "UPDATE Customer SET customerPassword = ? WHERE customerID = ?"
            );
            psCustomer.setString(1, customer.getCustomerPassword());
            psCustomer.setString(2, customer.getCustomerID());
            psCustomer.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteCustomer(String customerID) {
        try (Connection conn = DBConnection.getConnection()) {
            // First, retrieve nationalID
            PreparedStatement psGet = conn.prepareStatement(
                    "SELECT nationalID FROM Customer WHERE customerID = ?"
            );
            psGet.setString(1, customerID);
            ResultSet rs = psGet.executeQuery();
            if (rs.next()) {
                int nationalID = rs.getInt("nationalID");

                // Delete from Customer
                PreparedStatement psCustomer = conn.prepareStatement(
                        "DELETE FROM Customer WHERE customerID = ?"
                );
                psCustomer.setString(1, customerID);
                psCustomer.executeUpdate();

                // Delete from Person
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