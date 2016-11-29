import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.*;

/**
 * @author Mrinal Abrol(2015062)
 * @author Shashwat Malik(2015092)
 */

public class QueryHandler2 {
    /**
     * This class handles Query2, it initiates and calls the
     * parser for building up author map, maintains a count
     * for every alias name and also a total count
     * for primary authors;
     */
    DBManager DB;

    public ArrayList<String> authorListMoreThanK;

    public QueryHandler2(DBManager outerDB){
        DB = outerDB;
    }

    public void authorMoreThanKPub(int k){
        System.out.println("Starting query 2");
        System.out.println("List of authors with more than " + k + " publications: ");


        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            UserHandler2 userHandler2 = new UserHandler2(DB);
            parser.getXMLReader().setFeature("http://xml.org/sax/features/validation", true);
            parser.parse(DB.dblp, userHandler2);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        HashMap<String, Integer> sortedMap = sortByValues(DB.getPubCountKey());

        authorListMoreThanK = new ArrayList<>();
        for(String pName : sortedMap.keySet()){
            if(sortedMap.get(pName) >= k){
//                System.out.println(pName);
                authorListMoreThanK.add(pName);
            }
        }

    }

    private static HashMap<String, Integer> sortByValues(Map<String, Integer> pubCount){
        List list = new LinkedList<>(pubCount.entrySet());

        Collections.sort(list, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Integer)((Map.Entry)(o2)).getValue()).compareTo((Integer)((Map.Entry)(o1)).getValue());
            }
        });

        HashMap<String, Integer> sortedMap = new LinkedHashMap<>();
        for(Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedMap.put((String)entry.getKey(), (Integer)entry.getValue());
        }
        return sortedMap;
    }
}
