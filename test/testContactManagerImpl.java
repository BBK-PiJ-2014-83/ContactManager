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
        contacts.add(new ContactImpl("james Buchanon", "No data about him",4));

    }

    //Add a new contact then check they are in there
    @Test
    public void addContactWorks() {
        testContactManager.addNewContact("Jim Smith", "The bald eagle");
        Set<Contact> testContacts =  testContactManager.getContacts(1);
        assertEquals(testContacts.size(),1);
    }
    //Add multiple contacts then checkthey are in there
    @Test
    public void addMultipleContacts() {
        testContactManager.addNewContact("Jim Smith", "The bald eagle");
        testContactManager.addNewContact("John Smith", "yes");
        Set<Contact> testContacts =  testContactManager.getContacts(1,2);
        assertEquals(testContacts.size(),2);
    }

    //Try to get a list of contacts from string split to array (which is what will happen when coming from a file)
    @Test
    public void getContactsSplit() {
        testContactManager.addNewContact("Jim Smith", "The bald eagle");
        testContactManager.addNewContact("John Smith", "yes");
        String[] test= "1,2".split(",");
        Set<Contact> testContacts =  testContactManager.getContacts(ContactManagerImpl.stringToInt(test));
        assertEquals(testContacts.size(),2);
    }
    //Try to get a contact that doesn't exist
    @Test(expected = IllegalArgumentException.class)
    public void getContactNotExist() {
        Set<Contact> testContacts =  testContactManager.getContacts(4);
    }

    //Try to get a contact by String that exista
    @Test
    public void getContactByStringExists() {
        testContactManager.addNewContact("Jim Smith", "The bald eagle");
        Set<Contact> testContacts =  testContactManager.getContacts("Jim");
    }
    //Try to get a contact with a null string
    @Test(expected = NullPointerException.class)
    public void GetContactByStringNullArgument() {
        testContactManager.getContacts("");
    }

    //Try to add a contact with a null argument
    @Test(expected = NullPointerException.class)
    public void addContactNullArgument() {
        testContactManager.addNewContact(null, "The bald eagle");
    }

    //This is going to try to add a future meeting that is set in the past Should fail.
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
    //Add a past meeting and then get it from the list should work
    @Test
    public void getPastMeetingExists(){
        meetingDate = new GregorianCalendar(2003, 06, 10);
        testContactManager.addNewPastMeeting(contacts, meetingDate, "This was a dull dull meeting");
        assertEquals(1,testContactManager.getPastMeeting(1).getId());
    }
    //Add a past meeting with a null argument. Should fail.
    @Test(expected = NullPointerException.class)
    public void addPastMeetingNullArgument() {
        meetingDate = new GregorianCalendar(2012, 06, 10);
        testContactManager.addNewPastMeeting(null, meetingDate,"This was a dull dull meeting");
    }




}
