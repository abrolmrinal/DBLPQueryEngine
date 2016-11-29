import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.*;

public class UserHandlerPersonMap extends DefaultHandler {
    /**
     * This class is used by the parser that produces the HashMap
     * on which Entity Resolution is based.
     * As www tags with title of the format: homepages/* are found,
     * an entry is made in the HashMap that stores these results
     */

    private DBManager DB;

    private String elementType;

    private boolean bPerson;
    private String name;

    private String key;

    private LinkedHashSet<String> currAuthorSet;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException{
        if(qName.equals("author") || qName.equals("editor")) {
            bPerson = true;
            name = "";
        }
        if(atts.getLength() > 0 && (atts.getValue("key") != null)){
            key = atts.getValue("key");
            elementType = qName;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length){
        if(bPerson){
            name += new String(ch, start, length);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException{
        if(qName.equals("author") || qName.equals("editor")){
            currAuthorSet.add(name);
            bPerson = false;
        }
        if(qName.equals(elementType)){
            StringTokenizer st = new StringTokenizer(key, "/");
            String checkHP = st.nextToken();
            if(elementType.equals("www") && checkHP.equals("homepages")) {
                if(currAuthorSet.size() > 0){
                    ArrayList<Person> tempCurrList= new ArrayList<>();
                    String primaryName = currAuthorSet.iterator().next();
                    Iterator it = currAuthorSet.iterator();
                    while(it.hasNext()){
                        String s = (String) it.next();
                        Person p = new Person(s.toLowerCase());
                        tempCurrList.add(p);
                        DB.addAuthorMapElement(s, primaryName);

                    }
                    DB.addAliasMapElement(key, tempCurrList);
                }
            }
            currAuthorSet.clear();
        }
    }

    public UserHandlerPersonMap(DBManager outerDB){
        currAuthorSet = new LinkedHashSet<>();
        DB = outerDB;
    }
}
