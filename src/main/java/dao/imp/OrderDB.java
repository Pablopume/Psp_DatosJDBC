package dao.imp;

import common.Constants;
import dao.OrdersDAO;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.Order;
import model.errors.OrderError;

import java.sql.*;
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

    public Either<OrderError, Integer> delete(Order c) {
        Either<OrderError, Integer> result;
        try (Connection myConnection = db.getConnection();
             PreparedStatement preparedStatement = myConnection.prepareStatement("DELETE FROM orders WHERE order_id = ?");
             PreparedStatement preparedStatementItems = myConnection.prepareStatement("DELETE FROM order_items WHERE order_id = ?")) {
            preparedStatement.setInt(1, c.getId());
            preparedStatementItems.setInt(1, c.getId());
            preparedStatementItems.executeUpdate();
            preparedStatement.executeUpdate();
            result = Either.right(1);
        } catch (SQLException ex) {
            new OrderError("Error connecting to database");
            result = Either.left(new OrderError("Error connecting to database"));
        }

        return result;
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


    public Either<OrderError, Integer> update(Order c) {
        Either<OrderError, Integer> result;
        try (Connection con = db.getConnection();
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
    public Either<OrderError, Order> save(Order c) {
        Either<OrderError, Order> result = null;
        try(Connection con = db.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement("insert into orders (order_date,customer_id,table_id) values (?, ?, ?)", Statement.RETURN_GENERATED_KEYS))  {
            preparedStatement.setTimestamp(1, Timestamp.valueOf(c.getDate()));
            preparedStatement.setInt(2, c.getCustomer_id());
            preparedStatement.setInt(3, c.getTable_id());

            int rs = preparedStatement.executeUpdate();
            if (rs == 0) {
                result = Either.left(new OrderError("Error saving orders"));
            } else {
                result = Either.right(new Order(c.getDate(), c.getCustomer_id(), c.getTable_id()));
            }
        } catch (SQLException ex) {
            result = Either.left(new OrderError("Error connecting database"));
        }
        return result;
    }

}
