package dao.imp;

import dao.OrdersDAO;
import io.vavr.control.Either;
import model.Order;
import model.errors.OrderError;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class OrdersDaoIMPL implements OrdersDAO {
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

    }
}
