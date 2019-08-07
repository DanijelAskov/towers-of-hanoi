/*
 * Copyright (c) 2019, Danijel Askov
 */

package askov.schoolprojects.cg.towersofhanoi.puzzlesolver;

import askov.schoolprojects.cg.towersofhanoi.puzzle.Puzzle;
import java.util.List;

/**
 *
 * @author Danijel Askov
 */
@SuppressWarnings("ALL")
public abstract class PuzzleSolver {

    protected final Puzzle puzzle;
    
    protected PuzzleSolver(Puzzle puzzle) {
        this.puzzle = puzzle;
    }
    
    public abstract void solve();
    
    public abstract List<DiskRelocation> getDiskRelocations();

}
