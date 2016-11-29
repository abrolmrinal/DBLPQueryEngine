import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class Query2TableModel extends AbstractTableModel{
    /**
     * This class defines the basic model for table that
     * is used to display results of query2.
     * The data to be filled in the table is added
     * within the constructor
     */
    private List<String> nameOfAuthors;

    private static int SNO = 0;
    private static int AUTHORNAME = 1;

    Object[][] data = new Object[20][2];

    String[] columnNames ={"S.No","Author Name"};

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

    public Query2TableModel(ArrayList<String> top20List){
        nameOfAuthors = top20List;
        int count = 0;
        for(String sName : top20List){
            data[count][SNO] = (Integer)count + 1;
            data[count][AUTHORNAME] = (String)sName;
            count++;
        }
    }

}
