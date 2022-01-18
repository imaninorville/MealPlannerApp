package sample;
// controls fxml clickable items

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


public class LoginController implements Initializable {

    @FXML
    private Button cancelButton;
    @FXML
    private Button loginButton;
    @FXML
    private Label loginMessageLabel;
    @FXML
    private ImageView brandingImageView;
    @FXML
    private ImageView lockImageView;
    @FXML
    public TextField usernameTextField ;
    @FXML
    private PasswordField enterPasswordField;
    @FXML
    private Label label1;
    public String curUser = null;
    public String c = "frank78";
    User user = new User();
   // String current = null;


   // ObservableList<User> userlist = FXCollections.observableArrayList();


    public LoginController() {

    }
    public void setLLuser( String curUser){
        this.curUser = curUser;
    }
    public String  getLLuser(){
        return curUser;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File brandingFile = new File("images/branding.png");
        Image brandingImage = new Image(brandingFile.toURI().toString());
        brandingImageView.setImage(brandingImage);

        File lockFile = new File("images/lock.png");
        Image lockImage = new Image(lockFile.toURI().toString());
        lockImageView.setImage(lockImage);

    }

    public void loginButtonOnAction(ActionEvent event) throws IOException {
        //loggedUser = new curUser(usernameTextField.getText());
        user.setLusername(usernameTextField.getText());
        //loggedUser.setCurUsername(usernameTextField.getText());
        if ((usernameTextField.getText().isBlank() == false) && (enterPasswordField.getText().isBlank() == false)) {
            //validateLogin();
            LoginService ls = new LoginService();
            ArrayList<User> uList = ls.getUser(user.getLusername());
            validateLogin(user.getLusername());

            //change2friendsPage();
            //loginMessageLabel.setText("You try to login");
        } else {
            loginMessageLabel.setText("Please enter username and password");
        }
    }

    public void cancelButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void validateLogin(String Cuser) {
        //String Cuser = user.getLusername();
        // The "users.users" would need to be changed to match the name of database table you have
        String verifyLogin = "SELECT count(1) FROM users.users WHERE username = '" + Cuser+ "' AND password ='" + enterPasswordField.getText() + "'";

        try {
            Connection connectDB = DBConnection.getConnection();
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyLogin);

            while (queryResult.next()) {
                if (queryResult.getInt(1) == 1) {
                    loginMessageLabel.setText(curUser);
                    changetoMain();
                    //change2friendsPage();
                } else {
                    loginMessageLabel.setText("Invalid login. Please try again");
                    //createAccountForm();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    public void changetoMain() throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Home.fxml"));
        Parent root = loader.load();
        Scene mainScene = new Scene(root);
        SideBarController sideBarController = loader.getController();
        LoginService ls = new LoginService();
        ArrayList<User> userList = ls.getUser(user.getLusername());
        sideBarController.dataLoad(userList);

        Stage primaryStage = (Stage) loginButton.getScene().getWindow();
        primaryStage.setScene(mainScene);
        primaryStage.show();

    }

    public void createAccountForm() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("register.fxml"));
            Stage registerStage = new Stage();
            registerStage.initStyle(StageStyle.UNDECORATED);
            registerStage.setScene(new Scene(root, 520, 567));
            registerStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }

    }

}

