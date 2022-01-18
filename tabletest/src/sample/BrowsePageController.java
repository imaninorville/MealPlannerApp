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
import javafx.scene.control.*;
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

public class BrowsePageController implements Initializable {
    @FXML
    private TableView<SearchRecipe> rec_table;
    @FXML
    private TableColumn<SearchRecipe, String> rec_col;
    @FXML
    private TableColumn<SearchRecipe, String> d_col;
    @FXML
    private TableColumn<SearchRecipe, String> add_col;
    @FXML
    private TextField search_b;
    @FXML
    private Label label1;
    @FXML
    private Label label2;
    @FXML
    private FontAwesomeIconView s_but;
    @FXML
    private FontAwesomeIconView home_b;
    String curUser = null;
    User selectedUser = new User();
    ArrayList<User> browseUserList;

    ObservableList<SearchRecipe> reclist = FXCollections.observableArrayList();

    public void browseRecipeDataLoad(ArrayList<User> userList){
        browseUserList = userList;
        selectedUser.setLid(userList.get(0).getLid());
        curUser = String.valueOf(selectedUser.getLid());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    private void search(MouseEvent event) {
        reclist.clear();
        //get text from search bar
        String search = search_b.getText();
        try {
            String sql = "SELECT recipes.recipe_id, recipes.rName, recipes.description, recipes.rType from users.recipes where rName LIKE '%" + search + "%'";
            Connection con = DBConnection.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                reclist.add(new SearchRecipe(
                        rs.getInt("recipe_id"),
                        rs.getString("rName"),
                        rs.getString("description"),
                        rs.getInt("rType")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(BrowsePageController.class.getName()).log(Level.SEVERE, null, ex);
        }
        rec_col.setCellValueFactory(new PropertyValueFactory<>("Rname"));
        d_col.setCellValueFactory(new PropertyValueFactory<>("Rdes"));

        Callback<TableColumn<SearchRecipe, String>, TableCell<SearchRecipe, String>> cellFactory = (TableColumn<SearchRecipe, String> param) -> {
            final TableCell<SearchRecipe, String> cell = new TableCell<SearchRecipe, String>() {
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
                            SearchRecipe rec = rec_table.getSelectionModel().getSelectedItem();
                            int selectedRec_id = rec.getR_id();
                            String sR_id = String.valueOf(selectedRec_id);
                            if (checkRecipe(curUser, sR_id) == true) {
                                String message = "Recipe already added to favorites list";
                                label2.setText(message);
                            } else {
                                addToFavorites(curUser, sR_id);
                                label2.setStyle("-fx-text-fill: green;");
                                String message2 = "Recipe added to favorites";
                                label2.setText(message2);
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
        add_col.setCellFactory(cellFactory);
        rec_table.setItems(reclist);
    }

//add recipe to favorites list
    private void addToFavorites(String id, String r_id){
        try{
            String sql = "Insert into favorite_recipes (recipe_id, u_id) values ('"+r_id+"','"+id+"')";
            Connection con = DBConnection.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement((sql));
            Boolean rs = preparedStatement.execute();
        } catch (SQLException ex) {
            Logger.getLogger(BrowsePageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //check if recipe has already been added to users favorite list
    private Boolean checkRecipe(String id, String rec_id){
                    boolean res = true;

                    try {
                        String sql = "SELECT * FROM users.favorite_recipes where recipe_id = '" + rec_id + "' and u_id = '" + id + "';";
                        Connection con = DBConnection.getConnection();
                        PreparedStatement ps = con.prepareStatement((sql));
                        ResultSet rs = ps.executeQuery();
                        if (rs.next() == false) {
                            res = false;
                        } else {
                            res = true;
                        }
                    }catch (SQLException ex) {
                            Logger.getLogger(BrowsePageController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    return res;
    }

}




