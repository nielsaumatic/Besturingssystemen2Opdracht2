package com.besturingssystemen2.opdracht2;

public class Page {

    private final int pageNumber;

    private boolean present;

    private boolean modify;

    private int lastAccess;

    private int frameNumber;

    public Page(int i) {
        this.pageNumber = i;
        this.present = false;
        this.modify = false;
        this.lastAccess = -1;
        this.frameNumber = -1;
    }

    public Page(Page p){
        this.pageNumber = p.pageNumber;
        this.present = p.present;
        this.modify = p.modify;
        this.lastAccess = p.lastAccess;
        this.frameNumber = p.frameNumber;
    }

    public int getPageNumber() {
        return pageNumber;
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
        this.setPresent(true);
        this.setFrameNumber(frameNumber);
    }

    public void resetPage() {
        this.setPresent(false);
        this.setModify(false);
        this.setLastAccess(-1);
        this.setFrameNumber(-1);
    }
}
