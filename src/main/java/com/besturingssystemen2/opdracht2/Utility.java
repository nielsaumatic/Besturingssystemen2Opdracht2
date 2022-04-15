package com.besturingssystemen2.opdracht2;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Utility {
    public static List<Instruction> readXML(String filename) {

        List<Instruction> instructions = new ArrayList<>();

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

            DocumentBuilder db = dbf.newDocumentBuilder();

            Document doc = db.parse(new File(filename));

            NodeList list = doc.getElementsByTagName("instruction");

            for (int i = 0; i < list.getLength(); i++) {
                Node node = list.item(i);

                if ( node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    int pid = Integer.parseInt(element.getElementsByTagName("processID").item(0).getTextContent());
                    String operation = element.getElementsByTagName("operation").item(0).getTextContent();
                    int address = Integer.parseInt(element.getElementsByTagName("address").item(0).getTextContent());

                    instructions.add(new Instruction(pid, operation, address));
                }
            }

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return instructions;
    }
}
