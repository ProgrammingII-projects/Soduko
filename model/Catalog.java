package model;


public class Catalog {
    
    private boolean current;
    
    
    
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

