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
public class CustomerDB implements CustomerDAO {
    private DBConnection db;

    @Inject
    public CustomerDB(DBConnection db) {
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
    public Either<CustomerError, Integer> delete(Customer customer, boolean deleteOrders){
        Either<CustomerError, Integer> result;
        if (!deleteOrders) {
            try (Connection con = db.getConnection();
                 PreparedStatement preparedStatement = con.prepareStatement("DELETE FROM customers WHERE id = ?");
                 PreparedStatement preparedStatementCredentials = con.prepareStatement("DELETE FROM credentials WHERE customer_id = ?")) {
                preparedStatement.setInt(1, customer.getId());
                preparedStatementCredentials.setInt(1, customer.getId());
                preparedStatementCredentials.executeUpdate();
                int rs = preparedStatement.executeUpdate();

                if (rs == 0) {
                    result = Either.left(new CustomerError(1,"Error deleting customer"));
                } else {
                    result = Either.right(0);
                }
            } catch (SQLException ex) {
                if (ex.getErrorCode() == 1451) {
                    result = Either.left(new CustomerError(2, "The customer has orders"));
                } else {
                    result = Either.left(new CustomerError(3, "Error connecting to database"));
                }
            }
        } else {
            try (Connection con = db.getConnection();
                 PreparedStatement preparedStatement = con.prepareStatement("DELETE FROM order_items WHERE order_id = (SELECT order_id FROM orders WHERE customer_id = ?)");
                 PreparedStatement preparedStatementDeleteOrders = con.prepareStatement("DELETE FROM orders WHERE customer_id = ?");
                 PreparedStatement preparedStatementCredentials = con.prepareStatement("DELETE FROM credentials WHERE customer_id = ?");
                 PreparedStatement preparedStatementCustomer = con.prepareStatement("DELETE FROM customers WHERE id = ?")) {
                preparedStatement.setInt(1, customer.getId());
                preparedStatementDeleteOrders.setInt(1, customer.getId());
                preparedStatement.executeUpdate();
                preparedStatementDeleteOrders.executeUpdate();
                preparedStatementCustomer.setInt(1, customer.getId());
                preparedStatementCredentials.setInt(1, customer.getId());
                preparedStatementCredentials.executeUpdate();


                int rs = preparedStatementCustomer.executeUpdate();

                if (rs == 0) {
                    result = Either.left(new CustomerError(1, "Error deleting customer"));
                } else {
                    result = Either.right(0);
                }
            } catch (SQLException ex) {
                result = Either.left(new CustomerError(3, "Error connecting to database"));
            }
        }
        return result;
    }
    public Either<CustomerError, List<Customer>> deleteOrderItems(int customerId){
        Either<CustomerError, List<Customer>> result=null;
        try (Connection myConnection = db.getConnection();
             PreparedStatement statement = myConnection.prepareStatement("delete from order_items where order_id in (select order_id from orders where customer_id=?)")) {
            statement.setInt(1, customerId);
            statement.executeUpdate();
            result = Either.right(getAll().get());
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
            if(customerContainOrders(id)) {
                result = Either.left(new CustomerError(0, Constants.ERROR_WHILE_RETRIEVING_ORDERS));
            }
            else {
                statement.setInt(1, id);
                statement.executeUpdate();
                deleteCredentials(id);
                result = Either.right(getAll().get());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            result = Either.left(new CustomerError(0, Constants.ERROR_WHILE_RETRIEVING_ORDERS));
        }
        return result;

    }

    private void deleteCredentials(int id) {
        try (Connection myConnection = db.getConnection();
             PreparedStatement statement = myConnection.prepareStatement("delete from credentials where customer_id=?")){
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
    private boolean customerContainOrders(int id) {
        boolean resultado = false;
        try (Connection myConnection = db.getConnection();
             Statement statement = myConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_READ_ONLY);
             ResultSet rs = statement.executeQuery("select * from orders where customer_id=" + id)) {
            if (rs.next()) {
                resultado = true;
            }
        } catch (SQLException e) {
            SQLException ex = e.getNextException();
        }
        return resultado;
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
