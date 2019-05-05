
package project3.pkg1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JFrame;



public class Project3 {

    // Method to populate table
    public void importData(Connection con,String filename) throws SQLException, FileNotFoundException, IOException
    {
        Statement stmt;
        String query;
        BufferedReader br;
        String line;
        
        
        br = new BufferedReader(new FileReader(filename));
        while ((line = br.readLine()) != null) {
            String strippedLine = line.replaceAll("[^a-zA-Z0-9,-]","");
            
            String[] info = strippedLine.split(",");
          
            
            stmt = con.createStatement();
            
 
            query = "INSERT INTO zip_codes (zip_code, city, state, latitude, longitude) VALUES ("+info[0]+",'"+info[1]+"','"+info[2]+"',"+info[3]+","+info[4]+")";
 
            stmt.executeUpdate(query);
        }

    }
    
    public static void main(String[] args) throws IOException {
        // TODO code application logic here\
        
        //Building Gui
        //Customer GUI
        CusView run = new CusView();
        JFrame f = new JFrame("Customer View");
        f.add(run.getGui());
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.setLocationByPlatform(true);
        f.pack();
        f.setSize(600,600);
        f.setVisible(true);
        
        //Managment View
        MangView run2 = new MangView();
        JFrame f2 = new JFrame("Managment View");
        f2.add(run2.getGui());
        f2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f2.setLocationByPlatform(true);
        f2.pack();
        f2.setSize(600,600);
        f2.setVisible(true);
        
        
        
        // sets up connection and calls method to import zipcodes to table from csv
        
        try {
            String host = "jdbc:derby://localhost:1527/zips";
            
            Connection con = DriverManager.getConnection(host);
            Project3 a = new Project3();
            a.importData(con, "");

        }
        catch ( SQLException error) {
            System.out.println(error.getMessage());
        }
        
    }
    
}
