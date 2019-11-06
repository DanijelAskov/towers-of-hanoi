/*
 * Copyright (C) 2019  Danijel Askov
 *
 * This file is part of Towers of Hanoi.
 *
 * Towers of Hanoi is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Towers of Hanoi is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
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
