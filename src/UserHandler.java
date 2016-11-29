import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.*;

class UserHandler extends DefaultHandler {
    /**
     * This class is the handles parsing for Query1. It basically helps
     * build up set of publications depending upon what search type it is.
     */
    private DBManager DB;
    /**
     * nameOrTitle :- query 1 search for publications by author name or title
     * queryType :- 0 for search by author name, 1 for search by title
     * sortType :-  0 -> sort by reverse date
     *              1 -> sort by relevance
     *              2 -> results since a given year
     *              3 -> result in between two years
     */
    private LinkedHashSet<String> nameSetOrTitleSet;
    private int queryType;
    private int sortType;

    private boolean bPerson;
    private boolean bTitle;
    private boolean bPages;
    private boolean bYear;
    private boolean bVolume;
    private boolean bJournalOrBookTitle;
    private boolean bURL;

    private boolean isPerson;

    private HashSet<String> currentPubAuthors = new HashSet<>();
    private String name;
    private String title;
    private String pages;
    private String year;
    private String volume;
    private String journalOrBookTitle;
    private String URL;

    private int prevMatchCountAuthor;
    private int matchCountAuthor;
    private int matchCountTitle;

    private String pubType;

    private boolean foundPublication;

    private String aName;
    private LinkedHashSet<String> aNameParts;

    public UserHandler(DBManager OuterDB, LinkedHashSet<String> i_nameSetOrTitleSet, int i_queryType){
        DB = OuterDB;
        nameSetOrTitleSet = i_nameSetOrTitleSet;
        queryType = i_queryType;
        prevMatchCountAuthor = -1;
        matchCountAuthor = 0;
        matchCountTitle = 0;

        if(queryType == 0){
            aName = nameSetOrTitleSet.iterator().next();
            aNameParts = new LinkedHashSet<>();
            StringTokenizer st = new StringTokenizer(aName, " -");
            while (st.hasMoreTokens()) {
                aNameParts.add(st.nextToken().toLowerCase());
            }
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        if(qName.equals("author") || qName.equals("editor")){
            matchCountAuthor = 0;
            bPerson = true;
            name = "";
        }
        if(qName.equals("title")){
            matchCountTitle = 0;
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
        if(qName.equals("url")){
            bURL = true;
            URL = "";
        }
        if((atts.getLength()) > 0){
            if((atts.getValue("key") != null)){
                pubType = qName;
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException{
        if(queryType == 0){
            if(qName.equals("author") || qName.equals("editor")){
                currentPubAuthors.add(name);

                if(nameSetOrTitleSet.contains(name.toLowerCase())){
                    if(aName.equals(name.toLowerCase())){
                        prevMatchCountAuthor = 1001;
                    }
                    else {
                        prevMatchCountAuthor = 1000;
                    }
                    foundPublication = true;
                }
                else {
                    LinkedHashSet<String> nameParts = new LinkedHashSet<>();
                    StringTokenizer st2 = new StringTokenizer(name, " -");
                    while (st2.hasMoreTokens()) {
                        nameParts.add(st2.nextToken().toLowerCase());
                    }

                    for (String subPart : aNameParts) {
                        if (nameParts.contains(subPart) && subPart.length() >= 3) {
                            matchCountAuthor++;
                            foundPublication = true;
                        }
                    }
                }
                if(matchCountAuthor > prevMatchCountAuthor){
                    prevMatchCountAuthor = matchCountAuthor;
                }
                bPerson = false;
            }

            if(qName.equals(pubType)){
                if(foundPublication == true){
                    HashSet<String> authors = new HashSet<>();
                    for(String aName : currentPubAuthors){
                        authors.add(aName);
                    }
                    Publication tempPublication = new Publication(authors, title, pages, year, volume, journalOrBookTitle, URL);
                    tempPublication.setMatchCount(prevMatchCountAuthor);
                    prevMatchCountAuthor = -1;
                    DB.addPublication(tempPublication);
                    foundPublication = false;
                }
                currentPubAuthors.clear();
            }

            if(qName.equals("title")){
                bTitle = false;
            }
        }
        else if(queryType == 1){
            if(qName.equals("author") || qName.equals("editor")){
                currentPubAuthors.add(name);
                bPerson = false;
            }
            if(qName.equals("title")){
                LinkedHashSet<String> titleParts = new LinkedHashSet<>();
                StringTokenizer st = new StringTokenizer(title, " .(),:;?[]{}_+=!@#$%^&*|'-");
                while (st.hasMoreTokens()){
                    titleParts.add(st.nextToken().toLowerCase());
                }
                for(String tName : nameSetOrTitleSet){
                    LinkedHashSet<String> tNameParts = new LinkedHashSet<>();
                    StringTokenizer st2 = new StringTokenizer(tName, " .(),:;?[]{}_+=!@#$%^&*|'-");
                    while(st2.hasMoreTokens()){
                        tNameParts.add(st2.nextToken().toLowerCase());
                    }
                    for(String subPart : tNameParts){
                        if(titleParts.contains(subPart)){
                            foundPublication = true;
                            matchCountTitle++;
                        }
                    }
                }
                bTitle = false;
            }
            if(qName.equals(pubType)){
                if(foundPublication == true){
                    HashSet<String> authors = new HashSet<>();
                    for(String aName : currentPubAuthors){
                        authors.add(aName);
                    }
                    Publication tempPublication = new Publication(authors, title, pages, year, volume, journalOrBookTitle, URL);
                    tempPublication.setMatchCount(matchCountTitle);
                    DB.addPublication(tempPublication);
                    foundPublication = false;
                }
                currentPubAuthors.clear();
            }
        }
        if(qName.equals("pages")){
            bPages = false;
        }
        if(qName.equals("year")){
            bYear = false;
        }
        if(qName.equals("volume")){
            bVolume = false;
        }
        if(qName.equals("journal") || qName.equals("booktitle")){
            bJournalOrBookTitle = false;
        }
        if(qName.equals("url")){
            bURL = false;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length){
        if(bPerson){
            name += new String(ch, start, length);
        }
        if(bTitle){
            title += new String(ch, start, length);
        }
        if(bPages){
            pages += new String(ch, start, length);
        }
        if(bYear){
            year += new String(ch, start, length);
        }
        if(bVolume){
            volume += new String(ch, start, length);
        }
        if(bJournalOrBookTitle){
            journalOrBookTitle += new String(ch, start, length);
        }
        if(bURL){
            URL += new String(ch, start, length);
        }
    }

}