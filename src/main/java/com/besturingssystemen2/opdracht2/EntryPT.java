package com.besturingssystemen2.opdracht2;

public class EntryPT {

    private final int pid;

    private int pageNumber;

    private boolean present;

    private boolean modify;

    private int lastAccess;

    private int frameNumber;

    public EntryPT(int pid, int pageNumber) {
        this.pid = pid;
        this.pageNumber = pageNumber;
        this.present = false;
        this.modify = false;
        this.lastAccess = 0;
        this.frameNumber = 0;
    }

    public int getPid() {
        return pid;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }

    public boolean isModify() {
        return modify;
    }

    public void setModify(boolean modify) {
        this.modify = modify;
    }

    public int getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(int lastAccess) {
        this.lastAccess = lastAccess;
    }

    public int getFrameNumber() {
        return frameNumber;
    }

    public void setFrameNumber(int frameNumber) {
        this.frameNumber = frameNumber;
    }
}
