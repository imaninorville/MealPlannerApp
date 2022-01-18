package sample;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.util.Callback;

public class AddFriends {
    @FXML
    private Button close_b;
    @FXML
    private FontAwesomeIconView s_but;
    @FXML
    private TableView<SearchFriendTable> sTable;
    @FXML
    private TableColumn<SearchFriendTable, String> name_col;
    @FXML
    private TableColumn<SearchFriendTable, String> user_col;
    @FXML
    private TableColumn<SearchFriendTable, String> add_col;
    @FXML
    private TextField search_b;
    @FXML
    private Label id_label;
    @FXML
    private Label label2;

    String query, query2 = null;
    Connection con, con2, con3 = null;
    PreparedStatement preparedStatement, preparedStatement2, preparedStatement3 = null;
    ResultSet rs;
    ResultSet rs2;
    ResultSet rs3;

    String curUser = null;
    User selectedUser = new User();

    ArrayList<User> adduserList;

    ObservableList<SearchFriendTable> userlist = FXCollections.observableArrayList();

    public void addFriendsDataLoad(ArrayList<User> userList) {
        adduserList = userList;
        selectedUser.setLid(userList.get(0).getLid());
        curUser = String.valueOf(selectedUser.getLid());

    }

    @FXML
    private void close(ActionEvent event) {
        Stage stage = (Stage) close_b.getScene().getWindow();
        stage.close();

    }
    //searches database for user, gives user option to request friendship by selecting addfriend icon
    @FXML
    private void search(MouseEvent event) {
        userlist.clear();
        //get text from search bar
        String search = search_b.getText();
        try {
            query = "SELECT profile.fName, users.username, users.user_id from users.profile inner join users.users on profile.idprofile = users.user_id where username or fName LIKE '%" + search + "%'";
            con = DBConnection.getConnection();
            preparedStatement = con.prepareStatement(query);
            rs = preparedStatement.executeQuery();

            while (rs.next()) {
                userlist.add(new SearchFriendTable(
                        rs.getString("Fname"),
                        rs.getString("username"),
                        rs.getInt("user_id")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(FriendsTableController.class.getName()).log(Level.SEVERE, null, ex);
        }

        name_col.setCellValueFactory(new PropertyValueFactory<>("Fname"));
        user_col.setCellValueFactory(new PropertyValueFactory<>("username"));

        Callback<TableColumn<SearchFriendTable, String>, TableCell<SearchFriendTable, String>> cellFactory = (TableColumn<SearchFriendTable, String> param) -> {
            final TableCell<SearchFriendTable, String> cell = new TableCell<SearchFriendTable, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    //that cell created only on non-empty rows
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {

                        FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.PENCIL_SQUARE);
                        editIcon.setStyle(
                                " -fx-cursor: hand ;"
                                        + "-glyph-size:28px;"
                                        + "-fx-fill:#00E676;"
                        );

                        editIcon.setOnMouseClicked((MouseEvent event) -> {
                            SearchFriendTable user = sTable.getSelectionModel().getSelectedItem();
                            int select_id = user.getId();
                            String s_id = String.valueOf(select_id);

                                if (checkFriend(curUser, s_id) == true){
                                    String mess1 = " Already friends";
                                    id_label.setText(mess1);
                                }
                                else if (checkRequest(curUser, s_id) == true){
                                    String mess2 = "Friend request already sent";
                                    id_label.setText(mess2);
                                }

                            else {
                                    insertFriend(curUser,s_id);
                                    id_label.setText("Friend Request Sent");
                                }

                        });
                        HBox managebtn = new HBox(editIcon);
                        managebtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(editIcon, new Insets(2, 3, 0, 2));

                        setGraphic(managebtn);

                        setText(null);
                    }
                }
            };
            return cell;
        };
        add_col.setCellFactory(cellFactory);
        sTable.setItems(userlist);

    }


    //creates friendship between two users
    private void insertFriend(String id1, String id2) {
        try {
            String sql = "INSERT INTO friendship ( user1_id, user2_id, status, action_user_id) VALUES(" + id1 + "," + id2 + ",0," + id1 + ")";
            Connection con = DBConnection.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            Boolean rs = preparedStatement.execute();
        } catch (SQLException ex) {
            Logger.getLogger(AddFriends.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //check if friendship request already exists
    private Boolean checkRequest(String id1, String id2) {
        boolean res = true;
        try {
            String sql = "select A.fName from users.profile A\n" +
                    "  where A.idprofile in (select B.user2_id from users.friendship B where B.user1_id ="+id1+" AND B.user2_id =" +id2+" AND B.status = 0)\n" +
                    "  or A.idprofile in (select B.user1_id from users.friendship B where B.user2_id ="+id1+" AND B.user1_id ="+id2+" AND B.status = 0)";
            Connection con = DBConnection.getConnection();
            PreparedStatement preparedStatement3 = con.prepareStatement((sql));
            ResultSet rs = preparedStatement3.executeQuery();
            if (rs.next() == false) {
                res = false;
            }
            else {
                res = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(AddFriends.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }

    //check if friendship already exists
    private Boolean checkFriend(String id1, String id2) {
        boolean res = true;

        try {
            query2 = "select A.fName from users.profile A\n" +
                    "  where A.idprofile in (select B.user2_id from users.friendship B where B.user1_id ="+id1+" AND B.user2_id =" +id2+" AND B.status = 1)\n" +
                    "  or A.idprofile in (select B.user1_id from users.friendship B where B.user2_id ="+id1+" AND B.user1_id ="+id2+" AND B.status = 1)";
            con2 = DBConnection.getConnection();
            preparedStatement2 = con2.prepareStatement((query2));
            rs2 = preparedStatement2.executeQuery();
            if (rs2.next() == false) {
                res= false;

            }
            else{
                res = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(AddFriends.class.getName()).log(Level.SEVERE, null, ex);
        }

        return res;
    }
}


