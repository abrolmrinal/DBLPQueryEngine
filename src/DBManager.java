import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.util.*;

public class DBManager {
    private Set<Publication> setOfPublications;
    private Map<String, Person> listOfPersons;
    private Map<String, HashSet<String>> aliasMap;

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

    public DBManager() {
        setOfPublications = new HashSet<>();
        listOfPersons = new HashMap<>();
        aliasMap = new HashMap<>();
    }

    public static void main(String[] args) {
        DBManager DB = new DBManager();

        File dblp = new File("dblp.xml");

        try {
            System.out.println("Starting Parser");
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            UserHandlerPersonMap userHandler = new UserHandlerPersonMap(DB);
            parser.getXMLReader().setFeature("http://xml.org/sax/features/validation", true);
            parser.parse(dblp, userHandler);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        /*int count = 0;
        for(String key : DB.aliasMap.keySet()) {
            HashSet<String> tempSet = DB.aliasMap.get(key);
            count++;
            if (count == 100) {
                System.exit(0);
            }
            for (String aName : tempSet) {
                System.out.println(aName + " ");
            }
            System.out.println();
        }*/

    }
}
