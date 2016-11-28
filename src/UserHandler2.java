import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.StringTokenizer;

public class UserHandler2 extends DefaultHandler{
    private DBManager DB;

    private String elementType;

    private boolean bPerson;
    private String name;

    private String key;

    private HashSet<String> currAuthorSet;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
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
            bPerson = false;
            name += new String(ch, start, length);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException{
        if(qName.equals("author") || qName.equals("editor")){
            currAuthorSet.add(name);
        }
        if(qName.equals(elementType)){
            if(!elementType.equals("www")){
                for(String tempName : currAuthorSet){
                    for(String tempKey : DB.getAliasMap().keySet()){
                        ArrayList<Person> tempListOfAliasPerson = DB.getAliasMap().get(tempKey);
                        ArrayList<String> tempListofAliasNames = new ArrayList<>();
                        for(Person tempPerson : tempListOfAliasPerson){
                            tempListofAliasNames.add(tempPerson.getName());
                        }
                        if(tempListofAliasNames.contains(tempName)){
                            int prev_count = DB.getPubCount().get(tempKey);
                            DB.getPubCount().put(key, prev_count + 1);
                            System.out.println(prev_count);
                        }
                    }
                }
            }
            currAuthorSet.clear();
        }
    }

    public UserHandler2(DBManager outerDB){
        currAuthorSet = new HashSet<>();
        DB = outerDB;
    }
}
