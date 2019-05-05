
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;


public class MangView {
    
    String host = "jdbc:derby://localhost:1527/zips";
    Connection con;
    String store_hours = "STORE_HOURS";
    int id;
    
    final JPanel gui;
    
    JLabel L1 = new JLabel("Please Enter an ID");
    JTextArea Id = new JTextArea(1,10);
    JLabel L2 = new JLabel("Please Enter a Password");
    JTextArea Pass = new JTextArea(1,10);
    JButton Logon = new JButton("Logon");
    
    JLabel day = new JLabel("Please Enter Day");
    JTextArea dayInput = new JTextArea(1,25);
    JLabel hours = new JLabel("Enter New Hours");
    JTextArea hoursInput = new JTextArea(1,25);
    JLabel idLab = new JLabel("Enter Store ID");
    JTextArea idInput = new JTextArea(1,4);
    JButton change = new JButton("Change");
    
    
    public MangView() {
        this.gui = new JPanel(new FlowLayout());
          
        gui.add(L1);
        gui.add(Id);
        gui.add(L2);
        gui.add(Pass);
        gui.add(Logon);
        gui.add(day);
        gui.add(dayInput);
        gui.add(hours);
        gui.add(hoursInput);
        gui.add(idLab);
        gui.add(idInput);
        gui.add(change);
        day.setVisible(false);
        dayInput.setVisible(false);
        hours.setVisible(false);
        hoursInput.setVisible(false);
        idLab.setVisible(false);
        idInput.setVisible(false);
        change.setVisible(false);
        setupCon(host);
        
        
        Logon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String a = Id.getText();
                String b = Pass.getText();
                try {
                    verify(con, a, b);
                } catch (SQLException ex) {
                    Logger.getLogger(MangView.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        });
        
        
        
        
        change.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               String a = hoursInput.getText();
               String b = dayInput.getText();
               id = Integer.parseInt(idInput.getText());
               try {
                   updateHours(con, store_hours, a, b, id);
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
    
    public void verify(Connection con, String id, String pass) throws SQLException {
        
        Statement stmt;
        String query;
        ResultSet results;
        stmt = con.createStatement();
   
        query = "SELECT * FROM LOG WHERE ID = '"+id+"'";

        System.out.println(query);

        results = stmt.executeQuery(query);
            while(results.next()) {
                
                String check = results.getString("password");
                if (check.equals(pass)) {
                day.setVisible(true);
                dayInput.setVisible(true);
                hours.setVisible(true);
                hoursInput.setVisible(true);
                idLab.setVisible(true);
                idInput.setVisible(true);
                change.setVisible(true);
                
            }
            else {
                JOptionPane.showMessageDialog(null, "Wrong Creds", null, JOptionPane.INFORMATION_MESSAGE);

            }
        }
        
    }
    
    
    
    
    public void updateHours(Connection con, String table, String hours,  String day, int id) throws SQLException {
        
        Statement stmt;
        String query;

        stmt = con.createStatement();
   
        query = "UPDATE "+table+" SET "+day+" = '"+hours+"' WHERE STORE_ID = "+id+"";

        stmt.execute(query);
        
    }
    
    public final JComponent getGui() {
        return gui;
    } 
}
