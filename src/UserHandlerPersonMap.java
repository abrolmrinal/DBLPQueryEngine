import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.HashSet;

public class UserHandlerPersonMap extends DefaultHandler {
    private DBManager DB;

    private String elementType;

    private boolean bPerson;
    private String name;

    private String key;

    private HashSet<String> currAuthorSet;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException{
        if(qName.equals("author")) {
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
        if(qName.equals("author")){
            currAuthorSet.add(name);
        }
        if(qName.equals(elementType)){
            if(elementType.equals("www")) {
                HashSet<String> tempCurrSet= new HashSet<>();
                for(String s : currAuthorSet){
                    tempCurrSet.add(s);
                }
                System.out.println();
                DB.addAliasMapElement(key, tempCurrSet);
            }
            currAuthorSet.clear();
        }
    }

    public UserHandlerPersonMap(DBManager outerDB){
        currAuthorSet = new HashSet<>();
        DB = outerDB;
    }
}
