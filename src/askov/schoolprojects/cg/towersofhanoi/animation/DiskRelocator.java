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

/**
 *
 * @author Danijel Askov
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class DiskRelocator {

    public interface Action {
        
        void action();
        
    }
    
    public static final double DEFAULT_ANIMATION_DURATION = 1;
    protected double animationDuration = DEFAULT_ANIMATION_DURATION;

    private int currentRelocationIndex = -1;
    protected final Puzzle puzzle;
    private final DiskRelocation[] diskRelocations;
    private Animation currentAnimation;
    
    protected DiskRelocator(Puzzle puzzle, DiskRelocation... diskRelocations) {
        this.puzzle = puzzle;
        this.diskRelocations = diskRelocations;
    }
    
    protected DiskRelocator(Puzzle puzzle, List<DiskRelocation> diskRelocations) {
        this.puzzle = puzzle;
        this.diskRelocations = diskRelocations.toArray(new DiskRelocation[0]);
    }
    
    protected abstract Animation getDiskRelocationAnimation(DiskRelocation move, Action doAfterEachRelocation);
    
    public final void start(Action doAfterLastRelocation) {
        currentRelocationIndex++;
        if (currentRelocationIndex < diskRelocations.length) {
            DiskRelocation currentMove = diskRelocations[currentRelocationIndex];
            Disk disk = currentMove.getDisk();
            Rod destRod = currentMove.getDestRod();
            
            disk.setSelected(true);
            destRod.setSelected(true);
            
            currentAnimation = getDiskRelocationAnimation(currentMove, () -> {          
                disk.getRod().pop();
                disk.setSelected(false);
                 
                destRod.push(disk);
                destRod.setSelected(false);

                start(doAfterLastRelocation); 
            });
            currentAnimation.setAutoReverse(false);
            currentAnimation.setCycleCount(0);
            currentAnimation.play();
        } else {
            doAfterLastRelocation.action();
        }
    }
    
    public final void start() {
        start(() -> {});
    }
    
    public final void setAnimationDuration(double animationDuration) {
        this.animationDuration = animationDuration;
    }
    
    public final void pause() {
        currentAnimation.pause();
    }
    
    public final void resume() {
        currentAnimation.play();
    }
    
    public final void stop() {
        currentAnimation.stop();
        diskRelocations[currentRelocationIndex].getDisk().setSelected(false);
        diskRelocations[currentRelocationIndex].getDestRod().setSelected(false);
        currentRelocationIndex = 0;
    }
    
    public int getCurrentRelocationIndex() {
        return currentRelocationIndex;
    }
    
    public int getNumDiskRelocations() {
        return diskRelocations.length;
    }
    
}
