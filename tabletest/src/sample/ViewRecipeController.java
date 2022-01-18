package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ViewRecipeController {
    @FXML
    private Text des_text;
    @FXML
    private Text rName_text;
    @FXML
    private Text cookTime_text;
    @FXML
    private Text instr_text;
    @FXML
    private TableView<RecipeIngredient> ingr_table;
    @FXML
    private TableColumn<RecipeIngredient, String> amount_col;
    @FXML
    private TableColumn<RecipeIngredient, String> ingr_col;
    @FXML
    private TableColumn<RecipeIngredient, String> measure_col;


    ArrayList<FavoriteRecipeConstructor> recList;
    ObservableList<RecipeIngredient> ingrList = FXCollections.observableArrayList();
public void recDataLoad(ArrayList<FavoriteRecipeConstructor> favRecList){
    recList = favRecList;
    int recId = favRecList.get(0).getRec_id();
    String recName = favRecList.get(0).getRec_Name();
    String recDes = favRecList.get(0).getRec_des();
    String recCookTime = favRecList.get(0).getRec_cookTime();
    String recInstr = favRecList.get(0).getRec_instr();
    rName_text.setText(recName);
    des_text.setText(recDes);
    cookTime_text.setText(recCookTime);
    instr_text.setText(recInstr);
    String rid = String.valueOf(recId);
    loadRecipeIngredients(rid);

}
private void loadRecipeIngredients(String rec_id){
    try{
        String sql = "SELECT  recipe_ingredients.amount, recipe_ingredients.measure, Ingredients.name FROM users.Ingredients inner join users.recipe_ingredients on recipe_ingredients.i_id = Ingredients.ingr_id where r_id ='"+rec_id+"'";
        Connection con = DBConnection.getConnection();
        PreparedStatement preparedStatement = con.prepareStatement(sql);
        ResultSet rs = preparedStatement.executeQuery();
        while(rs.next()){
            ingrList.add(new RecipeIngredient(
                    rs.getString("amount"),
                    rs.getString("measure"),
                    rs.getString("name")
            ));
        }
    }catch (
            SQLException ex) {
        Logger.getLogger(ViewRecipeController.class.getName()).log(Level.SEVERE, null, ex);
    }
    amount_col.setCellValueFactory(new PropertyValueFactory<>("amount"));
    measure_col.setCellValueFactory(new PropertyValueFactory<>("measure"));
    ingr_col.setCellValueFactory(new PropertyValueFactory<>("rec_name"));
    ingr_table.setItems(ingrList);
    }
}
