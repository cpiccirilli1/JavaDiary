package javadiary;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;

/**
 * @author Chelsea
 */
public class diary {

    /**
     * @return the dArr
     */
    public ArrayList<diary> getdArr() {
        return dArr;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the db
     */
    public String getDb() {
        return db;
    }

    /**
     * @param db the db to set
     */
    public void setDb(String db) {
        this.db = db;
    }

    /**
     * @return the tags
     */
    public String getTags() {
        return tags;
    }

    /**
     * @param tags the tags to set
     */
    public void setTags(String tags) {
        this.tags = tags;
    }

    /**
     * @return the entry
     */
    public String getEntry() {
        return entry;
    }

    /**
     * @param entry the entry to set
     */
    public void setEntry(String entry) {
        this.entry = entry;
    }

    /**
     * @return the dateTime
     */
    public String getDateTime() {
        return dateTime;
    }

    /**
     * @param dateTime the dateTime to set
     */
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
    
    //Declarations
   public String title;
   public String tags;
   public String entry;
   public String dateTime;
   public String db = "C:\\Users\\Chelsea\\Documents\\DB\\diary.accdb";
   public ArrayList<diary> dArr = new ArrayList<diary>();
    
    /********
     * Blank Constructor
     ********/
    public void diary (){
        setTags("");
        setEntry("");
        setDateTime("");
        setTitle("");
    }
    
    /***********
     * Full setting constructor
     * @param tags1 - String
     * @param entry1 - String
     * @param date1 - String
     ************/
    public void diary(String tags1, String entry1, String date1, String title1){
        setTags(tags1);
        setEntry(entry1);
        setDateTime(date1);
        setTitle(title1);
    }
    
    
    public void selectDB(String dateT){
       Display(); 
       try{
            String query = "Select * FROM firstdiary WHERE dayTime = ?";
            dbconnector conn = new dbconnector(getDb());
            PreparedStatement prep = conn.accessConnect(query);
            prep.setString(1, dateT);
            ResultSet rs = prep.executeQuery();
            System.out.println("Select All by dateTime: Attempting to select");
            
            while (rs.next()){
                setDateTime(rs.getString("dayTime"));
                setTags(rs.getString("tags"));
                setEntry(rs.getString("diaryEntry"));
                setTitle(rs.getString("title"));
            }
            prep.close();
            
        } catch(SQLException ex){
            System.out.println(ex.toString());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(diary.class.getName()).log(Level.SEVERE, null, ex);
        }
  
    }//end selectDB
    
    /********
     * Select last 20 db row by datetime
     * 
     ********/
    public void selectAllDB(){
        
       try{
            String query = "Select * FROM firstdiary ORDER BY dayTime Desc  limit 20";
            dbconnector conn = new dbconnector(getDb());
            PreparedStatement prep = conn.accessConnect(query);
            ResultSet rs = prep.executeQuery();
            System.out.println("Select last 10 entries: Attempting to select");
            
            while (rs.next()){
                diary nd = new diary();
                nd.diary(rs.getString("tags"), rs.getString("diaryEntry"),
                        rs.getString("dayTime"), rs.getString("title"));
                getdArr().add(nd);
            }
            prep.close();
            
        } catch(SQLException ex){
            System.out.println(ex.toString());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(diary.class.getName()).log(Level.SEVERE, null, ex);
        }
  
    }//end selectDB
    
    /**
     * Inserts into database. Needs a full setting constructor call first
     * @throws ClassNotFoundException 
     */
    public void insertDB() throws ClassNotFoundException{
        Display();
        try{
            
            String query = "Insert INTO firstdiary(dayTime, diaryEntry, tags, title) Values(?, ?, ?, ?)";
            dbconnector conn = new dbconnector(getDb()); //change to db location
            PreparedStatement prepSt = conn.accessConnect(query); //change to sql insert stmt with prepared stmt format
            prepSt.setString(1, getDateTime());
            prepSt.setString(2, getEntry());
            prepSt.setString(3, getTags());
            prepSt.setString(4, getTitle());
            
            prepSt.executeUpdate();
            prepSt.close();
            
            
            
        } catch(SQLException ex){
            System.out.println(ex.toString());
        }
        
        
    }//end insertDB
    /**
    *updates database entry. Needs a full setting construct call first.
    */
    public void updateDB(){
       Display();
       try{
            String query = "Update firstdiary SET diaryEntry=?, tags=?, title=? WHERE dayTime=?";
            dbconnector conn = new dbconnector(getDb());
            PreparedStatement prepSt = conn.accessConnect(query);
            prepSt.setString(1, getEntry());
            prepSt.setString(2, getTags());
            prepSt.setString(4, getDateTime());
            prepSt.setString(3, getTitle());

            prepSt.executeUpdate();
            prepSt.close();
            
            
       } catch(SQLException ex){
             System.out.println(ex.toString());
       }   catch (ClassNotFoundException ex) {
            Logger.getLogger(diary.class.getName()).log(Level.SEVERE, null, ex);
        }
    }// end update
    
    /**
     * deletes a row based on dateTime. Datetime should be set in the class first.
     */
    public void deleteDB(){
        try{
            String query = "DELETE FROM firstdiary WHERE dayTime=?";
            dbconnector conn = new dbconnector(getDb());
            PreparedStatement prepSt = conn.accessConnect(query);
            prepSt.setString(1, getDateTime());
            
            prepSt.executeUpdate();
            prepSt.close();
                    
        } catch(SQLException ex){
            System.out.println(ex.toString());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(diary.class.getName()).log(Level.SEVERE, null, ex);
        }
    } //end delete
    
    public void Display(){
        System.out.println("Date: "+getDateTime());
        System.out.println("Title: "+getTitle());
        System.out.println("Tags:" + getTags());
        System.out.println("Entry: "+getEntry());
    }
    
    
}