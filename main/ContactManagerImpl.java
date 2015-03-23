package main;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
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
    static String filename  = "contactmanager";

    public ContactManagerImpl() {
        contacts = new HashSet<Contact>();
        meetings = new ArrayList<Meeting>();
        pastMeetings = new ArrayList<PastMeeting>();
        file = new XmlFile();
        loadFile("contactmanager");
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
        for(Contact contact : contacts) {
            //try to get each contact by id - will throw an exception if they don't exist.
            getContacts(contact.getId());
        }
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
        if (getMeetingById(id) != null) {
            return getMeetingById(id);
        } else if (getPastMeetingById(id) != null) {
            return getPastMeetingById(id);
        } else {
            return null;
        }
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
    public List<Meeting> getFutureMeetingList(Contact contact) throws IllegalArgumentException {
        if (getContacts(contact.getId()).size() == 0) {
            throw new IllegalArgumentException("This contact doesn't exist!");
        }
        List<FutureMeetingImpl> tmpMeetings = new ArrayList<FutureMeetingImpl>();
        for(Meeting meeting : meetings) {
            Set<Contact> tmpContacts = meeting.getContacts();
            for(Contact tmpContact : tmpContacts) {
                if (contact.getId() == tmpContact.getId()) {
                    tmpMeetings.add((FutureMeetingImpl) meeting);
                    break;
                }
            }
        }
        //Now sort the meetings chronologically
        Collections.sort(tmpMeetings);
        List<Meeting> rtnList = new ArrayList<Meeting>();
        for(FutureMeetingImpl tmpMeeting: tmpMeetings) {
            rtnList.add(tmpMeeting);
        }

       return rtnList;
    }
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
       List<FutureMeetingImpl> tmpMeetings = new ArrayList<FutureMeetingImpl>();

       for(Meeting meeting : meetings) {
            if (setStartOfDay(date).equals(setStartOfDay(meeting.getDate()))) {
                tmpMeetings.add((FutureMeetingImpl) meeting);
            }
       }

       //Now sort the meetings chronologically
       Collections.sort(tmpMeetings);
       List<Meeting> rtnList = new ArrayList<Meeting>();
       for(FutureMeetingImpl tmpMeeting: tmpMeetings) {
           rtnList.add(tmpMeeting);
       }
       return rtnList;
   }
    /**
     * A private helper method to help compare dates by setting it to beggining of the day
     *
     * @param date Date with time
     * @return The date with the time set to 0
     */
    private Calendar setStartOfDay(Calendar date) {
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        return date;
    }
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
    public List<PastMeeting> getPastMeetingList(Contact contact) throws IllegalArgumentException {
        if (getContacts(contact.getId()).size() == 0) {
            throw new IllegalArgumentException("This contact doesn't exist!");
        }
        List<PastMeeting> rtnList = new ArrayList<PastMeeting>();
        for(PastMeeting meeting : pastMeetings) {
            Set<Contact> tmpContacts = meeting.getContacts();
            for(Contact tmpContact : tmpContacts) {
                if (contact.getId() == tmpContact.getId()) {
                    rtnList.add(meeting);
                    break;
                }
            }
        }
        return rtnList;
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
        Calendar timeNow = GregorianCalendar.getInstance();
        if (getMeeting(id) == null) {
            throw new IllegalArgumentException("Meeting does not exist");
        } else if (getMeeting(id).getDate().compareTo(timeNow) > 0) {
            throw new IllegalStateException("Meeting is set to a date in the future!");
        } else if (text == null){
            throw new NullPointerException("Notes are null");
        } else {
            PastMeetingImpl tmpMeeting = (PastMeetingImpl) getPastMeetingById(id);
            tmpMeeting.setNotes(text);
        }
    }
    /**
     * Create a new contact with the specified name and notes.
     *
     * @param name the name of the contact.
     * @param notes notes to be added about the contact.
     * @throws NullPointerException if the name or the notes are null
     */
    public void addNewContact(String name, String notes) throws NullPointerException{
        contacts.add(new ContactImpl(name,notes,getLargestId(false)));
    }
    /**
     * Returns a list containing the contacts that correspond to the IDs.
     *
     * @param ids an arbitrary number of contact IDs
     * @return a list containing the contacts that correspond to the IDs.
     * @throws IllegalArgumentException if any of the IDs does not correspond to a real contact
     */
    public Set<Contact> getContacts(int... ids) throws IllegalArgumentException{
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
        try {
            DocumentBuilderFactory docBuilder = DocumentBuilderFactory.newInstance();
            DocumentBuilder dbuilder = docBuilder.newDocumentBuilder();
            Document meetingData = dbuilder.newDocument();
            Element rootElement = meetingData.createElement("contactManager");
            meetingData.appendChild(rootElement);
            Element contactXML =  meetingData.createElement("contacts");
            //First contacts
            for (Contact contact : contacts) {
                Element tmpContact =  meetingData.createElement("contact");
                file.createNode(meetingData,"id",Integer.toString(contact.getId()), tmpContact);
                file.createNode(meetingData,"name",contact.getName(),tmpContact);
                file.createNode(meetingData,"notes",contact.getNotes(),tmpContact);
                contactXML.appendChild(tmpContact);
            }
            rootElement.appendChild(contactXML);
            //now Create future meeting nodes
            Element futureXML =  meetingData.createElement("futureMeetings");
            for (Meeting meeting : meetings) {
                //Convert to a temp meeting impl so you can use the getContactsAsString method
                MeetingImpl tmpMeetingImpl = (MeetingImpl) meeting;
                Element tmpMeeting =  meetingData.createElement("futureMeeting");
                file.createNode(meetingData,"id",Integer.toString(meeting.getId()),tmpMeeting);
                file.createNode(meetingData,"contacts",tmpMeetingImpl.getContactsAsString(), tmpMeeting);
                file.createNode(meetingData,"date",df.format(meeting.getDate().getTime()),tmpMeeting);
                futureXML.appendChild(tmpMeeting);
            }
            rootElement.appendChild(futureXML);
            //Now past meetings
            Element pastXML =  meetingData.createElement("pastMeetings");
            for (PastMeeting meeting : pastMeetings) {
                //Convert to a temp meeting impl so you can use the getContactsAsString method
                PastMeetingImpl tmpMeetingImpl = (PastMeetingImpl) meeting;
                Element tmpMeeting =  meetingData.createElement("pastMeeting");
                file.createNode(meetingData,"id",Integer.toString(meeting.getId()),tmpMeeting);
                file.createNode(meetingData,"contacts",tmpMeetingImpl.getContactsAsString(), tmpMeeting);
                file.createNode(meetingData,"date",df.format(meeting.getDate().getTime()),tmpMeeting);
                file.createNode(meetingData,"notes",tmpMeetingImpl.getNotes(),tmpMeeting);
                pastXML.appendChild(tmpMeeting);
            }
            rootElement.appendChild(pastXML);
            System.out.println((file.saveFile(filename,meetingData)) ? "File saved!" : "File save didn't work!");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

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

    /**
     * A helper method to turn an array of strings into an array of ints. Needed for contacts.
     * @param input The input array of strings
     * @return The converted array of integers
     */
    public static int[] stringToInt(String[] input) {
        int[] numbers = new int[input.length];
        for (int i = 0; i < input.length; i++) {
            numbers[i] = Integer.parseInt(input[i]);
        }
        return numbers;
    }

    /**
     * Load the file and populate the contacts and meetings
     * @param fileName Enables you to specify a filename - good for testing.
     */
    public void loadFile(String fileName) {
        meetingData = file.readFile(fileName);
        loadContacts();
        try {
            //load past meetings
            loadMeetings(true);
            //load future meetings
            loadMeetings(false);
        } catch (ParseException e) {
            System.out.println("There has been a parsing error when reading in the meetings. Please make sure the dates are in the correct format.");
        }
    }

    /**
     * Populate the contact list from the file
     */
    public void loadContacts(){
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

    /**
     * Populate the meeting lists from the file
     * @param past if set to true then this populates past meetings, otherwise it populates future meetings
     */
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
            //All the data is gathered - create a new future or past meeting
            if (past) {
                pastMeetings.add(new PastMeetingImpl(id,meetDate,meetContacts,notes));
            } else {
                meetings.add(new FutureMeetingImpl(id,meetDate,meetContacts));
            }

        }

    }
}
