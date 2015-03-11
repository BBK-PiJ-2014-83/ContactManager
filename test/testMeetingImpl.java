package test;

import main.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class testMeetingImpl {
    FutureMeetingImpl testFutureMeeting;
    PastMeetingImpl testPastMeeting;
    Calendar meetingDate;
    Set<Contact> invitees;
    Contact john;
    Contact uninvited;
    @Before
    public void before() {
        meetingDate = new GregorianCalendar(2015, 04, 02);
        invitees = new HashSet<Contact>();
        john = new ContactImpl("John Spear","testing 1", 2);
        uninvited = new ContactImpl("Persona NonGrata","We don't like him, he's not invited",1);
        invitees.add(john);
        invitees.add(new ContactImpl("David Smith","testing 2", 3));

        testFutureMeeting = new FutureMeetingImpl(3, meetingDate,invitees);
        testPastMeeting = new PastMeetingImpl(3,meetingDate,invitees,"These are the notes from the meeting");
    }

    @Test
    public void test_FutureGetDate() {
        assertTrue(testFutureMeeting.getDate().equals(meetingDate));
    }

    @Test
    public void GetNotesPast() {
        assertEquals("These are the notes from the meeting", testPastMeeting.getNotes());
    }
    @Test
    public void GetIdPast() {
        assertEquals(3, testPastMeeting.getId());
    }

    @Test
    public void GetIdFuture() {
        assertEquals(3, testFutureMeeting.getId());
    }

    @Test
    public void getAttendees() {
        assertTrue(testFutureMeeting.getContacts().contains(john));
    }

    @Test
    public void testNotOnContactList() {
        //This person isn't on the contacts list so it should return false
        assertFalse(testFutureMeeting.getContacts().contains(uninvited));
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullValPassedInException() {
        //Shouldn't work because the date is in the past.
        Calendar pastDate = new GregorianCalendar(2001,10, 5);
        Meeting futureMeet = new MeetingImpl(4,pastDate,invitees);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullConstructor() {
        //The set needs to contain at least one person
        Set<Contact> emptySet = new HashSet<Contact>();
        Meeting futureMeet = new MeetingImpl(4,meetingDate,emptySet);
    }

}
