package org.example.project4cs213.controller;

import model.OrdersManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;


public class MainController {

    @FXML
    private Button donutsBtn, coffeeBtn, sandwichBtn, currentOrderBtn, allOrdersBtn;

    private OrdersManager ordersManager = new OrdersManager();

    public void setOrdersManager(OrdersManager ordersManager) {
        this.ordersManager = ordersManager;
    }


    @FXML
    private void openDonutView() throws IOException {
        openModal("/org/example/project4cs213/donut-view.fxml",
                "Order Donuts", DonutController.class);
    }

    @FXML
    private void openCoffeeView() throws IOException {
        openModal("/org/example/project4cs213/coffee-view.fxml",
                "Order Coffee", CoffeeController.class);
    }

    @FXML
    private void openSandwichView() throws IOException {
        openModal("/org/example/project4cs213/sandwich-view.fxml",
                "Order Sandwich", SandwichController.class);
    }

    @FXML
    private void openCurrentOrderView() throws IOException {
        openModal("/org/example/project4cs213/current-order-view.fxml",
                "Current Order", CurrentOrderController.class);
    }

    @FXML
    private void openAllOrdersView() throws IOException {
        openModal("/org/example/project4cs213/all-orders-view.fxml",
                "All Orders", AllOrdersController.class);
    }

    private <T extends OrdersAware> void openModal(String fxmlPath, String title, Class<T> controllerType)
            throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Scene scene = new Scene(loader.load());
        Object controller = loader.getController();

        if (controllerType.isInstance(controller) && controller instanceof OrdersAware) {
            ((OrdersAware) controller).setOrdersManager(ordersManager);
        }

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }
}
