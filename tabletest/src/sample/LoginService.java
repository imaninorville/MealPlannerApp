package sample;

import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginService {

    public ArrayList<User> getUser(String uName) {
        ArrayList<User> userlist = new ArrayList<User>();

        try {

            String sql1 = "SELECT users.user_id, users.username,profile.fName, profile.lName  from users.profile inner join users.users on profile.idprofile = users.user_id where username = '" + uName + "'";
            Connection con1 = DBConnection.getConnection();
            PreparedStatement ps = con1.prepareStatement(sql1);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setLid(rs.getInt("user_id"));
                user.setLusername(rs.getString("username"));
                user.setLFname(rs.getString("Fname"));
                user.setLLname(rs.getString("Lname"));

                userlist.add(user);
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return (userlist);
    }
    public Boolean searchFriendRequest(String cUser){
        Boolean result = true;
        String query = "(select A.username, A.user_id from users.users A " +
                "where A.user_id in (select B.user2_id from users.friendship B where B.user1_id = " + cUser + " AND B.action_user_id !=" + cUser + " AND B.status = 0)" +
                " or (A.user_id in (select B.user1_id from users.friendship B where B.user2_id = " + cUser + "  AND B.action_user_id !=" + cUser + " AND B.status = 0)))";
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery(query);
            if (rs.next() == true) {
                result = true;
            }
            else{
                result = false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

}