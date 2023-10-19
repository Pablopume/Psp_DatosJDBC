package ui.screens.login;

import dao.LoginDAO;
import jakarta.inject.Inject;
import javafx.event.ActionEvent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.Credentials;
import services.ServicesLogin;
import ui.screens.common.BaseScreenController;

public class LoginController extends BaseScreenController {
    public PasswordField txtPassword;
    public TextField txtUserName;
    private final ServicesLogin services;

    @Inject
    public LoginController(ServicesLogin services) {
        this.services = services;
    }
    public void doLogin(ActionEvent actionEvent) {
        Credentials credentials = new Credentials(txtUserName.getText(), txtPassword.getText());
        if (services.doLogin(credentials)) {
            getPrincipalController().onLoginDone(credentials);
        }
    }
}
