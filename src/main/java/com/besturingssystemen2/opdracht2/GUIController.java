package com.besturingssystemen2.opdracht2;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class GUIController implements Initializable {
    public String selectedp;
    public ListView lvFramesRam;
    public Label lbFramesRam;
    public Label lbNextInstruction;
    public Label lbNextInstructionSpec;
    public TableColumn tcFrame;
    public TableColumn tcPid;
    public TableColumn tcPagenumber;
    public TableView tvFrames;
    public TableColumn tcPage;
    public TableColumn tcPresent;
    public TableColumn tcModify;
    public TableColumn tcLastAccess;
    public TableView tvPageTable;
    @FXML
    private Label lbTimer;
    @FXML
    private ListView<Integer> lvRam;

    Set<Integer> vprocesseninram = new HashSet<>();
    int vtimer = 0;
    List<Frame> vram = new ArrayList<>();

    List<Instruction> instructions = Utility.readXML("Instructions_30_3.xml");
    Controller c = new Controller(instructions);

    List<Process> vprocesses = c.getProcesses();

    @FXML
    public void step(ActionEvent actionEvent) {



        lbTimer.setText("Timer: " + String.valueOf(c.getTimer()));
        lvRam.getItems().clear();
        lvRam.getItems().addAll(c.getProcessesInRAM());
        resetLvPagesRam();
        updateNextInstruction();
        updateFrames();

        vprocesseninram.clear();
        vprocesseninram.addAll(c.getProcessesInRAM());
        vtimer = c.getTimer();
        vram.clear();
        vram.addAll(c.getRAM());
        c.oneInstruction();

        updatePageTable();

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
        updateFrames();
        updateNextInstruction();

        lvRam.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observableValue, Integer s, Integer t1) {
                selectedp = String.valueOf(lvRam.getSelectionModel().getSelectedItem());
                lvFramesRam.getItems().clear();
                lvFramesRam.getItems().addAll(vram.stream().filter(f -> !lvRam.getSelectionModel().isEmpty()).filter(f -> f.getPid() == lvRam.getSelectionModel()
                                .getSelectedItem())
                        .map(Frame::getFrameNumber)
                        .toList());
            }
        });

        c.oneInstruction();
    }

    private void updateNextInstruction(){
        lbNextInstructionSpec.setText("Process: " + c.getInstructions().get(c.getTimer()).getPid() + "\n" +
                                        "Operation: " + c.getInstructions().get(c.getTimer()).getOperation() + "\n" +
                                        "Virtual adress: " + c.getInstructions().get(c.getTimer()).getAddress());
    }
    private void updateFrames(){
        tvFrames.getItems().clear();
        tvFrames.getItems().addAll(c.getRAM());
        tcFrame.setCellValueFactory(new PropertyValueFactory<>("frameNumber"));
        tcPid.setCellValueFactory(new PropertyValueFactory<>("pid"));
    }

    private void updatePageTable(){
        ArrayList<Integer> a = new ArrayList();
        for(int i=0;i<16;i++){
            a.add(i);
        }
        tvPageTable.getItems().clear();
        //tvPageTable.getItems().addAll(c.getProcesses().get(c.getInstructions().get(vtimer).getPid()).getPageTable());
        tvPageTable.getItems().addAll(c.getProcesses().stream().filter(p -> p.getPid() == c.getInstructions().get(vtimer).getPid())
                .findFirst().orElse(null)
                .getPageTable());
        //tvPageTable.getItems().addAll(a);
        tcPresent.setCellValueFactory(new PropertyValueFactory<>("present"));
        tcModify.setCellValueFactory(new PropertyValueFactory<>("modify"));
        tcLastAccess.setCellValueFactory(new PropertyValueFactory<>("lastAccess"));
    }

}