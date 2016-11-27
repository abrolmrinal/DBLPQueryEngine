import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.util.*;

public class DBManager {
    File dblp = new File("dblp.xml");

    private Set<Publication> setOfPublications;
    private Map<String, Person> listOfPersons;


    private Map<String, HashSet<String>> aliasMap;

    public Set<Publication> getSetOfPublications() {
        return setOfPublications;
    }

    public Map<String, Person> getListOfPersons() {
        return listOfPersons;
    }

    public Map<String, HashSet<String>> getAliasMap() {
        return aliasMap;
    }

    public Person personExist(String name){
        Person tempPerson = listOfPersons.get(name);
        return tempPerson;
    }

    public void addPublication(Publication tempPublication){
        setOfPublications.add(tempPublication);
    }

    public void addAliasMapElement(String key, HashSet<String> authorSet){
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
        System.out.println("===================================================");
    }

    public void printPublicationsSet(){
        int count = 1;
        for(Publication p : setOfPublications){
            printPublication(p, count);
            count++;
        }
    }

    public DBManager() {
        listOfPersons = new HashMap<>();
        aliasMap = new HashMap<>();
    }

    public static void main(String[] args) {
        DBManager DB = new DBManager();

        DB.createAliasMap(DB);

        QueryHandler qHandler = new QueryHandler(DB);

        Scanner scanner = new Scanner(System.in);

        ///Query0 - Sort0
        System.out.print("Query0 Sort0: Author name for search: ");
        String i_name = scanner.nextLine();
        DB.setOfPublications = new HashSet<>();
        qHandler.pubSearch_author(i_name, 0);

        ///Query0 - Sort1
        DB.setOfPublications = new HashSet<>();
        System.out.print("Query0 Sort1: Author name for search: ");
        i_name = scanner.nextLine();
        qHandler.pubSearch_author(i_name, 1);

        ///Query0 - Sort2
        DB.setOfPublications = new HashSet<>();
        System.out.print("Query0 Sort2: Author name for search: ");
        i_name = scanner.nextLine();
        qHandler.pubSearch_author(i_name, 2);

        ///Query0 - Sort3
        DB.setOfPublications = new HashSet<>();
        System.out.print("Query0 Sort3: Author name for search: ");
        i_name = scanner.nextLine();
        qHandler.pubSearch_author(i_name, 3);

        ///Query1 - Sort0
        System.out.print("Title tags for search: ");
        String i_title = scanner.nextLine();
        DB.setOfPublications = new HashSet<>();
        qHandler.pubSearch_title(i_title, 0);

        ///Query1 - Sort1
        System.out.print("Title tags for search: ");
        String i_title = scanner.nextLine();
        DB.setOfPublications = new HashSet<>();
        qHandler.pubSearch_title(i_title, 1);

        ///Query1 - Sort2
        System.out.print("Title tags for search: ");
        i_title = scanner.nextLine();
        DB.setOfPublications = new HashSet<>();
        qHandler.pubSearch_title(i_title, 2);

        ///Query1 - Sort3
        System.out.print("Title tags for search: ");
        i_title = scanner.nextLine();
        DB.setOfPublications = new HashSet<>();
        qHandler.pubSearch_title(i_title, 3);

    }
}
