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
        testContactManager.loadFile("testfile");
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
    //load the futureMeeting from the testfile then test to see if it exists by calling it back
    @Test
    public void GetFutureMeetingsFromFile() {
        Meeting testMeeting = testContactManager.getFutureMeeting(2);
        assertEquals(2,testMeeting.getId());
    }

    //Try to get a future meeting  that is in the past
    @Test(expected = IllegalArgumentException.class)
    public void GetFutureMeetingsFromPast() {
        Meeting testMeeting = testContactManager.getFutureMeeting(1);
    }


    //Try to get a future meeting  that doesn't exist. SHould return null.
    @Test
    public void GetFutureMeetingNotExist() {
        Meeting testMeeting = testContactManager.getFutureMeeting(4);
        assertEquals(null,testMeeting);
    }

    @Test
    public void testSave() {
        testContactManager.flush();
    }
}
