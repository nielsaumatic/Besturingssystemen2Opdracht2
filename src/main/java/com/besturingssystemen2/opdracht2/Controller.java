package com.besturingssystemen2.opdracht2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Controller {
    private final List<Frame> RAM;

    private int timer;

    private final List<Process> processes;

    private final Set<Integer> processesInRAM;

    private final List<Instruction> instructions;

    public Controller(List<Instruction> instructions) {
        this.RAM = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            RAM.add(new Frame(i));
        }

        this.timer = 0;
        this.processes = new ArrayList<>();
        this.processesInRAM = new HashSet<>();
        this.instructions = instructions;
    }



    public void selectInstruction(Instruction instruction) {
        switch (instruction.getOperation()) {
            case "Start" -> startInstruction(instruction);
            case "Write" -> writeInstruction(instruction);
            case "Read" -> readInstruction(instruction);
            case "Terminate" -> terminateInstruction(instruction);
        }
        timer++;
    }

    public void startInstruction(Instruction instruction) {
        processes.add(new Process(instruction.getPid()));

        if (processesInRAM.size() == 0) {
            for (Frame frame : RAM) {
                frame.setPid(processes.get(0).getPid());
                frame.setEntryPT(null);
            }
            processesInRAM.add(instruction.getPid());
        }
        else {
            int framesPerProcessOld = 12 / (processesInRAM.size() - 1);
            int framesPerProcess = 12 / processesInRAM.size();
            int framesToRemovePerProcess = framesPerProcessOld - framesPerProcess;

            //find the frames to replace per process
            List<Frame> availableFrames = new ArrayList<>();
            for (int pid: processesInRAM) {
                List<Frame> framesInRamWithPid = getFramesInRamWithPid(pid);

                for (int i = 0; i < framesToRemovePerProcess; i++) {
                    Frame lruFrame = getLruFrame(framesInRamWithPid);
                    availableFrames.add(lruFrame);
                    framesInRamWithPid.remove(lruFrame);
                }
            }

            //convert found frames to frames belonging to new process
            for (Frame frame : availableFrames) {
                frame.setPid(instruction.getPid());
                frame.setEntryPT(null);
            }

            processesInRAM.add(instruction.getPid());
        }
    }

    public void writeInstruction(Instruction instruction) {

    }

    public void readInstruction(Instruction instruction) {

    }

    public void terminateInstruction(Instruction instruction) {
        int framesPerProcessOld = 12 / processes.size();
        int framesPerProcess = 12 / (processes.size() - 1);
        int framesToAddPerProcess = framesPerProcess - framesPerProcessOld;

        processesInRAM.remove(instruction.getPid());

        List<Frame> framesInRamWithPid = getFramesInRamWithPid(instruction.getPid());
        for (int pid : processesInRAM) {
            for (int i = 0; i < framesToAddPerProcess; i++) {
                framesInRamWithPid.get(0).setPid(pid);
                framesInRamWithPid.get(0).setEntryPT(null);
                framesInRamWithPid.remove(0);
            }
        }

    }

    public void oneInstruction() {
        selectInstruction(instructions.get(timer));
    }

    public void allInstructions() {
        while (timer < instructions.size()-1) {
            selectInstruction(instructions.get(timer));
        }
    }


    public List<Frame> getFramesInRamWithPid(int pid) {
        List<Frame> framesWithPid = new ArrayList<>();
        for (Frame frame : RAM) {
            if(frame.getPid() == pid) {
                framesWithPid.add(frame);
            }
        }
        return framesWithPid;
    }

    public Frame getLruFrame(List<Frame> frames) {
        Frame lruFrame = null;
        for (Frame frame : frames) {
            if (lruFrame == null) {
                lruFrame = frame;
                continue;
            }
            if (frame.getEntryPT() == null) {
                lruFrame = frame;
                continue;
            }
            if (frame.getEntryPT().getLastAccess() < lruFrame.getEntryPT().getLastAccess()) {
                lruFrame = frame;
            }
        }
        return lruFrame;
    }

    /*
    public Frame getMostRecentFrame(List<Frame> frames) {
        Frame mostRecentFrame = null;
        for (Frame frame : frames) {
            if (mostRecentFrame == null) {
                if (frame.getEntryPT() == null) {
                    continue;
                }
                mostRecentFrame = frame;
                continue;
            }
            if (frame.getEntryPT() == null) {
                continue;
            }
            if (frame.getEntryPT().getLastAccess() > mostRecentFrame.getEntryPT().getLastAccess()) {
                mostRecentFrame = frame;
            }
        }
        return mostRecentFrame;
    }
    */
}
