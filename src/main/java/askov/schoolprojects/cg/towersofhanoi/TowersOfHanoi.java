/*
 * Copyright (c) 2019, Danijel Askov
 */

package askov.schoolprojects.cg.towersofhanoi;

import askov.schoolprojects.cg.towersofhanoi.gamepane.GamePane;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author Danijel Askov
 */
public class TowersOfHanoi extends Application {
    
    private static final Color SCENE_FILL = Color.web("0xF4F4F4");
    
    @Override
    public void start(Stage primaryStage) {
        GamePane gamePane = new GamePane();
        Group root = new Group(gamePane);
        Scene scene = new Scene(root, GamePane.WIDTH, GamePane.HEIGHT, true, SceneAntialiasing.BALANCED);
        scene.setFill(SCENE_FILL);
        
        primaryStage.setTitle("Towers of Hanoi");
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
