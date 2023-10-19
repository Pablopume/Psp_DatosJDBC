package dao.imp;

import common.Constants;
import dao.CustomerDAO;
import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;
import model.Customer;
import model.errors.CustomerError;
import io.vavr.control.Either;

import java.sql.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Log4j2
public class CustomerDAOFiles implements CustomerDAO {
    private DBConnection db;

    @Inject
    public CustomerDAOFiles(DBConnection db) {
        this.db = db;
    }
    public Either<CustomerError, List<Customer>> add(Customer customer){
        Either<CustomerError, List<Customer>> result=null;
        try (Connection myConnection = db.getConnection();
             PreparedStatement statement = myConnection.prepareStatement("insert into customers (first_name, last_name, email, phone, date_of_birth) values (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, customer.getFirst_name());
            statement.setString(2, customer.getLast_name());
            statement.setString(3, customer.getEmail());
            statement.setString(4, customer.getPhone());
            statement.setDate(5, Date.valueOf(customer.getDob()));
            statement.executeQuery();
            ResultSet rs = statement.getGeneratedKeys();
            rs.next();
            customer.setId(rs.getInt(1));
            result = Either.right(readRS(rs).get());
        } catch (SQLException e) {
            e.printStackTrace();
            result = Either.left(new CustomerError(0, Constants.ERROR_WHILE_RETRIEVING_ORDERS));
        }
        return result;

    }
    public Either<CustomerError, List<Customer>> delete(int id){
        Either<CustomerError, List<Customer>> result=null;
        try (Connection myConnection = db.getConnection();
             PreparedStatement statement = myConnection.prepareStatement("delete from customers where id=?")) {
            statement.setInt(1, id);
            statement.executeUpdate();
            result = Either.right(readRS(statement.getResultSet()).get());
        } catch (SQLException e) {
            e.printStackTrace();
            result = Either.left(new CustomerError(0, Constants.ERROR_WHILE_RETRIEVING_ORDERS));
        }
        return result;

    }

    public Either<CustomerError, List<Customer>> update( String first_name, String last_name, String email, String phone, LocalDate dob, int id){
        Either<CustomerError, List<Customer>> result=null;
        try (Connection myConnection = db.getConnection();
             PreparedStatement statement = myConnection.prepareStatement("update customers set first_name=?, last_name=?, email=?, phone=?, date_of_birth=? where id=?")) {
            statement.setString(1, first_name);
            statement.setString(2, last_name);
            statement.setString(3, email);
            statement.setString(4, phone);
            statement.setDate(5, Date.valueOf(dob));
            statement.setInt(6, id);
            statement.executeUpdate();
            result = Either.right(getAll().get());
        } catch (SQLException e) {
            e.printStackTrace();
            result = Either.left(new CustomerError(0, Constants.ERROR_WHILE_RETRIEVING_ORDERS));
        }
        return result;

    }
    private int getNextId(){
        int result=0;
        try (Connection myConnection = db.getConnection();
             Statement statement = myConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_READ_ONLY)) {
            ResultSet rs = statement.executeQuery("select max(id) from customers");
            rs.next();
            result=rs.getInt(1)+1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    public Either<CustomerError, List<Customer>> get(int id){
        Either<CustomerError, List<Customer>> result=null;
        try (Connection myConnection = db.getConnection();
             PreparedStatement statement = myConnection.prepareStatement("select * from customers where id=?")) {
            statement.setInt(1, id);
            statement.executeUpdate();
            result = Either.right(readRS(statement.getResultSet()).get());
        } catch (SQLException e) {
            e.printStackTrace();
            result = Either.left(new CustomerError(0, Constants.ERROR_WHILE_RETRIEVING_ORDERS));
        }
        return result;

    }
    public Either<CustomerError, Customer> add( String first_name, String last_name, String email, String phone, LocalDate dob){
        Either<CustomerError, Customer> result=null;
        int id=getNextId();
        try (Connection myConnection = db.getConnection();
             PreparedStatement statement = myConnection.prepareStatement("insert into customers (first_name, last_name, email, phone, date_of_birth, id) values (?,?,?,?,?,?)")) {
            statement.setString(1, first_name);
            statement.setString(2, last_name);
            statement.setString(3, email);
            statement.setString(4, phone);
            statement.setDate(5, Date.valueOf(dob));
            statement.setInt(6, id);
            statement.executeUpdate();

            Customer customer = new Customer(
                    id,
                    first_name,
                    last_name,
                    email,
                    phone,
                    dob
            );
            result = Either.right(customer);
        } catch (SQLException e) {
            e.printStackTrace();
            result = Either.left(new CustomerError(0, Constants.ERROR_WHILE_RETRIEVING_ORDERS));
        }
        return result;
    }


    @Override
    public Either<CustomerError, List<Customer>> getAll() {

        Either<CustomerError, List<Customer>> result=null;

        try (Connection myConnection = db.getConnection();
             Statement statement = myConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_READ_ONLY)) {
            ResultSet rs = statement.executeQuery("select * from customers");



            result = Either.right(readRS(rs).get());
        } catch (SQLException e) {


        }
        return result;
    }

    private Either<CustomerError, List<Customer>> readRS(ResultSet rs) {
        Either<CustomerError, List<Customer>> either;
        try {
            List<Customer> customers = new ArrayList<>();
            while (rs.next()) {
                Customer resultCustomer = new Customer(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getDate("date_of_birth").toLocalDate()
                );
                customers.add(resultCustomer);
            }
            either = Either.right(customers);
        } catch (SQLException e) {
            e.printStackTrace();
            either = Either.left(new CustomerError(0, Constants.ERROR_WHILE_RETRIEVING_ORDERS));
        }
        return either;
    }

}
