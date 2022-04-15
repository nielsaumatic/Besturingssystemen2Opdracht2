package com.besturingssystemen2.opdracht2;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class GUIController {
    @FXML
    private Label timer;
    @FXML
    private Label instruction;

    @FXML
    protected void onHelloButtonClick() {
        timer.setText("Welcome to JavaFX Application!");
    }
}