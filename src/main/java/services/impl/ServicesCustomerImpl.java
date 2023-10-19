package services.impl;

import dao.CustomerDAO;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import model.Customer;
import model.Order;
import model.errors.CustomerError;
import services.ServicesCustomer;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Singleton
public class ServicesCustomerImpl implements ServicesCustomer {
    private final CustomerDAO customerDAO;

    @Inject
    public ServicesCustomerImpl(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    @Override
    public Either<CustomerError, List<Customer>> getAll() {
        return customerDAO.getAll();
    }


}

