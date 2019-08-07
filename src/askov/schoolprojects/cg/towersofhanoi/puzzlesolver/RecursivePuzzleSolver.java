/*
 * Copyright (c) 2019, Danijel Askov
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
