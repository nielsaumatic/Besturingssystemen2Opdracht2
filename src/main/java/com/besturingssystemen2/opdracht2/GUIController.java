package com.besturingssystemen2.opdracht2;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class GUIController implements Initializable {
    public String selectedp;
    public ListView lvFramesRam;
    public Label lbFramesRam;
    public Label lbNextInstruction;
    public Label lbNextInstructionSpec;
    @FXML
    private Label lbTimer;
    @FXML
    private ListView<Integer> lvRam;

    List<Instruction> instructions = Utility.readXML("Instructions_30_3.xml");
    Controller c = new Controller(instructions);

    @FXML
    public void step(ActionEvent actionEvent) {
        c.oneInstruction();
        lbTimer.setText("Timer: " + String.valueOf(c.getTimer()));
        lvRam.getItems().clear();
        lvRam.getItems().addAll(c.getProcessesInRAM());
        resetLvPagesRam();
        updateNextInstruction();
    }

    private void resetLvPagesRam(){
        lvFramesRam.getItems().clear();
        lvFramesRam.getItems().add("Selecteer een proces");
    }

    @FXML
    public void runAll(ActionEvent actionEvent) {
        c.allInstructions();
        lbTimer.setText(String.valueOf(c.getTimer()));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateNextInstruction();
        lvRam.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observableValue, Integer s, Integer t1) {
                selectedp = String.valueOf(lvRam.getSelectionModel().getSelectedItem());
                lvFramesRam.getItems().clear();
                lvFramesRam.getItems().addAll(c.getRAM().stream().filter(f -> !lvRam.getSelectionModel().isEmpty()).filter(f -> f.getPid()==lvRam.getSelectionModel()
                        .getSelectedItem())
                        .map(Frame::getFrameNumber)
                        .collect(Collectors.toList()));
            }
        });
    }

    private void updateNextInstruction(){
        lbNextInstructionSpec.setText("Operation: " + c.getInstructions().get(c.getTimer()).getOperation() + "\n" +
                                        "Virtual adress: " + c.getInstructions().get(c.getTimer()).getAddress());
    }

}