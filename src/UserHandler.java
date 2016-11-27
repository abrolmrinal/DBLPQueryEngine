import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

class UserHandler extends DefaultHandler {
    DBManager DB;

    private boolean bPerson;
    private boolean bTitle;
    private boolean bPages;
    private boolean bYear;
    private boolean bVolume;
    private boolean bJournalOrBookTitle;

    private List<Person> currentPubAuthors= new ArrayList<>();
    private String name;
    private String title;
    private String pages;
    private String year;
    private String volume;
    private String journalOrBookTitle;
    private String mDate;

    private String pubType;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        if(qName.equals("author") || qName.equals("editor")){
            bPerson = true;
            name = "";
        }
        if(qName.equals("title")){
            bTitle = true;
            title = "";
        }
        if(qName.equals("pages")){
            bPages = true;
            pages = "";
        }
        if(qName.equals("year")){
            bYear = true;
            year = "";
        }
        if(qName.equals("volume")){
            bVolume = true;
            volume = "";
        }
        if(qName.equals("journal")){
            bJournalOrBookTitle = true;
            journalOrBookTitle = "";
        }
        if(qName.equals("booktitle")){
            bJournalOrBookTitle = true;
            journalOrBookTitle = "";
        }
        if((atts.getLength()) > 0){
            if((atts.getValue("key") != null) && (atts.getValue("mdate") != null)){
                pubType = qName;
                mDate = atts.getValue("mdate");
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException{
        if(qName.equals("author") || qName.equals("editor")){
            Person tempPerson = DB.personExist(name);
            if(tempPerson == null) {
                tempPerson = new Person(name);
            }
            currentPubAuthors.add(tempPerson);
        }
        if(qName.equals(pubType) && !(qName.equals("www"))){
            List<Person> authors = new ArrayList<Person>();
            for(Person p : currentPubAuthors){
                authors.add(p);
            }
            currentPubAuthors.clear();
            Publication tempPublication = new Publication(authors, title, pages, year, volume, journalOrBookTitle, mDate);
            DB.addPublication(tempPublication);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length){
        if(bPerson){
            name += new String(ch, start, length);
            bPerson = false;
        }
        if(bTitle){
            title += new String(ch, start, length);
            bTitle = false;
        }
        if(bPages){
            pages += new String(ch, start, length);
            bPages = false;
        }
        if(bYear){
            year += new String(ch, start, length);
            bYear = false;
        }
        if(bVolume){
            volume += new String(ch, start, length);
            bVolume = false;
        }
        if(bJournalOrBookTitle){
            journalOrBookTitle += new String(ch, start, length);
            bJournalOrBookTitle = false;
        }
    }

    public UserHandler(DBManager OuterDB){
        DB = OuterDB;
    }
}