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
   public Either<CustomerError, Customer> add( String first_name, String last_name, String email, String phone, LocalDate dob){
        return customerDAO.add(first_name, last_name, email, phone, dob);
   }
   public Either<CustomerError, List<Customer>> update( String first_name, String last_name, String email, String phone, LocalDate dob, int id){
        return customerDAO.update(first_name, last_name, email, phone, dob, id);
    }

    @Override
    public Either<CustomerError, List<Customer>> delete(int id) {
        return customerDAO.delete(id);
    }

    @Override
    public Either<CustomerError, List<Customer>> deleteOrder(int customerId) {
        return customerDAO.deleteOrder(customerId);
    }

    @Override
    public Either<CustomerError, List<Customer>> deleteOrderItems(int customerId) {
        return customerDAO.deleteOrderItems(customerId);
    }

}

