package dao;

import io.vavr.control.Either;
import model.Customer;
import model.errors.CustomerError;

import java.time.LocalDate;
import java.util.List;

public interface CustomerDAO {

    Either<CustomerError, List<Customer>> getAll();
    Either<CustomerError, Customer> add( String first_name, String last_name, String email, String phone, LocalDate dob);
    Either<CustomerError, List<Customer>> update( String first_name, String last_name, String email, String phone, LocalDate dob, int id);
    Either<CustomerError, List<Customer>> delete(int id);
}

