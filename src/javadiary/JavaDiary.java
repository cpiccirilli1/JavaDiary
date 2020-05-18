/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javadiary;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalDateTime; // Import the LocalDateTime class
import java.time.format.DateTimeFormatter; // Import the DateTimeFormatter class
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
// import javafx.stage.FileChooser;


/**
 *
 * @author
 */
public class JavaDiary extends Application {
    
    //Nodes
    MenuBar mBar = menubar();
    TextField tagsArea = new TextField(); 
    TextField title = new TextField();
    TextArea entryArea = new TextArea();
    Label statusBar = new Label("Welcome!");
    String todaysDate = postingDate();
    Label displayDate = new Label("Date:  "+todaysDate);
    Button pubBtn = new Button();
    Button reset = new Button("Reset");
    ListView listV = new ListView();
    
    // Variables for settings.
    diary masterD = new diary();
    String dbStr = masterD.getDb();
    boolean showSaveAlert = true;//save alert
    
    // Font variables
    String fontName = "Arial";
    double fontSize = 12.0;
    FontWeight fw = FontWeight.NORMAL;
    
        
    public void setDb(String dBase){
        masterD.setDb(dBase);
    }
    
    public Menu fileMenu(){
        
        Menu fMenu = new Menu("File");
        
        //Instantiate submenus
        MenuItem nEntry = new MenuItem("New Entry");
        MenuItem exit = new MenuItem("Exit");
        MenuItem oEntry = new MenuItem("Select Diary");
        MenuItem readMode = new MenuItem("Reading Mode");
        MenuItem save = new MenuItem("Save Current");
        MenuItem export = new MenuItem("Export...");
        
        //actions for submenus
        save.setOnAction(new SaveBtnAction());
        exit.setOnAction(new exitBtnHandle());
        nEntry.setOnAction(new resetBtnHandle());
        
        oEntry.setOnAction(new EventHandler<ActionEvent>() {
            public void handle (ActionEvent event){
                //setDb("");
            }
        });
        
        //Separators for menus
        SeparatorMenuItem sep = new SeparatorMenuItem();
        SeparatorMenuItem sep1 = new SeparatorMenuItem();
        
        fMenu.getItems().add(nEntry);
        fMenu.getItems().add(oEntry);
        fMenu.getItems().add(save);
        fMenu.getItems().add(sep);
        fMenu.getItems().add(readMode);
        fMenu.getItems().add(export);
        fMenu.getItems().add(sep1);
        fMenu.getItems().add(exit);        
        return fMenu;
    }
    
    public Menu editMenu(){
        Menu eMenu = new Menu("Edit");
        
        MenuItem cut = new MenuItem("Cut");
        MenuItem copy = new MenuItem("Copy");
        MenuItem paste = new MenuItem("Paste");
        MenuItem find = new MenuItem("Find");
        MenuItem fReplace = new MenuItem("Find/Replace");
        
        SeparatorMenuItem sep = new SeparatorMenuItem();
        
        eMenu.getItems().add(find);
        eMenu.getItems().add(fReplace);
        eMenu.getItems().add(sep);
        eMenu.getItems().add(cut);
        eMenu.getItems().add(copy);
        eMenu.getItems().add(paste);
        
        return eMenu;
    }
    
    public Menu toolsMenu(){
        Menu tMenu = new Menu("Tools");
        
        MenuItem settings = new MenuItem("Settings");
        MenuItem search = new MenuItem("Search Tags");
        MenuItem themes = new MenuItem("Themes");
        
        settings.setOnAction(new SettingsWindow());
        
        tMenu.getItems().add(search);
        tMenu.getItems().add(themes);
        tMenu.getItems().add(settings);
        
        return tMenu;
    }
    
    public MenuBar menubar(){
        
        MenuBar mb = new MenuBar();
        
        
        Menu fMenu = this.fileMenu();
        Menu eMenu = this.editMenu();
        Menu tMenu = this.toolsMenu();
        
        mb.getMenus().add(fMenu);
        
        mb.getMenus().add(eMenu);
        
        mb.getMenus().add(tMenu);
        
        return mb;
    }
    
    public String postingDate(){
        LocalDateTime  local_dt = LocalDateTime.now();
        DateTimeFormatter local_fmt = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
        String formattedDate = local_fmt.format(local_dt);
        
        return formattedDate;
    }
    
    public ArrayList<diary> reverseArrayList(ArrayList<diary> alist) 
    { 
        // Arraylist for storing reversed elements 
        ArrayList<diary> revArrayList = new ArrayList<diary>(); 
        for (int i = alist.size() - 1; i >= 0; i--) { 
  
            // Append the elements in reverse order 
            revArrayList.add(alist.get(i)); 
        } 
  
        // Return the reversed arraylist 
        return revArrayList; 
    } 
    
    public ListView loadList(){
        diary listFillUp = new diary();
        listFillUp.selectAllDB();
        ArrayList<diary> entries = listFillUp.getdArr();
        
        for (diary i : entries){
            
            listV.getItems().add(i.getDateTime());
            
        }
        

        return listV;
    }
    
    public VBox listBox(){
        
        ListView lv = loadList();
        lv.setMinWidth(100);
        lv.setMaxWidth(150);
        lv.setMinHeight(500);
        
        Label entriesLbl = new Label("Past 20 entries:");
        Button view = new Button("View >>");
        view.setOnAction(new viewBtnHandler());
        
        
        view.setMinWidth(78);
        
        VBox listBox = new VBox();
        listBox.setAlignment(Pos.TOP_CENTER);
        listBox.setSpacing(5);
        
        
        listBox.getChildren().addAll(entriesLbl, lv, view);
        
        
        return listBox;
    }
    
    /*****
     * 
     * @return 
     */
    public HBox middle(){
        VBox editArea = editArea();
        VBox lb = listBox();
        
                
        HBox middle = new HBox();
        middle.setAlignment(Pos.TOP_CENTER);
        Separator separator = new Separator(Orientation.VERTICAL);
        //add padding
        middle.getChildren().addAll(lb, separator, editArea);
        return middle;
    }
    
    /**************
     * All the Elements to make up area between the entryArea variable
     * and statusBar Variable
     * @return 
     **************/
    public HBox pubHBox(){
        
        HBox pubHBox = new HBox();
        pubHBox.setSpacing(5);
        pubHBox.setAlignment(Pos.CENTER);
        HBox tagsHBox = new HBox();
        VBox tagsVBox = new VBox();
        Label tags = new Label("Tags: ");
        Label tagsEx = new Label("        Use commas to separate tags. ");
        
        pubBtn.setText("Save");
        pubBtn.setMinSize(85, 40);
        tagsArea.setMinWidth(200);
        
        tagsHBox.getChildren().add(tags);
        tagsHBox.getChildren().add(tagsArea);
        
        tagsVBox.getChildren().addAll(tagsHBox, tagsEx); 
        pubHBox.getChildren().addAll(tagsVBox, pubBtn);
        return pubHBox;
    }
    
    /**********
     * Returns an HBox with the title global and a label
     * @return 
     **********/
    public HBox titleBox(){
        HBox titleHBox = new HBox();
        titleHBox.setSpacing(5);
        Label titleLabel = new Label("Title: ");
        reset.setOnAction(new resetBtnHandle());
        
        titleHBox.getChildren().addAll(titleLabel, title, reset);
        
        return titleHBox;
    }
    
    /***********
     * Compiles elements below the menubar into a v-box
     * and includes their settings.
     * @return 
     ************/
    public VBox editArea(){
        String display = "The quick brown fox jumps over the lazy dog";
        
        VBox editArea = new VBox();
        HBox titleBox = titleBox();
        HBox pubHBox = pubHBox();
        
        editArea.setSpacing(5); //add padding
        
        entryArea.setPrefRowCount(50);
        entryArea.setMinWidth(500);
        
        title.setMinWidth(300);
        
        entryArea.setPrefRowCount(50);
        entryArea.setMinWidth(500);
        entryArea.setText("");
        entryArea.setWrapText(true);
        
        title.setFont(Font.font(fontName));
        tagsArea.setFont(Font.font(fontName));
        entryArea.setFont(Font.font(fontName,fw, fontSize));
        
        pubBtn.setOnAction(new SaveBtnAction());
        Separator separator1 = new Separator();
        
        //adds to editArea VBox element
        editArea.getChildren().addAll(displayDate, titleBox, entryArea, pubHBox, separator1, statusBar);
        return editArea;
    }//end editArea
    
    @Override
    public void start(Stage primaryStage) {

        
        VBox root = new VBox();
        root.setSpacing(5);
        HBox middle = middle();
        middle.setSpacing(10); //add padding
       
        //adds to root VBox element
        root.getChildren().addAll(mBar, middle);
         root.setAlignment(Pos.CENTER);
        
        Scene scene = new Scene(root, 700, 700);
        //primaryStage.setOnCloseRequest(new exitBtnHandle());
        primaryStage.setTitle("Java Diary!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    /***************
     * Alert function that checks if one wants to save the entry
     * will return yes, no, or cancel. cancel is the default.
     * @param option
     * for inserting text of the action you are doing.
     * @return 
     */
    public ButtonType alertDialogue(String option){
        ButtonType results = ButtonType.CANCEL;
    
        if (showSaveAlert == true){
            Alert saveAlert = new Alert(AlertType.CONFIRMATION, "Save "+title.getText()+" before "+ option+"?",
            ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
            saveAlert.showAndWait();
            results = saveAlert.getResult();
            if (results == ButtonType.CANCEL){
                saveAlert.close();
            }
        }
       return results;
    } //end alertDialogue
    
    /*************
     * Returns true if all textfields and the textarea are blank
     * @return bool
     **************/
    public boolean checkIfBlank(){
        String tit = title.getText();
        String tgs = tagsArea.getText();
        String entry = entryArea.getText();

        if (tit.equals("") && tgs.equals("") && entry.equals("")){
            return true;
        }
        
        return false;
    }
    
    /******************
     * Save method that inserts a new save if freshly opened, reset or the 
     * file new entry menuitem is clicked.
     * Updates the database row if it already exists in sql.
     ******************/
    public void saveMethod(){
            String todayD = todaysDate;
            String text = entryArea.getText();
            String tagText = tagsArea.getText();
            String titleText = title.getText();
            statusBar.setText("Saving...");
//            diary d2 = new diary();
            
            try {
                diary newD = new diary();
                
                newD.selectDB(todayD);
                if (newD.getDateTime() == todayD){
                    System.out.println("Row exists... Trying to update!");
                    
                    masterD.diary(tagText, text, todayD, titleText);
                    masterD.updateDB();
                    System.out.println("Update Success!");
                    
                } else {
                    
                    masterD.diary(tagText, text, todayD, titleText);
                    System.out.println("Trying to Insert!");
                    masterD.insertDB();
                    System.out.println("Success!");
                    System.out.println("Updating list after new save.");
                    listV.getItems().clear();
                    listV = loadList();
                }
                statusBar.setText("Saved!");
            } catch (ClassNotFoundException ex) {
            
                Logger.getLogger(JavaDiary.class.getName()).log(Level.SEVERE, null, ex);
            
            }
        } //end saveMethod

    
    /***********
     * @param args the command line arguments
     *************/
    public static void main(String[] args) {
        launch(args);
    }
    
    /****************
     * Save Button Handler
     ****************/
    public class SaveBtnAction implements EventHandler<ActionEvent> {
        public void handle (ActionEvent ae){
           saveMethod();
            
        } // end handle
        
       
    } //end SaveBtnAction
    
    /*******************
     * Handles the view button. 
     *******************/
    public class viewBtnHandler implements EventHandler<ActionEvent> {
        diary loadEntry = new diary();
        
        public void handle (ActionEvent ae){
            boolean checkBlank = checkIfBlank();
            
            if (checkBlank == true || showSaveAlert == false){
                viewBtnAction();
            } else {
                ButtonType results = alertDialogue("Viewing");
                if (results==ButtonType.YES){
                    saveMethod();
                    viewBtnAction();
                } else if(results == ButtonType.NO){
                    viewBtnAction();
                }
            }    
            
        }
        
        /****************
         * Takes the listview variable pulls the datestamp string
         * does a call to the access 
         * database and populates the textfield/textarea
         ****************/
        public void viewBtnAction(){    
            String dt = "N/A";
            String etry = "Error - Test";
            String tgs="Error - Test";
            String tit="Error - Test";
            
            ObservableList<String> topics; 
            topics = listV.getSelectionModel().getSelectedItems();

                for (String timedate: topics)
                {
                    
                    masterD.selectDB(timedate);
                    if (masterD.getDateTime() != null){
                        dt = masterD.getDateTime();
                        etry = masterD.getEntry();
                        tgs = masterD.getTags();
                        tit = masterD.getTitle();
                        
                       
                    }
                   
                   
                }
                
            title.setText(tit);
            tagsArea.setText(tgs);
            entryArea.setText(etry);
            displayDate.setText("Date: "+dt);
            todaysDate = dt;
        }
        
    } //end viewBtn class

    /**********************
     * resetBtn class handler
     * Blanks out the title, tagsArea, and entryArea
     * asks if saving is necessary if not turned off in settings 
     **********************/
    public class resetBtnHandle implements EventHandler<ActionEvent> {
        public void handle (ActionEvent ae){
            
            boolean checkBlank = checkIfBlank();
            if(checkBlank != true){
                ButtonType results = alertDialogue("starting new?");
                
                if (results == ButtonType.YES){
                    saveMethod();
                    this.clearFields();
                }
                else if (results==ButtonType.NO){
                    this.clearFields();
                    
                } 
                
            }
        
        }
        
        /**********
         * Sets all the textfields and textareas to blank
         ***********/
        public void clearFields(){
            title.setText("");
            tagsArea.setText("");
            entryArea.setText("");
            displayDate.setText("Date: " + postingDate());
            todaysDate = postingDate();
            statusBar.setText("Reset all fields!");
        }
    } //resetbtnhandle end   
    
    /****************
     * handle exit file menuitem
     * Checks if user wishes to save before exiting
     ****************/
    public class exitBtnHandle implements EventHandler<ActionEvent> {
        public void handle(ActionEvent ae){
            boolean checkBlank = checkIfBlank();
            if (checkBlank == true){
                Platform.exit();
            }
            else{
                ButtonType results = alertDialogue("exiting");
                if(results == ButtonType.YES){
                    saveMethod();
                    Platform.exit();
                }
                else if(results == ButtonType.NO){
                    Platform.exit();
                }
            }
        }
    }
    
    public class dbWindow implements EventHandler<ActionEvent>{
        Stage dbStage = new Stage();
        TextField db_tf = new TextField();
        Button db_btn = new Button("Set");
        
        public void handle (ActionEvent ae){
            HBox db_box = dbBox();
            
            Scene dbScene = new Scene(db_box);
            dbStage.setScene(dbScene);
            dbStage.show();
        }
        
        public HBox dbBox(){
            HBox db_box = new HBox();
            db_box.setSpacing(10);
            db_btn.setOnAction(new setButton());
            db_box.getChildren().addAll(db_tf, db_btn);
            return db_box;
            
        }
        
        public class setButton implements EventHandler<ActionEvent>{
            
            public void handle(ActionEvent ae){
                setDb(db_tf.getText());
                listV.getItems().clear();
                listV = loadList();
            }
        }
        
    }
    
    
    /**************
     * Class handler for settings menuitem
     **************/
    public class SettingsWindow implements EventHandler<ActionEvent>{
        CheckBox saveCheck = new CheckBox();
        Button applyBtn = new Button("Apply");
        Button cancelBtn = new Button("Cancel");
        Stage settingsStage = new Stage();
        ComboBox cBox = new ComboBox();
        ComboBox sBox = new ComboBox();
        ComboBox fWeight = new ComboBox();
    
        /**********
         * Handler for menuitem settings
         * @param ae 
         ************/
        public void handle (ActionEvent ae){
            VBox  settingsBox = new VBox();
            HBox show_a = showAlerts();
            HBox applyBox = ApplyButtons();
            HBox fontBox = setFonts();
            HBox wBox = setFontWeight();
          //  HBox sizeBox = setFontSize();
            
            Label settingsLbl = new Label("Settings:");
            Separator fontSep = new Separator();
            Separator applySep = new Separator();
            
            Label fontLbl = new Label("Fonts:");
            
            settingsBox.getChildren().addAll(settingsLbl, show_a, fontSep, 
                    fontLbl , fontBox, wBox, applySep, applyBox);
            settingsBox.setSpacing(10);
            settingsBox.setAlignment(Pos.TOP_LEFT);
            
            Scene settingsScene = new Scene(settingsBox);
            
            settingsStage.setMinWidth(250);
            //settingsStage.setMinHeight(250);
            
            settingsStage.setScene(settingsScene);
            settingsStage.show();
            
            
        }
        
        /**************
         * HBox Constructor for font name and size
         * @return HBox
         **************/
        public HBox setFonts(){
            HBox fontBox = new HBox();
            
            for(String i : Font.getFontNames()){
                cBox.getItems().add(i);
            }
            Label fontLbl = new Label("Name:");
            cBox.setMaxWidth(150);
            cBox.setValue(fontName);
            
            for(double z = 1.0; z < 72.0; z++){
                sBox.getItems().add(z);
            }
            
            sBox.setMaxWidth(50);
            sBox.setValue(fontSize);
            
            Label sLabel = new Label("Size:");
            
            
            fontBox.getChildren().addAll(fontLbl, cBox, sLabel, sBox);
            fontBox.setSpacing(10);
            
            return fontBox;
        }
        /**************
         * Hbox constructor for fontweight label and combobox
         * @return HBox
         **************/
        public HBox setFontWeight(){
            HBox sizeBox = new HBox();
            Label  fwLbl = new Label("Font Weight: ");
            
            for(FontWeight i : FontWeight.values()){
                fWeight.getItems().add(i);
            }
            fWeight.setValue(fw);
            sizeBox.setSpacing(10);
            sizeBox.getChildren().addAll(fwLbl, fWeight);
            
            return sizeBox;
        }
        /*************
         * HBox constructor function for saveAlerts
         * @return HBox
         **************/
        public HBox showAlerts(){
            saveCheck.setSelected(showSaveAlert);
            
            HBox alert_q = new HBox();
            Label show_a = new Label("Show save alerts?");
            alert_q.getChildren().addAll(show_a, saveCheck);
            alert_q.setSpacing(15);
            alert_q.setAlignment(Pos.CENTER);
            
            return alert_q;
            
        }
        
        
        /**********
         * HBox constructor function for Apply/Cancel buttons
         * @return HBox
         **********/
        public HBox ApplyButtons(){
            HBox applyBox = new HBox();
            applyBox.setSpacing(10);
            applyBox.setAlignment(Pos.CENTER);
            applyBtn.setOnAction(new ApplyHandler());
            cancelBtn.setOnAction(new CancelHandler());
            
            applyBox.getChildren().addAll(applyBtn, cancelBtn);
            return applyBox;
        }
        
        /**********
         * Class for handling apply button actions
         * on settings menu
         *************/
        public class ApplyHandler  implements EventHandler<ActionEvent>{
            public void handle(ActionEvent ae){
                String fontString = (String)cBox.getValue();
                double sizeDouble = (double)sBox.getValue();
                FontWeight fwVal = (FontWeight)fWeight.getValue();
                if(saveCheck.isSelected()){
                    showSaveAlert = true;
                }
                else if (!saveCheck.isSelected()){
                    showSaveAlert = false;
                }
                
                if(fontString != fontName){
                    fontName = fontString;
                }
                
                if (sizeDouble != fontSize){
                    fontSize = sizeDouble;
                }
                
                if (fw != fwVal){
                    fw = fwVal;
                }
                
                entryArea.setFont(Font.font(fontName,fw, fontSize));
                tagsArea.setFont(Font.font(fontName));
                title.setFont(Font.font(fontName));
                
                settingsStage.close();
            }
        }
        
        /*********
         * class for handling Cancel button on settings menu
         **********/
        public class CancelHandler implements EventHandler<ActionEvent>{
            public void handle(ActionEvent ae){
                settingsStage.close();
            }
        }
    }
}