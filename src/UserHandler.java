import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.*;

class UserHandler extends DefaultHandler {
    private DBManager DB;
    /**
     * nameOrTitle :- query 1 search for publications by author name or title
     * queryType :- 0 for search by author name, 1 for search by title
     * sortType :-  0 -> sort by reverse date
     *              1 -> sort by relevance
     *              2 -> results since a given year
     *              3 -> result in between two years
     */
    private HashSet<String> nameSetOrTitleSet;
    private int queryType;
    private int sortType;

    private boolean bPerson;
    private boolean bTitle;
    private boolean bPages;
    private boolean bYear;
    private boolean bVolume;
    private boolean bJournalOrBookTitle;

    private boolean isPerson;

    private HashSet<String> currentPubAuthors = new HashSet<>();
    private HashSet<String> currentSearchList = new HashSet<>();
    private String name;
    private String title;
    private String pages;
    private String year;
    private String volume;
    private String journalOrBookTitle;
    private int matchCountAuthor;
    private int matchCountTitle;

    private String pubType;

    private boolean foundPublication;

    public UserHandler(DBManager OuterDB, HashSet<String> i_nameSetOrTitleSet, int i_queryType){
        DB = OuterDB;
        nameSetOrTitleSet = i_nameSetOrTitleSet;
        queryType = i_queryType;
        matchCountAuthor = 0;
        matchCountTitle = 0;
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

                HashSet<String> nameParts = new HashSet<>();
                StringTokenizer st2 = new StringTokenizer(name, " -");
                while(st2.hasMoreTokens()){
                    nameParts.add(st2.nextToken().toLowerCase());
                }

                for(String aName : nameSetOrTitleSet){
                    HashSet<String> aNameParts = new HashSet<>();
                    StringTokenizer st = new StringTokenizer(aName, " -");
                    while(st.hasMoreTokens()){
                        aNameParts.add(st.nextToken().toLowerCase());
                    }

                    for(String subPart : aNameParts){
                        if(nameParts.contains(subPart)){
                            matchCountAuthor++;
                            foundPublication = true;
                        }
                    }
                }
            }

            if(qName.equals(pubType)){
                if(foundPublication == true){
                    HashSet<String> authors = new HashSet<>();
                    for(String aName : currentPubAuthors){
                        authors.add(aName);
                    }
                    Publication tempPublication = new Publication(authors, title, pages, year, volume, journalOrBookTitle);
                    tempPublication.setMatchCount(matchCountAuthor);
                    DB.addPublication(tempPublication);
                    foundPublication = false;
                }
                currentPubAuthors.clear();
            }
        }
        else if(queryType == 1){
            if(qName.equals("author") || qName.equals("editor")){
                currentPubAuthors.add(name);
            }
            if(qName.equals("title")){
                HashSet<String> titleParts = new HashSet<>();
                StringTokenizer st = new StringTokenizer(title, " .(),:;?[]{}_+=!@#$%^&*|'-");
                while (st.hasMoreTokens()){
                    titleParts.add(st.nextToken().toLowerCase());
                }
                for(String tName : nameSetOrTitleSet){
                    HashSet<String> tNameParts = new HashSet<>();
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
            }
            if(qName.equals(pubType)){
                if(foundPublication == true){
                    HashSet<String> authors = new HashSet<>();
                    for(String aName : currentPubAuthors){
                        authors.add(aName);
                    }
                    Publication tempPublication = new Publication(authors, title, pages, year, volume, journalOrBookTitle);
                    tempPublication.setMatchCount(matchCountTitle);
                    DB.addPublication(tempPublication);
                    foundPublication = false;
                }
                currentPubAuthors.clear();
            }
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

}