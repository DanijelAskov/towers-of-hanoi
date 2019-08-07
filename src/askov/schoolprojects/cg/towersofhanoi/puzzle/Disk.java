/*
 * Copyright (c) 2019, Danijel Askov
 */

package askov.schoolprojects.cg.towersofhanoi.puzzle;

import askov.schoolprojects.cg.towersofhanoi.pattern.Pattern;
import askov.schoolprojects.cg.towersofhanoi.shapes.CoaxialCylinder;

/**
 *
 * @author Danijel Askov
 */
public class Disk extends CoaxialCylinder {
    
    private Rod rod;
    private Pattern pattern;
    private boolean selected;
    
    public Disk(double innerRadius, double outerRadius, float height) {
        super(innerRadius, outerRadius, height);
        
        this.setOnMouseEntered((event) -> {
            if (rod.getPuzzle().getInteractionEnabled()) {
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
            if (rod.getPuzzle().getInteractionEnabled()) {
                if (!selected) {
                    setMaterial(pattern.getMaterial());
                } else {
                    setMaterial(pattern.getSelectedMaterial());
                }
            }
        });
    }
    
    public void setRod(Rod rod) {
        this.rod = rod;
    }
    
    public Rod getRod() {
        return rod;
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
            setMaterial(pattern.getMaterial());
        }
    }
    
    public boolean isSelectable() {
        return rod != null && rod.peek() == this;
    }
    
}
