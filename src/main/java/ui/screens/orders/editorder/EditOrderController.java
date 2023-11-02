package ui.screens.orders.editorder;

import common.Constants;
import jakarta.inject.Inject;
import jakarta.xml.bind.JAXBException;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.Data;
import model.Customer;
import model.MenuItem;
import model.Order;
import model.OrderItem;
import ui.screens.common.BaseScreenController;

import java.io.IOException;
import java.time.LocalDateTime;

public class EditOrderController extends BaseScreenController {
    @FXML
    public TableView<Order> orderTable;
    @FXML
    public TableColumn<Order, Integer> idOrder;
    @FXML
    public TableColumn<Order, LocalDateTime> orderDate;

    @FXML
    public TableColumn<Order, Integer> customerId;
    @FXML
    public TableColumn<Order, String> tableId;

    public TextField custIdField;
    public TextField tableFIeld;
    public TableView<OrderItem> ordersXMLTable;
    public TableColumn<OrderItem, String> menuItem;
    public TableColumn<OrderItem, Integer> quantity;
    public TextField quantityItems;
    public ComboBox<String> menuItems;

    @Inject
    EditOrderViewModel editOrderViewModel;

    public void initialize() {
        idOrder.setCellValueFactory(new PropertyValueFactory<>(Constants.ID));
        orderDate.setCellValueFactory(new PropertyValueFactory<>(Constants.DATE));
        customerId.setCellValueFactory(new PropertyValueFactory<>(Constants.CUSTOMER_ID));
        tableId.setCellValueFactory(new PropertyValueFactory<>(Constants.TABLE_ID));
        menuItem.setCellValueFactory(new PropertyValueFactory<>("menuItem"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        orderTable.setOnMouseClicked(event -> {
            Order selectedOrder = orderTable.getSelectionModel().getSelectedItem();

            ordersXMLTable.getItems().setAll(editOrderViewModel.getOrderItemService().getOrdersById(selectedOrder.getId()));



        });
        orderTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {

                Order selectedOrder = orderTable.getSelectionModel().getSelectedItem();


                custIdField.setText(Integer.toString(selectedOrder.getCustomer_id()));
                tableFIeld.setText(Integer.toString(selectedOrder.getTable_id()));
            }
        });

        editOrderViewModel.getState().addListener((observableValue, oldValue, newValue) -> {

                    if (newValue.getError() != null) {
                        getPrincipalController().sacarAlertError(newValue.getError());
                    }
                    if (newValue.getListOrders() != null) {
                        orderTable.getItems().clear();
                        orderTable.getItems().setAll(newValue.getListOrders());
                    }

                }

        );
        //  orderTable.setOnMouseClicked(event -> {
        //       Order selectedOrder = orderTable.getSelectionModel().getSelectedItem();
        //      try {
        //          ordersXMLTable.getItems().setAll(editOrderViewModel.getServicesDaoXML().getAll(selectedOrder.getId()).get());
        //      } catch (JAXBException | IOException e) {
        //         throw new RuntimeException(e);
        //     }
        //  });
        editOrderViewModel.voidState();

    }

    @Override
    public void principalLoaded() {
        editOrderViewModel.loadState();
    }


    public void editOrder(ActionEvent actionEvent) {
        ObservableList<Order> orders = orderTable.getItems();
        SelectionModel<Order> selectionModel = orderTable.getSelectionModel();
        Order selectedOrder = selectionModel.getSelectedItem();
        editOrderViewModel.getServices().update(new Order(selectedOrder.getId(), LocalDateTime.now(), Integer.parseInt(custIdField.getText()), Integer.parseInt(tableFIeld.getText())));
        editOrderViewModel.getOrderItemService().update(ordersXMLTable.getItems());
        orders.clear();
        orders.addAll(editOrderViewModel.getServices().getAll().get());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(Constants.ORDER_EDITED);
        alert.setHeaderText(null);
        alert.setContentText(Constants.ORDER_EDITED_CORRECTLY);
        alert.showAndWait();
    }

    public void addOrder(ActionEvent actionEvent) {
        OrderItem selectedOrderItem= ordersXMLTable.getSelectionModel().getSelectedItem();
        ObservableList<OrderItem> orderItem= ordersXMLTable.getItems();
        orderItem.add(new OrderItem(selectedOrderItem.getId(),selectedOrderItem.getIdOrder(),editOrderViewModel.getMenuItemService().getByName(menuItems.getValue()),Integer.parseInt(quantityItems.getText())));
        ordersXMLTable.setItems(orderItem);
    }

    public void deleteOrder(ActionEvent actionEvent) {
        ObservableList<OrderItem> orderItemXMLS= ordersXMLTable.getItems();
        OrderItem selectedOrder = ordersXMLTable.getSelectionModel().getSelectedItem();
        orderItemXMLS.remove(selectedOrder);
        ordersXMLTable.setItems(orderItemXMLS);
    }
}
