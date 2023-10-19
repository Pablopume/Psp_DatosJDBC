package dao;

import model.Credentials;

public interface LoginDAO {
    boolean doLogin(Credentials credentials);
}
