package org.example.project4cs213.controller;

import model.Coffee;
import model.MenuItem;
import model.Order;
import model.OrdersManager;
import enums.CoffeeAddIn;
import enums.CupSize;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.EnumSet;

/**
 * Controller for the coffee ordering view.
 */
public class CoffeeController implements OrdersAware {

    @FXML
    private ComboBox<CupSize> sizeCombo;

    @FXML
    private ListView<CoffeeAddIn> addInsList;

    @FXML
    private Spinner<Integer> quantitySpinner;

    @FXML
    private Label priceLabel;

    @FXML
    private ImageView coffeeImage;

    private OrdersManager ordersManager;

    /**
     * Set order manager for controller
     * @param manager orders manager
     */
    @Override
    public void setOrdersManager(OrdersManager manager) {
        this.ordersManager = manager;
        if (ordersManager.getCurrentOrder() == null) {
            ordersManager.startNewOrder();
        }
    }

    /**
     * initialize controller, set up UI components being used
     */
    @FXML
    private void initialize() {
        sizeCombo.setItems(FXCollections.observableArrayList(CupSize.values()));
        sizeCombo.getSelectionModel().selectedItemProperty()
                .addListener((obs, o, n) -> updatePrice());

        addInsList.setItems(FXCollections.observableArrayList(CoffeeAddIn.values()));
        addInsList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        addInsList.getSelectionModel().selectedItemProperty()
                .addListener((obs, o, n) -> updatePrice());

        quantitySpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1));
        quantitySpinner.valueProperty().addListener((obs, o, n) -> updatePrice());

        coffeeImage.setImage(new Image(getClass().getResource("/images/coffee-large.jpg").toExternalForm()));

        sizeCombo.getSelectionModel().select(CupSize.SHORT);
    }

    /**
     * Update the price label based on current selections
     */
    private void updatePrice() {
        CupSize size = sizeCombo.getValue();
        Integer qty = quantitySpinner.getValue();
        if (size == null || qty == null) {
            priceLabel.setText("$0.00");
            return;
        }
        var selectedAddIns = addInsList.getSelectionModel().getSelectedItems();
        EnumSet<CoffeeAddIn> AddIns = EnumSet.noneOf(CoffeeAddIn.class);
        AddIns.addAll(selectedAddIns);
        MenuItem item = new Coffee(size, AddIns, qty);
        priceLabel.setText(String.format("$%.2f", item.price()));
    }

    /**
     * Add selected coffee to current order
     */
    @FXML
    private void addCoffeeToOrder() {
        CupSize size = sizeCombo.getValue();
        Integer qty = quantitySpinner.getValue();
        if (size == null || qty == null || qty <= 0) {
            show("Please choose a size and quantity.");
            return;
        }
        var selectedAddIns = addInsList.getSelectionModel().getSelectedItems();
        EnumSet<CoffeeAddIn> AddIns = EnumSet.noneOf(CoffeeAddIn.class);
        AddIns.addAll(selectedAddIns);
        Order order = ensureCurrentOrder();
        order.addItem(new Coffee(size, AddIns, qty));
        show("Coffee added to order #" + order.getOrderNumber());
    }

    /**
     * Makes sure there is a current order in OrderManagers
     * @return the current order
     */
    private Order ensureCurrentOrder() {
        Order cur = ordersManager.getCurrentOrder();
        if (cur == null) cur = ordersManager.startNewOrder();
        return cur;
    }

    /**
     * Display information of the current message
     * @param msg the message being shown
     */
    private void show(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
