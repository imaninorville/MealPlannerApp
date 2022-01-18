package sample;

public class RequestConstructor {
    String username;
            int id;
    public RequestConstructor(String username, int id){
        this.username = username;
        this.id = id;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public String getUsername(){
        return username;
    }
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }
}
