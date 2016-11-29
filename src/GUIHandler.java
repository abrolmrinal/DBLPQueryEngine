import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class GUIHandler extends JFrame implements ItemListener{
    private DBManager DB;
    private QueryHandler Q1;
    private QueryHandler2 Q2;

    JLabel titleLabel,nametaglabel,sinceyearlabel,customrangelabel,numberofpublicationslabel;
    JButton b1,b2,nextbutton;
    JRadioButton rb1,rb2;
    ButtonGroup group=new ButtonGroup();
    JTable q1table,q2table;
    JPanel panel1,panel2,panel3,comboboxpanel,cardpanel,cardpanel1,cardpanel2,searchpanel,tableholder,q1tablepanel,q2tablepanel;
    JButton button;
    JTextField jtf1,jtf2,jtf3,jtf4,jtf5;
    String[] boxitems={"Query 1","Query 2"};
    String[] boxitems2={"By Name","By Title Tags"};
    JComboBox<String> ourbox= new JComboBox<String>(boxitems);
    JComboBox<String> ourbox2= new JComboBox<String>(boxitems2);
    String[] columnnames1 ={"S.No","Authors","Title","Pages","Year","Volume","Book/Journal Title","url"};
    String[] columnnames2={"S.No", "Author Names"};
    Object[][] dataq1={{"","","","","","","",""}};
    Object[] dataq2= {"", ""};
    final static boolean shouldFill = true;

    private int queryType;

    public GUIHandler(DBManager outerDB, QueryHandler outerQ1, QueryHandler2 outerQ2){

        queryType = -1;
        DB = outerDB;
        Q1 = outerQ1;
        Q2 = outerQ2;

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        if (shouldFill) {
            //natural height, maximum width
            c.fill = GridBagConstraints.BOTH;
        }
        c.insets= new Insets(5,5,0,0);
        //c.anchor= GridBagConstraints.BOTH;

        //----------panel1---------
        panel1= new JPanel();
        titleLabel = new JLabel("DBLP Query Engine");
        panel1.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel1.add(titleLabel);
        titleLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 72));
        //panel1.setBackground(Color.GREEN);
        //c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 40;
        c.weighty = 0.0;
        c.ipadx=40;
        c.weightx = 1.0;
        c.weighty= 0.25;
        c.gridwidth = 5;
        //c.gridheight=(int) 2.0;
        c.gridx=0;
        c.gridy=0;
        add(panel1,c);

        //---------Panel2-----------

        panel2= new JPanel();
        panel2.setLayout(new GridBagLayout());
        //panel2.setBackground(Color.RED);
        //comboboxpanel
        comboboxpanel = new JPanel();
        comboboxpanel.setLayout(new GridBagLayout());
        c.gridx=0;
        c.gridy=0;
        c.fill= GridBagConstraints.BOTH;
        comboboxpanel.add(ourbox,c);
        //c.fill = GridBagConstraints.VERTICAL;
        //c.fill = GridBagConstraints.HORIZONTAL;
        ourbox.addItemListener(this);
        ourbox.setEditable(false);
        //cardpanel
        cardpanel=new JPanel();
        cardpanel.setLayout(new CardLayout());

        //cardpanel1
        cardpanel1=new JPanel();
        cardpanel1.setLayout(new GridLayout(6,1,5,5));
        cardpanel1.setBackground(Color.CYAN);
        /* ----adding a panel for row 2 */
        JPanel row2=new JPanel(new GridLayout(0,2,1,1));
        cardpanel1.add(ourbox2);
        nametaglabel =new JLabel("Name/Title tags");
        row2.add(nametaglabel);
        jtf1 =new JTextField();
        row2.add(jtf1);
        cardpanel1.add(row2);
        JPanel row3=new JPanel(new GridLayout(0,2,1,1));
        sinceyearlabel =new JLabel("Since Year");
        row3.add(sinceyearlabel);
        jtf2 =new JTextField();
        row3.add(jtf2);
        cardpanel1.add(row3);

        JPanel row4=new JPanel(new GridLayout(0,3,1,1));
        customrangelabel =new JLabel("Custom Range");
        row4.add(customrangelabel);
        jtf3 =new JTextField();
        row4.add(jtf3);
        jtf4 =new JTextField();
        row4.add(jtf4);
        cardpanel1.add(row4);

        rb1=new JRadioButton("Sort By Year");
        rb2=new JRadioButton("Sort By Relevance");
        group.add(rb1);
        group.add(rb2);
        cardpanel1.add(rb1);
        cardpanel1.add(rb2);





        //cardpanel2
        cardpanel2=new JPanel();
        cardpanel2.setLayout(new GridLayout());
        cardpanel2.setBackground(Color.PINK);
        JPanel newrow=new JPanel(new GridLayout(0,2,2,2));
        numberofpublicationslabel=new JLabel("No. of Publications");
        newrow.add(numberofpublicationslabel);
        jtf5=new JTextField();
        newrow.add(jtf5);
        cardpanel2.add(newrow);



        //searchpanel
        searchpanel=new JPanel();
        searchpanel.setLayout(new GridLayout(2,1));
        b1=new JButton("Search");
        b1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int sortType, filterType;
                sortType = 0;
                filterType = 0;
                queryType = ourbox.getSelectedIndex();
                if (queryType == 0){ /** Query 1 is selected) */
                    int nameOrTitle = ourbox2.getSelectedIndex();
                    if (rb1.isSelected()) {
                        sortType = 0;
                    }
                    else if (rb2.isSelected()) {
                        sortType = 1;
                    }
                    else {
                        /** ERROR MESSAGE */
                    }
                    if (jtf2.getText().equals("") && (jtf3.getText().equals("") && jtf4.getText().equals(""))) {
                        filterType = 0;
                    }
                    else if (!jtf2.getText().equals("")) {
                        filterType = 1;
                        int sinceYear = 0;
                        try{
                            sinceYear = Integer.parseInt(jtf2.getText());
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                        Q1.setSinceYear(sinceYear);
                    }
                    else if (!jtf3.getText().equals("") && !jtf4.getText().equals("")) {
                        filterType = 2;

                        int lowerYear = 0;
                        int upperYear = Integer.MAX_VALUE;
                        try{
                            lowerYear = Integer.parseInt(jtf3.getText());
                            upperYear = Integer.parseInt(jtf4.getText());

                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                        Q1.setLowerYear(lowerYear);
                        Q1.setUpperYear(upperYear);
                    }

                    System.out.println("Query1 " + jtf1.getText() + filterType + sortType);

                    if (nameOrTitle == 0) {
                        DB.initializeSetOfPublications();
                        Q1.pubSearch_author(jtf1.getText(), filterType, sortType);
                    }
                    else if (nameOrTitle == 1) {
                        DB.initializeSetOfPublications();
                        Q1.pubSearch_title(jtf1.getText(), filterType, sortType);
                    }
                    next20Publications();
                }
                else if(queryType == 1){
                    int kPublications = 0;
                    if(!jtf5.getText().equals("")){
                        try{
                            kPublications = Integer.parseInt(jtf5.getText());
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                        DB.initializePubCountKey();
                        Q2.authorMoreThanKPub(kPublications);
                        next20PublicationsQuery2();
                    }
                    else{
                        /** error **/
                    }
                }
            }
        });
        b2=new JButton("Reset");
        nextbutton=new JButton("Next");
        nextbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(queryType == 0){
                    next20Publications();
                }
                else if(queryType == 1){
                    next20PublicationsQuery2();
                }
            }
        });
        searchpanel.add(b1);
        searchpanel.add(b2);
        searchpanel.add(nextbutton);

        cardpanel.add(cardpanel1,boxitems[0]);
        cardpanel.add(cardpanel2,boxitems[1]);





        //combobox panel gbc setting
        c.gridx=0;
        c.gridy=0;
        c.ipadx=40;
        panel2.add(comboboxpanel,c);

        //cardpanel settings gbc
        c.gridx=0;
        c.gridy=1;
        c.weighty=2.0;

        panel2.add(cardpanel,c);

        //searchpanel settings
        c.gridx=0;
        c.gridy=2;
        c.weighty=0.5;
        panel2.add(searchpanel,c);

        //panel2 settings
        c.ipadx = 40;
        c.weighty = 2.0;
        c.weightx = 1.0;
        c.gridwidth = 1;
        c.gridheight=6;
        c.gridx=0;
        c.gridy=1;
        add(panel2,c);



        //---------panel 3----------

        panel3= new JPanel();
        panel3.setBackground(Color.RED);
        //c.fill = GridBagConstraints.VERTICAL;
        c.ipadx = 40;
        c.weighty = 2.0;
        c.weightx = 2.0;
        c.gridwidth = 4;
        c.gridheight=6;
        c.gridx=1;
        c.gridy=1;
        add(panel3,c);

//		tableholder=new JPanel();
//		tableholder.setLayout(new CardLayout());
//		q1tablepanel=new JPanel();
//		q1table=new JTable(new Query1TableModel());
//
//		q1tablepanel.setLayout(new GridLayout(1,1,0,0));
//		q1tablepanel.add(q1table);
//		q2tablepanel.setLayout(new FlowLayout());
//
//		tableholder.add(q1tablepanel,boxitems[0]);
//		tableholder.add(q2tablepanel,boxitems[1]);

    }

    public void next20Publications(){
        System.out.println("Printing 20 entries: ");
        ArrayList<Publication> tempList = new ArrayList<>();

        int count = 0;
        for(Publication p : Q1.getResultArrayList()){
            if(count < 20){
                tempList.add(p);
                count++;
            }
            else{
                count = 0;
                break;
            }
        }

        for(Publication p : tempList){
            Q1.getResultArrayList().remove(p);
        }

//        tableholder = new JPanel();
        panel3.removeAll();
        q1table=new JTable(new Query1TableModel(tempList));
        q1table.setRowHeight(25);
        q1table.setPreferredScrollableViewportSize(new Dimension(1000,1000));

        JScrollPane scrollPane = new JScrollPane(q1table);
        panel3.add(scrollPane);
        panel3.repaint();
        panel3.revalidate();
        System.out.println("Printed 20 entries");
    }

    public void next20PublicationsQuery2(){
        System.out.println("Printing 20 entries");
        ArrayList<String> tempList = new ArrayList<>();

        int count = 0;
        for(String tempName : Q2.authorListMoreThanK){
            if(count < 20) {
                tempList.add(tempName);
                count++;
            }
            else{
                count = 0;
                break;
            }
        }

        for(String tempName : tempList){
            Q2.authorListMoreThanK.remove(tempName);
        }

        panel3.removeAll();
        q1table = new JTable(new Query2TableModel(tempList));
        q1table.setRowHeight(25);
        q1table.setPreferredScrollableViewportSize(new Dimension(1000, 1000));

        JScrollPane scrollPane = new JScrollPane(q1table);
        panel3.add(scrollPane);
        panel3.repaint();
        panel3.revalidate();
        System.out.println("Printed 20 entries");
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        // TODO Auto-generated method stub
        CardLayout cardlayout=(CardLayout)(cardpanel.getLayout());
        cardlayout.show(cardpanel,(String)e.getItem());

//		CardLayout cardlayout2=(CardLayout)(tableholder.getLayout());
//		cardlayout2.show(tableholder,(String)e.getItem());

    }
}