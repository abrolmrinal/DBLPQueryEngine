import javax.print.DocFlavor;
import javax.swing.table.AbstractTableModel;
import java.util.*;

public class Query1TableModel extends AbstractTableModel {

    private static int SNO = 0;
    private static int AUTHORS = 1;
    private static int TITLE = 2;
    private static int PAGES = 3;
    private static int YEAR = 4;
    private static int VOLUME = 5;
    private static int JOURNALSORBOOKS = 6;
    private static int URL = 7;

    private List<Publication> listOfPublications = new ArrayList<>();
    Object[][] data = new Object[20][8];

    String[] columnNames ={"S.No","Authors","Title","Pages","Year","Volume","Book/Journal Title","url"};

    public int getRowCount(){
        return data.length;
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        return data[row][col];
    }

    public Query1TableModel(List<Publication> top20List){
        listOfPublications = top20List;

        int count = 0;
        for(Publication p : listOfPublications){
            data[count][SNO] = (Integer)count + 1;
            data[count][AUTHORS] = (HashSet<String>)p.getAuthors();
            data[count][TITLE] = (String)p.getTitle();
            data[count][PAGES] = (String)p.getPages();
            data[count][YEAR] = (Integer)p.getYear();
            data[count][VOLUME] = (String)p.getVolume();
            data[count][JOURNALSORBOOKS] = (String)p.getJournalOrBookTitle();
            data[count][URL] = (String)p.getURL();
            count++;
        }
    }
}
