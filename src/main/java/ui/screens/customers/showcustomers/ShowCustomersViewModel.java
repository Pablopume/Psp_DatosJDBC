package ui.screens.customers.showcustomers;

import common.Constants;
import jakarta.inject.Inject;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import model.Customer;
import services.CustomerServices;

import java.util.ArrayList;
import java.util.List;

public class ShowCustomersViewModel {

    private final CustomerServices services;
    private final ObjectProperty<ShowCustomersState> state;

    @Inject
    public ShowCustomersViewModel(CustomerServices services) {
        this.state = new SimpleObjectProperty<>(new ShowCustomersState(new ArrayList<>(), null));
        this.services = services;

    }

    public void voidState() {
        state.set(new ShowCustomersState(null, null));
    }

    public ReadOnlyObjectProperty<ShowCustomersState> getState() {
        return state;
    }

    public void loadState() {
        List<Customer> listCust = services.getAll().get();
        if (listCust.isEmpty()) {
            state.set(new ShowCustomersState(null, Constants.THERE_ARE_NO_CUSTOMERS));


        } else {
            state.set(new ShowCustomersState(listCust, null));
        }
    }


}
