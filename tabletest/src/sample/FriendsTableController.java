package sample;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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

public class FriendsTableController implements Initializable {
    @FXML
    private TableView<FriendsTable> table;
    @FXML
    private TableColumn<FriendsTable, String> col_fname;
    @FXML
    private Label label1;
    @FXML
    private FontAwesomeIconView home_b;
    @FXML
    private Button close_b;
    ArrayList<User> FuserList;
    User cUser = new User();
    //int cID = null;

    // observable list to store data
    ObservableList<FriendsTable> oblist = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String pid = "112345";
        // sql query

        /*

         */
        //col_fname.setCellValueFactory(new PropertyValueFactory<>("fName"));
       // table.setItems(oblist);

    }

    public void FdataLoad(ArrayList<User> userList){
        FuserList = userList;
        label1.setText(userList.get(0).getLFname()+"'s friends");
        cUser.setLusername(userList.get(0).getLusername());
        int cID = userList.get(0).getLid();
        String s = String.valueOf(cID);
        loadFriends(s);
       /* FuserList = userList;

        int cID = userList.get(0).getLid();

        String s = String.valueOf(cID);
        loadFriends(s);
        SideBarController sb = new SideBarController();
        sb.getResult(s);*/
    }
    public void loadFriends(String pid){


        String query = "(select A.fName from users.profile A \n" +
                "where A.idprofile in (select B.user2_id from users.friendship B where B.user1_id = "+pid+" AND B.status = 1) \n"+
                "or A.idprofile in (select B.user1_id from users.friendship B where B.user2_id = "+pid+" AND B.status = 1))";
        //connect to db and execute query
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery(query);
            while (rs.next()) {
                oblist.add(new FriendsTable(rs.getString("fName")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(FriendsTableController.class.getName()).log(Level.SEVERE, null, ex);
        }
        col_fname.setCellValueFactory(new PropertyValueFactory<>("fName"));
        table.setItems(oblist);


    }
    @FXML
    private void addFriend(ActionEvent event){
        //open add friends window
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("addFriends.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            AddFriends addFriends = loader.getController();
            LoginService ls = new LoginService();
            ArrayList<User> addUserList = ls.getUser(cUser.getLusername());
            addFriends.addFriendsDataLoad(addUserList);

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
        }   catch (IOException ex) {
            Logger.getLogger(FriendsTableController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }


}