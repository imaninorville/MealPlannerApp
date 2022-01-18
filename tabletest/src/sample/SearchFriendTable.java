package sample;

import javafx.scene.control.Button;

public class SearchFriendTable {

        String username, Fname;
        int id;

        public SearchFriendTable(String Fname,String username, int id){

            this.username = username;
            this.Fname = Fname;
            this.id = id;


        }

    public void setUsername(String username){
            this.username = username;
        }
        public void setFname(String Fname){
            this.Fname = Fname;
        }
        public String getFname(){
            return Fname;
        }

        public String getUsername() {
            return username;
    }
        public int getId(){
            return id;
        }
        public void setId(int id){
            this.id = id;
        }

}
