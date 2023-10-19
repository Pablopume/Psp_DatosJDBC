package ui.screens.customers.editcustomers;

import jakarta.inject.Inject;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Data;
import model.Customer;
import services.ServicesCustomer;

import java.util.ArrayList;
import java.util.List;
@Data
public class EditCustomerViewModel {
    private final ServicesCustomer services;
    private final ObjectProperty<EditCustomerState> state;
    @Inject
    public EditCustomerViewModel(ServicesCustomer services) {
        this.services = services;
        this.state =  new SimpleObjectProperty<>(new EditCustomerState(new ArrayList<>(), null));;
    }

    public ReadOnlyObjectProperty<EditCustomerState> getState() {
        return state;
    }

    public void  voidState(){state.set(new EditCustomerState(null,null));}

    public void loadState() {
        List<Customer> listCust = services.getAll().get();
        if (listCust.isEmpty()) {
            state.set(new EditCustomerState(null,"There are no customers"));


        }else {
            state.set(new EditCustomerState(listCust,null));
        }
    }
}
