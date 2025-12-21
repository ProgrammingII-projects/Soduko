package utils;

import java.util.Iterator;
import java.util.NoSuchElementException;


public class PermutationIterator implements Iterator<int[]> {
    private final int numCells;
    private final int maxValue;
    private final int[] current;
    private final long totalPermutations;
    private long currentIndex;
    private boolean hasNext;
    
    
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
        
        
        long index = currentIndex;
        for (int i = numCells - 1; i >= 0; i--) {
            current[i] = (int) (index % maxValue) + 1; 
            index /= maxValue;
        }
        
        currentIndex++;
        hasNext = currentIndex < totalPermutations;
        
        return current.clone();
    }
    
    
    public long getTotalPermutations() {
        return totalPermutations;
    }
    
    
    public long getCurrentIndex() {
        return currentIndex - 1;
    }
}

