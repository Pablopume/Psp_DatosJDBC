package ui.screens.customers.addcustomer;

import common.Constants;
import jakarta.inject.Inject;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Data;
import model.Customer;
import services.CustomerServices;

import java.util.ArrayList;
import java.util.List;
@Data
public class AddCustomerViewModel {
    private final CustomerServices services;
    private final ObjectProperty<AddCustomerState> state;

    @Inject
    public AddCustomerViewModel(CustomerServices services) {
        this.services = services;
        this.state = new SimpleObjectProperty<>(new AddCustomerState(new ArrayList<>(), null));
        ;
    }

    public ReadOnlyObjectProperty<AddCustomerState> getState() {
        return state;
    }

    public void voidState() {
        state.set(new AddCustomerState(null, null));
    }

    public void loadState() {
        List<Customer> listCust = services.getAll().get();
        if (listCust.isEmpty()) {
            state.set(new AddCustomerState(null, Constants.THERE_ARE_NO_CUSTOMERS));


        } else {
            state.set(new AddCustomerState(listCust, null));
        }
    }
}
