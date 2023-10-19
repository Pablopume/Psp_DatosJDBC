package services.impl;

import dao.LoginDAO;
import jakarta.inject.Inject;
import model.Credentials;
import services.ServicesLogin;

public class ServicesLoginImpl implements ServicesLogin {
    private final LoginDAO dao;

    @Inject
    public ServicesLoginImpl(LoginDAO dao) {
        this.dao = dao;
    }

    public boolean doLogin(Credentials credentials) {
        return dao.doLogin(credentials);

    }

}
