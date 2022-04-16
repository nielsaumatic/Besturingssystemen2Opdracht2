package com.besturingssystemen2.opdracht2;

import java.util.ArrayList;
import java.util.List;

public class Process {

    private final int pid;
    private final List<EntryPT> pageTable;

    private int numberOfWrites;

    public Process(int pid) {
        this.pid = pid;

        this.pageTable = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            pageTable.add(new EntryPT(pid, i));
        }
    }

    public int getPid() {
        return pid;
    }

    public List<EntryPT> getPageTable() {
        return pageTable;
    }

}

