/*import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.util.*;

public class QueryHandler {
    *//**
     * QUERY 1 methods
     *//*
    public void pubSearch_author(String name){
        DBManager DB;
        Scanner s = new Scanner(System.in);
        String i_name = s.next();

        File dblp = new File("dblp.xml");

        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            UserHandler userHandler = new UserHandler(DB, i_name);
            parser.getXMLReader().setFeature("http://xml.org/sax/features/validation", true);
            parser.parse(dblp, userHandler);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}*/
