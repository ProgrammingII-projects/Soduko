package view;


public class UserAction {
    private final int x;
    private final int y;
    private final int value;
    private final int previousValue;
    
    public UserAction(int x, int y, int value, int previousValue) {
        this.x = x;
        this.y = y;
        this.value = value;
        this.previousValue = previousValue;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public int getValue() {
        return value;
    }
    
    public int getPreviousValue() {
        return previousValue;
    }
    
    
    public String toLogString() {
        return String.format("(%d, %d, %d, %d)", x, y, value, previousValue);
    }
    
    
    public static UserAction fromLogString(String logLine) {
        
        String cleaned = logLine.trim().replace("(", "").replace(")", "");
        String[] parts = cleaned.split(",");
        if (parts.length != 4) {
            throw new IllegalArgumentException("Invalid log format: " + logLine);
        }
        int x = Integer.parseInt(parts[0].trim());
        int y = Integer.parseInt(parts[1].trim());
        int val = Integer.parseInt(parts[2].trim());
        int prev = Integer.parseInt(parts[3].trim());
        return new UserAction(x, y, val, prev);
    }
}

