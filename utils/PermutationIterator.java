package utils;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Iterator Pattern: Generates permutations for Sudoku solver
 * Generates all possible combinations of values for 5 empty cells
 * Each cell can have values 1-9, so total permutations = 9^5
 * Encapsulation: Encapsulates permutation generation logic
 */
public class PermutationIterator implements Iterator<int[]> {
    private final int numCells;
    private final int maxValue;
    private final int[] current;
    private final long totalPermutations;
    private long currentIndex;
    private boolean hasNext;
    
    /**
     * Create iterator for permutations
     * @param numCells Number of cells to fill (must be 5)
     * @param maxValue Maximum value per cell (9 for Sudoku)
     */
    public PermutationIterator(int numCells, int maxValue) {
        if (numCells != 5) {
            throw new IllegalArgumentException("Solver only supports exactly 5 empty cells");
        }
        this.numCells = numCells;
        this.maxValue = maxValue;
        this.current = new int[numCells];
        this.totalPermutations = (long) Math.pow(maxValue, numCells);
        this.currentIndex = 0;
        this.hasNext = currentIndex < totalPermutations;
    }
    
    @Override
    public boolean hasNext() {
        return hasNext;
    }
    
    @Override
    public int[] next() {
        if (!hasNext) {
            throw new NoSuchElementException("No more permutations");
        }
        
        // Convert currentIndex to base-maxValue representation
        long index = currentIndex;
        for (int i = numCells - 1; i >= 0; i--) {
            current[i] = (int) (index % maxValue) + 1; // Values are 1-9, not 0-8
            index /= maxValue;
        }
        
        currentIndex++;
        hasNext = currentIndex < totalPermutations;
        
        return current.clone();
    }
    
    /**
     * Get total number of permutations
     */
    public long getTotalPermutations() {
        return totalPermutations;
    }
    
    /**
     * Get current permutation index
     */
    public long getCurrentIndex() {
        return currentIndex - 1;
    }
}

