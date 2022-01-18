package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.swing.plaf.metal.MetalLabelUI;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RecipeDetails {
    public ArrayList<FavoriteRecipeConstructor> getRecipe(String rec_id){
        ArrayList<FavoriteRecipeConstructor> recList = new ArrayList<FavoriteRecipeConstructor>();
        try{
            String sql = "SELECT recipes.recipe_id, recipes.rName, recipes.description, recipes.instructions, recipes.cook_time  from users.recipes inner join users.favorite_recipes on recipes.recipe_id = favorite_recipes.recipe_id where recipes.recipe_id = '"+rec_id+"'";
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                recList.add(new FavoriteRecipeConstructor(
                        rs.getInt("recipe_id"),
                        rs.getString("rName"),
                        rs.getString("description"),
                        rs.getString("instructions"),
                        rs.getString("cook_time")
                ));
            }
        } catch (
                SQLException ex) {
            Logger.getLogger(RecipeDetails.class.getName()).log(Level.SEVERE, null, ex);
        }
            return (recList);
        }
        public ObservableList<FavoriteRecipeConstructor> getFavRecipes(String u_id){
        ObservableList<FavoriteRecipeConstructor> favList = FXCollections.observableArrayList();
            try {
                String sql = "SELECT recipes.recipe_id, recipes.rName, recipes.description, recipes.instructions, recipes.cook_time from users.recipes inner join users.favorite_recipes on recipes.recipe_id = favorite_recipes.recipe_id where u_id = '" + u_id + "'";
                Connection con = DBConnection.getConnection();
                PreparedStatement preparedStatement = con.prepareStatement(sql);
                ResultSet rs = preparedStatement.executeQuery();

                while (rs.next()) {
                    favList.add(new FavoriteRecipeConstructor(
                            rs.getInt("recipe_id"),
                            rs.getString("rName"),
                            rs.getString("description"),
                            rs.getString("instructions"),
                            rs.getString("cook_time")
                    ));
                }
            } catch (
                    SQLException ex) {
                Logger.getLogger(RecipeDetails.class.getName()).log(Level.SEVERE, null, ex);
            }
            return (favList);
        }
        public String getRecipeName (String rec_id){
        String result = null;
        try{
            String sql = "SELECT rName FROM users.recipes where recipe_id = '"+rec_id+"';";
            Connection con = DBConnection.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                result = rs.getString("rName");
            }
        }catch (
                SQLException ex) {
            Logger.getLogger(RecipeDetails.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
        }
        public ArrayList<MealPlan> getMealPlan(String u_id){
        ArrayList<MealPlan> currMealPlan = new ArrayList<MealPlan>();
        try{
            String sql ="SELECT * FROM users.meal_plan where u_id = '"+u_id+"'";
            Connection con = DBConnection.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                MealPlan mp = new MealPlan();
                mp.setMon_meal(rs.getInt("mon_meal"));
                mp.setTues_meal(rs.getInt("tues_meal"));
                mp.setWed_meal(rs.getInt("wed_meal"));
                mp.setThur_meal(rs.getInt("thur_meal"));
                mp.setFri_meal(rs.getInt("fri_meal"));
                mp.setSat_meal(rs.getInt("sat_meal"));
                mp.setSun_meal(rs.getInt("sun_meal"));
                mp.setU_id(rs.getInt("u_id"));

                currMealPlan.add(mp);
            }

        }catch (
                SQLException ex) {
            Logger.getLogger(RecipeDetails.class.getName()).log(Level.SEVERE, null, ex);
        }
        return currMealPlan;
        }
        }



