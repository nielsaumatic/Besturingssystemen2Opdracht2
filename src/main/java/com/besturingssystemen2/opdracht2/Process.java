package com.besturingssystemen2.opdracht2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Process {

    private final int pid;
    private final List<Page> pageTable;

    private int pageFaults;

    public static List<Page> copyPageList(List<Page> list) {
        ArrayList<Page> copy = new ArrayList<>();

        Iterator<Page> iterator = list.iterator();
        while(iterator.hasNext()){
            copy.add(new Page(iterator.next()));
        }
        return copy;
    }

    public Process(){
        this.pid = -1;
        this.pageTable = null;
    }

    public Process(int pid) {
        this.pid = pid;

        this.pageTable = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            pageTable.add(new Page(i));
        }

        pageFaults = 0;
    }

    public Process(Process p) {
        this.pid = p.pid;

        this.pageTable = copyPageList(p.pageTable);

        pageFaults = p.pageFaults;
    }

    public int getPid() {
        return pid;
    }

    public List<Page> getPageTable() {
        return pageTable;
    }

    public int getPageFaults() {
        return pageFaults;
    }

    public void setPageFaults(int pageFaults) {
        this.pageFaults = pageFaults;
    }

    public int addressToPage(int address) {
        return address / 4096;
    }

    public boolean presentInRam(int address) {
        return pageTable.get(addressToPage(address)).isPresent();
    }

    public void writeToAddress(int address) {
        pageTable.get(addressToPage(address)).setModify(true);
    }

    public Page getPage(int address) {
        return pageTable.get(addressToPage(address));
    }

    public void incrementPageFaults() {
        pageFaults++;
    }
}

