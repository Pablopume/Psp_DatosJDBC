package dao.imp;

import common.Constants;
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
    public void delete(Order c) {
        try (Connection myConnection = db.getConnection();
             PreparedStatement preparedStatement = myConnection.prepareStatement("DELETE FROM orders WHERE order_id = ?");
             PreparedStatement preparedStatementItems = myConnection.prepareStatement("DELETE FROM order_items WHERE order_id = ?")) {
            preparedStatement.setInt(1, c.getId());
            preparedStatementItems.setInt(1, c.getId());
            preparedStatementItems.executeUpdate();
            int rs = preparedStatement.executeUpdate();


        } catch (SQLException ex) {
         new OrderError("Error connecting to database");
        }

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
    public Either<OrderError, Integer> update(Order c) {
        Either<OrderError, Integer> result;
        try(Connection con = db.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement("update orders set order_date=?, customer_id=?, table_id=? where order_id=?  ")) {
            preparedStatement.setTimestamp(1, Timestamp.valueOf(c.getDate()));
            preparedStatement.setInt(2, c.getCustomer_id());
            preparedStatement.setInt(3, c.getTable_id());
            preparedStatement.setInt(4, c.getId());

            int rs = preparedStatement.executeUpdate();
            if (rs == 0) {
                result = Either.left(new OrderError("Error updating order"));
            } else {
                result = Either.right(0);
            }
        } catch (SQLException ex) {
            result = Either.left(new OrderError("ERROR CONNECTING TO DATABASE"));
        }
        return result;
    }
    @Override
    public void updateOrder(int id, Order order) {
        Either<OrderError, Integer> result;
        try(Connection con = db.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement("update orders set order_date=?, customer_id=?, table_id=? where order_id=?"+ id)) {
            preparedStatement.setTimestamp(1, Timestamp.valueOf(order.getDate()));
            preparedStatement.setInt(2, order.getCustomer_id());
            preparedStatement.setInt(3, order.getTable_id());
            System.out.println("eyou");
            int rs = preparedStatement.executeUpdate();

        } catch (SQLException ex) {
            System.out.println("Error connecting to database");
        }

    }

    @Override
    public Either<OrderError, Order> save(LocalDateTime date, int customer_id, int table_id) {
Either<OrderError, Order> result = null;
        try(Connection con = db.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement("insert into orders (order_date, customer_id, table_id) values (?,?,?)")) {
            preparedStatement.setTimestamp(1, Timestamp.valueOf(date));
            preparedStatement.setInt(2, customer_id);
            preparedStatement.setInt(3, table_id);
            System.out.println("ey");
            int rs = preparedStatement.executeUpdate();
    Either.right(new Order(date, customer_id, table_id));
        } catch (SQLException ex) {

        }
        return result;
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
        try (Connection myConnection = db.getConnection();
             PreparedStatement preparedStatement = myConnection.prepareStatement("INSERT INTO orders ( order_date, customer_id, table_id) VALUES ( ?, ?, ?)")) {
            preparedStatement.setInt(1, id);
            preparedStatement.setTimestamp(2, Timestamp.valueOf(date));
            preparedStatement.setInt(3, customer_id);
            preparedStatement.setInt(4, table_id);
            int rs = preparedStatement.executeUpdate();
            return Either.right(new Order(id, date, customer_id, table_id));
        } catch (SQLException ex) {
            return Either.left(new OrderError("Error connecting to database"));
        }
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
