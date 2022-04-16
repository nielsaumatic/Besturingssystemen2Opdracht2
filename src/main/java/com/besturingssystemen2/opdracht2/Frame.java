package com.besturingssystemen2.opdracht2;

public class Frame {
    private final int frameNumber;
    private int pid;
    private EntryPT entryPT;

    public Frame(int frameNumber) {
        this.frameNumber = frameNumber;
        this.entryPT = null;
    }

    public int getFrameNumber() {
        return frameNumber;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public EntryPT getEntryPT() {
        return entryPT;
    }

    public void setEntryPT(EntryPT entryPT) {
        this.entryPT = entryPT;
    }
}
