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

import askov.schoolprojects.cg.towersofhanoi.pattern.Pattern;
import askov.schoolprojects.cg.towersofhanoi.shapes.CoaxialCylinder;
import javafx.scene.Cursor;

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
                    setCursor(Cursor.HAND);
                } else {
                    setMaterial(pattern.getUnselectableMaterial());
                    setCursor(Cursor.DEFAULT);
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
            setCursor(Cursor.DEFAULT);
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
