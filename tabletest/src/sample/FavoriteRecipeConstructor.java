package sample;

public class FavoriteRecipeConstructor {
    int rec_id;
    String rec_Name;
    String rec_des;
    String rec_instr;
    String rec_cookTime;

    public FavoriteRecipeConstructor(int rec_id, String rec_Name,String rec_des, String rec_instr, String rec_cookTime ){
        this.rec_id = rec_id;
        this.rec_Name = rec_Name;
        this.rec_des = rec_des;
        this.rec_instr = rec_instr;
        this.rec_cookTime = rec_cookTime;
    }
    public void setRec_Name(String rec_Name){
        this.rec_Name = rec_Name;
    }
    public String getRec_Name(){
        return rec_Name;
    }

    public int getRec_id() {
        return rec_id;
    }

    public void setRec_id(int rec_id) {
        this.rec_id = rec_id;
    }

    public String getRec_des() {
        return rec_des;
    }

    public void setRec_des(String rec_des) {
        this.rec_des = rec_des;
    }

    public String getRec_instr() {
        return rec_instr;
    }

    public void setRec_instr(String rec_instr) {
        this.rec_instr = rec_instr;
    }

    public String getRec_cookTime() {
        return rec_cookTime;
    }

    public void setRec_cookTime(String rec_cookTime) {
        this.rec_cookTime = rec_cookTime;
    }
}
