package test;

import main.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
public class testContactManagerImpl {
    ContactManagerImpl testContactManager;
    Set<Contact> contacts;
    Calendar meetingDate;
    @Before
    public void before() {
        testContactManager = new ContactManagerImpl();
        contacts = new HashSet<Contact>();
        contacts.add(new ContactImpl("John Spear", "He's pretty busy at the moment.",3));
        contacts.add(new ContactImpl("james Buchanon", "No data about him",3));

    }
    //This is going to try to add a future meeting that is set in the past
    @Test(expected = IllegalArgumentException.class)
    public void addFutureMeetingPast() {
        meetingDate = new GregorianCalendar(2012, 06, 10);
        int id = testContactManager.addFutureMeeting(contacts, meetingDate);
    }
    //This is going to try to add a future meeting where the contact isn't in the list
   // @Test(expected = IllegalArgumentException.class)
   // public void addFutureMeetingNonContact() {
        //can't do yet as there are no contacts
        //meetingDate = new GregorianCalendar(2012, 06, 10);
       // int id = testContactManager.addFutureMeeting(contacts, meetingDate);
    //}

    //Add a future meeting and then get it from the list. Should work.
    @Test
    public void getFutureMeetingExists(){
        meetingDate = new GregorianCalendar(2018, 06, 10);
        FutureMeetingImpl dummyMeeting = new FutureMeetingImpl(1,meetingDate,contacts);
        testContactManager.addFutureMeeting(contacts,meetingDate);
        assertEquals(dummyMeeting.getId(),testContactManager.getFutureMeeting(1).getId());
    }

    @Test
    public void getPastMeetingExists(){
        meetingDate = new GregorianCalendar(2003, 06, 10);
        testContactManager.addNewPastMeeting(contacts, meetingDate, "This was a dull dull meeting");
        assertEquals(1,testContactManager.getPastMeeting(1).getId());
    }

    @Test(expected = NullPointerException.class)
    public void addPastMeetingNullArgument() {
        meetingDate = new GregorianCalendar(2012, 06, 10);
        testContactManager.addNewPastMeeting(null, meetingDate,"This was a dull dull meeting");
    }



}
