import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.lang.reflect.Array;
import java.util.*;

/**
 * @author Mrinal Abrol(2015062)
 * @author Shashwat Malik(2015092)
 */

public class QueryHandler {
    /**
     * This class handles Query1, it helps build up a  set of
     * Publications depending upon queryType
     * This set of Publications is further processed depending
     * upon filterType and sortType.
     */
    DBManager DB;
    /**
     * Constants for Query 1
     */
    private final int SEARCH_BY_AUTHOR = 0;
    private final int SEARCH_BY_TITLE = 1;

    private final int SORT_BY_REVERSE_DATE = 0;
    private final int SORT_BY_RELEVANCE = 1;

    private final int NO_FILTER = 0;
    private final int PUBS_SINCE_GIVEN_YEAR = 1;
    private final int PUBS_BETWEEN_TWO_YEARS = 2;

    private int fType;
    private int sType;

    private int sinceYear;

    private int lowerYear;
    private int upperYear;


    public ArrayList<Publication> resultArrayList;

    public void setSinceYear(int sinceYear) {
        this.sinceYear = sinceYear;
    }

    public void setLowerYear(int lowerYear) {
        this.lowerYear = lowerYear;
    }

    public void setUpperYear(int upperYear) {
        this.upperYear = upperYear;
    }

    public ArrayList<Publication> getResultArrayList() {
        return resultArrayList;
    }


    public QueryHandler(DBManager outerDB){
        DB = outerDB;
    }
    /**
     *Query1 Methods
     */
    public void pubSearch_author(String i_name, int filterType, int sortType){
        fType = filterType;
        sType = sortType;
        System.out.println("Starting Query1 --> search by author name: ");
        LinkedHashSet<String> i_nameSet = new LinkedHashSet<>();
        i_nameSet.add(i_name);
        for(String tempKey : DB.getAliasMap().keySet()){
            ArrayList<Person> tempListOfAliasPerson = DB.getAliasMap().get(tempKey);
            ArrayList<String> tempListOfAliasNames = new ArrayList<>();
            for(Person p : tempListOfAliasPerson ){
                tempListOfAliasNames.add(p.getName());
            }
            if(tempListOfAliasNames.contains(i_name)){
                i_nameSet.addAll(tempListOfAliasNames);
            }
        }

        System.out.println("Printing publications for these authors and first/middle/last name matches: ");

        for(String aName : i_nameSet){
            System.out.println(aName);
        }

        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            UserHandler userHandler = new UserHandler(DB, i_nameSet, SEARCH_BY_AUTHOR);
            parser.getXMLReader().setFeature("http://xml.org/sax/features/validation", true);
            parser.parse(DB.dblp, userHandler);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        switch (filterType){
            case NO_FILTER : noFilter();
                break;
            case PUBS_SINCE_GIVEN_YEAR : pubsSinceGivenYear();
                break;
            case PUBS_BETWEEN_TWO_YEARS : pubsBetweenTwoYears();
                break;

        }
    }

    public void pubSearch_title(String i_title, int filterType, int sortType){
        fType = filterType;
        sType = sortType;

        System.out.println("Starting Query1 --> search by title tags: ");
        LinkedHashSet<String> i_titleSet = new LinkedHashSet<>();
        i_titleSet.add(i_title);

        System.out.println("Printing publications with these title tags: ");

        StringTokenizer st = new StringTokenizer(i_title, " .()-,");
        while(st.hasMoreTokens()){
            System.out.println(st.nextToken().toLowerCase());
        }

        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            UserHandler userHandler = new UserHandler(DB, i_titleSet, SEARCH_BY_TITLE);
            parser.getXMLReader().setFeature("http://xml.org/sax/features/validation", true);
            parser.parse(DB.dblp, userHandler);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        switch (filterType){
            case NO_FILTER : noFilter();
                break;
            case PUBS_SINCE_GIVEN_YEAR : pubsSinceGivenYear();
                break;
            case PUBS_BETWEEN_TWO_YEARS : pubsBetweenTwoYears();
                break;

        }
    }

    public void printList(List<Publication> pub){
        int count = 1;
        for(Publication p : pub){
            DB.printPublication(p, count);
            count++;
        }
    }

    public void noFilter(){
        ArrayList<Publication> tempListOfPublications = new ArrayList<>();
        for(Publication tempPub : DB.getSetOfPublications()){
                tempListOfPublications.add(tempPub);
        }
        if(sType == SORT_BY_REVERSE_DATE){
            Collections.sort(tempListOfPublications);
        }

        else if(sType == SORT_BY_RELEVANCE){
            Collections.sort(tempListOfPublications, Publication.matchCountOrder);

        }
        resultArrayList = new ArrayList<>();
        resultArrayList.addAll(tempListOfPublications);
//        printList(tempListOfPublications);
        tempListOfPublications.clear();
    }

    public void pubsSinceGivenYear(){
        ArrayList<Publication> tempListOfPublications = new ArrayList<>();
        for(Publication tempPub : DB.getSetOfPublications()){
            if(tempPub.getYear() >= sinceYear){
                tempListOfPublications.add(tempPub);
            }
        }
        if(sType == SORT_BY_REVERSE_DATE){
            Collections.sort(tempListOfPublications);
        }

        else if(sType == SORT_BY_RELEVANCE){
            Collections.sort(tempListOfPublications, Publication.matchCountOrder);

        }
        resultArrayList = new ArrayList<>();
        resultArrayList.addAll(tempListOfPublications);
//        printList(tempListOfPublications);
        tempListOfPublications.clear();
    }

    public void pubsBetweenTwoYears(){
        ArrayList<Publication> tempListOfPublications = new ArrayList<>();
        System.out.println("printing publications between " + lowerYear + " and " + upperYear);
        for(Publication tempPub : DB.getSetOfPublications()){
            if((tempPub.getYear() >= lowerYear) && (tempPub.getYear() <= upperYear)){
                tempListOfPublications.add(tempPub);
            }
        }
        if(sType == SORT_BY_REVERSE_DATE){
            Collections.sort(tempListOfPublications);
        }

        else if(sType == SORT_BY_RELEVANCE){
            Collections.sort(tempListOfPublications, Publication.matchCountOrder);

        }
        resultArrayList = new ArrayList<>();
        resultArrayList.addAll(tempListOfPublications);
//        printList(tempListOfPublications);
        tempListOfPublications.clear();
    }

}
