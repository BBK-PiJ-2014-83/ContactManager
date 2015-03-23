package test;

import main.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Created by John on 23/03/2015.
 */
public class testFileOps {
    ContactManagerImpl testContactManager;
    @Before
    public void before() {
        testContactManager = new ContactManagerImpl();
        testContactManager.loadFile("testFile");
    }
    @Test
    public void addContactsFromFile() {
        Set<Contact> testContacts =  testContactManager.getContacts("John");
        assertEquals(testContacts.size(),1);
    }

    //load the pastMeeting from the testfile then test to see if it exists by calling it back
    @Test
    public void GetPastMeetingsFromFile() {
        Meeting testMeeting = testContactManager.getPastMeeting(1);
        assertEquals(1,testMeeting.getId());
    }

}
