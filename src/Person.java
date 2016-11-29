/**
 * @author Mrinal Abrol(2015062)
 * @author Shashwat Malik(2015092)
 */


public class Person {
    /**
     * Person Class that defines a single Person in the database
     */
    private String name;


    public Person(String i_name){
        name = i_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
