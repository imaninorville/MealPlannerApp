package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.net.URL;

public class fmxlLoader {
    public Pane view;

    public Pane getPage(String fileName){
        try {
            URL fileUrl = Main.class.getResource("/sample/" + fileName + ".fxml");
            if (fileUrl == null) {
                throw new java.io.FileNotFoundException("FXML file cannot be found");
            }
            FXMLLoader loader = new FXMLLoader();
            view = loader.load(fileUrl);
        }   catch (Exception e){
                System.out.println("No page "+ fileName+ " please check fxmlLoader.");
            }
        return view;
        }


}



