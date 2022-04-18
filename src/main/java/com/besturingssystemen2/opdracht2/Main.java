package com.besturingssystemen2.opdracht2;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        List<Instruction> instructions = Utility.readXML("Instructions_30_3.xml");

        Controller controller = new Controller(instructions);

        //controller.allInstructions();


        //GUIApplication.launchGUI();
    }
}
