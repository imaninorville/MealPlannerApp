package sample;

public class RecipeIngredient {
    String rec_name;
    String measure;
    String amount;
    public RecipeIngredient(String amount, String measure, String rec_name){
        this.amount = amount;
        this.measure =measure;
        this.rec_name = rec_name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getRec_name() {
        return rec_name;
    }

    public void setRec_name(String rec_name) {
        this.rec_name = rec_name;
    }
}
