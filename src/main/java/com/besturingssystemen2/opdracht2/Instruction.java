package com.besturingssystemen2.opdracht2;

import java.util.Map;

public class Instruction {
    private final int pid;
    private final int operation;
    private final int address;

    static final Map<String , Integer> operations = Map.of("Start", 0, "Write", 1, "Read", 2, "Terminate", 3);

    public Instruction(int pid, String operation, int address) {
        this.pid = pid;
        this.operation = operations.get(operation);
        this.address = address;
    }

    public int getPid() {
        return pid;
    }

    public int getOperation() {
        return operation;
    }

    public int getAddress() {
        return address;
    }
}
