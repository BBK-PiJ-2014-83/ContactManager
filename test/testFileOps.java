package test;

import main.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class testFileOps {
    ContactManagerImpl testContactManager;
    @Before
    public void before() {
        testContactManager = new ContactManagerImpl();
    }
    @Test
    public void addContactsFromFile() {
        Set<Contact> testContacts =  testContactManager.getContacts("John");
        assertEquals(1,testContacts.size());
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
        Meeting testMeeting = testContactManager.getFutureMeeting(20040);
        assertEquals(null,testMeeting);
    }

    @Test
         public void testSave() {
        testContactManager.flush();
    }

    @Test
    public void testLoadAddContactSave() {
        if (testContactManager.getContacts("Bailey") == null) {
            testContactManager.addNewContact("David Bailey", "Why did I think of him?");
        }
        testContactManager.flush();
    }

    @Test
    public void testFutureLoadAddMeetingSave() {
        if (testContactManager.getMeeting(3) == null) {
            testContactManager.addFutureMeeting(testContactManager.getContacts("John"),new GregorianCalendar(2018,11,10,10,45,00));
        }
        testContactManager.flush();
    }

    @Test
    public void testGetFutureMeetingByContactId() {
        Set<Contact> contacts = testContactManager.getContacts(1);
        Contact john = new ContactImpl("John", "blah", 1);
        List<Meeting> test = testContactManager.getFutureMeetingList(john);
        assertEquals(4,test.size());
    }

    @Test
    public void testGetFutureMeetingByDate() {
        Calendar testDt = new GregorianCalendar(2016,0,10);
        List<Meeting> test = testContactManager.getFutureMeetingList(testDt);
        assertEquals(2,test.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFutureMeetingByContactIdNoExist() {
        Set<Contact> contacts = testContactManager.getContacts(45);

    }

}
