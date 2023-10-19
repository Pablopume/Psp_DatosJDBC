package dao.imp;

import common.Constants;
import dao.CustomerDAO;
import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;
import model.Customer;
import model.errors.CustomerError;
import io.vavr.control.Either;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;


@Log4j2
public class CustomerDAOFiles implements CustomerDAO {
    private DBConnection db;

    @Inject
    public CustomerDAOFiles(DBConnection db) {
        this.db = db;
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
