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

package askov.schoolprojects.cg.towersofhanoi.animation;

import askov.schoolprojects.cg.towersofhanoi.puzzlesolver.DiskRelocation;
import askov.schoolprojects.cg.towersofhanoi.puzzle.Disk;
import askov.schoolprojects.cg.towersofhanoi.puzzle.Puzzle;
import askov.schoolprojects.cg.towersofhanoi.puzzle.Rod;
import java.util.List;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

/**
 *
 * @author Danijel Askov
 */
@SuppressWarnings("unused")
public class DiskFlipper extends DiskRelocator {

    public DiskFlipper(Puzzle puzzle, DiskRelocation... diskRelocations) {
        super(puzzle, diskRelocations);
    }
    
    public DiskFlipper(Puzzle puzzle, List<DiskRelocation> diskRelocations) {
        super(puzzle, diskRelocations);
    }

    @Override
    protected Animation getDiskRelocationAnimation(DiskRelocation diskRelocation, Action doAfterEachRelocation) {
        Disk disk = diskRelocation.disk();
        Rod destRod = diskRelocation.destRod();
        
        Disk destRodTopDisk = destRod.peek();
        double destRodTopDiskTranslateY = destRodTopDisk == null ? 0.5 * disk.getBoundsInParent().getHeight() : destRodTopDisk.getTranslateY();

        Rotate rotate = new Rotate(0, (destRod.getTranslateX() - disk.getRod().getTranslateX()) / 2, 0, 0, Rotate.Z_AXIS);
        disk.getTransforms().add(rotate);

        double angle = destRod.getTranslateX() - disk.getRod().getTranslateX() > 0 ? 180 : -180;

        Animation animation = new Timeline(
            new KeyFrame(Duration.ZERO, 
                new KeyValue(disk.translateYProperty(), disk.getTranslateY()),
                new KeyValue(rotate.angleProperty(), 0)
            ),
            new KeyFrame(Duration.seconds(animationDuration / 3), 
                new KeyValue(disk.translateYProperty(), -(Puzzle.ROD_HEIGHT + disk.getBoundsInParent().getHeight() / 2)),
                new KeyValue(rotate.angleProperty(), 0)
            ),
            new KeyFrame(Duration.seconds(2 * animationDuration / 3),
                new KeyValue(disk.translateYProperty(), -(Puzzle.ROD_HEIGHT + disk.getBoundsInParent().getHeight() / 2)),
                new KeyValue(rotate.angleProperty(), angle)
            ),
            new KeyFrame(Duration.seconds(animationDuration),
                new KeyValue(disk.translateYProperty(), destRodTopDiskTranslateY - disk.getBoundsInParent().getHeight()),
                new KeyValue(rotate.angleProperty(), angle)
            )
        );
        animation.setOnFinished(event -> {
            disk.getTransforms().add(new Rotate(-angle, Rotate.Z_AXIS));
            doAfterEachRelocation.action();
        });
        return animation;
    }
    
}
