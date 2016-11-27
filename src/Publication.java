import java.util.*;

public class Publication {
    private List<Person> authors;
    private String title;
    private String pages;
    private int year;
    private String volume;
    private String journalOrBookTitle;
    private String mDate;

    public Publication(List i_authors, String i_title, String i_pages, String i_year, String i_volume, String i_journalOrBookTitle, String i_mDate){
        authors = new ArrayList<Person>();
        title = i_title;
        authors = i_authors;
        pages = i_pages;
        year = Integer.parseInt(i_year);
        volume = i_volume;
        journalOrBookTitle = i_journalOrBookTitle;
        mDate = i_mDate;
    }

    public List<Person> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Person> authors) {
        this.authors = authors;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getJournalOrBookTitle() {
        return journalOrBookTitle;
    }

    public void setJournalOrBookTitle(String journalOrBookTitle) {
        this.journalOrBookTitle = journalOrBookTitle;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }
}
