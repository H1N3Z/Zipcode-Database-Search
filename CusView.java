
package project3.pkg1;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;


public class CusView {
        
    
    String host = "jdbc:derby://localhost:1527/zips";
    Connection con;
    
    // table names
    String stores = "STORES";

    final JPanel gui;
    
    JLabel L1 = new JLabel("Browse By State");
    JTextArea stateBrowse = new JTextArea(1,5);
    JButton stateSearch = new JButton("Click to Search");
    JLabel L2 = new JLabel("Do A Basic Search ");
    JLabel L3 = new JLabel("Enter a Zipcode");
    JTextArea zip = new JTextArea(1,8);
    JLabel L4 = new JLabel("Enter a Radius in Miles"); 
    JTextArea dis = new JTextArea(1,6);
    JButton basicSearch = new JButton("Search");
    
    private static final String[] COLS1 = {"Store ID","Address","City","State","Type"};
    Object[][] data = new Object[25][5];
   
    
    JTable tab = new JTable(data, COLS1);
    
    double[] coords = new double[4];
    
    
    public CusView() {
        this.gui = new JPanel(new FlowLayout());
        gui.add(L1);
        gui.add(stateBrowse);
        gui.add(stateSearch);
        gui.add(L2);
        gui.add(L3);
        gui.add(zip);
        gui.add(L4);
        gui.add(dis);
        gui.add(basicSearch);
        setupCon(host);
        gui.add(new JScrollPane(tab));
        
        stateSearch.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               String a = stateBrowse.getText();
               try {
                   selectByState(con, stores, a);
                   tab.revalidate();
               } catch (SQLException ex) {
                   Logger.getLogger(CusView.class.getName()).log(Level.SEVERE, null, ex);
               }
           } 
        });
        
        basicSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int z = Integer.parseInt(zip.getText());
                double rad = Double.parseDouble(dis.getText());
                try {
                    distance(con, z, rad);
                    basicSearchByZip(con);
                    tab.revalidate();
                } catch (SQLException ex) {
                    Logger.getLogger(CusView.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }  
        });
    
    }
    
    public void setupCon(String host) { 
         try {       
            con = DriverManager.getConnection(host);
        }
        catch ( SQLException error) {
            System.out.println(error.getMessage());
        }
        
    }
    
    public void selectByState(Connection con, String table, String state) throws SQLException {   
        int row = 0;
        Statement stmt;
        String query;
        ResultSet results;

        stmt = con.createStatement();
   
        query = "SELECT * FROM " + table + " WHERE STATE = '"+state+"'";
        //stmt = con.prepareStatement(query);
        
        System.out.println(query);
        results = stmt.executeQuery(query);  
        
        while(results.next()) {          
            tab.setValueAt(results.getInt("store_id"), row, 0);
            tab.setValueAt(results.getString("address"), row, 1);
            tab.setValueAt(results.getString("city"), row, 2);
            tab.setValueAt(results.getString("state"), row, 3);
            tab.setValueAt(results.getString("type"), row, 4);

         row++;
        }
        results.close();
        stmt.close();
    }
    
    public void basicSearchByZip(Connection con) throws SQLException {
        int row = 0;
        Statement stmt, stmt2;
        String query, query2;
        ResultSet results, results2;
        int[] zips = new int[100];
        

        stmt = con.createStatement();
        stmt2 = con.createStatement();
   
        query = "SELECT * FROM ZIP_CODES WHERE LATITUDE >= "+coords[0]+" AND LATITUDE<= "+coords[2]+" AND LONGITUDE >= "+coords[1]+" AND LONGITUDE <= "+coords[3]+"";
        
        System.out.println(query);
        results = stmt.executeQuery(query);  
        
        while(results.next()) { 
            
            zips[row] = results.getInt("zip_code");
            
         row++;
        }
        results.close();
        stmt.close();
        System.out.println(zips[0]);
        // I dont know how to fully search a table
        query2 = "SELECT * FROM STORES WHERE ZIP_CODE IN ("+zips[0]+","+zips[1]+")";
        System.out.println(query2);
        results2 = stmt2.executeQuery(query2);
        while(results2.next()) {
            tab.setValueAt(results2.getInt("store_id"), row, 0);
            tab.setValueAt(results2.getString("address"), row, 1);
            tab.setValueAt(results2.getString("city"), row, 2);
            tab.setValueAt(results2.getString("state"), row, 3);
            tab.setValueAt(results2.getString("type"), row, 4);
        }
        
        
    }
    
    public void distance(Connection con, int zipcode, double rad) throws SQLException {
        Statement stmt;
        String query;
        ResultSet results;
        double del = 0;
        double lamb = 0;
        double r = (rad)*1000;

        stmt = con.createStatement();
   
        query = "SELECT * FROM ZIP_CODES WHERE ZIP_CODE = "+zipcode+"";
        
        results = stmt.executeQuery(query);  
        while(results.next()) {          
            
            del = results.getDouble("latitude");
            lamb = results.getDouble("longitude");       
        }
        System.out.println(del);
        System.out.println(r);
        System.out.println(del-r);
        coords[0] = del - r;
        coords[1] = lamb - r;
        coords[2] = del + r;
        coords[3] = lamb +r;
      
    }
    
    public final JComponent getGui() {
        return gui;
    } 
}
