package dao.imp;

import dao.OrdersDAO;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.Order;
import model.errors.OrderError;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderDB implements OrdersDAO {
    private DBConnection db;

    @Inject
    public OrderDB(DBConnection db) {
        this.db = db;
    }

    @Override
    public Either<OrderError, List<Order>> getAll() {
        try (Connection myConnection = db.getConnection();
             Statement statement = myConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_READ_ONLY)) {
            ResultSet rs = statement.executeQuery("select * from orders");
            return Either.right(readRS(rs).get());
        } catch (SQLException e) {
            return Either.left(new OrderError("Error while reading orders"));
        }
    }

    private Either<OrderError, List<Order>> readRS(ResultSet rs) {
        try {
            List<Order> orders = new ArrayList<>();
            while (rs.next()) {
                Order order = new Order(
                        rs.getInt("order_id"),
                        rs.getTimestamp("order_date").toLocalDateTime(),
                        rs.getInt("customer_id"),
                        rs.getInt("table_id")
                );

                orders.add(order);

            }
            return Either.right(orders);
        } catch (SQLException e) {
            return Either.left(new OrderError("Error while reading orders"));
        }
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
            ResultSet rs = statement.executeQuery("delete * orders where order_id=" + idToDelete);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteOrderItem(int id) {
        try (Connection myConnection = db.getConnection();
             Statement statement = myConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_READ_ONLY)) {
            ResultSet rs = statement.executeQuery("delete * order_items where order_item_id=" + id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
