package ui.screens.customers.addcustomer;

import common.Constants;
import jakarta.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.scene.control.cell.PropertyValueFactory;
import model.Customer;
import ui.screens.common.BaseScreenController;

import java.time.LocalDate;

public class AddCustomersController extends BaseScreenController {
    @FXML
    public TextField nameField;
    @FXML
    public TextField phoneField;
    @FXML
    public TextField surnameField;
    public TextField mailField;
    public DatePicker dobField;
    @FXML
    private TableColumn<Customer, Integer> idCustomerColumn;
    @FXML
    private TableColumn<Customer, String> nameCustomerColumn;
    @FXML
    private TableColumn<Customer, String> surnameCustomerColumn;
    @FXML
    private TableColumn<Customer, String> emailColumn;
    @FXML
    private TableColumn<Customer, String> phoneColumn;
    @FXML
    private TableColumn<Customer, LocalDate> dateOfBirthdayColumn;
    @FXML
    private TableView<Customer> customersTable;


    @Inject
    private AddCustomerViewModel addCustomerViewModel;

    public void initialize() {
        idCustomerColumn.setCellValueFactory(new PropertyValueFactory<>(Constants.ID));
        nameCustomerColumn.setCellValueFactory(new PropertyValueFactory<>(Constants.FIRST_NAME));
        surnameCustomerColumn.setCellValueFactory(new PropertyValueFactory<>(Constants.LAST_NAME));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>(Constants.EMAIL));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>(Constants.PHONE));
        dateOfBirthdayColumn.setCellValueFactory(new PropertyValueFactory<>(Constants.DOB));
        addCustomerViewModel.getState().addListener((observableValue, oldValue, newValue) -> {
                    if (newValue.getError() != null) {
                        getPrincipalController().sacarAlertError(newValue.getError());
                    }
                    if (newValue.getListCustomers() != null) {
                        customersTable.getItems().clear();
                        customersTable.getItems().setAll(newValue.getListCustomers());
                    }
                }
        );
        addCustomerViewModel.voidState();
    }

    @Override
    public void principalLoaded() {
        addCustomerViewModel.loadState();
    }


    public void addClient(ActionEvent actionEvent) {

    }
}
