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
