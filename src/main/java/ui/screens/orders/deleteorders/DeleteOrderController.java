package ui.screens.orders.deleteorders;

import common.Constants;
import jakarta.inject.Inject;
import jakarta.xml.bind.JAXBException;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Order;
import model.OrderItem;

import ui.screens.common.BaseScreenController;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

public class DeleteOrderController extends BaseScreenController {
    @FXML
    public TableView<Order> ordersTable;
    @FXML
    public TableColumn<Order, Integer> idOrder;
    @FXML
    public TableColumn<Order, LocalDateTime> orderDate;

    @FXML
    public TableColumn<Order, Integer> customerId;
    @FXML
    public TableColumn<Order, String > tableId;
    public Button buttonDelete;
    public TableView<OrderItem> ordersXMLTable;
    public TableColumn<OrderItem, String> menuItem;
    public TableColumn<OrderItem, Integer> quantity;
    @Inject
    DeleteOrderViewModel deleteOrderViewModel;

    public void initialize() {
        idOrder.setCellValueFactory(new PropertyValueFactory<>(Constants.ID));
        orderDate.setCellValueFactory(new PropertyValueFactory<>(Constants.DATE));
        customerId.setCellValueFactory(new PropertyValueFactory<>(Constants.CUSTOMER_ID));
        tableId.setCellValueFactory(new PropertyValueFactory<>(Constants.TABLE_ID));
        menuItem.setCellValueFactory(new PropertyValueFactory<>("menuItem"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        deleteOrderViewModel.getState().addListener((observableValue, oldValue, newValue) -> {

                    if (newValue.getError() != null) {
                        getPrincipalController().sacarAlertError(newValue.getError());
                    }
                    if (newValue.getListOrders() != null) {
                        ordersTable.getItems().clear();
                        ordersTable.getItems().setAll(newValue.getListOrders());
                    }

                }

        );
        //    ordersTable.setOnMouseClicked(event -> {
            //    Order selectedOrder = ordersTable.getSelectionModel().getSelectedItem();
            // try {

                //          ordersXMLTable.getItems().setAll(deleteOrderViewModel.getServicesDaoXML().getAll(selectedOrder.getId()).get());
            // } catch (JAXBException | IOException e) {
        //             throw new RuntimeException(e);
            // }
            // });
        deleteOrderViewModel.voidState();

    }
    @Override
    public void principalLoaded() {
        deleteOrderViewModel.loadState();
    }

    public void deleteOrder(ActionEvent actionEvent) {
        SelectionModel<Order> selectionModel = ordersTable.getSelectionModel();
        Order selectedOrder = selectionModel.getSelectedItem();
        if (!deleteOrderViewModel.getServices().orderContained(selectedOrder.getId())) {

            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirm delete");
            confirmationAlert.setHeaderText("Delete order");
            confirmationAlert.setContentText("Are you sure you want to delete this order?");
            Optional<ButtonType> result = confirmationAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                deleteOrderViewModel.getServices().delete(selectedOrder.getId());
                ObservableList<Order> orders = ordersTable.getItems();
                orders.remove(selectedOrder);
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Order deleted");
                successAlert.setHeaderText(null);
                successAlert.setContentText("The Order has been deleted");
                successAlert.showAndWait();
            }
        } else if (deleteOrderViewModel.getServices().orderContained(selectedOrder.getId())) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("You can't delete");
            alert.setHeaderText(null);
            alert.setContentText("There are order items created in that order, do you want to delete them?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                deleteOrderViewModel.getServices().deleteOrders(deleteOrderViewModel.getServices().getAll().get(), selectedOrder.getId());
                //            deleteOrderViewModel.getServicesDaoXML().delete(selectedOrder.getId());
                ObservableList<OrderItem> orderItemXMLS=ordersXMLTable.getItems();
                orderItemXMLS.clear();
                ObservableList<Order> orders = ordersTable.getItems();
                orders.remove(selectedOrder);
            }
        } else {
            Alert alert=new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Delete cancelled");
            alert.setHeaderText(null);
            alert.setContentText("You cancelled the delete");
            alert.show();
        }
    }
    }


