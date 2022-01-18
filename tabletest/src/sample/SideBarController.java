package sample;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SideBarController implements Initializable {
    @FXML
    private ImageView logoView;
    @FXML
    private BorderPane mainPane;
    @FXML
    private BorderPane bPane2;
    @FXML
    private AnchorPane background;
    @FXML
    private FontAwesomeIconView home_b;
    @FXML
    private Label curUser_label;
    @FXML
    private FontAwesomeIconView friends_b;
    @FXML
    private FontAwesomeIconView browse_b;
    @FXML
    private FontAwesomeIconView calendar_b;
    @FXML
    private FontAwesomeIconView bell;
    @FXML
    private FontAwesomeIconView fav_b;
    @FXML
    Circle bell_circle;

    @FXML
    private Button closefB;
    User selectedUser = new User();

    ArrayList<User> MuserList;
    public void initialize(URL url, ResourceBundle rb){
        File lockFile = new File("images/ric_logo.png");
        Image logoImage = new Image(lockFile.toURI().toString());
        logoView.setImage(logoImage);

        fmxlLoader object = new fmxlLoader();
        Pane view = object.getPage("Dashboard");
        mainPane.setCenter(view);
        bell_circle.setVisible(false);

    }

    public void dataLoad(ArrayList<User> userList){
        MuserList = userList;
        curUser_label.setText("Hi, " +userList.get(0).getLFname());
        selectedUser.setLusername(userList.get(0).getLusername());
        String cUser = String.valueOf(userList.get(0).getLid());

    }

    @FXML
    private void HomePage (MouseEvent event){
        fmxlLoader object = new fmxlLoader();
        Pane view = object.getPage("Dashboard");
        mainPane.setCenter(view);

    }
    @FXML
    private void FriendsPage(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("FriendsPage.fxml"));
        Pane view = loader.load();
        FriendsTableController friendsTableController = loader.getController();
        LoginService ls = new LoginService();
        ArrayList<User> fUserlist = ls.getUser(selectedUser.getLusername());
        friendsTableController.FdataLoad(fUserlist);
        mainPane.setCenter(view);

    }
    @FXML
    private void favoritePage(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("FavoriteRecipes.fxml"));
        Pane view = loader.load();
        FavoriteRecipesController favoriteRecipesController = loader.getController();
        LoginService ls = new LoginService();
        ArrayList<User> favUserlist = ls.getUser(selectedUser.getLusername());
        favoriteRecipesController.favRecipeDataLoad(favUserlist);
        mainPane.setCenter(view);
    }
    @FXML
    private void Browse_page(MouseEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("BrowsePage.fxml"));
        Pane view = loader.load();
        BrowsePageController browsePageController = loader.getController();
        LoginService ls = new LoginService();
        ArrayList<User> bUserlist = ls.getUser(selectedUser.getLusername());
        browsePageController.browseRecipeDataLoad(bUserlist);
        mainPane.setCenter(view);

    }
    @FXML
    private void Grocery_Page(MouseEvent event) throws IOException {
        //fmxlLoader object = new fmxlLoader();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("GroceryPage.fxml"));
        Pane view = loader.load();
        GroceryListController groceryListController = loader.getController();
        LoginService ls = new LoginService();
        ArrayList<User> grocUserList = ls.getUser(selectedUser.getLusername());
        groceryListController.GroceryDataLoad(grocUserList);
        mainPane.setCenter(view);
    }
    @FXML
    private void Calander_Page(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("calendarPage.fxml"));
        Pane view = loader.load();
        CalendarContoller calendarContoller = loader.getController();
        LoginService ls = new LoginService();
        ArrayList<User> calUserlist = ls.getUser(selectedUser.getLusername());
        calendarContoller.calendarDataLoad(calUserlist);
        mainPane.setCenter(view);
    }


    @FXML
    private void show_request(MouseEvent event){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("FriendRequest.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            FriendRequest friendRequest = loader.getController();
            LoginService ls = new LoginService();
            ArrayList<User> frUserlist = ls.getUser(selectedUser.getLusername());
            friendRequest.frLoadData(frUserlist);

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
        }   catch (IOException ex) {
            Logger.getLogger(FriendsTableController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    public void change2friendsPage() throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("FriendsPage.fxml"));
        Parent root = loader.load();
        Scene mainScene = new Scene(root);
        FriendsTableController friendsTableController = loader.getController();
        LoginService ls = new LoginService();
        ArrayList<User> userList = ls.getUser(selectedUser.getLusername());
        friendsTableController.FdataLoad(userList);

        Stage primaryStage = (Stage) friends_b.getScene().getWindow();
        primaryStage.setScene(mainScene);
        primaryStage.show();
        // access the controller
    }


}

