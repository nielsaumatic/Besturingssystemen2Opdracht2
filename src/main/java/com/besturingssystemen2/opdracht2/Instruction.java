package com.besturingssystemen2.opdracht2;

public class Instruction {
    private final int pid;
    private final String operation;
    private final int address;

    public Instruction(int pid, String operation, int address) {
        this.pid = pid;
        this.operation = operation;
        this.address = address;
    }

    public int getPid() {
        return pid;
    }

    public String getOperation() {
        return operation;
    }

    public int getAddress() {
        return address;
    }
}
