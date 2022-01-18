package sample;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.shape.Rectangle;

import java.awt.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GroceryListController implements Initializable {
    @FXML
    private Label label1;
    @FXML
    private TableView<String> groc_table;
    @FXML
    private TableColumn<String, String> ingr_col;
    @FXML
    TilePane pane;
    @FXML
    AnchorPane pane2;


    ArrayList<User> gUserList;
    ArrayList<MealPlan> currMealPlan;
    User curUser = new User();
    RecipeDetails rd = new RecipeDetails();
    String cUserId = null;
    ArrayList<String> ingrList = new ArrayList<String>();
    ArrayList<String> initGroceryList = new ArrayList<String>();
    ArrayList<String> finalGroceryList = new ArrayList<String>();

    public void initialize(URL location, ResourceBundle resources) {

    }

    public void GroceryDataLoad(ArrayList<User> curUserList) {
        gUserList = curUserList;
        curUser.setLid(curUserList.get(0).getLid());
        cUserId = String.valueOf(curUser.getLid());

        if (checkMeal(cUserId)==true) {
            loadGroceryList();

        }

    }
    //load and print grocery list
    private void loadGroceryList(){
        currMealPlan = rd.getMealPlan(cUserId);
        int monMeal = (currMealPlan.get(0).getMon_meal());
        int tuesMeal = (currMealPlan.get(0).getTues_meal());
        int wedMeal = (currMealPlan.get(0).getWed_meal());
        int thurMeal = (currMealPlan.get(0).getThur_meal());
        int friMeal = (currMealPlan.get(0).getFri_meal());
        int satMeal = (currMealPlan.get(0).getSat_meal());
        int sunMeal = (currMealPlan.get(0).getSun_meal());
        if (monMeal != 0){
            initGroceryList.addAll(loadRecipeIngredients(String.valueOf(monMeal)));
        }
        if (tuesMeal != 0){
            initGroceryList.addAll(loadRecipeIngredients(String.valueOf(tuesMeal)));
        }
        if (wedMeal != 0){
            initGroceryList.addAll(loadRecipeIngredients(String.valueOf(wedMeal)));
        }
        if (thurMeal != 0){
            initGroceryList.addAll(loadRecipeIngredients(String.valueOf(thurMeal)));
        }
        if (friMeal != 0){
            initGroceryList.addAll(loadRecipeIngredients(String.valueOf(friMeal)));
        }
        if (satMeal != 0){
            initGroceryList.addAll(loadRecipeIngredients(String.valueOf(satMeal)));
        }
        if (sunMeal != 0){
            initGroceryList.addAll(loadRecipeIngredients(String.valueOf(sunMeal)));
        }

        finalGroceryList = removeDuplicates(initGroceryList);

        for (String s : finalGroceryList){
            FontAwesomeIconView checkIcon = new FontAwesomeIconView(FontAwesomeIcon.CIRCLE);
            checkIcon.setStyle(
                    " -fx-cursor: hand ;"
                            + "-glyph-size:10px;"
                            + "-fx-fill:#0f27ff;"
            );

            Label label = new Label(s, checkIcon);
            HBox addButton = new HBox(label);
            label.setStyle("-fx-text-fill: 00E676;"+" -fx-font-size:15px;");
            pane.getChildren().add(addButton);

        }
    }
    //load recipe ingredients into array
    private ArrayList<String> loadRecipeIngredients(String rec_id) {

        try {
            String sql = "SELECT Ingredients.name FROM users.Ingredients inner join users.recipe_ingredients on recipe_ingredients.i_id = Ingredients.ingr_id where r_id = '" + rec_id + "'";
            Connection con = DBConnection.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {

                ingrList.add(rs.getString("name"));

            }

        } catch (
                SQLException ex) {
            Logger.getLogger(ViewRecipeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ingrList;
    }
    //remove duplicate array elements
    public static <T> ArrayList<T> removeDuplicates(ArrayList<T> list) {

        // Create a new ArrayList
        ArrayList<T> newList = new ArrayList<T>();

        // Traverse through the first list
        for (T element : list) {

            // If this element is not present in newList
            // then add it
            if (!newList.contains(element)) {

                newList.add(element);
            }
        }

        // return the new list
        return newList;
    }
    //checks if meal plan exists in database
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
}
