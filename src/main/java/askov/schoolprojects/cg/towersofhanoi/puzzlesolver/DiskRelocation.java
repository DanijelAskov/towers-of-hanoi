/*
 * Copyright (c) 2019, Danijel Askov
 */

package askov.schoolprojects.cg.towersofhanoi.puzzlesolver;

import askov.schoolprojects.cg.towersofhanoi.puzzle.Disk;
import askov.schoolprojects.cg.towersofhanoi.puzzle.Rod;

/**
 *
 * @author Danijel Askov
 */
public class DiskRelocation {

    private final Disk disk;
    private final Rod destRod;

    public DiskRelocation(Disk disk, Rod destRod) {
        this.disk = disk;
        this.destRod = destRod;
    }

    public Disk getDisk() {
        return disk;
    }

    public Rod getDestRod() {
        return destRod;
    }
    
    @Override
    public String toString() {
        return disk + " -> " + destRod;
    }
    
}
