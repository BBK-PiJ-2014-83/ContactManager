package test;

import main.*;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
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




}
