package main;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Set;

public class MeetingImpl implements Meeting {
    private int Id;
    private Calendar Date;
    private Set<Contact> Contacts;

    public MeetingImpl(int id,Calendar date, Set<Contact> contacts, boolean future){
        Calendar timeNow = GregorianCalendar.getInstance();
        if ((future) && (date.compareTo(timeNow) < 0)) {
            throw new IllegalArgumentException("This meeting is in the past! Create a PastMeeting instead.");
        } else if ((!future) && ((date.compareTo(timeNow) > 0))) {
            throw new IllegalArgumentException("This meeting is in the future! Create a FutureMeeting instead.");
        } else if (contacts.size() == 0) {
            throw new IllegalArgumentException("A meeting must have attendees");
        }
        this.Id = id;
        this.Date = date;
        this.Contacts = contacts;
    }
    /**
     * Returns the id of the meeting.
     *
     * @return the id of the meeting.
     */
    public int getId() {
        return this.Id;
    }
    /**
     * Return the date of the meeting.
     *
     * @return the date of the meeting.
     */
    public Calendar getDate(){
        return this.Date;
    }
    /**
     * Return the details of people that attended the meeting.
     *
     * The list contains a minimum of one contact (if there were
     * just two people: the user and the contact) and may contain an
     * arbitraty number of them.
     *
     * @return the details of people that attended the meeting.
     */
    public Set<Contact> getContacts() {
        return this.Contacts;
    }

    public String getContactsAsString() {
        String returnStr = "";
        for(Contact contact : Contacts) {
            returnStr += contact.getId() + ",";
        }
        return returnStr.substring(0, returnStr.lastIndexOf(","));
    }
}
