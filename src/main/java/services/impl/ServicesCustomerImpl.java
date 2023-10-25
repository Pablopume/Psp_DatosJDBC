package services.impl;

import dao.CustomerDAO;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import model.Customer;
import model.errors.CustomerError;
import services.ServicesCustomer;

import java.time.LocalDate;
import java.util.List;

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
   public Either<CustomerError, List<Customer>> add(Customer customer){
        return customerDAO.add(customer);
   }
   public Either<CustomerError, List<Customer>> update(Customer customer){
        return customerDAO.update(customer);
    }

    @Override
    public Either<CustomerError, Integer> delete(Customer customer, boolean deleteOrders) {
        return customerDAO.delete(customer, deleteOrders);
    }


}

