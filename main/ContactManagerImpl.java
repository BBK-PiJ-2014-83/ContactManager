package main;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ContactManagerImpl implements ContactManager{
    Set<Contact> contacts;
    List<Meeting> meetings;
    List<PastMeeting> pastMeetings;
    XmlFile file;

    Document meetingData;
    public static DateFormat df = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
    private boolean past;

    public ContactManagerImpl() {
        contacts = new HashSet<Contact>();
        meetings = new ArrayList<Meeting>();
        pastMeetings = new ArrayList<PastMeeting>();
        file = new XmlFile();

    }

    /**
     * Add a new meeting to be held in the future.
     *
     * @param contacts a list of contacts that will participate in the meeting
     * @param date the date on which the meeting will take place
     * @return the ID for the meeting
     * @throws IllegalArgumentException if the meeting is set for a time in the past,
     * of if any contact is unknown / non-existent
     */
    public int addFutureMeeting(Set<Contact> contacts, Calendar date) throws IllegalArgumentException {
        int id = getLargestId(true);
        meetings.add(new FutureMeetingImpl(id ,date,contacts));
        return id;
    }
    /**
     * Returns the PAST meeting with the requested ID, or null if it there is none.
     *
     * @param id the ID for the meeting
     * @return the meeting with the requested ID, or null if it there is none.
     * @throws IllegalArgumentException if there is a meeting with that ID happening in the future
     */
    public PastMeeting getPastMeeting(int id) {
        if(getMeetingById(id) != null){
            throw new IllegalArgumentException("This meeting already exists in the future meetings list.");
        }
        return getPastMeetingById(id);
    }
    /**
     * Returns the FUTURE meeting with the requested ID, or null if there is none.
     *
     * @param id the ID for the meeting
     * @return the meeting with the requested ID, or null if it there is none.
     * @throws IllegalArgumentException if there is a meeting with that ID happening in the past
     */
    public FutureMeeting getFutureMeeting(int id) {
        if(getPastMeetingById(id) != null){
            throw new IllegalArgumentException("This meeting already exists in the past meetings list.");
        }
        return getMeetingById(id);
    }
    /**
     * Returns the meeting with the requested ID, or null if it there is none.
     *
     * @param id the ID for the meeting
     * @return the meeting with the requested ID, or null if it there is none.
     */
    public Meeting getMeeting(int id) {
        return new FutureMeetingImpl(1,new GregorianCalendar(2000,01,01),contacts);
    };
    /**
     * Returns the list of future meetings scheduled with this contact.
     *
     * If there are none, the returned list will be empty. Otherwise,
     * the list will be chronologically sorted and will not contain any
     * duplicates.
     *
     * @param contact one of the user’s contacts
     * @return the list of future meeting(s) scheduled with this contact (maybe empty).
     * @throws IllegalArgumentException if the contact does not exist
     */
    public List<Meeting> getFutureMeetingList(Contact contact) {
       return meetings;
    };
    /**
     * Returns the list of meetings that are scheduled for, or that took
     * place on, the specified date
     *
     * If there are none, the returned list will be empty. Otherwise,
     * the list will be chronologically sorted and will not contain any
     * duplicates.
     *
     * @param date the date
     * @return the list of meetings
     */
   public List<Meeting> getFutureMeetingList(Calendar date) {
       return meetings;
   };
    /**
     * Returns the list of past meetings in which this contact has participated.
     *
     * If there are none, the returned list will be empty. Otherwise,
     * the list will be chronologically sorted and will not contain any
     * duplicates.
     *
     * @param contact one of the user’s contacts
     * @return the list of future meeting(s) scheduled with this contact (maybe empty).
     * @throws IllegalArgumentException if the contact does not exist
     */
    public List<PastMeeting> getPastMeetingList(Contact contact) {
        List<PastMeeting> test = new ArrayList<PastMeeting>();
        return test;
    }
    /**
     * Create a new record for a meeting that took place in the past.
     *
     * @param contacts a list of participants
     * @param date the date on which the meeting took place
     * @param text messages to be added about the meeting.
     * @throws IllegalArgumentException if the list of contacts is
     * empty, or any of the contacts does not exist
     * @throws NullPointerException if any of the arguments is null
     */
    public void addNewPastMeeting(Set<Contact> contacts, Calendar date, String text) {
        if ((contacts == null) || (date == null) ||  (text == null)) {
            throw new NullPointerException("You can't pass a null value into the constructor for a past meeting.");
        }
        pastMeetings.add(new PastMeetingImpl(getLargestId(true),date,contacts,text));
    }
    /**
     * Add notes to a meeting.
     *
     * This method is used when a future meeting takes place, and is
     * then converted to a past meeting (with notes).
     *
     * It can be also used to add notes to a past meeting at a later date.
     *
     * @param id the ID of the meeting
     * @param text messages to be added about the meeting.
     * @throws IllegalArgumentException if the meeting does not exist
     * @throws IllegalStateException if the meeting is set for a date in the future
     * @throws NullPointerException if the notes are null
     */
    public void addMeetingNotes(int id, String text) {

    }
    /**
     * Create a new contact with the specified name and notes.
     *
     * @param name the name of the contact.
     * @param notes notes to be added about the contact.
     * @throws NullPointerException if the name or the notes are null
     */
    public void addNewContact(String name, String notes){
        contacts.add(new ContactImpl(name,notes,getLargestId(false)));
    }
    /**
     * Returns a list containing the contacts that correspond to the IDs.
     *
     * @param ids an arbitrary number of contact IDs
     * @return a list containing the contacts that correspond to the IDs.
     * @throws IllegalArgumentException if any of the IDs does not correspond to a real contact
     */
    public Set<Contact> getContacts(int... ids){
       Set<Contact> contactSet = new HashSet<Contact>();
        for(int i: ids) {
            Optional<Contact> person =  contacts.stream().filter(x -> (x.getId() == i)).findFirst();
            if (person.isPresent()) {
                contactSet.add(person.get());
            } else {
                throw new IllegalArgumentException("The user with id :" + i + " does not exist");
            }
       }
        return contactSet;
    }
    /**
     * Returns a list with the contacts whose name contains that string.
     *
     * @param name the string to search for
     * @return a list with the contacts whose name contains that string.
     * @throws NullPointerException if the parameter is null
     */
    public Set<Contact> getContacts(String name) {
        if ((name == null) || (name == ""))
            throw new NullPointerException("You cannot search for a contact with an empty string!");
        final Set<Contact> contactSet = new HashSet<Contact>();
        //Uses Lamda expressions to see if any names contain the string entered
        contacts.stream().filter(x -> (x.getName().toLowerCase().contains(name.toLowerCase())))
            .forEach(contact -> contactSet.add(contact));

        return contactSet;
    }
    /**
     * Save all data to disk.
     *
     * This method must be executed when the program is
     * closed and when/if the user requests it.
     */
    public void flush(){

    };

    /**
     * Loop through list and bring back the largest id
     * @param meetingList true if this is to get largest meeting id. False if to get largest contact id.
     * @return
     */
    private int getLargestId(boolean meetingList) {
        int largestId = 0;
        if (meetingList) {
            for (Meeting meeting : meetings)
                largestId = (meeting.getId() > largestId) ? meeting.getId() : largestId;
            for (PastMeeting pastMeeting : pastMeetings)
                largestId = (pastMeeting.getId() > largestId) ? pastMeeting.getId() : largestId;
        } else {
            for (Contact contact : contacts)
                largestId = (contact.getId() > largestId) ? contact.getId() : largestId;
        }

        return largestId + 1;
    }

    private FutureMeeting getMeetingById(int id) {
        for (Meeting meeting : meetings)
            if (meeting.getId() == id )
                return (FutureMeeting) meeting;
        return null;
    }

    private PastMeeting getPastMeetingById(int id) {
        for (PastMeeting meeting : pastMeetings)
            if (meeting.getId() == id )
                return meeting;

        return null;
    }

    public void getContacts() {

    }

    public static int[] stringToInt(String[] input) {
        int[] numbers = new int[input.length];
        for (int i = 0; i < input.length; i++) {
            numbers[i] = Integer.parseInt(input[i]);
        }
        return numbers;
    }
    //Load the file and populate the contacts / future meetings / past meetings
    public void loadFile(String fileName) {
        meetingData = file.readFile(fileName);
        loadContacts();
        try {
            loadMeetings(true);
            loadMeetings(false);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    //Populate the contacts from the xml
    public void loadContacts() {
        NodeList contactList = file.getItems("contact", meetingData);

        for (int i = 0; i < contactList.getLength(); i++) {
            int id = 0;
            String notes = "";
            String name = "";
            NodeList contact = contactList.item(i).getChildNodes();
            for (int j = 0; j < contact.getLength(); j++) {
                if (contact.item(j).getNodeType() == Node.ELEMENT_NODE){
                    switch (contact.item(j).getNodeName()) {
                        case "name" :
                            name = contact.item(j).getTextContent();
                            break;
                        case "notes" :
                            notes = contact.item(j).getTextContent();
                            break;
                        case "id" :
                            id = Integer.parseInt(contact.item(j).getTextContent());
                    }

                }
            }
            contacts.add(new ContactImpl(name,notes,id));
        }

    }

    public void loadMeetings(boolean past) throws ParseException {
        NodeList meetingList = file.getItems((past == true ? "pastMeeting" : "futureMeeting"), meetingData);

        for (int i = 0; i < meetingList.getLength(); i++) {
            //Declare inside as they are use in the loop only
            int id = 0;

            String notes = "";
            Set<Contact> meetContacts = null;
            Calendar meetDate = new GregorianCalendar();

            NodeList contact = meetingList.item(i).getChildNodes();
            for (int j = 0; j < contact.getLength(); j++) {
                if (contact.item(j).getNodeType() == Node.ELEMENT_NODE){
                    switch (contact.item(j).getNodeName()) {
                        case "id" :
                            id = Integer.parseInt(contact.item(j).getTextContent());
                            break;
                        case "contacts" :
                            //They are listed as a string of integers. Split to string array then convert to integers and search for contacts.
                            String[] tmpContacts = contact.item(j).getTextContent().split(",");
                            meetContacts = getContacts(ContactManagerImpl.stringToInt(tmpContacts));
                            break;
                        case "date" :
                            meetDate.setTime(df.parse(contact.item(j).getTextContent()));
                            break;
                        case "notes" :
                            notes = contact.item(j).getTextContent();
                    }

                }
            }
            if (past) {
                pastMeetings.add(new PastMeetingImpl(id,meetDate,meetContacts,notes));
            } else {
                meetings.add(new FutureMeetingImpl(id,meetDate,meetContacts));
            }

        }

    }
}
