package test;

import main.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class testContactImpl {
    Contact testContact;
    public void before() {
        //Like a constructor

        testContact = new ContactImpl("John Spear", "He's pretty busy at the moment.");
        testContact.setId(3);
    }

    @Test
    public void test_GetName() {
        assertEquals("John Spear", testContact.getName());
    }
    @Test
    public void GetNotes() {
        assertEquals("He's pretty busy at the moment.", testContact.getNotes());
    }
    @Test
    public void GetId() {
        assertEquals(3, testContact.getId());
    }
    @Test
    public void SetGetNotes) {
        testContact.setNotes("He should have some spare time in 2016.");
        assertEquals("He should have some spare time in 2016.", testContact.getNotes());
    }
}
