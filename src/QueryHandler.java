import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.lang.reflect.Array;
import java.util.*;

public class QueryHandler {
    DBManager DB;
    /**
     * Constants for Query 1
     */
    private final int SEARCH_BY_AUTHOR = 0;
    private final int SEARCH_BY_TITLE = 1;

    private final int SORT_BY_REVERSE_DATE = 0;
    private final int SORT_BY_RELEVANCE = 1;
    private final int PUBS_SINCE_GIVEN_YEAR = 2;
    private final int PUBS_BETWEEN_TWO_YEARS = 3;

    public QueryHandler(DBManager outerDB){
        DB = outerDB;
    }
    /**
     *Query1 Methods
     */
    public void pubSearch_author(String i_name, int sortType){
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

        switch (sortType){
            case SORT_BY_REVERSE_DATE : sortByReverseDate();
                break;
            case SORT_BY_RELEVANCE : sortByRelevanceAuthor();
                break;
            case PUBS_SINCE_GIVEN_YEAR : pubsSinceGivenYear(1980);
                break;
            case PUBS_BETWEEN_TWO_YEARS : pubsBetweenTwoYears(1980, 2005);
                break;

        }
    }

    public void pubSearch_title(String i_title, int sortType){
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

        switch (sortType){
            case SORT_BY_REVERSE_DATE : sortByReverseDate();
                break;
            case SORT_BY_RELEVANCE : sortByRelevanceTitle();
                break;
            case PUBS_SINCE_GIVEN_YEAR : pubsSinceGivenYear(1980);
                break;
            case PUBS_BETWEEN_TWO_YEARS : pubsBetweenTwoYears(1980, 2005);
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

    public void printSet(Set<Publication> pub){
        int count = 1;
        for(Publication p : pub){
            DB.printPublication(p, count);
            count++;
        }
    }

    public void sortByReverseDate(){
        ArrayList<Publication> tempListOfPublications = new ArrayList<>();
        for(Publication tempPub : DB.getSetOfPublications()){
            tempListOfPublications.add(tempPub);
        }
        Collections.sort(tempListOfPublications);
        printList(tempListOfPublications);
        tempListOfPublications.clear();
    }

    public void sortByRelevanceAuthor(){
        ArrayList<Publication> tempListOfPublications = new ArrayList<>();
        for(Publication tempPub : DB.getSetOfPublications()){
            tempListOfPublications.add(tempPub);
        }
        Collections.sort(tempListOfPublications, Publication.matchCountOrder);
        printList(tempListOfPublications);
        tempListOfPublications.clear();
    }

    public void sortByRelevanceTitle(){
        ArrayList<Publication> tempListOfPublications = new ArrayList<>();
        for(Publication tempPub : DB.getSetOfPublications()){
            tempListOfPublications.add(tempPub);
        }
        Collections.sort(tempListOfPublications, Publication.matchCountOrder );
        printList(tempListOfPublications);
        tempListOfPublications.clear();
    }

    public void pubsSinceGivenYear(int year){
        ArrayList<Publication> tempListOfPublications = new ArrayList<>();
        for(Publication tempPub : DB.getSetOfPublications()){
            if(tempPub.getYear() >= year){
                tempListOfPublications.add(tempPub);
            }
        }
        Collections.sort(tempListOfPublications, Collections.reverseOrder());
        printList(tempListOfPublications);
        tempListOfPublications.clear();
    }

    public void pubsBetweenTwoYears(int year1, int year2){
        ArrayList<Publication> tempListOfPublications = new ArrayList<>();
        for(Publication tempPub : DB.getSetOfPublications()){
            if((tempPub.getYear() >= year1) && (tempPub.getYear() <= year2)){
                tempListOfPublications.add(tempPub);
            }
        }
        Collections.sort(tempListOfPublications, Collections.reverseOrder());
        printList(tempListOfPublications);
        tempListOfPublications.clear();
    }
}
