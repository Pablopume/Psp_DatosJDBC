package ui.screens.customers.deletecustomer;

import common.Constants;
import jakarta.inject.Inject;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Data;
import model.Customer;
import services.ServicesCustomer;
import services.ServicesOrder;

import java.util.ArrayList;
import java.util.List;
@Data
public class DeleteCustomerViewModel {
    private final ServicesOrder servicesOrder;
    private final ServicesCustomer services;

    private final ObjectProperty<DeleteCustomerState> state;

    @Inject
    public DeleteCustomerViewModel(ServicesOrder servicesOrder, ServicesCustomer services) {
        this.servicesOrder = servicesOrder;
        this.services = services;

        this.state = new SimpleObjectProperty<>(new DeleteCustomerState(new ArrayList<>(), null));

    }

    public ReadOnlyObjectProperty<DeleteCustomerState> getState() {
        return state;
    }

    public void voidState() {
        state.set(new DeleteCustomerState(null, null));
    }

    public void loadState() {
        List<Customer> listCust = services.getAll().get();
        if (listCust.isEmpty()) {
            state.set(new DeleteCustomerState(null, Constants.THERE_ARE_NO_CUSTOMERS2));
        } else {
            state.set(new DeleteCustomerState(listCust, null));
        }
    }
}
