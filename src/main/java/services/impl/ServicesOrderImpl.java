package services.impl;

import dao.OrdersDAO;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.Order;
import model.errors.OrderError;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import services.ServicesOrder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ServicesOrderImpl implements ServicesOrder {
    private OrdersDAO ordersDAO;

    @Inject
    public ServicesOrderImpl(OrdersDAO ordersDAO) {
        this.ordersDAO = ordersDAO;
    }

    public Either<OrderError, List<Order>> getAll() {
        return ordersDAO.getAll();
    }



    @Override
    public void updateOrder(int id, Order order) {
        ordersDAO.updateOrder(id, order);
    }

    @Override
    public void delete(Order order) {
        ordersDAO.delete(order);
    }


    public Either<OrderError, Order> createOrder(LocalDateTime date, int customer_id, int table_id) {
        System.out.println("hola");
        return ordersDAO.save(date, customer_id, table_id);
    }

    public Either<OrderError, List<Order>> filteredList(int id) {
        return ordersDAO.get(id);
    }

    @Override
    public Either<OrderError, List<Order>> filteredListDate(LocalDate localDate) {
        return ordersDAO.get(localDate);
    }

    @Override
    public void delete(int idToDelete) {
        ordersDAO.delete(idToDelete);
    }

    public void deleteOrders(List<Order> listOrd, int id) {
        listOrd.forEach(order -> {
            if (id == order.getCustomer_id()) {
                ordersDAO.delete(order.getId());
            }
        });
    }

    public boolean orderContained(int orderId) {
        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse("data/orders.xml");
            Element root = doc.getDocumentElement();
            NodeList orderList = root.getElementsByTagName("order");


            for (int i = 0; i < orderList.getLength(); i++) {
                Element orderElement = (Element) orderList.item(i);
                Element idElement = (Element) orderElement.getElementsByTagName("id").item(0);
                int id = Integer.parseInt(idElement.getTextContent());

                if (id == orderId) {
                    return true;
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return false;
    }
    @Override
    public List<Order> getOrdersByCustomerId(int id) {
        Either<OrderError, List<Order>> result = ordersDAO.getAll();

        if (result.isLeft()) {
            return Collections.emptyList();
        } else {
            List<Order> allOrders = result.get();
            return allOrders.stream()
                    .filter(order -> order.getCustomer_id() == id)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public Either<OrderError, Integer> update(Order c) {
        return ordersDAO.update(c);
    }

    @Override
    public Either<OrderError, Order> addOrder(int id, LocalDateTime date, int customer_id, int table_id) {
        return ordersDAO.addOrder(id, date, customer_id, table_id);
    }

    @Override
    public void deleteByCustomerId(int id) {
        List<Order> orders = getOrdersByCustomerId(id);
        orders.forEach(this::delete);
    }
}
