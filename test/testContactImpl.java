package test;

import main.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class testContactImpl {
    Contact testContact;
    @Before
    public void before() {
        testContact = new ContactImpl("John Spear", "He's pretty busy at the moment.",3);
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
    public void SetGetNotes() {
        testContact.addNotes("He should have some spare time in 2016.");
        assertEquals("He should have some spare time in 2016.", testContact.getNotes());
    }
    @Test(expected = IllegalArgumentException.class)
    public void nullValPassedInException() {
        testContact.addNotes(null);
    }

    @Test(expected = NullPointerException.class)
    public void nullConstructor() {
        Contact tester = new ContactImpl(null,"busy busy busy", 5);
    }

}
