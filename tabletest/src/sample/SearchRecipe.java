package sample;

public class SearchRecipe {
    String Rname, Rdes;
    int R_id, RType;

    public SearchRecipe(int R_id, String Rname, String Rdes, int RType){
        this.R_id = R_id;
        this.Rname = Rname;
        this.Rdes = Rdes;
        this.RType = RType;

    }
    public void setRname(String Rname){
        this.Rname = Rname;
    }
    public String getRname(){
        return Rname;
    }
    public void setRdes(String Rdes){
        this.Rdes = Rdes;
    }
    public String getRdes(){
        return Rdes;
    }
    public void setR_id(int R_id){
        this.R_id = R_id;
    }
    public int getR_id(){
        return R_id;
    }
    public void setRType(int RType){
        this.RType = RType;
    }
    public int getRType(){
        return RType;
    }
}
