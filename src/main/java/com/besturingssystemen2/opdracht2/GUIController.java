package com.besturingssystemen2.opdracht2;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class GUIController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}