package org.example.project4cs213.controller;

import model.MenuItem;
import model.Order;
import model.OrdersManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Shows placed orders, allows cancellation and export.
 */
public class AllOrdersController implements OrdersAware {

    @FXML
    private ListView<Order> ordersList;

    @FXML
    private ListView<MenuItem> itemsList;

    @FXML
    private Label totalLabel;

    private OrdersManager ordersManager;

    /**
     * Set managed orders for controller
     *
     * @param manager orders manager
     */
    @Override
    public void setOrdersManager(OrdersManager manager) {
        this.ordersManager = manager;
        refreshOrders();
    }

    /**
     * Initialize the controller
     */
    @FXML
    private void initialize() {
        ordersList.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Order order, boolean empty) {
                super.updateItem(order, empty);
                if (empty || order == null) {
                    setText(null);
                } else {
                    setText("Order #" + order.getOrderNumber()
                            + "  -  Total: " + String.format("$%.2f", order.getTotal()));
                }
            }
        });


        itemsList.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(MenuItem item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.displayLine());
            }
        });


        ordersList.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldOrder, newOrder) -> showOrderDetails(newOrder));
    }

    /**
     * Refresh list of orders displayed and their details
     */
    private void refreshOrders() {
        ordersList.setItems(FXCollections.observableArrayList(
                ordersManager.getPlacedOrders()));
        itemsList.setItems(FXCollections.emptyObservableList());
        totalLabel.setText("$0.00");
    }

    /**
     * Displays details of the given order
     * @param order the order whose details to display
     */
    private void showOrderDetails(Order order) {
        if (order == null) {
            itemsList.setItems(FXCollections.emptyObservableList());
            totalLabel.setText("$0.00");
            return;
        }
        itemsList.setItems(FXCollections.observableArrayList(order.getItems()));
        totalLabel.setText(String.format("$%.2f", order.getTotal()));
    }

    /**
     * Cancel current selected order
     */
    @FXML
    private void cancelSelectedOrder() {
        Order order = ordersList.getSelectionModel().getSelectedItem();
        if (order == null) return;
        ordersManager.cancelPlacedOrder(order);
        refreshOrders();
    }

    /**
     * Opens file chooser to export all placed orders to a text file
     */
    @FXML
    private void exportOrders() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Export Orders");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );
        Stage stage = (Stage) ordersList.getScene().getWindow();
        var file = chooser.showSaveDialog(stage);
        if (file == null) return;

        try {
            ordersManager.exportOrders(file);
            show("Orders exported to " + file.getName());
        } catch (Exception e) {
            show("Failed to export: " + e.getMessage());
        }
    }

    /**
     * Display the information of the given message
     * @param msg the message to display
     */
    private void show(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
