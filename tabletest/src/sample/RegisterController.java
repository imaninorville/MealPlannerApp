package sample;

import javafx.application.Platform;
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
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegisterController implements Initializable {

    @FXML
    private ImageView lockImageView;
    @FXML
    private Button closeButton;
    @FXML
    private Label registrationMessageLabel;
    @FXML
    private PasswordField setPasswordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Label confirmPasswordLabel;
    @FXML
    private Label userLabel;
    @FXML
    private TextField firstnameTextField;
    @FXML
    private TextField lastnameTextField;
    @FXML
    private TextField usernameTextField;


    //create unique user id
    int randomNum = (int) (100000 + (Math.random() * (300000 - 100000)));
    String u_ID = String.valueOf(randomNum);

    public void initialize(URL url, ResourceBundle resourceBundle) {

        File lockFile = new File("images/lock.png");
        Image lockImage = new Image(lockFile.toURI().toString());
        lockImageView.setImage(lockImage);
    }

    public void registerButtonOnAction(ActionEvent event) {
        if (setPasswordField.getText().equals(confirmPasswordField.getText())) {
            registerUser();
            //registrationMessageLabel.setText("Invalid Login: Please fill out this form to register");
            confirmPasswordLabel.setText("");

        } else {
            confirmPasswordLabel.setText("Password does not match");
        }
    }

    public void closeButtonOnAction(ActionEvent event) throws IOException {
        Stage currentStage = (Stage)closeButton.getScene().getWindow();
        currentStage.hide();
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        Scene scene = new Scene(root);
        Stage primaryStage = new Stage();
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public void registerUser() {

        String firstname = firstnameTextField.getText();
        String lastname = lastnameTextField.getText();
        String username = usernameTextField.getText();
        String password = setPasswordField.getText();


        if (checkBlank(firstname, lastname, username, password) == false) {
            registrationMessageLabel.setText("ERROR: Please fill out all fields");
        } else if (validateUser(username) == true) {
            userLabel.setText("Username already taken");
        }
        else {
            userLabel.setText("");
            createLogin(u_ID, username, password);
            createProfile(u_ID, firstname, lastname);
            registrationMessageLabel.setText("User has been registered successfully!");
            firstnameTextField.clear();
            lastnameTextField.clear();
            usernameTextField.clear();
            setPasswordField.clear();
            confirmPasswordField.clear();

        }
    }


    private Boolean checkBlank(String fname, String lname, String username, String password) {
        boolean result = true;
        if (fname.isBlank()) {
            result = false;
        } else if (lname.isBlank()) {
            result = false;
        } else if (username.isBlank()) {
            result = false;
        } else if (password.isBlank()) {
            result = false;
        }
        return result;

    }

    private Boolean validateUser(String username) {
        boolean result = true;
        try {
            String sql = "SELECT username FROM users.users where username = '" + username + "';";
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement((sql));
            ResultSet rs = ps.executeQuery();
            if (rs.next() == false) {
                result = false;
            } else {
                result = true;
            }

        } catch (SQLException ex) {
            Logger.getLogger(RegisterController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;

    }

    //insert username, id, and password into database
    private void createLogin(String id, String username, String password) {
        try {

            String sql = "INSERT into users (user_id, username, password) values (" + id + ",'" + username + "','" + password + "')";
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            Boolean rs = ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(RegisterController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
// inserts fName, lName into database
    private void createProfile(String id, String fName, String lName) {
        try {
            String sql = "INSERT into profile (idprofile, fName, lName, diet_label) values (" + id + ", '" + fName + "','" + lName + "' ,null)";
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            Boolean rs = ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(RegisterController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}