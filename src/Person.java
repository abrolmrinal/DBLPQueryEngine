import java.util.*;

public class Person {
    private String name;
    private List<Publication> pubList;
    private int pubCount;

    /**
     * For entity resolution
     */
    private Set<String> nameBreakUp;
    private int numNameParts;

    public Person(String i_name){
        name = i_name;
        pubList = new ArrayList<Publication>();
        pubCount = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Publication> getPubList() {
        return pubList;
    }

    public void setPubList(List<Publication> pubList) {
        this.pubList = pubList;
    }

    public int getPubCount() {
        return pubCount;
    }

    public void setPubCount(int pubCount) {
        this.pubCount = pubCount;
    }

}
