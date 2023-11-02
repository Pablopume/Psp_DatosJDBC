package dao.imp;

import dao.OrderItemDAO;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.MenuItem;
import model.Order;
import model.OrderItem;
import model.errors.OrderError;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderItemDaoJDBC implements OrderItemDAO {

    private DBConnection db;

    @Inject
    public OrderItemDaoJDBC(DBConnection db) {
        this.db = db;
    }

    @Override
    public Either<OrderError, List<OrderItem>> getAll() {
        Either<OrderError, List<OrderItem>> result = null;
        try (Connection con = db.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM order_items")) {
            ResultSet rs = preparedStatement.executeQuery();
            List<OrderItem> orderitems = readRS(rs);
            if (orderitems.isEmpty()) {
                result = Either.left(new OrderError("Theare are no order Items in this order"));
            } else {
                result = Either.right(orderitems);
            }
        } catch (SQLException ex) {
            result = Either.left(new OrderError("Error connecting to database"));
        }
        return result;
    }

    @Override
    public Either<OrderError, List<OrderItem>> get(Order o) {
        Either<OrderError, List<OrderItem>> result = null;
        try (Connection con = db.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement("Select from order_items where order_id = ?")) {
            preparedStatement.setInt(1, o.getId());
            ResultSet rs = preparedStatement.executeQuery();
            result = Either.right(readRS(rs));
        } catch (SQLException ex) {
            result = Either.left(new OrderError("Couldn't obtain order items"));
        }
        return result;
    }

    @Override
    public Either<OrderError, Integer> save(List<OrderItem> c) {
        Either<OrderError, Integer> result;
        try(Connection con = db.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO order_items (order_id, menu_item_id, quantity) VALUES (?, ?, ?)")) {
            int rs = 0;
            for (OrderItem orderItem : c) {
                preparedStatement.setInt(1, orderItem.getIdOrder());
                preparedStatement.setInt(2, orderItem.getMenuItem().getId());
                preparedStatement.setInt(3, orderItem.getQuantity());
                rs = rs + preparedStatement.executeUpdate();
            }

        if (rs==0) {
                result = Either.left(new OrderError("Error saving order items"));
            } else {
                result = Either.right(0);
            }
        } catch (SQLException ex) {
            result = Either.left(new OrderError("Error connecting to database"));
        }
        return result;
    }


    public Either<OrderError, Integer> update(List<OrderItem> c) {
        Either<OrderError, Integer> result = null;
        try(Connection con = db.getConnection();
            PreparedStatement preparedStatementDelete = con.prepareStatement("DELETE FROM order_items WHERE order_id = ?");
            PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO order_items (order_id, menu_item_id, quantity) VALUES (?, ?, ?)")) {
            preparedStatementDelete.setInt(1, c.get(0).getIdOrder());
            preparedStatementDelete.executeUpdate();
            for (OrderItem orderItem : c) {
                preparedStatement.setInt(1, orderItem.getIdOrder());
                preparedStatement.setInt(2, orderItem.getMenuItem().getId());
                preparedStatement.setInt(3, orderItem.getQuantity());
                preparedStatement.executeUpdate();
            }
            result = Either.right(0);
        } catch (SQLException ex) {
            result = Either.left(new OrderError("Error connecting to database"));
        }
        return result;
    }

    @Override
    public Either<OrderError, Integer> delete(Order o) {
        Either<OrderError, Integer> result = null;
        try (Connection con = db.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement("DELETE FROM order_items WHERE order_id = ?")    ) {
            preparedStatement.setInt(1, o.getId());
            preparedStatement.executeUpdate();
            result = Either.right(1);
        } catch (SQLException ex) {
            result = Either.left(new OrderError("Error while deleting"));
        }
        return result;
    }

    private List<OrderItem> readRS(ResultSet rs) throws SQLException {
        List<OrderItem> result = new ArrayList<>();
        while (rs.next()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setId(rs.getInt("order_item_id"));
            orderItem.setIdOrder(rs.getInt("order_id"));
            orderItem.setMenuItem(getMenuItem(rs.getInt("menu_item_id")));
            orderItem.setQuantity(rs.getInt("quantity"));
            result.add(orderItem);
        }
        return result;
    }


    private MenuItem getMenuItem(int id){
        MenuItem result = null;
        try(Connection con = db.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM menu_items WHERE menu_item_id = ?")) {
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            result = readRSMenuItem(rs);

        } catch (SQLException ex) {
            result = null;
        }
        return result;
    }

    private MenuItem readRSMenuItem(ResultSet rs) throws SQLException {
        MenuItem result = new MenuItem();
        while (rs.next()) {
            result.setId(rs.getInt("menu_item_id"));
            result.setName(rs.getString("name"));
            result.setDescription(rs.getString("description"));
            result.setPrice(rs.getInt("price"));
        }
        return result;
    }
}
