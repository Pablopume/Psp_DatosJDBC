package dao;

import io.vavr.control.Either;
import model.Order;
import model.errors.OrderError;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface OrdersDAO {
    Either<OrderError, List<Order>> getAll();

    void save(Order order);

    void updateOrder(int id, Order order);

    Either<OrderError, Order> save(LocalDateTime date, int customer_id, int table_id);

    Either<OrderError, List<Order>> get(int id);

    Either<OrderError, List<Order>> get(LocalDate localDate);

    Either<OrderError, Order> addOrder(int id, LocalDateTime date, int customer_id, int table_id);

    void delete(int idToDelete);

}
