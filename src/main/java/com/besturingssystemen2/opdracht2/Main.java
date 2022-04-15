package com.besturingssystemen2.opdracht2;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        GUIApplication.launchGUI();

        List<Instruction> instructions = Utility.readXML("Instructions_30_3.xml");
    }
}
