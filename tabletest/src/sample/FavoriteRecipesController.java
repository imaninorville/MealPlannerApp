package sample;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FavoriteRecipesController implements Initializable {
    @FXML
    private TableView<FavoriteRecipeConstructor> favRec_table;
    @FXML
    private TableColumn<FavoriteRecipeConstructor, String> fav_col;
    @FXML
    private TableColumn<FavoriteRecipeConstructor, String> view_col;
    @FXML
    private Label label1;
    @FXML
    private FontAwesomeIconView home_b;
    String curUser = null;
    User selectedUser = new User();
    ArrayList<User> favUserList;
    int selectedRec_id;
    String sR_id;
    ObservableList<FavoriteRecipeConstructor> favReclist = FXCollections.observableArrayList();


    public void favRecipeDataLoad(ArrayList<User> userList) {
        favUserList = userList;
        selectedUser.setLid(userList.get(0).getLid());
        curUser = String.valueOf(selectedUser.getLid());
        loadFavoriteRecipes(curUser);
        label1.setText(userList.get(0).getLFname()+"'s Favorites");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    private void loadFavoriteRecipes(String u_id) {
        try {
            String sql = "SELECT recipes.recipe_id, recipes.rName, recipes.description, recipes.instructions, recipes.cook_time from users.recipes inner join users.favorite_recipes on recipes.recipe_id = favorite_recipes.recipe_id where u_id = '" + u_id + "'";
            Connection con = DBConnection.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                favReclist.add(new FavoriteRecipeConstructor(
                        rs.getInt("recipe_id"),
                        rs.getString("rName"),
                        rs.getString("description"),
                        rs.getString("instructions"),
                        rs.getString("cook_time")
                ));
            }
        } catch (
                SQLException ex) {
            Logger.getLogger(FavoriteRecipesController.class.getName()).log(Level.SEVERE, null, ex);
        }
        fav_col.setCellValueFactory(new PropertyValueFactory<>("rec_Name"));

        Callback<TableColumn<FavoriteRecipeConstructor, String>, TableCell<FavoriteRecipeConstructor, String>> cellFactory = (TableColumn<FavoriteRecipeConstructor, String> param) -> {
            final TableCell<FavoriteRecipeConstructor, String> cell = new TableCell<FavoriteRecipeConstructor, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    //that cell created only on non-empty rows
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        FontAwesomeIconView addIcon = new FontAwesomeIconView(FontAwesomeIcon.PLUS_SQUARE);
                        addIcon.setStyle(
                                " -fx-cursor: hand ;"
                                        + "-glyph-size:28px;"
                                        + "-fx-fill:#00E676;"
                        );
                        addIcon.setOnMouseClicked((MouseEvent event) -> {
                            FavoriteRecipeConstructor favRec = favRec_table.getSelectionModel().getSelectedItem();
                            selectedRec_id = favRec.getRec_id();
                            sR_id = String.valueOf(selectedRec_id);
                            try {
                                showRecipe();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        HBox addButton = new HBox(addIcon);
                        addButton.setStyle("-fx-alignment:center");
                        HBox.setMargin(addIcon, new Insets(2, 3, 0, 2));
                        setGraphic(addButton);
                        setText(null);
                    }
                }
            };
            return cell;
        };
        view_col.setCellFactory(cellFactory);
        favRec_table.setItems(favReclist);

    }


public void showRecipe() throws IOException{
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("ViewRecipe.fxml"));
                        Parent root = loader.load();
                        Scene mainScene = new Scene(root);
                        ViewRecipeController viewRecipeController = loader.getController();
                        RecipeDetails rd = new RecipeDetails();
                        ArrayList<FavoriteRecipeConstructor> recList = rd.getRecipe(sR_id);
                        viewRecipeController.recDataLoad(recList);

                        Stage primaryStage = new Stage();
                        primaryStage.setScene(mainScene);
                        primaryStage.show();
                    }
                    }
