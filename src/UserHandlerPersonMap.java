import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.HashSet;
import java.util.StringTokenizer;

public class UserHandlerPersonMap extends DefaultHandler {
    private DBManager DB;

    private String elementType;

    private boolean bPerson;
    private String name;

    private String key;

    private HashSet<String> currAuthorSet;

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
            StringTokenizer st = new StringTokenizer(key, "/");
            String checkHP = st.nextToken();
            if(elementType.equals("www") && checkHP.equals("homepages")) {
                if(currAuthorSet.size() > 0){
                    HashSet<String> tempCurrSet= new HashSet<>();
                    for(String s : currAuthorSet){
                        tempCurrSet.add(s);
                    }
                    DB.addAliasMapElement(key, tempCurrSet);
                }
            }
            currAuthorSet.clear();
        }
    }

    public UserHandlerPersonMap(DBManager outerDB){
        currAuthorSet = new HashSet<>();
        DB = outerDB;
    }
}
