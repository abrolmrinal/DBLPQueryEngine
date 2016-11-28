import java.util.*;

public class Person {
    private String name;
    private int pubCount;


    public Person(String i_name){
        name = i_name;
        pubCount = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPubCount() {
        return pubCount;
    }

    public void setPubCount(int pubCount) {
        this.pubCount = pubCount;
    }

    public void incrementPubCount(){
        this.pubCount++;
    }

}
