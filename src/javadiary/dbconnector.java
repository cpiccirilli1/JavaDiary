
package javadiary;

import java.sql.*;

public class dbconnector {
    String db_loc;
    
    public dbconnector(String db_path){
        db_loc = db_path;
    }
    
    
    public PreparedStatement accessConnect(String prepSt) throws SQLException, ClassNotFoundException{
        
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            Connection conn;
            //Edit this next line of code starting with 
            //C: to the file path of the database
            conn = DriverManager.getConnection("jdbc:ucanaccess://"+db_loc);
            PreparedStatement ps = conn.prepareStatement(prepSt);
            System.out.println("Connecting to "+db_loc);
            return ps;
        } catch (SQLException e) {
            System.out.println(e);
            throw e;
        }
        
    }  //end accessSelect
}
