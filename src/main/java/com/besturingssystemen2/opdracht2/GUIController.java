package com.besturingssystemen2.opdracht2;

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
    public TableColumn<Object, Object> tcFrame;
    public TableColumn<Object, Object> tcPid;
    public TableColumn<Object, Object> tcPagenumber;
    public TableView<Frame> tvFrames;
    public TableColumn<Object, Object> tcPage;
    public TableColumn<Object, Object> tcPresent;
    public TableColumn<Object, Object> tcModify;
    public TableColumn<Object, Object> tcLastAccess;
    public TableColumn<Object, Object> tcFrameNumber;
    public TableView<Page> tvPageTable;
    public Label lbLastInstructionSpec;
    public TableView<Process> tvPageWrites;
    public TableColumn<Object, Object> tcProcess;
    public TableColumn<Object, Object> tcPageWrites;
    public ChoiceBox<String> cbPageTable;
    public String selectedInstruction = "Last instruction";
    @FXML
    private Label lbTimer;

    Set<Integer> vprocesseninram = new HashSet<>();
    int vtimer = 0;
    List<Frame> vram = new ArrayList<>();

    List<Instruction> instructions = Utility.readXML(Utility.filename);
    Controller c = new Controller(instructions);

    List<Process> vprocesses = c.getProcesses();
    Boolean ranAll = false;

    public static List<Frame> copyFrameList(List<Frame> list) {
        ArrayList<Frame> copy = new ArrayList<>();

        for (Frame frame : list) {
            copy.add(new Frame(frame));
        }
        return copy;
    }

    public static Set<Integer> copy(Set<Integer> set) {
        return new HashSet<>(set);
    }

    public static List<Process> copy(List<Process> list) {
        ArrayList<Process> copy = new ArrayList<>();

        for (Process process : list) {
            copy.add(new Process(process));
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
    public void runAll() {
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

        cbPageTable.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            selectedInstruction = String.valueOf(cbPageTable.getSelectionModel().getSelectedItem());
            if(!ranAll){
                updatePageTable();
            }
            else{
                updatePageTableEnd();
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
        int vpn = (int)floor((double) va / 4096);
        int offset = va % 4096;

        Process p = vprocesses.stream().filter(pr -> pr.getPid()==pid).findFirst().orElse(null);
        assert p != null;
        int pfn = p.getPageTable().stream().filter(pa -> pa.getPageNumber()==vpn).findFirst().orElse(null).getFrameNumber();

        if(pfn==-1){
            a += "/";
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
        tcFrameNumber.setCellValueFactory(new PropertyValueFactory<>("frameNumber"));
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
        tcFrameNumber.setCellValueFactory(new PropertyValueFactory<>("frameNumber"));
    }

    private void clearPageTable(){
        tvPageTable.getItems().clear();
    }
}