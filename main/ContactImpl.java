package main;

/**
 * Created by John on 07/03/2015.
 */
public class ContactImpl implements Contact {
    private int Id;
    private String Name;
    private String Notes;

    public ContactImpl(String name, String notes, int id) {
       if ((name == null) || (notes == null)) {
           throw new NullPointerException("You can't pass a null value into the constructor for a contact.");
       } else {
           this.Name = name;
           this.Notes = notes;
           this.Id = id;
       }
    }

    /**
     * Returns the ID of the contact.
     *
     * @return the ID of the contact.
     */
    public int getId() {
        return this.Id;
    }
    /**
     * Returns the name of the contact.
     *
     * @return the name of the contact.
     */
    public String getName(){
        return this.Name;
    };
    /**
     * Returns our notes about the contact, if any.
     *
     * If we have not written anything about the contact, the empty
     * string is returned.
     *
     * @return a string with notes about the contact, maybe empty.
     */
    public String getNotes(){
        return this.Notes;
    };
    /**
     * Add notes about the contact.
     *
     * @param note the notes to be added
     */
    public void addNotes(String note) {
        if (note == null) {
          throw new IllegalArgumentException("You cannot enter null as the notes value");
        }
        this.Notes = note;
    };
}
