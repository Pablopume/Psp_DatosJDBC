package ui.screens.orders.deleteorders;

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
public class DeleteOrderViewModel {
    private final ServicesOrder services;
    private final ObjectProperty<DeleteOrderState> state;



    @Inject
    public DeleteOrderViewModel( ServicesOrder services) {

        this.state = new SimpleObjectProperty<>(new DeleteOrderState(new ArrayList<>(), null));
        this.services = services;

    }
    public void voidState() {
        state.set(new DeleteOrderState(null, null));
    }
    public ReadOnlyObjectProperty<DeleteOrderState> getState(){return state;}

    public void loadState() {
        List<Order> listOrd = services.getAll().get();
        if (listOrd.isEmpty()) {
            state.set(new DeleteOrderState(null, Constants.THERE_ARE_NO_ORDERS));


        }else {
            state.set(new DeleteOrderState(listOrd,null));
        }
    }
}
