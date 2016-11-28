import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.util.*;

public class DBManager {
    File dblp = new File("dblp.xml");

    private Set<Publication> setOfPublications;

    private Map<String, ArrayList<Person>> aliasMap;
    private Map<String, Integer> pubCount;

    public Set<Publication> getSetOfPublications() {
        return setOfPublications;
    }


    public Map<String, ArrayList<Person>> getAliasMap() {
        return aliasMap;
    }

    public Map<String, Integer> getPubCount() {
        return pubCount;
    }

    public void addPublication(Publication tempPublication){
        setOfPublications.add(tempPublication);
    }

    public void addAliasMapElement(String key, ArrayList<Person> authorSet){
        aliasMap.put(key, authorSet);
    }

    public void createAliasMap(DBManager DB){
        try {
            System.out.println("Starting Parser for Alias Map");
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            UserHandlerPersonMap userHandler = new UserHandlerPersonMap(DB);
            parser.getXMLReader().setFeature("http://xml.org/sax/features/validation", true);
            parser.parse(dblp, userHandler);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {
            System.out.println("Exited Parser for Alias Map");
        }
    }

    public void printPublication(Publication p, int count){
        System.out.println("==================Publication "+ count +" =================");
        System.out.println("Authors : ");
        for(String aName : p.getAuthors()){
            System.out.println(aName);
        }
        System.out.println("Title: " + p.getTitle());
        System.out.println("Pages: " + p.getPages());
        System.out.println("Year: " + p.getYear());
        System.out.println("Volume: " + p.getVolume());
        System.out.println("journalOrBookTitle: " + p.getJournalOrBookTitle());
        System.out.println("relevance: " + p.getMatchCount());
        System.out.println("===================================================");
    }

    public DBManager() {
        aliasMap = new HashMap<>();

    }

    public static void main(String[] args) {
        DBManager DB = new DBManager();

        int count = 0;

        DB.createAliasMap(DB);
        System.out.println("Number of www tags: " + DB.aliasMap.size());

        for(String key : DB.aliasMap.keySet()){
            count = count + DB.aliasMap.get(key).size();
        }

        System.out.println("number of authors: " + count);

        QueryHandler qHandler = new QueryHandler(DB);
        QueryHandler2 qHandler2 = new QueryHandler2(DB);

        Scanner scanner = new Scanner(System.in);

        ///Query0 - Sort0
        System.out.print("Query0 Sort0: Author name for search: ");
        String i_name = scanner.nextLine();
        DB.setOfPublications = new HashSet<>();
        qHandler.pubSearch_author(i_name.toLowerCase(), 1);

/*        ///Query0 - Sort1
        DB.setOfPublications = new HashSet<>();
        System.out.print("Query0 Sort1: Author name for search: ");
        String i_name = scanner.nextLine();
        qHandler.pubSearch_author(i_name.toLowerCase(), 1);*/

/*        ///Query0 - Sort2
        DB.setOfPublications = new HashSet<>();
        System.out.print("Query0 Sort2: Author name for search: ");
        i_name = scanner.nextLine();
        qHandler.pubSearch_author(i_name.toLowerCase(), 2);*/

/*        ///Query0 - Sort3
        DB.setOfPublications = new HashSet<>();
        System.out.print("Query0 Sort3: Author name for search: ");
        i_name = scanner.nextLine();
        qHandler.pubSearch_author(i_name.toLowerCase(), 3);*/

/*        ///Query1 - Sort0
        System.out.print("Title tags for search: ");
        String i_title = scanner.nextLine();
        DB.setOfPublications = new HashSet<>();
        qHandler.pubSearch_title(i_title.toLowerCase(), 0);*/

/*        ///Query1 - Sort1
        System.out.print("Title tags for search: ");
        i_title = scanner.nextLine();
        DB.setOfPublications = new HashSet<>();
        qHandler.pubSearch_title(i_title.toLowerCase(), 1);*/

/*        ///Query1 - Sort2
        System.out.print("Title tags for search: ");
        i_title = scanner.nextLine();
        DB.setOfPublications = new HashSet<>();
        qHandler.pubSearch_title(i_title.toLowerCase(), 2);*/

/*        ///Query1 - Sort3
        System.out.print("Title tags for search: ");
        i_title = scanner.nextLine();
        DB.setOfPublications = new HashSet<>();
        qHandler.pubSearch_title(i_title.toLowerCase(), 3);*/

/*        DB.pubCount = new HashMap<>();
        qHandler2.authorMoreThanKPub(500);*/

    }
}
