package com.besturingssystemen2.opdracht2;

public class Frame {
    private final int frameNumber;
    private int pid;
    private Page page;

    public Frame(Frame f){
        this.frameNumber = f.frameNumber;
        this.pid = f.pid;
        this.page = f.page;
    }

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

    public int getPageNumber(){ //zogezegd unused maar nodig voor gui
        if(this.page == null){
            return -1;
        }
        else{
            return page.getPageNumber();
        }
    }
}
