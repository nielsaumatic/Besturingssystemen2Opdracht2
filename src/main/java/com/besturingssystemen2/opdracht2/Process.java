package com.besturingssystemen2.opdracht2;

import java.util.ArrayList;
import java.util.List;

public class Process {

    private final int pid;
    private final List<Page> pageTable;

    private int numberOfWrites;

    public Process(int pid) {
        this.pid = pid;

        this.pageTable = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            pageTable.add(new Page());
        }
    }

    public int getPid() {
        return pid;
    }

    public List<Page> getPageTable() {
        return pageTable;
    }

    public int addressToPage(int address) {
        return address / 4096;
    }

    public boolean presentInRam(int address) {
        return pageTable.get(addressToPage(address)).isPresent();
    }

    public void writeToAddress(int address) {
        numberOfWrites++;
        pageTable.get(addressToPage(address)).setModify(true);
    }

    public Page getPage(int address) {
        return pageTable.get(addressToPage(address));
    }

}

