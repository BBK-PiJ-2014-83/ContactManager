package test;

import main.*;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import static org.junit.Assert.assertEquals;
/**
 *This testing will check that the file read in works correctly - dates are formatted properly , clients are added etc.
 */
public class testFileImport {
    XmlFile test;
    Document testDoc;
    @Before
    public void before() {
        test = new XmlFile();
        testDoc = test.readFile("testfile");
    }

    @Test
    public void getContacts() {
        NodeList contacts = test.getItems("contact",testDoc);
        assertEquals(2,contacts.getLength());
    }

    @Test
    public void getPastMeeting() {
        NodeList meetings = test.getItems("pastMeeting",testDoc);
        assertEquals(1,meetings.getLength());
    }

    @Test
    public void getFutureMeeting() {
        NodeList meetings = test.getItems("futureMeeting",testDoc);
        assertEquals(1,meetings.getLength());
    }

    //Check that you can access a future meetings date
    @Test
    public void getFutureMeetingDate() {
        NodeList meetings = test.getItems("futureMeeting",testDoc);
        NodeList tester = meetings.item(0).getChildNodes();
        for (int j = 0; j < tester.getLength(); j++) {
            if (tester.item(j).getNodeType() == Node.ELEMENT_NODE){
                if (tester.item(j).getNodeName() == "date") {
                    assertEquals("2016/04/02 10:00:00",tester.item(j).getTextContent());
                    break;
                }
            }
        }
    }



}
