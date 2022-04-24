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

    public List<Frame> getRAM() {
        return RAM;
    }

    public Set<Integer> getProcessesInRAM() {
        return processesInRAM;
    }

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
                frame.setPage(null);
            }
            processesInRAM.add(instruction.getPid());
        }
        else {
            int framesPerProcessOld = 12 / processesInRAM.size();
            int framesPerProcess = 12 / (processesInRAM.size() + 1);
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
                frame.setPage(null);
            }

            processesInRAM.add(instruction.getPid());
        }
    }

    public void writeInstruction(Instruction instruction) {
        checkRamElsePutIn(instruction);
        processes.get(instruction.getPid()).writeToAddress(instruction.getAddress());
    }

    public void readInstruction(Instruction instruction) {
        checkRamElsePutIn(instruction);
    }

    public void terminateInstruction(Instruction instruction) {
        if (processesInRAM.size() == 1) {
            for (Frame frame : RAM) {
                if (frame.getPage() != null) {
                    frame.getPage().resetPage();
                    frame.setPage(null);
                }
                frame.setPid(-1);
            }
            processesInRAM.remove(instruction.getPid());
        }
        else {
            int framesPerProcessOld = 12 / processesInRAM.size();
            int framesPerProcess = 12 / (processesInRAM.size() - 1);
            int framesToAddPerProcess = framesPerProcess - framesPerProcessOld;

            processesInRAM.remove(instruction.getPid());

            List<Frame> framesInRamWithPid = getFramesInRamWithPid(instruction.getPid());
            for (int pid : processesInRAM) {
                for (int i = 0; i < framesToAddPerProcess; i++) {
                    framesInRamWithPid.get(0).setPid(pid);
                    framesInRamWithPid.get(0).setPage(null);
                    framesInRamWithPid.remove(0);
                }
            }
        }


    }

    public void oneInstruction() {
        selectInstruction(instructions.get(timer));
    }

    public void allInstructions() {
        while (timer < instructions.size()) {
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
                if (frame.getPage() == null) {
                    break;
                }
                continue;
            }
            if (frame.getPage() == null) {
                lruFrame = frame;
                break;
            }
            if (frame.getPage().getLastAccess() < lruFrame.getPage().getLastAccess()) {
                lruFrame = frame;
            }
        }
        return lruFrame;
    }

    public void checkRamElsePutIn(Instruction instruction) {
        int pid = instruction.getPid();
        Process process = processes.get(pid);
        int address = instruction.getAddress();
        Page page = process.getPage(address);
        if (!process.presentInRam(address)) {
            List<Frame> framesInRamWithPid = getFramesInRamWithPid(pid);
            Frame lruFrame = getLruFrame(framesInRamWithPid);
            if (lruFrame.getPage() != null) {
                Page framePage = lruFrame.getPage();
                framePage.resetPage();
            }
            lruFrame.setPage(page);
            page.setPage(lruFrame.getFrameNumber());
        }
        page.setLastAccess(timer);
    }

    public int getTimer(){
        return timer;
    }

    public List<Instruction> getInstructions(){
        return instructions;
    }
}
