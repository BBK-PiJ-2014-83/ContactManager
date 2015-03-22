package main;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import java.io.File;


public class XmlFile {
    private static final String SRC = "src";
    public XmlFile() {

    }
    public static void main(String[] args) {
        XmlFile test = new XmlFile();
        test.readFile("contactmanager");
    }
    public Document readFile(String filename) {
        try {
            //Load up file
            File xmlFile = new File( SRC + "/" + filename + ".xml");
            DocumentBuilderFactory docBuilder = DocumentBuilderFactory.newInstance();
            DocumentBuilder dbuilder = docBuilder.newDocumentBuilder();
            //Parse the document to xml
            Document meetingData = dbuilder.parse(xmlFile);
            return meetingData;

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static NodeList getItems(String type, Document doc) {
        NodeList returnList = doc.getElementsByTagName(type);
        return returnList;
    }

    public void saveFile() {

    }

}
