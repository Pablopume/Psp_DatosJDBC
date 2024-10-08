package services.impl;

import dao.OrderItemDAO;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.Order;
import model.OrderItem;
import model.errors.OrderError;
import services.OrderItemService;

import java.util.Collections;
import java.util.List;

public class OrderItemServiceImpl implements OrderItemService {
    private final OrderItemDAO dao;

    @Inject
    public OrderItemServiceImpl(OrderItemDAO dao) {
        this.dao = dao;
    }

    @Override
    public Either<OrderError, List<OrderItem>> getAll() {
        return dao.getAll();
    }

    @Override
    public Either<OrderError, List<OrderItem>> get(Order o) {
        return dao.get(o);
    }

    @Override
    public Either<OrderError, Integer> save(List<OrderItem> c) {
        return dao.save(c);
    }

    @Override
    public Either<OrderError, Integer> update(List<OrderItem> c) {
        return dao.update(c);
    }

    @Override
    public Either<OrderError, Integer> delete(Order o) {
        return dao.delete(o);
    }

    @Override
    public Integer getTotalPrice(List<OrderItem> orderItems) {
        return null;
    }

    public List<OrderItem> getOrdersById(int id) {
        Either<OrderError, List<OrderItem>> result = dao.getAll();

        if (result.isLeft()) {
            return Collections.emptyList();
        } else {
            List<OrderItem> allOrders = result.get();
            return allOrders.stream()
                    .filter(order -> order.getIdOrder() == id).toList();
        }
    }


    public int getAutoId(){
        Either<OrderError, List<OrderItem>> result = dao.getAll();
        if (result.isLeft()) {
            return 0;
        } else {
            List<OrderItem> allOrders = result.get();
            return allOrders.size();
        }
    }
}
