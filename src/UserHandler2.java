import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.StringTokenizer;

public class UserHandler2 extends DefaultHandler{
    private DBManager DB;

    private String elementType;

    private boolean bPerson;
    private String name;

    private String key;

    private LinkedHashSet<String> currAuthorSet;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        if(qName.equals("author") || qName.equals("editor")) {
            bPerson = true;
            name = "";
        }
        if(atts.getLength() > 0 && (atts.getValue("key") != null)){
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
            if(!elementType.equals("www")){
                for(String sName : currAuthorSet){
                    if(DB.getAuthorMap().get(sName) != null){
                        DB.incrementPubCountKey(DB.getAuthorMap().get(sName));
                    }
                }
            }
            currAuthorSet.clear();
        }
    }

    public UserHandler2(DBManager outerDB){
        currAuthorSet = new LinkedHashSet<>();
        DB = outerDB;
    }
}
