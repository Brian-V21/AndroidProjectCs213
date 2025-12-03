package org.example.project4cs213.controller;

import model.Donut;
import model.MenuItem;
import model.Order;
import model.OrdersManager;
import enums.DonutFlavor;
import enums.DonutType;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Controller for the donut ordering view.
 */
public class DonutController implements OrdersAware {

    @FXML
    private ComboBox<DonutType> typeCombo;

    @FXML
    private ComboBox<DonutFlavor> flavorCombo;

    @FXML
    private Spinner<Integer> quantitySpinner;

    @FXML
    private Label runningPriceLabel;

    @FXML
    private ImageView donutImage;

    @FXML
    private Button addButton;

    private OrdersManager ordersManager;

    /**
     *Set Order manager for controller
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
     * Initialize the controller
     */
    @FXML
    private void initialize() {

        typeCombo.setItems(FXCollections.observableArrayList(DonutType.values()));
        typeCombo.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldVal, newVal) -> updateFlavorList(newVal));

        quantitySpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 12, 1));
        quantitySpinner.valueProperty()
                .addListener((obs, oldVal, newVal) -> updatePrice());

        flavorCombo.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldVal, newVal) -> updatePrice());

        typeCombo.getSelectionModel().select(DonutType.YEAST);
    }

    /**
     * Updates the flavor ComboBox & donut image based on selected type
     * @param type the selected donut type
     */
    private void updateFlavorList(DonutType type) {
        if (type == null) {
            flavorCombo.setItems(FXCollections.emptyObservableList());
            return;
        }

        // filter flavors by type
        var flavors = FXCollections.observableArrayList(DonutFlavor.values());

        flavorCombo.setItems(flavors);
        if (!flavors.isEmpty()) {
            flavorCombo.getSelectionModel().select(0);
        }

        // change image per type (adjust paths to resources)
        String imgPath;
        switch (type) {
            case YEAST -> imgPath = "/images/yeast.jpg";
            case CAKE -> imgPath = "/images/cakedonut.jpg";
            case HOLE -> imgPath = "/images/holedonut.jpg";
            case SEASONAL -> imgPath = "/images/seasonal.jpg";
            default -> imgPath = null;
        }
        var url = getClass().getResource(imgPath);
        if (url != null) {
            donutImage.setImage(new Image(url.toExternalForm()));
        }
        updatePrice();
    }

    /**
     * Refreshes the price of donuts to current order
     */
    private void updatePrice() {
        DonutFlavor flavor = flavorCombo.getValue();
        Integer qty = quantitySpinner.getValue();
        DonutType type = typeCombo.getValue();
        if (flavor == null || qty == null) {
            runningPriceLabel.setText("$0.00");
            return;
        }
        MenuItem item = new Donut(type, flavor, qty);
        runningPriceLabel.setText(String.format("$%.2f", item.price()));
    }

    /**
     * Add donut item to the current order
     */
    @FXML
    private void addDonutToOrder() {
        DonutFlavor flavor = flavorCombo.getValue();
        Integer qty = quantitySpinner.getValue();
        DonutType type = typeCombo.getValue();
        if (flavor == null || qty == null || qty <= 0) {
            showAlert("Please select a flavor and quantity.");
            return;
        }

        Order order = ensureCurrentOrder();
        order.addItem(new Donut(type, flavor, qty));
        showAlert("Donut(s) added to order #" + order.getOrderNumber());
    }

    /**
     * Makes sure there is a current order
     * @return the current order
     */
    private Order ensureCurrentOrder() {
        Order cur = ordersManager.getCurrentOrder();
        if (cur == null) {
            cur = ordersManager.startNewOrder();
        }
        return cur;
    }

    /**
     * Displays information alert of a message
     * @param msg the message being shown
     */
    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
