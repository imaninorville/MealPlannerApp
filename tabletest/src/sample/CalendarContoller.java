package sample;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CalendarContoller implements Initializable {

    @FXML
    private ComboBox<FavoriteRecipeConstructor> monday_menu;
    @FXML
    private ComboBox<FavoriteRecipeConstructor> tues_menu;
    @FXML
    private ComboBox<FavoriteRecipeConstructor> wed_menu;
    @FXML
    private ComboBox<FavoriteRecipeConstructor> thur_menu;
    @FXML
    private ComboBox<FavoriteRecipeConstructor> fri_menu;
    @FXML
    private ComboBox<FavoriteRecipeConstructor> sat_menu;
    @FXML
    private ComboBox<FavoriteRecipeConstructor> sun_menu;
    @FXML
    private Text mon_text;
    @FXML
    private Text tues_text;
    @FXML
    private Text wed_text;
    @FXML
    private Text thur_text;
    @FXML
    private Text fri_text;
    @FXML
    private Text sat_text;
    @FXML
    private Text sun_text;
    @FXML
    private Button create_meal_but;
    @FXML
    private FontAwesomeIconView mon_clear;
    @FXML
    private FontAwesomeIconView tues_clear;
    @FXML
    private FontAwesomeIconView wed_clear;
    @FXML
    private FontAwesomeIconView thur_clear;
    @FXML
    private FontAwesomeIconView fri_clear;
    @FXML
    private FontAwesomeIconView sat_clear;
    @FXML
    private FontAwesomeIconView sun_clear;

    //current user

    String curUser = null;
    User selectedUser = new User();
    MealPlan curMealPlan = new MealPlan();
    RecipeDetails rd = new RecipeDetails();
    ArrayList<User> calUser;
    ArrayList<MealPlan> mealList;
    final String monId = null;
    String id;
            //tuesId, wedId, thurId, friId, satId, sunId = null;



    @Override
    public void initialize(URL location, ResourceBundle resources) {


        //hide clear buttons
        mon_clear.setVisible(false);
        tues_clear.setVisible(false);
        wed_clear.setVisible(false);
        thur_clear.setVisible(false);
        fri_clear.setVisible(false);
        sat_clear.setVisible(false);
        sun_clear.setVisible(false);

    }
    public void calendarDataLoad(ArrayList<User> calUserList){
        calUser = calUserList;
        selectedUser.setLid(calUserList.get(0).getLid());
        curUser = String.valueOf(selectedUser.getLid());
        mealList = rd.getMealPlan(curUser);
        //check if meal plan exists for user
        if (checkMeal(curUser)== false){
            create_meal_but.setVisible(true);
            monday_menu.setVisible(false);
            tues_menu.setVisible(false);
            wed_menu.setVisible(false);
            thur_menu.setVisible(false);
            fri_menu.setVisible(false);
            sat_menu.setVisible(false);
            sun_menu.setVisible(false);

        }else{
            loadCurrentMealPlan();
            create_meal_but.setVisible(false);

        }
        //load users current meal plan


        //get favorite recipes list based on current user
        ObservableList<FavoriteRecipeConstructor> favRecList = rd.getFavRecipes(curUser);

        StringConverter<FavoriteRecipeConstructor> monConverter = getRecName(monday_menu);
        StringConverter<FavoriteRecipeConstructor> tueConverter = getRecName(tues_menu);
        StringConverter<FavoriteRecipeConstructor> wedConverter = getRecName(wed_menu);
        StringConverter<FavoriteRecipeConstructor> thurConverter = getRecName(thur_menu);
        StringConverter<FavoriteRecipeConstructor> friConverter = getRecName(fri_menu);
        StringConverter<FavoriteRecipeConstructor> satConverter = getRecName(sat_menu);
        StringConverter<FavoriteRecipeConstructor> sunConverter = getRecName(monday_menu);

        //set items to dropdown list
        monday_menu.setItems(favRecList);
        monday_menu.setConverter(monConverter);
        tues_menu.setItems(favRecList);
        tues_menu.setConverter(tueConverter);
        wed_menu.setItems(favRecList);
        wed_menu.setConverter(wedConverter);
        thur_menu.setItems(favRecList);
        thur_menu.setConverter(thurConverter);
        fri_menu.setItems(favRecList);
        fri_menu.setConverter(friConverter);
        sat_menu.setItems(favRecList);
        sat_menu.setConverter(satConverter);
        sun_menu.setItems(favRecList);
        sun_menu.setConverter(sunConverter);

        // Create action event
        EventHandler<ActionEvent> MonEvent = getMonEvent(monday_menu);
        EventHandler<ActionEvent> tueEvent = getTuesEvent(tues_menu);
        EventHandler<ActionEvent> wedEvent = getWedEvent(wed_menu);
        EventHandler<ActionEvent> thurEvent = getThurEvent(thur_menu);
        EventHandler<ActionEvent> friEvent = getFriEvent(fri_menu);
        EventHandler<ActionEvent> satEvent = getSatEvent(sat_menu);
        EventHandler<ActionEvent> sunEvent = getSunEvent(sun_menu);

        // Set action event
        monday_menu.setOnAction(MonEvent);
        tues_menu.setOnAction(tueEvent);
        wed_menu.setOnAction(wedEvent);
        thur_menu.setOnAction(thurEvent);
        fri_menu.setOnAction(friEvent);
        sat_menu.setOnAction(satEvent);
        sun_menu.setOnAction(sunEvent);

        }
    private void loadCurrentMealPlan(){
        int monId = mealList.get(0).getMon_meal();
        int tueId = mealList.get(0).getTues_meal();
        int wedId = mealList.get(0).getWed_meal();
        int thurId = mealList.get(0).getThur_meal();
        int friId = mealList.get(0).getFri_meal();
        int satId = mealList.get(0).getSat_meal();
        int sunId = mealList.get(0).getSun_meal();
        if(monId != 0) {
            String curMonMeal = rd.getRecipeName(String.valueOf(monId));
            mon_text.setText(curMonMeal);
        }
        if(tueId != 0){
            String curTuesMeal = rd.getRecipeName(String.valueOf(tueId));
            tues_text.setText(curTuesMeal);
        }
        if(wedId != 0){
            String curWedMeal = rd.getRecipeName(String.valueOf(wedId));
            wed_text.setText(curWedMeal);
        }
        if(thurId != 0){
            String curThurMeal = rd.getRecipeName(String.valueOf(thurId));
            thur_text.setText(curThurMeal);
        }
        if(friId != 0){
            String curFriMeal = rd.getRecipeName(String.valueOf(friId));
            fri_text.setText(curFriMeal);
        }
        if(satId != 0){
            String curSatMeal = rd.getRecipeName(String.valueOf(satId));
            sat_text.setText(curSatMeal);
        }
        if(sunId != 0){
            String curSunMeal = rd.getRecipeName(String.valueOf(sunId));
            sun_text.setText(curSunMeal);
        }
    }
    @FXML
    private void createNewMealPlan (ActionEvent event ){
        int u_id = selectedUser.getLid();
        newMealPlan(String.valueOf(u_id));
        create_meal_but.setVisible(false);
        monday_menu.setVisible(true);
        tues_menu.setVisible(true);
        wed_menu.setVisible(true);
        thur_menu.setVisible(true);
        fri_menu.setVisible(true);
        sat_menu.setVisible(true);
        sun_menu.setVisible(true);

    }


    @FXML
    private void clearMenuSelection(javafx.scene.input.MouseEvent mouseEvent) {
        monday_menu.getSelectionModel().clearSelection();
        mon_clear.setVisible(false);
        mon_text.setText("");
        resetMonday(curUser);

    }
    @FXML
    private void tuesClearSelection(javafx.scene.input.MouseEvent mouseEvent) {
        tues_menu.getSelectionModel().clearSelection();
        tues_clear.setVisible(false);
        tues_text.setText("");
        resetTuesday(curUser);
    }
    @FXML
    private void wedClearSelection(javafx.scene.input.MouseEvent mouseEvent) {
        wed_menu.getSelectionModel().clearSelection();
        wed_clear.setVisible(false);
        wed_text.setText("");
        resetWednesday(curUser);
    }
    @FXML
    private void thurClearSelection(javafx.scene.input.MouseEvent mouseEvent) {
        thur_menu.getSelectionModel().clearSelection();
        thur_clear.setVisible(false);
        thur_text.setText("");
        resetThursday(curUser);
    }
    @FXML
    private void friClearSelection(javafx.scene.input.MouseEvent mouseEvent) {
        fri_menu.getSelectionModel().clearSelection();
        fri_clear.setVisible(false);
        fri_text.setText("");
        resetFriday(curUser);
    }
    @FXML
    private void satClearSelection(javafx.scene.input.MouseEvent mouseEvent) {
        sat_menu.getSelectionModel().clearSelection();
        sat_clear.setVisible(false);
        sat_text.setText("");
        resetSaturday(curUser);
    }
    @FXML
    private void sunClearSelection(javafx.scene.input.MouseEvent mouseEvent) {
        sun_menu.getSelectionModel().clearSelection();
        sun_clear.setVisible(false);
        sun_text.setText("");
        resetSunday(curUser);
    }

//method to display recipe names from favorite recipes list, converts recipe object to a simple recipe name string
private StringConverter<FavoriteRecipeConstructor> getRecName(ComboBox<FavoriteRecipeConstructor> menuButton){
    StringConverter<FavoriteRecipeConstructor> converter = new StringConverter<FavoriteRecipeConstructor>() {
        @Override
        public String toString(FavoriteRecipeConstructor favoriteRecipeConstructor) {
            return favoriteRecipeConstructor.getRec_Name();
        }

        @Override
        public FavoriteRecipeConstructor fromString(String s) {
            return menuButton.getItems().stream().filter(fav -> fav.getRec_Name().equals(s)).findFirst().orElse(null);
        }
    };
    return converter;
}
//method to get selected menu item, update database,  and set text to name of selected recipe
private EventHandler<ActionEvent> getMonEvent (ComboBox<FavoriteRecipeConstructor> menu){
        EventHandler<javafx.event.ActionEvent> event =
                new EventHandler<javafx.event.ActionEvent>() {
                    public void handle(javafx.event.ActionEvent e) {
                        if (menu.getValue() != null) {
                            int dayId = menu.getValue().getRec_id();
                            String monMeal = menu.getValue().getRec_Name();
                            String rec_id = String.valueOf(dayId);
                            updateMonday(rec_id, curUser);
                            mon_text.setText(monMeal);
                            mon_clear.setVisible(true);
                        }
                    }
                };
        return event;
    }

    private EventHandler<ActionEvent> getTuesEvent (ComboBox<FavoriteRecipeConstructor> menu){
        EventHandler<javafx.event.ActionEvent> event =
                new EventHandler<javafx.event.ActionEvent>() {
                    public void handle(javafx.event.ActionEvent e)
                    {
                        if (menu.getValue() != null) {
                            int dayId = menu.getValue().getRec_id();
                            String monMeal = menu.getValue().getRec_Name();
                            String rec_id = String.valueOf(dayId);
                            updateTuesday(rec_id, curUser);
                            tues_text.setText(monMeal);
                            tues_clear.setVisible(true);
                        }
                    }
                };
        return event;
    }
    private EventHandler<ActionEvent> getWedEvent (ComboBox<FavoriteRecipeConstructor> menu){
        EventHandler<javafx.event.ActionEvent> event =
                new EventHandler<javafx.event.ActionEvent>() {
                    public void handle(javafx.event.ActionEvent e) {
                        if (menu.getValue() != null) {
                            int dayId = menu.getValue().getRec_id();
                            String monMeal = menu.getValue().getRec_Name();
                            String rec_id = String.valueOf(dayId);
                            updateWednesday(rec_id, curUser);
                            wed_text.setText(monMeal);
                            wed_clear.setVisible(true);
                        }
                    }
                };
        return event;
    }
    private EventHandler<ActionEvent> getThurEvent (ComboBox<FavoriteRecipeConstructor> menu){
        EventHandler<javafx.event.ActionEvent> event =
                new EventHandler<javafx.event.ActionEvent>() {
                    public void handle(javafx.event.ActionEvent e) {
                        if (menu.getValue() != null) {
                            int dayId = menu.getValue().getRec_id();
                            String monMeal = menu.getValue().getRec_Name();
                            String rec_id = String.valueOf(dayId);
                            updateThursday(rec_id, curUser);
                            thur_text.setText(monMeal);
                            thur_clear.setVisible(true);

                        }
                    }
                };
        return event;
    }
    private EventHandler<ActionEvent> getFriEvent (ComboBox<FavoriteRecipeConstructor> menu){
        EventHandler<javafx.event.ActionEvent> event =
                new EventHandler<javafx.event.ActionEvent>() {
                    public void handle(javafx.event.ActionEvent e) {
                        if (menu.getValue() != null) {
                            int dayId = menu.getValue().getRec_id();
                            String monMeal = menu.getValue().getRec_Name();
                            String rec_id = String.valueOf(dayId);
                            updateFriday(rec_id, curUser);
                            fri_text.setText(monMeal);
                            fri_clear.setVisible(true);
                        }
                    }
                };
        return event;
    }
    private EventHandler<ActionEvent> getSatEvent (ComboBox<FavoriteRecipeConstructor> menu){
        EventHandler<javafx.event.ActionEvent> event =
                new EventHandler<javafx.event.ActionEvent>() {
                    public void handle(javafx.event.ActionEvent e) {
                        if (menu.getValue() != null) {
                            int dayId = menu.getValue().getRec_id();
                            String monMeal = menu.getValue().getRec_Name();
                            String rec_id = String.valueOf(dayId);
                            updateSaturday(rec_id, curUser);
                            sat_text.setText(monMeal);
                            sat_clear.setVisible(true);
                        }
                    }
                };
        return event;
    }
    private EventHandler<ActionEvent> getSunEvent (ComboBox<FavoriteRecipeConstructor> menu){
        EventHandler<javafx.event.ActionEvent> event =
                new EventHandler<javafx.event.ActionEvent>() {
                    public void handle(javafx.event.ActionEvent e) {
                        if (menu.getValue() != null) {
                            int dayId = menu.getValue().getRec_id();
                            String monMeal = menu.getValue().getRec_Name();
                            String rec_id = String.valueOf(dayId);
                            updateSunday(rec_id, curUser);
                            sun_text.setText(monMeal);
                            sun_clear.setVisible(true);
                        }
                    }
                };
        return event;
    }


    //checks if mealPlan exists for current user
    private Boolean checkMeal(String id) {
        Boolean result = true;

        try {
            String sql = "SELECT * FROM users.meal_plan where u_id ='" + id + "';";
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement((sql));
            ResultSet rs = ps.executeQuery(sql);
            if (rs.next() == false) {
                result = false;
            } else {
                result = true;
            }

        } catch (SQLException ex) {
            Logger.getLogger(CalendarContoller.class.getName()).log(Level.SEVERE, null, ex);

        }
        return result;
    }

    //create new meal plan for new user
    private void newMealPlan(String id){
    try{
        String sql = "Insert into users.meal_plan (mon_meal, tues_meal, wed_meal, thur_meal, fri_meal, sat_meal, sun_meal, u_id) values ('0', '0', '0', '0', '0', '0', '0','"+id+"')";
        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement((sql));
        Boolean rs = ps.execute();
    }catch (SQLException ex) {
        Logger.getLogger(CalendarContoller.class.getName()).log(Level.SEVERE, null, ex);

    }
    }
    /////////////////update meal queries////////////////
    //update monday meal
    private void updateMonday (String rec_id, String u_id){
        try{
            String sql = "update users.meal_plan set mon_meal = '"+rec_id+"' where u_id = '"+u_id+"'";
            Connection con = DBConnection.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement((sql));
            Boolean rs = preparedStatement.execute();
        }catch (SQLException ex) {
            Logger.getLogger(CalendarContoller.class.getName()).log(Level.SEVERE, null, ex);

        }
    }
    private void updateTuesday (String rec_id, String u_id){
        try{
            String sql = "update users.meal_plan set tues_meal = '"+rec_id+"' where u_id = '"+u_id+"'";
            Connection con = DBConnection.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement((sql));
            Boolean rs = preparedStatement.execute();
        }catch (SQLException ex) {
            Logger.getLogger(CalendarContoller.class.getName()).log(Level.SEVERE, null, ex);

        }
    }
    private void updateWednesday (String rec_id, String u_id){
        try{
            String sql = "update users.meal_plan set wed_meal = '"+rec_id+"' where u_id = '"+u_id+"'";
            Connection con = DBConnection.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement((sql));
            Boolean rs = preparedStatement.execute();
        }catch (SQLException ex) {
            Logger.getLogger(CalendarContoller.class.getName()).log(Level.SEVERE, null, ex);

        }
    }
    private void updateThursday (String rec_id, String u_id){
        try{
            String sql = "update users.meal_plan set thur_meal = '"+rec_id+"' where u_id = '"+u_id+"'";
            Connection con = DBConnection.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement((sql));
            Boolean rs = preparedStatement.execute();
        }catch (SQLException ex) {
            Logger.getLogger(CalendarContoller.class.getName()).log(Level.SEVERE, null, ex);

        }
    }
    private void updateFriday (String rec_id, String u_id){
        try{
            String sql = "update users.meal_plan set fri_meal = '"+rec_id+"' where u_id = '"+u_id+"'";
            Connection con = DBConnection.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement((sql));
            Boolean rs = preparedStatement.execute();
        }catch (SQLException ex) {
            Logger.getLogger(CalendarContoller.class.getName()).log(Level.SEVERE, null, ex);

        }
    }
    private void updateSaturday (String rec_id, String u_id){
        try{
            String sql = "update users.meal_plan set sat_meal = '"+rec_id+"' where u_id = '"+u_id+"'";
            Connection con = DBConnection.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement((sql));
            Boolean rs = preparedStatement.execute();
        }catch (SQLException ex) {
            Logger.getLogger(CalendarContoller.class.getName()).log(Level.SEVERE, null, ex);

        }
    }
    private void updateSunday (String rec_id, String u_id){
        try{
            String sql = "update users.meal_plan set sun_meal = '"+rec_id+"' where u_id = '"+u_id+"'";
            Connection con = DBConnection.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement((sql));
            Boolean rs = preparedStatement.execute();
        }catch (SQLException ex) {
            Logger.getLogger(CalendarContoller.class.getName()).log(Level.SEVERE, null, ex);

        }
    }
    ///////////////////////reset queries/////////////////////////////
    private void resetMonday (String u_id){
        try{
            String sql = "update users.meal_plan set mon_meal = '0' where u_id = '"+u_id+"'";
            Connection con = DBConnection.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement((sql));
            Boolean rs = preparedStatement.execute();
        }catch (SQLException ex) {
            Logger.getLogger(CalendarContoller.class.getName()).log(Level.SEVERE, null, ex);

        }
    }
    private void resetTuesday (String u_id){
        try{
            String sql = "update users.meal_plan set tues_meal = '0' where u_id = '"+u_id+"'";
            Connection con = DBConnection.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement((sql));
            Boolean rs = preparedStatement.execute();
        }catch (SQLException ex) {
            Logger.getLogger(CalendarContoller.class.getName()).log(Level.SEVERE, null, ex);

        }
    }
    private void resetWednesday (String u_id){
        try{
            String sql = "update users.meal_plan set wed_meal = '0' where u_id = '"+u_id+"'";
            Connection con = DBConnection.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement((sql));
            Boolean rs = preparedStatement.execute();
        }catch (SQLException ex) {
            Logger.getLogger(CalendarContoller.class.getName()).log(Level.SEVERE, null, ex);

        }
    }
    private void resetThursday (String u_id){
        try{
            String sql = "update users.meal_plan set thur_meal = '0' where u_id = '"+u_id+"'";
            Connection con = DBConnection.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement((sql));
            Boolean rs = preparedStatement.execute();
        }catch (SQLException ex) {
            Logger.getLogger(CalendarContoller.class.getName()).log(Level.SEVERE, null, ex);

        }
    }
    private void resetFriday (String u_id){
        try{
            String sql = "update users.meal_plan set fri_meal = '0' where u_id = '"+u_id+"'";
            Connection con = DBConnection.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement((sql));
            Boolean rs = preparedStatement.execute();
        }catch (SQLException ex) {
            Logger.getLogger(CalendarContoller.class.getName()).log(Level.SEVERE, null, ex);

        }
    }
    private void resetSaturday (String u_id){
        try{
            String sql = "update users.meal_plan set sat_meal = '0' where u_id = '"+u_id+"'";
            Connection con = DBConnection.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement((sql));
            Boolean rs = preparedStatement.execute();
        }catch (SQLException ex) {
            Logger.getLogger(CalendarContoller.class.getName()).log(Level.SEVERE, null, ex);

        }
    }
    private void resetSunday (String u_id){
        try{
            String sql = "update users.meal_plan set sun_meal = '0' where u_id = '"+u_id+"'";
            Connection con = DBConnection.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement((sql));
            Boolean rs = preparedStatement.execute();
        }catch (SQLException ex) {
            Logger.getLogger(CalendarContoller.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

}


