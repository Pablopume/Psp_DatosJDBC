package ui.screens.orders.addorder;

import common.Constants;
import jakarta.inject.Inject;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Data;
import model.Order;
import services.ServicesOrder;

import java.util.ArrayList;
import java.util.List;
@Data
public class AddOrderViewModel {


    private final ServicesOrder services;
    private final ObjectProperty<AddOrderState> state;

    @Inject
    public AddOrderViewModel(ServicesOrder services) {
        this.state = new SimpleObjectProperty<>(new AddOrderState(new ArrayList<>(), null));
        this.services = services;

    }

    public void voidState() {
        state.set(new AddOrderState(null, null));
    }

    public ReadOnlyObjectProperty<AddOrderState> getState() {
        return state;
    }

    public void loadState() {
        List<Order> listOrd = services.getAll().get();
        if (listOrd.isEmpty()) {
            state.set(new AddOrderState(null, Constants.THERE_ARE_NO_ORDERS));


        } else {
            state.set(new AddOrderState(listOrd, null));
        }
    }
}
