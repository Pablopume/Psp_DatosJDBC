package dao.imp;
import dao.MenuItemDAO;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.MenuItem;
import model.errors.OrderError;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MenuItemDaoJDBC implements MenuItemDAO {

    private DBConnection db;

    @Inject
    public MenuItemDaoJDBC(DBConnection db) {
        this.db = db;
    }


    public Either<OrderError, List<MenuItem>> getAll() {
        Either<OrderError, List<MenuItem>> result = null;
        try (Connection con = db.getConnection();
             Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_READ_ONLY)) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM menu_items");
            List<MenuItem> menuItems = readRS(rs);
            if (menuItems.isEmpty()) {
                result = Either.left(new OrderError("There are no menu items"));
            } else {
                result = Either.right(menuItems);
            }
        } catch (SQLException ex) {
            result = Either.left(new OrderError("Error connecting to database"));
        }
        return result;
    }

    private List<MenuItem> readRS(ResultSet rs) {
        List<MenuItem> menuItems = new ArrayList<>();
        try {
            while (rs.next()) {
                int id = rs.getInt("menu_item_id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                int price = rs.getInt("price");
                menuItems.add(new MenuItem(id, name, description, price));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return menuItems;
    }
}
