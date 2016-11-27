import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.util.*;

public class DBManager {
    private Set<Publication> setOfPublications;
    private Map<String, Person> listOfPersons;

    public Person personExist(String name){
        Person tempPerson = listOfPersons.get(name);
        return tempPerson;
    }

    public void addPublication(Publication tempPublication){
        setOfPublications.add(tempPublication);
    }

    public DBManager() {
        setOfPublications = new HashSet<>();
        listOfPersons = new HashMap<>();
    }

    public static void main(String[] args) {
        DBManager DB = new DBManager();

        File dblp = new File("dblp.xml");

        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            UserHandler userHandler = new UserHandler(DB);
            parser.getXMLReader().setFeature("http://xml.org/sax/features/validation", true);
            parser.parse(dblp, userHandler);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        for(Publication pub : DB.setOfPublications){
            System.out.println(pub.getTitle());
        }
    }
}
