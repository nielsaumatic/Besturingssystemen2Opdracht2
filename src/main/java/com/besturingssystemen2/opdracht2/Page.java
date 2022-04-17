package com.besturingssystemen2.opdracht2;

public class Page {

    private boolean present;

    private boolean modify;

    private int lastAccess;

    private int frameNumber;

    public Page() {
        this.present = false;
        this.modify = false;
        this.lastAccess = -1;
        this.frameNumber = -1;
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

    public void setPage(int frameNumber) {
        this.present = true;
        this.frameNumber = frameNumber;
    }

    public void resetPage() {
        this.present = false;
        this.modify = false;
        this.lastAccess = -1;
        this.frameNumber = -1;
    }
}