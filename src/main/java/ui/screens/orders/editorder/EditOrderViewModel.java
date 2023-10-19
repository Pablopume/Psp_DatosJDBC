package ui.screens.orders.editorder;

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
public class EditOrderViewModel {

    private final ServicesOrder services;
    private final ObjectProperty<EditOrderState> state;

    @Inject
    public EditOrderViewModel( ServicesOrder services) {

        this.state = new SimpleObjectProperty<>(new EditOrderState(new ArrayList<>(), null));
        this.services = services;

    }

    public void voidState() {
        state.set(new EditOrderState(null, null));
    }

    public ReadOnlyObjectProperty<EditOrderState> getState(){return state;}

    public void loadState() {
        List<Order> listOrd = services.getAll().get();
        if (listOrd.isEmpty()) {
            state.set(new EditOrderState(null, Constants.THERE_ARE_NO_ORDERS));


        }else {
            state.set(new EditOrderState(listOrd,null));
        }
    }
}
