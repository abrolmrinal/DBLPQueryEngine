import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.*;

public class QueryHandler2 {
    DBManager DB;

    public QueryHandler2(DBManager outerDB){
        DB = outerDB;
    }

    public void authorMoreThanKPub(int k){
        System.out.println("Starting query 2");


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

        System.out.println("List of authors with more than " + k + " publications: ");
        HashMap<String, Integer> sortedMap = sortByValues(DB.getPubCountKey());

        for(String pName : sortedMap.keySet()){
            if(sortedMap.get(pName) >= k){
                System.out.print(pName+ " --- ");
                System.out.println(sortedMap.get(pName));
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
