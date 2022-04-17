package com.besturingssystemen2.opdracht2;

public class Frame {
    private final int frameNumber;
    private int pid;
    private Page page;

    public Frame(int frameNumber) {
        this.frameNumber = frameNumber;
        this.pid = -1;
        this.page = null;
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

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}
