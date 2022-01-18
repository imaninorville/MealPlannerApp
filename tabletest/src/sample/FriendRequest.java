package sample;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FriendRequest implements Initializable {
    @FXML
    private TableView<RequestConstructor> req_table;
    @FXML
    private TableColumn<RequestConstructor, String> users;
    @FXML
    private Label f_label;

    @FXML
    private TableColumn<RequestConstructor, String> button_col;
    @FXML
    private Button close_b;
    private String curUser = null;
    User selectedUser = new User();
    ArrayList<User> fRuserList;

    ObservableList<RequestConstructor> userlist = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void frLoadData(ArrayList<User> userList){
        fRuserList = userList;
        selectedUser.setLid(userList.get(0).getLid());
        curUser = String.valueOf(selectedUser.getLid());

        loadData(curUser);

    }
    @FXML
    private void close(ActionEvent event) {
        Stage stage = (Stage) close_b.getScene().getWindow();
        stage.close();
    }

    private void loadData(String curUser) {
        String query = "(select A.username, A.user_id from users.users A " +
                "where A.user_id in (select B.user2_id from users.friendship B where B.user1_id = " + curUser + " AND B.action_user_id !=" + curUser + " AND B.status = 0)" +
                " or (A.user_id in (select B.user1_id from users.friendship B where B.user2_id = " + curUser + "  AND B.action_user_id !=" + curUser + " AND B.status = 0)))";
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery(query);
            while (rs.next()) {
                userlist.add(new RequestConstructor(rs.getString("username"),
                        rs.getInt("user_id")));

            }
        } catch (SQLException ex) {
            Logger.getLogger(FriendRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
        users.setCellValueFactory(new PropertyValueFactory<>("username"));

        Callback<TableColumn<RequestConstructor, String>, TableCell<RequestConstructor, String>> cellFactory = (TableColumn<RequestConstructor, String> param) -> {
            final TableCell<RequestConstructor, String> cell = new TableCell<RequestConstructor, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    //that cell created only on non-empty rows
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {

                        FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.PLUS_SQUARE);
                        editIcon.setStyle(
                                " -fx-cursor: hand ;"
                                        + "-glyph-size:28px;"
                                        + "-fx-fill:#00E676;"
                        );

                        editIcon.setOnMouseClicked((MouseEvent event) -> {
                            RequestConstructor user = req_table.getSelectionModel().getSelectedItem();
                            int select_id = user.getId();
                            String s_id = String.valueOf(select_id);
                            acceptFriend(curUser, s_id);
                            f_label.setText("Friend request accepted");


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
        button_col.setCellFactory(cellFactory);
        req_table.setItems(userlist);
    }

    //accepts friend request
    private void acceptFriend (String id1, String id2){
        try {
            String sql = "UPDATE users.friendship SET status = 1, action_user_id =" + id2 + " WHERE user1_id =" + id2 + " AND user2_id =" + id1;
            Connection con = DBConnection.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement((sql));
            Boolean rs = preparedStatement.execute();
        } catch (SQLException ex) {
            Logger.getLogger(FriendRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

