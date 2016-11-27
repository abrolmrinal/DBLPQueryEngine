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

        nameBreakUp = new HashSet<String>();
        StringTokenizer s = new StringTokenizer(i_name, " -");
        while(s.hasMoreTokens()){
            nameBreakUp.add(s.nextToken());
        }
    }
}
