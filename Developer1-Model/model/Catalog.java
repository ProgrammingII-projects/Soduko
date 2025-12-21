package model;

/**
 * Catalog class representing game availability status
 * Used only in controller layer
 */
public class Catalog {
    // True if there is a game in progress, False otherwise.
    private boolean current;
    
    // True if there is at least one game available
    // for each difficulty, False otherwise.
    private boolean allModesExist;
    
    public Catalog(boolean current, boolean allModesExist) {
        this.current = current;
        this.allModesExist = allModesExist;
    }
    
    public boolean hasCurrent() {
        return current;
    }
    
    public void setCurrent(boolean current) {
        this.current = current;
    }
    
    public boolean hasAllModesExist() {
        return allModesExist;
    }
    
    public void setAllModesExist(boolean allModesExist) {
        this.allModesExist = allModesExist;
    }
}

