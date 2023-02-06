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
            Disk disk = currentMove.disk();
            Rod destRod = currentMove.destRod();
            
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
        diskRelocations[currentRelocationIndex].disk().setSelected(false);
        diskRelocations[currentRelocationIndex].destRod().setSelected(false);
        currentRelocationIndex = 0;
    }
    
    public int getCurrentRelocationIndex() {
        return currentRelocationIndex;
    }
    
    public int getNumDiskRelocations() {
        return diskRelocations.length;
    }
    
}
