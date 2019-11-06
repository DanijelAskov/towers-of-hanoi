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

package askov.schoolprojects.cg.towersofhanoi.gamepane;

import askov.schoolprojects.cg.towersofhanoi.animation.DiskFlipper;
import askov.schoolprojects.cg.towersofhanoi.animation.DiskRelocator;
import askov.schoolprojects.cg.towersofhanoi.pattern.Pattern;
import askov.schoolprojects.cg.towersofhanoi.puzzlesolver.PuzzleSolver;
import askov.schoolprojects.cg.towersofhanoi.puzzlesolver.RecursivePuzzleSolver;
import askov.schoolprojects.cg.towersofhanoi.puzzle.Puzzle;
import javafx.animation.AnimationTimer;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.util.StringConverter;

/**
 *
 * @author Danijel Askov
 */
@SuppressWarnings("unchecked")
public class GamePane extends BorderPane {
    
    private enum DemonstrationState { NOT_RUNNING, RUNNING, PAUSED, FINISHED }

    private static final String PATH_PREFIX = "playercontrols/";
    
    public static final double WIDTH = 1000;
    public static final double HEIGHT = 600;
    
    private static final double ROTATE_INCR = 0.50;
    private static final double MIN_ANGLE_X = -85;
    private static final double MAX_ANGLE_X = 0;
    private static final double INITIAL_ANGLE_X = -30;
    private static final double MIN_ANGLE_Y = -85;
    private static final double MAX_ANGLE_Y = 85;
    private static final double INITIAL_ANGLE_Y = 0;
    private static final double INITIAL_TRANSLATE_Z = -700;
    private static final Color DEFAULT_LIGHT_COLOR = Color.WHITE;
    private static final int MIN_NUM_DISKS = 3;
    private static final int MAX_NUM_DISKS = 10;
    private static final int DEFAULT_NUM_DISKS = MIN_NUM_DISKS;
    private static final double SLIDER_ANIMATION_SPEED_MIN = -1;
    private static final double SLIDER_ANIMATION_SPEED_MAX = 1;
    private static final double SLIDER_ANIMATION_SPEED_DEFAULT = 0;
    private static final int FPS = 60;
    private static final ImageView IMAGE_VIEW_PLAY = new ImageView(new Image(Thread.currentThread().getContextClassLoader().getResource(PATH_PREFIX + "play.png").toString()));
    private static final ImageView IMAGE_VIEW_PAUSE = new ImageView(new Image(Thread.currentThread().getContextClassLoader().getResource(PATH_PREFIX + "pause.png").toString()));
    private static final ImageView IMAGE_VIEW_STOP = new ImageView(new Image(Thread.currentThread().getContextClassLoader().getResource(PATH_PREFIX + "stop.png").toString()));
    private static final double MIN_SPECULAR_POWER = 0;
    private static final double MAX_SPECULAR_POWER = 150;
    private static final double DEFAULT_SPECULAR_POWER = 32;
    private static final Color DEFAULT_SPECULAR_COLOR = Color.BLACK;
    private static final int DEFAULT_ROD_INDEX = 1;

    private final Slider sliderNumOfDisks = new Slider(MIN_NUM_DISKS, MAX_NUM_DISKS, DEFAULT_NUM_DISKS);
    private final GridPane gridPaneNumOfDisks = new GridPane();
    private final ChoiceBox choiceBoxMaterial = new ChoiceBox();
    private final Slider sliderSpecularPower = new Slider(MIN_SPECULAR_POWER, MAX_SPECULAR_POWER, DEFAULT_SPECULAR_POWER);
    private final ColorPicker colorPickerSpecularColor = new ColorPicker(DEFAULT_SPECULAR_COLOR);
    private final ColorPicker colorPickerLightColor = new ColorPicker(DEFAULT_LIGHT_COLOR);
    private final Slider sliderLightPositionX = new Slider(MIN_ANGLE_X, MAX_ANGLE_X, MIN_ANGLE_X - INITIAL_ANGLE_X);
    private final Slider sliderLightPositionY = new Slider(MIN_ANGLE_Y, MAX_ANGLE_Y, INITIAL_ANGLE_Y);
    private final GridPane gridPaneSettings = new GridPane();
    private final AnchorPane anchorPaneSettings = new AnchorPane(gridPaneSettings);
    private final TitledPane titledPaneSettings = new TitledPane("Settings", anchorPaneSettings);
    
    private final Button buttonStartSolving = new Button("Start Solving");
    private final Button buttonStopSolvingResetPuzzle = new Button("Stop Solving");
    private final Label labelDemo = new Label("Demonstration");
    private final Slider sliderSpeed = new Slider(SLIDER_ANIMATION_SPEED_MIN, SLIDER_ANIMATION_SPEED_MAX, SLIDER_ANIMATION_SPEED_DEFAULT);
    private final GridPane gridPaneSpeed = new GridPane();
    private final Button buttonPlayPause = new Button();
    private final Button buttonStop = new Button();
    private final GridPane gridPaneSolve = new GridPane();
    private final AnchorPane anchorPaneSolving = new AnchorPane(gridPaneSolve);
    private final TitledPane titledPaneSolving = new TitledPane("Solving", anchorPaneSolving);

    private final Group camPosition = new Group();
    private final PointLight light = new PointLight(DEFAULT_LIGHT_COLOR);
    
    private final Rotate camPositionRotateAroundXAxis = new Rotate(INITIAL_ANGLE_X, Rotate.X_AXIS);
    private final Rotate camPositionRotateAroundYAxis = new Rotate(INITIAL_ANGLE_Y, Rotate.Y_AXIS);
    private final Translate camPositionTranslate = new Translate(0, 0, INITIAL_TRANSLATE_Z);

    private final Translate camTranslate1 = new Translate(0, 0, 0);
    private final Rotate camRotateAroundXAxis = new Rotate(0, Rotate.X_AXIS);
    private final Rotate camRotateAroundYAxis = new Rotate(0, Rotate.Y_AXIS);
    private final Translate camTranslate2 = new Translate(0, 0, 0);

    private final Rotate lightRotateAroundXAxis = new Rotate(INITIAL_ANGLE_X, Rotate.X_AXIS);
    private final Rotate lightRotateAroundYAxis = new Rotate(INITIAL_ANGLE_Y, Rotate.Y_AXIS);

    private double startX, startY, endX, endY;
    private double deltaX, deltaY;
    
    private final Puzzle puzzle = new Puzzle(DEFAULT_NUM_DISKS, this);
    private final PuzzleSolver puzzleSolver;
    private DiskRelocator diskRelocator;

    private int time;
    private int moves;
    private boolean timerOn;
    
    private final Label labelTime = new Label(String.format("Time: %5d", time / 60));
    private final Label labelMoves = new Label(String.format("Moves: %5d", 0));
    private final ProgressBar progressBar = new ProgressBar();

    private DemonstrationState demonstrationState = DemonstrationState.NOT_RUNNING;

    private int currentRodIndex = DEFAULT_ROD_INDEX;
    
    public GamePane() {
        new AnimationTimer() {

            @Override
            public void handle(long now) {
                if (timerOn) {
                    time++;
                    labelTime.setText(String.format("Time: %5d", time / FPS));
                    if (demonstrationState == DemonstrationState.RUNNING) {
                        labelMoves.setText(String.format("Moves: %5d/%d", diskRelocator.getCurrentRelocationIndex(), diskRelocator.getNumDiskRelocations()));
                        progressBar.setProgress((double) (diskRelocator.getCurrentRelocationIndex()) / diskRelocator.getNumDiskRelocations());
                    } else {
                        labelMoves.setText(String.format("Moves: %5d", moves));
                    }
                }
            }

        }.start();
        
        puzzleSolver = new RecursivePuzzleSolver(puzzle);
        
        // SETTINGS
        
        int currentRow = 0;

        Label labelNumOfDisks = new Label("Number of Disks");
        gridPaneNumOfDisks.add(labelNumOfDisks, 0, 0);
        GridPane.setHalignment(labelNumOfDisks, HPos.CENTER);
        
        gridPaneNumOfDisks.add(sliderNumOfDisks, 0, 1);
        GridPane.setHalignment(sliderNumOfDisks, HPos.CENTER);
        gridPaneNumOfDisks.setMaxWidth(0.20 * WIDTH);
        sliderNumOfDisks.setMajorTickUnit(1);
        sliderNumOfDisks.setMinorTickCount(0);
        sliderNumOfDisks.setShowTickLabels(true);
        sliderNumOfDisks.setSnapToTicks(true);
        sliderNumOfDisks.valueProperty().addListener((ChangeListener) (arg0, arg1, arg2) -> puzzle.setNumDisks((int) sliderNumOfDisks.getValue()));
        
        gridPaneSettings.add(gridPaneNumOfDisks, 0, currentRow++, 2, 1);
        GridPane.setHalignment(gridPaneNumOfDisks, HPos.CENTER);
        ColumnConstraints columnConstraintsNumOfDisks = new ColumnConstraints();
        columnConstraintsNumOfDisks.setPercentWidth(100);
        gridPaneNumOfDisks.getColumnConstraints().addAll(columnConstraintsNumOfDisks);

        Label labelCamPosition = new Label("Camera Position");
        gridPaneSettings.add(labelCamPosition, 0, currentRow);
        GridPane.setHalignment(labelCamPosition, HPos.LEFT);

        ChoiceBox choiceBoxCamPosition = new ChoiceBox();
        gridPaneSettings.add(choiceBoxCamPosition, 1, currentRow++);
        GridPane.setHalignment(choiceBoxCamPosition, HPos.RIGHT);
        choiceBoxCamPosition.getItems().addAll("Left", "Center", "Right");
        choiceBoxCamPosition.getSelectionModel().select(DEFAULT_ROD_INDEX);
        choiceBoxCamPosition.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Number> observableValue, Number number, Number number2) -> {
            currentRodIndex = number2.intValue();
            alignCamera(currentRodIndex);
        });

        Separator separator1 = new Separator(Orientation.HORIZONTAL);
        gridPaneSettings.add(separator1, 0, currentRow++, 2, 1);
        GridPane.setHalignment(separator1, HPos.CENTER);

        Label labelMaterial = new Label("Material");
        gridPaneSettings.add(labelMaterial, 0, currentRow);
        GridPane.setHalignment(labelMaterial, HPos.LEFT);
        
        gridPaneSettings.add(choiceBoxMaterial, 1, currentRow++);
        GridPane.setHalignment(choiceBoxMaterial, HPos.RIGHT);
        choiceBoxMaterial.getItems().addAll(Pattern.HAMMERED_METAL, Pattern.GALVANIZED_METAL, Pattern.STONE, Pattern.PLASTIC, Pattern.WOOD, Pattern.BLUE_MARBLE);
        choiceBoxMaterial.getSelectionModel().selectFirst();
        puzzle.setPattern((Pattern) choiceBoxMaterial.getSelectionModel().getSelectedItem());
        choiceBoxMaterial.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Number> observableValue, Number number, Number number2) -> puzzle.setPattern((Pattern) choiceBoxMaterial.getItems().get((Integer) number2)));

        GridPane gridPaneSpecularPower = new GridPane();
        Label labelSpecularPower = new Label("Specular Power");
        gridPaneSpecularPower.add(labelSpecularPower, 0, 0);
        GridPane.setHalignment(labelSpecularPower, HPos.CENTER);
        
        gridPaneSpecularPower.add(sliderSpecularPower, 0, 1);
        GridPane.setHalignment(sliderSpecularPower, HPos.CENTER);
        gridPaneSpecularPower.setMaxWidth(0.20 * WIDTH);
        sliderSpecularPower.setMajorTickUnit((MAX_SPECULAR_POWER - MIN_SPECULAR_POWER) / 2);
        sliderSpecularPower.setMinorTickCount(0);
        sliderSpecularPower.setShowTickMarks(true);
        sliderSpecularPower.setShowTickLabels(true);
        sliderSpecularPower.setLabelFormatter(new StringConverter<Double>() {
            
            @Override
            public String toString(Double value) {
                if (value == MIN_SPECULAR_POWER) {
                    return "Low";
                }
                else if (value == (MAX_SPECULAR_POWER - MIN_SPECULAR_POWER) / 2) {
                    return "Medium";
                } else {
                    return "High";
                }
            }

            @Override
            public Double fromString(String string) {
                switch (string) {
                    case "Low":
                        return MIN_SPECULAR_POWER;
                    case "Fast":
                        return MAX_SPECULAR_POWER;
                    default:
                        return (MAX_SPECULAR_POWER - MIN_SPECULAR_POWER) / 2;
                }
            }
            
        });
        sliderSpecularPower.valueProperty().addListener((ChangeListener) (arg0, arg1, arg2) -> puzzle.getPattern().setSpecularPower(sliderSpecularPower.getValue()));
        
        gridPaneSettings.add(gridPaneSpecularPower, 0, currentRow++, 2, 1);
        GridPane.setHalignment(gridPaneSpecularPower, HPos.CENTER);
        ColumnConstraints columnConstraintsSpecularPower = new ColumnConstraints();
        columnConstraintsSpecularPower.setPercentWidth(100);
        gridPaneSpecularPower.getColumnConstraints().addAll(columnConstraintsSpecularPower);

        Label labelSpecularColor = new Label("Specular Color");
        gridPaneSettings.add(labelSpecularColor, 0, currentRow);
        GridPane.setHalignment(labelSpecularColor, HPos.LEFT);
        
        gridPaneSettings.add(colorPickerSpecularColor, 1, currentRow++);
        GridPane.setHalignment(colorPickerSpecularColor, HPos.RIGHT);
        colorPickerSpecularColor.setOnAction((event) -> puzzle.getPattern().setSpecularColor(colorPickerSpecularColor.getValue()));

        Separator separator2 = new Separator(Orientation.HORIZONTAL);
        gridPaneSettings.add(separator2, 0, currentRow++, 2, 1);
        GridPane.setHalignment(separator2, HPos.CENTER);

        Label labelLightColor = new Label("Light Color");
        gridPaneSettings.add(labelLightColor, 0, currentRow);
        GridPane.setHalignment(labelLightColor, HPos.LEFT);
        
        gridPaneSettings.add(colorPickerLightColor, 1, currentRow++);
        GridPane.setHalignment(colorPickerLightColor, HPos.RIGHT);
        colorPickerLightColor.setOnAction((event) -> light.setColor(colorPickerLightColor.getValue()));

        GridPane gridPaneLightPosition = new GridPane();
        Label labelLightPosition = new Label("Light Position");
        gridPaneLightPosition.add(labelLightPosition, 0, 0);
        GridPane.setHalignment(labelLightPosition, HPos.CENTER);
        
        gridPaneLightPosition.add(sliderLightPositionX, 0, 1);
        GridPane.setHalignment(sliderLightPositionX, HPos.CENTER);
        sliderLightPositionX.setMaxWidth(0.20 * WIDTH);
        sliderLightPositionX.setMajorTickUnit((MAX_ANGLE_X - MIN_ANGLE_X) / 2);
        sliderLightPositionX.setMinorTickCount(0);
        sliderLightPositionX.setShowTickMarks(true);
        sliderLightPositionX.setShowTickLabels(true);
        sliderLightPositionX.setLabelFormatter(new StringConverter<Double>() {
            
            @Override
            public String toString(Double value) {
                if (value == MIN_ANGLE_X) {
                    return "Front";
                }
                else if (value == MIN_ANGLE_X + (MAX_ANGLE_X - MIN_ANGLE_X) / 2) {
                    return "Above";
                } else {
                    return "Back";
                }
            }

            @Override
            public Double fromString(String string) {
                switch (string) {
                    case "Front":
                        return MIN_ANGLE_X;
                    case "Above":
                        return MIN_ANGLE_X + (MAX_ANGLE_X - MIN_ANGLE_X) / 2;
                    case "Back":
                        return MAX_ANGLE_X;
                    default:
                        return (MAX_ANGLE_X - MIN_ANGLE_X) / 2;
                }
            }
            
        });
        sliderLightPositionX.valueProperty().addListener((ChangeListener) (arg0, arg1, arg2) -> lightRotateAroundXAxis.setAngle(MIN_ANGLE_X - sliderLightPositionX.getValue()));
        
        gridPaneLightPosition.add(sliderLightPositionY, 0, 2);
        GridPane.setHalignment(sliderLightPositionY, HPos.CENTER);
        sliderLightPositionY.setMaxWidth(0.20 * WIDTH);
        sliderLightPositionY.setMajorTickUnit((MAX_ANGLE_Y - MIN_ANGLE_Y) / 2);
        sliderLightPositionY.setMinorTickCount(0);
        sliderLightPositionY.setShowTickMarks(true);
        sliderLightPositionY.setShowTickLabels(true);
        sliderLightPositionY.setLabelFormatter(new StringConverter<Double>() {
            
            @Override
            public String toString(Double value) {
                if (value == MIN_ANGLE_Y) {
                    return "Left";
                }
                else if (value == MIN_ANGLE_Y + (MAX_ANGLE_Y - MIN_ANGLE_Y) / 2) {
                    return "Center";
                } else {
                    return "Right";
                }
            }

            @Override
            public Double fromString(String string) {
                switch (string) {
                    case "Left":
                        return MIN_ANGLE_Y;
                    case "Center":
                        return MIN_ANGLE_Y + (MAX_ANGLE_Y - MIN_ANGLE_Y) / 2;
                    case "Right":
                        return MAX_ANGLE_Y;
                    default:
                        return (MAX_ANGLE_Y - MIN_ANGLE_Y) / 2;
                }
            }
            
        });
        sliderLightPositionY.valueProperty().addListener((ChangeListener) (arg0, arg1, arg2) -> lightRotateAroundYAxis.setAngle(-sliderLightPositionY.getValue()));
        
        gridPaneSettings.add(gridPaneLightPosition, 0, currentRow, 2, 1);
        GridPane.setHalignment(gridPaneLightPosition, HPos.CENTER);
        ColumnConstraints columnConstraintsLightPosition = new ColumnConstraints();
        columnConstraintsLightPosition.setPercentWidth(100);
        gridPaneLightPosition.getColumnConstraints().addAll(columnConstraintsLightPosition);

        ColumnConstraints columnConstraintsSettings1 = new ColumnConstraints();
        columnConstraintsSettings1.setPercentWidth(40);
        columnConstraintsSettings1.setFillWidth(true);
        ColumnConstraints columnConstraintsSettings2 = new ColumnConstraints();
        columnConstraintsSettings2.setPercentWidth(60);
        columnConstraintsSettings2.setFillWidth(true);
        gridPaneSettings.getColumnConstraints().addAll(
                columnConstraintsSettings1, 
                columnConstraintsSettings2
        );
        AnchorPane.setLeftAnchor(gridPaneSettings, 0d);
        AnchorPane.setRightAnchor(gridPaneSettings, 0d);
        AnchorPane.setTopAnchor(gridPaneSettings, 0d);
        AnchorPane.setBottomAnchor(gridPaneSettings, 0d);
        gridPaneSettings.setVgap(5);
        gridPaneSettings.setHgap(5);

        titledPaneSettings.setFont(Font.font(14));
        
        // DEMONSTRATION
        
        currentRow = 0;
        
        gridPaneSolve.add(buttonStartSolving, 0, currentRow);
        GridPane.setHalignment(buttonStartSolving, HPos.CENTER);
        buttonStartSolving.setOnAction(e -> {
            if (demonstrationState == DemonstrationState.NOT_RUNNING || demonstrationState == DemonstrationState.FINISHED) {
                puzzle.reset();

                labelDemo.setDisable(true);
                buttonStopSolvingResetPuzzle.setDisable(false);
                buttonStartSolving.setDisable(true);
                buttonPlayPause.setDisable(true);
                gridPaneSpeed.setDisable(true);
                gridPaneNumOfDisks.setDisable(true);
                progressBar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);

                timerOn = true;
                time = 0;
                moves = 0;

                puzzle.setInteractionEnabled(true);
            }
        });
        
        gridPaneSolve.add(buttonStopSolvingResetPuzzle, 1, currentRow++);
        GridPane.setHalignment(buttonStopSolvingResetPuzzle, HPos.CENTER);
        buttonStopSolvingResetPuzzle.setDisable(true);
        buttonStopSolvingResetPuzzle.setOnAction(e -> {
            puzzle.reset();

            labelDemo.setDisable(false);
            buttonStopSolvingResetPuzzle.setDisable(true);
            buttonStartSolving.setDisable(false);
            buttonPlayPause.setDisable(false);
            gridPaneSpeed.setDisable(false);
            gridPaneNumOfDisks.setDisable(false);
            labelTime.setText(String.format("Time: %5d", 0));
            labelMoves.setText(String.format("Moves: %5d", 0));
            buttonStopSolvingResetPuzzle.setText("Stop Solving");

            timerOn = false;
            time = 0;
            moves = 0;

            if (puzzle.getSelectedDisk() != null) {
                puzzle.getSelectedDisk().setSelected(false);
            }
            puzzle.setSelectedDisk(null);

            puzzle.setInteractionEnabled(false);
        });

        Separator separator3 = new Separator(Orientation.HORIZONTAL);
        gridPaneSolve.add(separator3, 0, currentRow++, 2, 1);
        GridPane.setHalignment(separator3, HPos.CENTER);
        
        gridPaneSolve.add(labelDemo, 0, currentRow++, 2, 1);
        GridPane.setHalignment(labelDemo, HPos.LEFT);

        Label labelSpeed = new Label("Speed");
        gridPaneSpeed.add(labelSpeed, 0, 0);
        GridPane.setHalignment(labelSpeed, HPos.CENTER);
        
        gridPaneSpeed.add(sliderSpeed, 0, 1);
        GridPane.setHalignment(sliderSpeed, HPos.CENTER);
        sliderSpeed.setMaxWidth(0.20 * WIDTH);
        sliderSpeed.setLabelFormatter(new StringConverter<Double>() {
            
            @Override
            public String toString(Double value) {
                if (value == SLIDER_ANIMATION_SPEED_MIN) {
                    return "Slow";
                }
                else if (value == SLIDER_ANIMATION_SPEED_DEFAULT) {
                    return "Normal";
                } else {
                    return "Fast";
                }
            }

            @Override
            public Double fromString(String string) {
                switch (string) {
                    case "Slow":
                        return SLIDER_ANIMATION_SPEED_MIN;
                    case "Fast":
                        return 1d;
                    default:
                        return SLIDER_ANIMATION_SPEED_DEFAULT;
                }
            }
            
        });
        sliderSpeed.setMinorTickCount(0);
        sliderSpeed.setMajorTickUnit(1);
        sliderSpeed.setShowTickMarks(true);
        sliderSpeed.setShowTickLabels(true);
        sliderSpeed.valueProperty().addListener((ChangeListener) (arg0, arg1, arg2) -> {
            if (demonstrationState == DemonstrationState.PAUSED || demonstrationState == DemonstrationState.RUNNING) {
                double sliderValue = sliderSpeed.getValue();
                if (sliderValue >= 0) {
                    diskRelocator.setAnimationDuration(DiskRelocator.DEFAULT_ANIMATION_DURATION - sliderValue * 0.90 * DiskRelocator.DEFAULT_ANIMATION_DURATION);
                } else {
                    diskRelocator.setAnimationDuration(DiskRelocator.DEFAULT_ANIMATION_DURATION - sliderValue * 3 * DiskRelocator.DEFAULT_ANIMATION_DURATION);
                }
            }
        });
         
        gridPaneSolve.add(gridPaneSpeed, 0, currentRow++, 2, 1);
        GridPane.setHalignment(gridPaneSpeed, HPos.CENTER);
        ColumnConstraints columnConstraintsSpeed = new ColumnConstraints();
        columnConstraintsSpeed.setPercentWidth(100);
        gridPaneSpeed.getColumnConstraints().add(columnConstraintsSpeed);
   
        gridPaneSolve.add(buttonPlayPause, 0, currentRow);
        GridPane.setHalignment(buttonPlayPause, HPos.RIGHT);
        buttonPlayPause.setGraphic(IMAGE_VIEW_PLAY);
        buttonPlayPause.setOnAction(e -> {
            switch (demonstrationState) {
                case FINISHED: case NOT_RUNNING:
                    puzzle.reset();
                    puzzleSolver.solve();

                    diskRelocator = new DiskFlipper(puzzle, puzzleSolver.getDiskRelocations());
                    double sliderValue = sliderSpeed.getValue();
                    if (sliderValue >= 0) {
                        diskRelocator.setAnimationDuration(DiskRelocator.DEFAULT_ANIMATION_DURATION - sliderValue * 0.90 * DiskRelocator.DEFAULT_ANIMATION_DURATION);
                    } else {
                        diskRelocator.setAnimationDuration(DiskRelocator.DEFAULT_ANIMATION_DURATION - sliderValue * 3 * DiskRelocator.DEFAULT_ANIMATION_DURATION);
                    }
                    diskRelocator.start(() -> {
                        timerOn = false;

                        buttonPlayPause.setGraphic(IMAGE_VIEW_PLAY);
                        buttonStop.setDisable(true);
                        buttonStartSolving.setDisable(false);
                        gridPaneNumOfDisks.setDisable(false);
                        labelMoves.setText(String.format("Moves: %5d/%d", diskRelocator.getCurrentRelocationIndex(), diskRelocator.getNumDiskRelocations()));
                        progressBar.setProgress((double) (diskRelocator.getCurrentRelocationIndex()) / diskRelocator.getNumDiskRelocations());

                        demonstrationState = DemonstrationState.FINISHED;
                    });

                    buttonPlayPause.setGraphic(IMAGE_VIEW_PAUSE);
                    buttonStop.setDisable(false);
                    buttonStartSolving.setDisable(true);
                    gridPaneNumOfDisks.setDisable(true);

                    timerOn = true;
                    time = 0;

                    demonstrationState = DemonstrationState.RUNNING;
                    break;

                case RUNNING:
                    diskRelocator.pause();

                    buttonPlayPause.setGraphic(IMAGE_VIEW_PLAY);

                    timerOn = false;

                    demonstrationState = DemonstrationState.PAUSED;
                    break;

                case PAUSED:
                    diskRelocator.resume();

                    buttonPlayPause.setGraphic(IMAGE_VIEW_PAUSE);

                    timerOn = true;

                    demonstrationState = DemonstrationState.RUNNING;
                    break;
            }
        });
        
        gridPaneSolve.add(buttonStop, 1, currentRow);
        GridPane.setHalignment(buttonStop, HPos.LEFT);
        buttonStop.setGraphic(IMAGE_VIEW_STOP);
        buttonStop.setDisable(true);
        buttonStop.setOnAction(e -> {
            diskRelocator.stop();
            puzzle.reset();

            timerOn = false;
            time = 0;

            labelDemo.setDisable(false);
            buttonPlayPause.setGraphic(IMAGE_VIEW_PLAY);
            buttonPlayPause.setDisable(false);
            buttonStop.setDisable(true);
            buttonStartSolving.setDisable(false);
            gridPaneNumOfDisks.setDisable(false);
            labelTime.setText(String.format("Time: %5d", 0));
            labelMoves.setText(String.format("Moves: %5d", 0));
            progressBar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);

            puzzle.setInteractionEnabled(true);
            demonstrationState = DemonstrationState.NOT_RUNNING;
        });
        
        ColumnConstraints columnConstraintsDemo = new ColumnConstraints();
        columnConstraintsDemo.setPercentWidth(50);
        columnConstraintsDemo.setFillWidth(true);
        gridPaneSolve.getColumnConstraints().addAll(
                columnConstraintsDemo, 
                columnConstraintsDemo
        );
        AnchorPane.setLeftAnchor(gridPaneSolve, 0d);
        AnchorPane.setRightAnchor(gridPaneSolve, 0d);
        AnchorPane.setTopAnchor(gridPaneSolve, 0d);
        AnchorPane.setBottomAnchor(gridPaneSolve, 0d);
        gridPaneSolve.setVgap(5);
        gridPaneSolve.setHgap(5);
        
        titledPaneSolving.setFont(Font.font(14));
        
        // LEFT
        
        titledPaneSettings.expandedProperty().addListener((Observable observable) -> titledPaneSolving.setExpanded(true));
        titledPaneSolving.expandedProperty().addListener((Observable observable) -> titledPaneSettings.setExpanded(true));

        Accordion accordion = new Accordion(titledPaneSettings, titledPaneSolving);
        accordion.setExpandedPane(titledPaneSettings);
        accordion.setMinWidth(0.25 * WIDTH);
        accordion.setPrefWidth(0.25 * WIDTH);
        accordion.setMaxWidth(0.25 * WIDTH);
        accordion.setMinHeight(0.95 * HEIGHT);
        accordion.setPrefHeight(0.95 * HEIGHT);
        accordion.setMaxHeight(0.95 * HEIGHT);
        BorderPane.setAlignment(accordion, Pos.TOP_LEFT);
        setLeft(accordion);
        
        // CENTER

        camPosition.getTransforms().addAll(camPositionRotateAroundYAxis, camPositionRotateAroundXAxis);
        camPosition.getTransforms().add(camPositionTranslate);

        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setFarClip(10000);
        Rotate camRotateAroundZAxis = new Rotate(0, Rotate.Z_AXIS);
        camera.getTransforms().addAll(camTranslate1, camRotateAroundYAxis, camRotateAroundXAxis, camTranslate2, camRotateAroundZAxis);
        alignCamera(currentRodIndex);
        
        light.getTransforms().addAll(lightRotateAroundXAxis, lightRotateAroundYAxis);
        Translate lightTranslate = new Translate(0, 0, INITIAL_TRANSLATE_Z);
        light.getTransforms().add(lightTranslate);

        Group puzzleGroup = new Group(puzzle, light, camPosition, camera);
        SubScene puzzleSubscene = new SubScene(puzzleGroup, 0.75 * WIDTH, 0.95 * HEIGHT, true, SceneAntialiasing.BALANCED);
        puzzleSubscene.setOnScroll(event -> {
            double delta = event.getDeltaY();
            if (camPositionTranslate.getZ() + delta <= -Math.max(Puzzle.ROD_HEIGHT, Puzzle.PLATFORM_WIDTH / 2)) {
                camPositionTranslate.setZ(camPositionTranslate.getZ() + delta);
            }
            alignCamera(currentRodIndex);
        });
        puzzleSubscene.setOnMousePressed(event -> {
            startX = endX = event.getSceneX();
            startY = endY = event.getSceneY();
        });
        puzzleSubscene.setOnMouseDragged(event -> {
            startX = endX;
            startY = endY;
            
            endX = event.getSceneX();
            endY = event.getSceneY();
            
            deltaX = endX - startX;
            deltaY = endY - startY;
            
            camPositionRotateAroundXAxis.setAngle(camPositionRotateAroundXAxis.getAngle() + deltaY * ROTATE_INCR);
            camPositionRotateAroundYAxis.setAngle(camPositionRotateAroundYAxis.getAngle() - deltaX * ROTATE_INCR);
            
            if (camPositionRotateAroundXAxis.getAngle() > MAX_ANGLE_X) {
                camPositionRotateAroundXAxis.setAngle(MAX_ANGLE_X);
            } else if (camPositionRotateAroundXAxis.getAngle() < MIN_ANGLE_X) {
                camPositionRotateAroundXAxis.setAngle(MIN_ANGLE_X);
            }
            
            if (camPositionRotateAroundYAxis.getAngle() > MAX_ANGLE_Y) {
                camPositionRotateAroundYAxis.setAngle(MAX_ANGLE_Y);
            } else if (camPositionRotateAroundYAxis.getAngle() < MIN_ANGLE_Y) {
                camPositionRotateAroundYAxis.setAngle(MIN_ANGLE_Y);
            }
            
            alignCamera(currentRodIndex);
        });
        puzzleSubscene.setFill(Color.web("0xF4F4F4"));
        puzzleSubscene.setCamera(camera);
        BorderPane.setAlignment(puzzleSubscene, Pos.TOP_LEFT);
        setCenter(puzzleSubscene);
        
        // TOOLBAR

        GridPane gridPaneToolbar = new GridPane();
        gridPaneToolbar.add(labelTime, 0, 0);
        GridPane.setValignment(labelTime, VPos.CENTER);
        GridPane.setHalignment(labelTime, HPos.CENTER);
        
        gridPaneToolbar.add(labelMoves, 1, 0);
        GridPane.setValignment(labelMoves, VPos.CENTER);
        GridPane.setHalignment(labelMoves, HPos.CENTER);
        
        gridPaneToolbar.add(progressBar, 2, 0);
        GridPane.setValignment(progressBar, VPos.CENTER);
        GridPane.setHalignment(progressBar, HPos.CENTER);
        progressBar.setMinWidth(0.65 * WIDTH);
        
        ColumnConstraints columnConstraintsToolbar12 = new ColumnConstraints();
        columnConstraintsToolbar12.setPercentWidth(15);
        columnConstraintsToolbar12.setFillWidth(true);
        ColumnConstraints columnConstraintsToolbar3 = new ColumnConstraints();
        columnConstraintsToolbar3.setPercentWidth(70);
        columnConstraintsToolbar3.setFillWidth(true);
        gridPaneToolbar.getColumnConstraints().addAll(
                columnConstraintsToolbar12, 
                columnConstraintsToolbar12,
                columnConstraintsToolbar3
        );
        RowConstraints rowConstraintsToolbar = new RowConstraints();
        rowConstraintsToolbar.setPercentHeight(100);
        rowConstraintsToolbar.setFillHeight(true);
        gridPaneToolbar.getRowConstraints().add(rowConstraintsToolbar);
        AnchorPane.setLeftAnchor(gridPaneToolbar, 0d);
        AnchorPane.setRightAnchor(gridPaneToolbar, 0d);
        AnchorPane.setTopAnchor(gridPaneToolbar, 0d);
        AnchorPane.setBottomAnchor(gridPaneToolbar, 0d);
        gridPaneToolbar.setVgap(5);
        gridPaneToolbar.setHgap(5);

        AnchorPane anchorPaneToolbar = new AnchorPane(gridPaneToolbar);
        ToolBar toolbar = new ToolBar(anchorPaneToolbar);
        BorderPane.setAlignment(toolbar, Pos.CENTER);
        setBottom(toolbar);
        
        anchorPaneToolbar.setMinHeight(0.05 * HEIGHT);
        anchorPaneToolbar.setMinWidth(WIDTH);
        
        super.setPrefWidth(WIDTH);
        super.setPrefHeight(HEIGHT);
    }
    
    private void alignCamera(final int rodIndex) {
        Bounds camPositionBounds = camPosition.getBoundsInParent();
        
        final double xCamPosition = camPositionBounds.getMinX();
        final double yCamPosition = camPositionBounds.getMinY();
        final double zCamPosition = camPositionBounds.getMinZ();
        
        camTranslate1.setX((rodIndex - 1) * Puzzle.ROD_HORIZONTAL_DISTANCE);
        camTranslate1.setY(-Puzzle.ROD_HEIGHT / 2);
        
        final double deltaX = xCamPosition - (rodIndex - 1) * Puzzle.ROD_HORIZONTAL_DISTANCE;
        final double distanceInXZPlane = Math.sqrt(deltaX * deltaX + zCamPosition * zCamPosition);
        final double deltaY = yCamPosition + Puzzle.ROD_HEIGHT/ 2;

        camRotateAroundYAxis.setAngle(Math.toDegrees(Math.atan(deltaX / zCamPosition)) + (zCamPosition > 0 ? Math.signum(xCamPosition) * 180 : 0));
        camRotateAroundXAxis.setAngle(Math.toDegrees(Math.atan(deltaY / distanceInXZPlane)));
        camTranslate2.setZ(-Math.sqrt(distanceInXZPlane * distanceInXZPlane + deltaY * deltaY));
    }
    
    public void incrementMoves() {
        moves++;
    }
    
    public Button getButtonStopSolvingResetPuzzle(){
        return buttonStopSolvingResetPuzzle;
    }
    
    public void setTimerOn(boolean timerOn) {
        this.timerOn = timerOn;
    }
    
    public Label getLabelMoves() {
        return labelMoves;
    }
    
    public int getMoves() {
        return moves;
    }
    
}
