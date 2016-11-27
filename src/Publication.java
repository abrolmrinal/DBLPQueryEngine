import java.util.*;

public class Publication implements Comparable<Publication> {
    private HashSet<String> authors;
    private String title;
    private String pages;
    private int year;
    private String volume;
    private String journalOrBookTitle;

    private int matchCount;

    public Publication(HashSet<String> i_authors, String i_title, String i_pages, String i_year, String i_volume, String i_journalOrBookTitle){
        authors = new HashSet<>();
        title = i_title;
        authors = i_authors;
        pages = i_pages;
        year = Integer.parseInt(i_year);
        volume = i_volume;
        journalOrBookTitle = i_journalOrBookTitle;

        matchCount = 0;
    }

    static Comparator<Publication> matchCountOrder = new Comparator<Publication>() {
        @Override
        public int compare(Publication p1, Publication p2) {
            return p2.matchCount - p1.matchCount;
        }
    };


    public HashSet<String> getAuthors() {
        return authors;
    }

    public void setAuthors(HashSet<String> authors) {
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

    public int getMatchCount() {
        return matchCount;
    }

    public void setMatchCount(int matchCount) {
        this.matchCount = matchCount;
    }


    @Override
    public int compareTo(Publication p){
        if (p.year > year)
            return 1;
        else if (p.year < year)
            return -1;
        else
            return 0;
    }
}
