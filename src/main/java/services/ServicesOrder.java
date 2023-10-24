package services;

import io.vavr.control.Either;
import model.Order;
import model.errors.OrderError;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ServicesOrder {
    Either<OrderError, List<Order>> getAll();

    void writeToFile(Order order);

    void updateOrder(int id, Order order);
    void delete(Order order);
    void deleteByCustomerId(int id);
    List<Order> getOrdersByCustomerId(int id);
    Either<OrderError, Integer> update(Order c);

    Either<OrderError, Order> createOrder(LocalDateTime date, int customer_id, int table_id);

    Either<OrderError, List<Order>> filteredList(int id);

    Either<OrderError, List<Order>> filteredListDate(LocalDate localDate);

    void delete(int idToDelete);

    void deleteOrders(List<Order> listOrd, int id);

    boolean orderContained(int orderId);

    Either<OrderError, Order> addOrder(int id, LocalDateTime date, int customer_id, int table_id);
}
