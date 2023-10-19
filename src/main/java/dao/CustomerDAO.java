package dao;

import io.vavr.control.Either;
import model.Customer;
import model.errors.CustomerError;

import java.time.LocalDate;
import java.util.List;

public interface CustomerDAO {

    Either<CustomerError, List<Customer>> getAll();


}

