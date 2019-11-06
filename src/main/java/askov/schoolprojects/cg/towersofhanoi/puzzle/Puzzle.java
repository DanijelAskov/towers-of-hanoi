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

package askov.schoolprojects.cg.towersofhanoi.puzzle;

import askov.schoolprojects.cg.towersofhanoi.animation.DiskFlipper;
import askov.schoolprojects.cg.towersofhanoi.gamepane.GamePane;
import askov.schoolprojects.cg.towersofhanoi.pattern.Pattern;
import askov.schoolprojects.cg.towersofhanoi.puzzlesolver.DiskRelocation;
import javafx.scene.Group;
import javafx.scene.input.PickResult;
import javafx.scene.shape.Shape3D;

/**
 *
 * @author Danijel Askov
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class Puzzle extends Group {
    
    public static final Pattern DEFAULT_PATTERN = Pattern.WOOD;
    public static final double ROD_HEIGHT = 150;
    public static final double PLATFORM_WIDTH = 400;
    public static final double ROD_HORIZONTAL_DISTANCE = 0.25 * PLATFORM_WIDTH;
    
    private static final double DISK_RADIUS = 48;
    private static final double PLATFORM_HEIGHT = 10;
    private static final double PLATFORM_DEPTH = 100;
    private static final int DEFAULT_NUM_DISKS = 3;

    private int numDisks;
    private final Platform platform;
    private final Rod[] rods;
    private Disk[] disks;
    private Pattern pattern;
    private double heightFactor = (numDisks / 15d) / (Math.sqrt(1 + (numDisks / 15d) * (numDisks / 15d))); // Sigmoid function for determining disk's height
    
    private Disk selectedDisk;
    private Rod selectedRod;
    private boolean interactionEnabled;

    public Puzzle(Pattern pattern, int numDisks, GamePane gamePane) {
        this.pattern = pattern;
        this.numDisks = numDisks <= 0 ? 1 : numDisks;

        int index = -1;
        platform = new Platform(PLATFORM_WIDTH, PLATFORM_HEIGHT, PLATFORM_DEPTH);
        platform.setTranslateY(0.5 * PLATFORM_HEIGHT);
        
        super.getChildren().add(platform);
        
        rods = new Rod[3];
        for (int i = 0; i < rods.length; i++) {
            rods[i] = new Rod(5, ROD_HEIGHT, this);
            rods[i].setTranslateY(-0.5 * ROD_HEIGHT);
            rods[i].setTranslateX(index++ * ROD_HORIZONTAL_DISTANCE);
            
            super.getChildren().add(rods[i]);
        }
        
        createDisks();
        
        this.setOnMouseClicked(event -> {
            if (!interactionEnabled) {
                return;
            }
            PickResult pickResult = event.getPickResult();
            if (pickResult != null && pickResult.getIntersectedNode() instanceof Shape3D) {
                Shape3D shape = (Shape3D)pickResult.getIntersectedNode();
                if (shape instanceof Disk) {
                    if (!((Disk)shape).isSelectable()) {
                        return;
                    }
                    if (selectedDisk != null && selectedDisk != shape) {
                        selectedDisk.setSelected(false);
                        selectedDisk = (Disk)shape;
                        selectedDisk.setSelected(true);
                    } else if (selectedDisk == null) {
                        selectedDisk = (Disk)shape;
                        selectedDisk.setSelected(true);
                    }
                } else if (shape instanceof Rod) {
                    Rod rod = (Rod)shape;
                    if (selectedDisk != null) {
                        if (selectedDisk.getRod() == rod) {
                            return;
                        }
                        if (!rod.isSelectable()) {
                            return;
                        }
                        if (selectedRod != null && selectedRod != shape) {
                            selectedRod.setSelected(false);
                            selectedRod = rod;
                            selectedRod.setSelected(true);
                        } else if (selectedRod == null) {
                            selectedRod = rod;
                            selectedRod.setSelected(true);
                        }
                    }
                    if (selectedDisk != null && selectedRod != null) {
                        gamePane.getButtonStopSolvingResetPuzzle().setDisable(true);
                        
                        interactionEnabled = false;
                        
                        new DiskFlipper(this, new DiskRelocation(selectedDisk, selectedRod)).start(() -> {
                            gamePane.incrementMoves();
                            
                            gamePane.getButtonStopSolvingResetPuzzle().setDisable(false);
                            
                            selectedDisk = null;
                            selectedRod = null;
                            
                            if (isSolved()) {
                                gamePane.setTimerOn(false);
                                
                                gamePane.getLabelMoves().setText(String.format("Moves: %5d", gamePane.getMoves()));
                                gamePane.getButtonStopSolvingResetPuzzle().setText("Reset Puzzle");
                            } else {
                                interactionEnabled = true;
                            }
                        });
                    }
                }
            }
        });
    }
    
    public Puzzle(int numDisks, GamePane gamePane) {
        this(DEFAULT_PATTERN, numDisks, gamePane);
    }
    
    public Puzzle(GamePane gamePane) {
        this(DEFAULT_PATTERN, DEFAULT_NUM_DISKS, gamePane);
    }
    
    public void reset() {
        for (Rod rod : rods) {
            rod.clear();
        }
        for (int i = 0; i < disks.length; i++) {
            disks[i].getTransforms().clear();
            disks[i].setTranslateY(-0.5 * ((heightFactor * ROD_HEIGHT) / numDisks) - i * ((heightFactor * ROD_HEIGHT) / numDisks));
            disks[i].setTranslateX(-0.25 * 400);
            rods[0].push(disks[i]);
        }
    }
    
    public int getNumDisks() {
        return numDisks;
    }
    
    @SuppressWarnings("SuspiciousMethodCalls")
    public void setNumDisks(int numDisks) {
        for (Rod rod : rods) {
            for (Object disk : rod.getDisks()) {
                super.getChildren().remove(disk);
            }
            rod.clear();
        }
        this.numDisks = numDisks;
        createDisks();
    }
    
    private void createDisks() {
        double diskWidthFactor = 1;
        double widthFactorDecrement = 0.8 / numDisks;
        heightFactor = (numDisks / 15d) / (Math.sqrt(1 + (numDisks / 15d) * (numDisks / 15d))); // Sigmoid function for determining disk's height
        disks = new Disk[numDisks];
        for (int i = 0; i < disks.length; i++, diskWidthFactor -= widthFactorDecrement) {
            disks[i] = new Disk(5, diskWidthFactor * DISK_RADIUS, (float)(heightFactor * ROD_HEIGHT) / numDisks);
            disks[i].setTranslateY(-0.5 * ((heightFactor * ROD_HEIGHT) / numDisks) - i * ((heightFactor * ROD_HEIGHT) / numDisks));
            disks[i].setTranslateX(-0.25 * 400);
            disks[i].setPattern(pattern);
            rods[0].push(disks[i]);    
            
            super.getChildren().add(disks[i]);
        }
    }
    
    public Rod[] getRods() {
        return rods;
    }
    
    public Disk[] getDisks() {
        return disks;
    }
    
    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
        platform.setMaterial(pattern.getMaterial());
        for (Rod rod : rods) {
            rod.setPattern(pattern);
        }
        for (Disk disk : disks) {
            disk.setPattern(pattern);
        }
    }
    
    public Pattern getPattern() {
        return pattern;
    }
    
    public boolean isSolved() {
        return rods[2].getNumDisks() == numDisks;
    }
    
    public boolean getInteractionEnabled() {
        return interactionEnabled;
    }
    
    public void setInteractionEnabled(boolean interactionEnabled) {
        this.interactionEnabled = interactionEnabled;
    }
    
    public Disk getSelectedDisk() {
        return selectedDisk;
    }
    
    public void setSelectedDisk(Disk selectedDisk) {
        this.selectedDisk = selectedDisk;
    }
 
}
