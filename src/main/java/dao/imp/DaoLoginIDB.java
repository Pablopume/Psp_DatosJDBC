package dao.imp;

import dao.LoginDAO;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.Credentials;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DaoLoginIDB implements LoginDAO {
    private DBConnectionPool db;

    @Inject
    public DaoLoginIDB(DBConnectionPool db) {
        this.db = db;
    }

    /*  public boolean doLogin(Credentials credentials) {
          return credentials.getUser().equals(Constants.ROOT) && credentials.getPassword().equals(Constants.ROOT1);

      }
  */
    public Either<String, List<Credentials>> getAll() {
        Either<String, List<Credentials>> response;
        try (
                Connection myConnection = db.getConnection(); Statement statement = myConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY)) {
            ResultSet rs = statement.executeQuery("SELECT * FROM credentials");
            List<Credentials> credentialsList = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("id");
                String user = rs.getString("user_name");
                String password = rs.getString("password");
                Credentials c = new Credentials(id, user, password);
                credentialsList.add(c);
            }
            response = Either.right(credentialsList);
            db.closeConnection(myConnection);
        } catch (SQLException e) {
            response = Either.left(e.getMessage());
        }
        return response;
    }


}
