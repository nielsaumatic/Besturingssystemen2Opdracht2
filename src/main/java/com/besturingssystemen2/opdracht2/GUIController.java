package com.besturingssystemen2.opdracht2;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.*;

import static java.lang.Math.floor;

public class GUIController implements Initializable {
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
    public Label lbLastInstructionSpec;
    public TableView tvPageWrites;
    public TableColumn tcProcess;
    public TableColumn tcPageWrites;
    public ChoiceBox cbPageTable;
    public String selectedInstruction = "Next instruction";
    @FXML
    private Label lbTimer;

    Set<Integer> vprocesseninram = new HashSet<>();
    int vtimer = 0;
    List<Frame> vram = new ArrayList<>();

    List<Instruction> instructions = Utility.readXML("Instructions_30_3.xml");
    Controller c = new Controller(instructions);

    List<Process> vprocesses = c.getProcesses();
    Boolean ranAll = false;

    public static List<Frame> copyFrameList(List<Frame> list) {
        ArrayList<Frame> copy = new ArrayList<>();

        Iterator<Frame> iterator = list.iterator();
        while(iterator.hasNext()){
            copy.add(new Frame(iterator.next()));
        }
        return copy;
    }

    public static Set<Integer> copy(Set<Integer> set) {
        Set<Integer> copy = new HashSet<Integer>();

        Iterator<Integer> iterator = set.iterator();
        while(iterator.hasNext()){
            copy.add(iterator.next());
        }
        return copy;
    }

    public static List<Process> copy(List<Process> list) {
        ArrayList<Process> copy = new ArrayList<>();

        Iterator<Process> iterator = list.iterator();
        while(iterator.hasNext()){
            copy.add(new Process(iterator.next()));
        }
        return copy;
    }

    @FXML
    public void step() {
        if(!ranAll){
            vprocesses = copy(c.getProcesses());
            vprocesseninram = copy(c.getProcessesInRAM());
            vtimer = c.getTimer();
            vram = copyFrameList(c.getRAM());

            lbTimer.setText("Timer: " + vtimer);
            updatePageWrites();
            updateNextInstruction();
            updateLastInstruction();
            updateFrames();

            if(c.getInstructions().size() > vtimer) {
                c.oneInstruction();
                updatePageTable();
            }
        }
    }

    @FXML
    public void runAll(ActionEvent actionEvent) {
        c.allInstructions();
        vprocesses = new ArrayList<>();
        ranAll = true;
        vram = copyFrameList(c.getRAM());
        lbTimer.setText("Timer: " + c.getTimer());
        updatePageWrites();
        resetNextInstruction();
        updateLastInstruction();
        updateFrames();
        updatePageTableEnd();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateFrames();
        updateNextInstruction();
        cbPageTable.getItems().addAll("Next instruction", "Last instruction");
        cbPageTable.setValue("Last instruction");

        cbPageTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                selectedInstruction = String.valueOf(cbPageTable.getSelectionModel().getSelectedItem());
                if(!ranAll){
                    updatePageTable();
                }
                else{
                    updatePageTableEnd();
                }
            }
        });

        c.oneInstruction();
        updatePageTable();
    }

    private void updateLastInstruction(){
        if(vtimer-1 >= 0 && !ranAll){
            int vaddress = c.getInstructions().get(vtimer-1).getAddress();
            int pid = c.getInstructions().get(vtimer-1).getPid();
            lbLastInstructionSpec.setText("Process: " + pid + "\n" +
                    "Operation: " + c.getInstructions().get(vtimer-1).getOperation() + "\n" +
                    "Virtual address: " + vaddress + "\n" +
                    "Physical address: " + toPhysical(vaddress, pid));
        }
        else{
            lbLastInstructionSpec.setText("Process: " + c.getInstructions().get(c.getInstructions().size()-1).getPid() + "\n" +
                                        "Operation: " + c.getInstructions().get(c.getInstructions().size()-1).getOperation() + "\n" +
                                        "Virtual address: " + c.getInstructions().get(c.getInstructions().size()-1).getAddress());
        }
    }

    private void updateNextInstruction(){
        if(c.getInstructions().size() > vtimer){
            lbNextInstructionSpec.setText("Process: " + c.getInstructions().get(vtimer).getPid() + "\n" +
                    "Operation: " + c.getInstructions().get(vtimer).getOperation() + "\n" +
                    "Virtual address: " + c.getInstructions().get(vtimer).getAddress());
        }
        else{
            resetNextInstruction();
        }
    }

    private void resetNextInstruction(){
        lbNextInstructionSpec.setText("NA");
    }

    private String toPhysical(int va, int pid){
        String a = "";
        int vpn = (int)floor(va / 4096);
        int offset = va % 4096;

        Process p = vprocesses.stream().filter(pr -> pr.getPid()==pid).findFirst().orElse(null);
        assert p != null;
        int pfn = p.getPageTable().stream().filter(pa -> pa.getPageNumber()==vpn).findFirst().orElse(null).getFrameNumber();

        if(pfn==-1){
            a += "PageFault";
        }
        else{
            int pa = pfn * 4096 + offset;
            a += pa + "\n" + "Page: " + vpn + "\n" + "Offset: " + offset;
        }
        return a;
    }

    private void updateFrames(){
        tvFrames.getItems().clear();
        tvFrames.getItems().addAll(vram);
        tcFrame.setCellValueFactory(new PropertyValueFactory<>("frameNumber"));
        tcPid.setCellValueFactory(new PropertyValueFactory<>("pid"));
        tcPagenumber.setCellValueFactory(new PropertyValueFactory<>("pageNumber"));
    }

    private void updatePageWrites(){
        tvPageWrites.getItems().clear();
        if(!ranAll){
            tvPageWrites.getItems().addAll(vprocesses);
        }
        else{
            tvPageWrites.getItems().addAll(c.getProcesses());
        }

        tcProcess.setCellValueFactory(new PropertyValueFactory<>("pid"));
        tcPageWrites.setCellValueFactory(new PropertyValueFactory<>("pageFaults"));
    }

    private void updatePageTable(){
        tvPageTable.getItems().clear();

        if(Objects.equals(selectedInstruction, "Next instruction")){
            tvPageTable.getItems().addAll(c.getProcesses().stream().filter(p -> p.getPid() == c.getInstructions().get(vtimer).getPid())
                    .findFirst().orElse(null)
                    .getPageTable());
        }
        else if(vtimer-1 >= 0){
            tvPageTable.getItems().addAll(vprocesses.stream().filter(p -> p.getPid() == c.getInstructions().get(vtimer-1).getPid())
                    .findFirst().orElse(new Process())
                    .getPageTable());
        }

        tcPage.setCellValueFactory(new PropertyValueFactory<>("pageNumber"));
        tcPresent.setCellValueFactory(new PropertyValueFactory<>("present"));
        tcModify.setCellValueFactory(new PropertyValueFactory<>("modify"));
        tcLastAccess.setCellValueFactory(new PropertyValueFactory<>("lastAccess"));
    }

    private void updatePageTableEnd(){
        clearPageTable();
        if(Objects.equals(selectedInstruction, "Last instruction")){
            tvPageTable.getItems().addAll(c.getProcesses().stream().filter(p -> p.getPid() == c.getInstructions().get(c.getTimer()-1).getPid())
                    .findFirst().orElse(new Process())
                    .getPageTable());
        }

        tcPage.setCellValueFactory(new PropertyValueFactory<>("pageNumber"));
        tcPresent.setCellValueFactory(new PropertyValueFactory<>("present"));
        tcModify.setCellValueFactory(new PropertyValueFactory<>("modify"));
        tcLastAccess.setCellValueFactory(new PropertyValueFactory<>("lastAccess"));
    }

    private void clearPageTable(){
        tvPageTable.getItems().clear();
    }
}