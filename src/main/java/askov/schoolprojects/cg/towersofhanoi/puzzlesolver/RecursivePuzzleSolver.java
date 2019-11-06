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

import askov.schoolprojects.cg.towersofhanoi.puzzle.Puzzle;
import askov.schoolprojects.cg.towersofhanoi.puzzle.Rod;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Danijel Askov
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class RecursivePuzzleSolver extends PuzzleSolver {

    private final List<DiskRelocation> moves = new ArrayList<>();
    private final StringBuilder stringBuilder = new StringBuilder();
    private int moveIndex = 1;
    
    public RecursivePuzzleSolver(Puzzle puzzle) {
        super(puzzle);
    }
    
    public void move(int n, Rod sourceRod, Rod targetRod, Rod auxiliaryRod) {
        if (n > 0) {
            move(n - 1, sourceRod, auxiliaryRod, targetRod);

            DiskRelocation move;
            moves.add(move = new DiskRelocation(sourceRod.peek(), targetRod));
            stringBuilder.append(moveIndex++).append(") ").append(move).append("\n");
            targetRod.push(sourceRod.pop());
            
            move(n - 1, auxiliaryRod, targetRod, sourceRod);
        }
    }
    
    @Override
    public void solve() {
        moves.clear();
        stringBuilder.setLength(0);
        move(puzzle.getNumDisks(), puzzle.getRods()[0], puzzle.getRods()[2], puzzle.getRods()[1]);
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        puzzle.getRods()[0].getDisksFrom(puzzle.getRods()[2]);
    }

    @Override
    public List<DiskRelocation> getDiskRelocations() {
        return moves;
    }
    
    @Override
    public String toString() {
        return stringBuilder.toString();
    }

}
