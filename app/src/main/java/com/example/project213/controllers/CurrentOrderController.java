package org.example.project4cs213.controller;

import model.MenuItem;
import model.Order;
import model.OrdersManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * Shows the current order, allows removing items and placing order.
 */
public class CurrentOrderController implements OrdersAware {

    @FXML
    private ListView<MenuItem> itemsList;

    @FXML
    private Label subtotalLabel, taxLabel, totalLabel, orderNumberLabel;

    @FXML
    private Button removeItemBtn, clearBtn, placeOrderBtn;

    private OrdersManager ordersManager;

    /**
     * Set order manager for controller
     * @param manager orders manager
     */
    @Override
    public void setOrdersManager(OrdersManager manager) {
        this.ordersManager = manager;
        refresh();
    }

    /**
     * Initialize the controller
     */
    @FXML
    private void initialize() {
        itemsList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        itemsList.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(MenuItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.displayLine());
                }
            }
        });
    }

    /**
     * Remove desired item in the current order
     */
    @FXML
    private void removeSelectedItem() {
        Order order = ordersManager.getCurrentOrder();
        if (order == null) return;

        MenuItem selected = itemsList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            order.removeItem(selected);
            refresh();
        }
    }

    /**
     * Clear all items in the current selected order
     */
    @FXML
    private void clearCurrentOrder() {
        Order order = ordersManager.getCurrentOrder();
        if (order != null) {
            order.clear();
            refresh();
        }
    }

    /**
     * Place current selected order into OrdersManager
     */
    @FXML
    private void placeOrder() {
        Order currentOrder = ordersManager.getCurrentOrder();
        if (currentOrder == null || currentOrder.getItems().isEmpty()) {
            show("No items in current order.");
            return;
        }

        ordersManager.placeCurrentOrder(); // moving current order to placedOrders
        show("Order placed!");
        refresh();                        // update UI
    }

    /**
     * Reset the view to show current order items and total
     */
    private void refresh() {
        Order order = ordersManager.getCurrentOrder();
        if (order == null) {
            itemsList.setItems(FXCollections.emptyObservableList());
            subtotalLabel.setText("$0.00");
            taxLabel.setText("$0.00");
            totalLabel.setText("$0.00");
            orderNumberLabel.setText("No current order");
            return;
        }
        itemsList.setItems(FXCollections.observableArrayList(order.getItems()));
        subtotalLabel.setText(String.format("$%.2f", order.getSubTotal()));
        taxLabel.setText(String.format("$%.2f", order.getTax()));
        totalLabel.setText(String.format("$%.2f", order.getTotal()));
        orderNumberLabel.setText("Order #" + order.getOrderNumber());
    }

    /**
     * Display information of the current message
     * @param msg message being shown
     */
    private void show(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
