/*
 * Copyright (c) 2019, Danijel Askov
 */

package askov.schoolprojects.cg.towersofhanoi.puzzle;

import askov.schoolprojects.cg.towersofhanoi.pattern.Pattern;
import java.util.EmptyStackException;
import java.util.Stack;
import javafx.scene.shape.Cylinder;

/**
 *
 * @author Danijel Askov
 */
public class Rod extends Cylinder {
    
    private Stack<Disk> disks = new Stack<>();
    private final Puzzle puzzle;
    private Pattern pattern;
    private boolean selected;
    
    public Rod(double radius, double height, Puzzle puzzle) {
        super(radius, height);
        this.puzzle = puzzle;
        
        this.setOnMouseEntered((event) -> {
            if (puzzle.getInteractionEnabled()) {
                if (isSelectable()) {
                    if (!selected) {
                        setMaterial(pattern.getSelectableMaterial());
                    }
                } else {
                    setMaterial(pattern.getUnselectableMaterial());
                }
            }
        });
        this.setOnMouseExited((event) -> {
            if (puzzle.getInteractionEnabled()) {
                if (!selected) {
                    setMaterial(pattern.getMaterial());
                } else {
                    setMaterial(pattern.getSelectedMaterial());
                }
            }
        });
    }
    
    public void push(Disk disk) {
        disks.push(disk);
        disk.setRod(this);
    }
    
    public Disk pop() {
        Disk topDisk = null; 
        try {
            topDisk = disks.pop();
        } catch (EmptyStackException ignored) {}
        if (topDisk != null) {
            topDisk.setRod(null);
        }
        return topDisk;
    }
    
    public Disk peek() {
        try {
            return disks.peek();
        } catch (EmptyStackException exception) {
            return null;
        }
    }
    
    public void clear() {
        disks.clear();
    }
    
    public void setSelected(boolean selected) {
        this.selected = selected;
        if (selected) {
            setMaterial(pattern.getSelectedMaterial());
        } else {
            setMaterial(pattern.getMaterial());
        }
    }

    void setPattern(Pattern pattern) {
        this.pattern = pattern;
        if (!selected) {
            this.setMaterial(pattern.getMaterial());
        }
    }
    
    public Object[] getDisks() {
        return disks.toArray();
    }
    
    public int getNumDisks() {
        return disks.size();
    }
    
    public void getDisksFrom(Rod rod) {
        Stack<Disk> temp = disks;
        disks = rod.disks;
        for (Disk disk : disks) {
            disk.setRod(this);
        }
        rod.disks = temp;
    }
    
    public Puzzle getPuzzle() {
        return puzzle;
    }
    
    public boolean isSelectable() {
        return puzzle.getSelectedDisk() != null && (disks.isEmpty() || disks.peek().getOuterRadius() > puzzle.getSelectedDisk().getOuterRadius());
    }

}