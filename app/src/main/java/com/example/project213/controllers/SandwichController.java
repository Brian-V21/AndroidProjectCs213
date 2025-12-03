package org.example.project4cs213.controller;

import model.Order;
import model.OrdersManager;
import model.Sandwich;
import enums.Bread;
import enums.Protein;
import enums.SandwichAddOn;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.EnumSet;

/**
 * Controller for the sandwich ordering view.
 */
public class SandwichController implements OrdersAware {

    @FXML
    private ComboBox<Bread> breadCombo;

    @FXML
    private ComboBox<Protein> proteinCombo;

    @FXML
    private ListView<SandwichAddOn> addOnList;

    @FXML
    private Spinner<Integer> quantitySpinner;

    @FXML
    private Label priceLabel;

    @FXML
    private ImageView sandwichImage;

    private OrdersManager ordersManager;

    /**
     * Set the order manager for the controller
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
     * Initializes the controller
     */
    @FXML
    private void initialize() {

        breadCombo.setItems(FXCollections.observableArrayList(Bread.values()));
        proteinCombo.setItems(FXCollections.observableArrayList(Protein.values()));
        addOnList.setItems(FXCollections.observableArrayList(SandwichAddOn.values()));
        addOnList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        quantitySpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1));

        breadCombo.getSelectionModel().selectedItemProperty()
                .addListener((o, a, b) -> updatePrice());
        proteinCombo.getSelectionModel().selectedItemProperty()
                .addListener((o, a, b) -> updatePrice());
        addOnList.getSelectionModel().selectedItemProperty()
                .addListener((o, a, b) -> updatePrice());
        quantitySpinner.valueProperty()
                .addListener((o, a, b) -> updatePrice());

        sandwichImage.setImage(new Image(getClass().getResource("/images/sandwich.jpg").toExternalForm()));

        breadCombo.getSelectionModel().select(Bread.BAGEL);
        proteinCombo.getSelectionModel().select(Protein.CHICKEN);
    }

    /**
     * Updates prices based on current selected order of sandwiches
     */
    private void updatePrice() {
        Bread bread = breadCombo.getValue();
        Protein protein = proteinCombo.getValue();
        Integer qty = quantitySpinner.getValue();
        if (bread == null || protein == null || qty == null) {
            priceLabel.setText("$0.00");
            return;
        }
        var addOn = addOnList.getSelectionModel().getSelectedItems();
        EnumSet<SandwichAddOn> addOns = EnumSet.noneOf(SandwichAddOn.class);
        addOns.addAll(addOn);
        Sandwich s = new Sandwich(bread, protein, addOns, qty);
        priceLabel.setText(String.format("$%.2f", s.price()));
    }

    /**
     * Add selected sandwich to current order
     */
    @FXML
    private void addSandwichToOrder() {
        Bread bread = breadCombo.getValue();
        Protein protein = proteinCombo.getValue();
        Integer qty = quantitySpinner.getValue();
        if (bread == null || protein == null || qty == null || qty <= 0) {
            show("Please choose bread, protein, and quantity.");
            return;
        }
        var addOn = addOnList.getSelectionModel().getSelectedItems();
        EnumSet<SandwichAddOn> addOns = EnumSet.noneOf(SandwichAddOn.class);
        addOns.addAll(addOn);
        Order order = ensureCurrentOrder();
        order.addItem(new Sandwich(bread, protein, addOns, qty));
        show("Sandwich added to order #" + order.getOrderNumber());
    }

    /**
     * Makes sure there is a current order placed
     * @return the current order
     */
    private Order ensureCurrentOrder() {
        Order cur = ordersManager.getCurrentOrder();
        if (cur == null) cur = ordersManager.startNewOrder();
        return cur;
    }

    /**
     * Display the information of the message
     * @param msg the message being shown
     */
    private void show(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
