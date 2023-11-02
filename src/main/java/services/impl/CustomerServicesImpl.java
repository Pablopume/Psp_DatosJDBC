package services.impl;

import dao.CustomerDAO;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import model.Customer;
import model.errors.CustomerError;
import services.CustomerServices;

import java.util.Comparator;
import java.util.List;

@Singleton
public class CustomerServicesImpl implements CustomerServices {
    private final CustomerDAO customerDAO;

    @Inject
    public CustomerServicesImpl(CustomerDAO customerDAO) {
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

    @Override
    public int newId() {
        List<Customer> customers = customerDAO.getAll().get();
        customers.sort(Comparator.comparing(Customer::getId));
        return customers.get(customers.size() - 1).getId() + 1;
    }

    public String getNameById(int id){
        List<Customer> customers = customerDAO.getAll().get();
        String name = "";
        for (Customer customer : customers) {
            if (customer.getId() == id) {
                name= customer.getFirst_name();
            }
        }
        return name;
    }

}

