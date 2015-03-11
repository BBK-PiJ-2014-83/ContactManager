package main;

import java.util.Calendar;
import java.util.Set;

/**
 * Created by John on 11/03/2015.
 */
public class FutureMeetingImpl extends MeetingImpl implements FutureMeeting {
    public FutureMeetingImpl(int id,Calendar date, Set<Contact> contacts){
        super(id,date,contacts,true);
    }
    /**
     * Returns the id of the meeting.
     *
     * @return the id of the meeting.
     */
    public int getId() {
        return super.getId();
    }
    /**
     * Return the date of the meeting.
     *
     * @return the date of the meeting.
     */
    public Calendar getDate(){
        return super.getDate();
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
        return super.getContacts();
    }

}
