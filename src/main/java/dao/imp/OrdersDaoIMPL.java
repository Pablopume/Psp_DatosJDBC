package dao.imp;

import common.Constants;
import dao.OrdersDAO;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.Customer;
import model.Order;
import model.errors.CustomerError;
import model.errors.OrderError;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class OrdersDaoIMPL implements OrdersDAO {
    private DBConnection db;
    @Inject
    public OrdersDaoIMPL(DBConnection db) {
        this.db = db;
    }
    @Override
    public Either<OrderError, List<Order>> getAll() {
        return null;
    }

    @Override
    public void save(Order order) {

    }

    @Override
    public void updateOrder(int id, Order order) {

    }

    @Override
    public Either<OrderError, Order> save(LocalDateTime date, int customer_id, int table_id) {
        return null;
    }

    @Override
    public Either<OrderError, List<Order>> get(int id) {
        return null;
    }

    @Override
    public Either<OrderError, List<Order>> get(LocalDate localDate) {
        return null;
    }

    @Override
    public Either<OrderError, Order> addOrder(int id, LocalDateTime date, int customer_id, int table_id) {
        return null;
    }

    @Override
    public void delete(int idToDelete) {
        deleteOrderItem(idToDelete);
        try (Connection myConnection = db.getConnection();
             Statement statement = myConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_READ_ONLY)) {
            ResultSet rs = statement.executeQuery("delete * orders where order_id="+idToDelete);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteOrderItem(int id){
        try (Connection myConnection = db.getConnection();
             Statement statement = myConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_READ_ONLY)) {
            ResultSet rs = statement.executeQuery("delete * order_items where order_item_id="+id);
    } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
