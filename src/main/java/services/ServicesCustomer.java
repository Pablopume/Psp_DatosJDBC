package services;

import io.vavr.control.Either;
import model.Customer;
import model.Order;
import model.errors.CustomerError;

import java.time.LocalDate;
import java.util.List;

public interface ServicesCustomer {
    Either<CustomerError, List<Customer>> getAll();


}
