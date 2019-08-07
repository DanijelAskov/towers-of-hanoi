/*
 * Copyright (c) 2019, Danijel Askov
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
        Disk disk = diskRelocation.getDisk();
        Rod destRod = diskRelocation.getDestRod();
        
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
