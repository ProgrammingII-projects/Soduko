# Developer 2 - View Layer

## Responsibilities
- GUI implementation
- User interface components
- View facade interface (Controllable)

## Package Structure
```
view/
  - SudokuGameGUI.java (contains Memento Pattern)
  - UserAction.java

facades/
  - Controllable.java

utils/
  - GameStateObserver.java (Observer Pattern)
```

## Design Patterns
- **Observer Pattern**: GameStateObserver - Interface for observing game state changes
- **Memento Pattern**: boardHistory Stack in SudokuGameGUI - Stores previous board states for undo

## Dependencies
- Requires: Developer 1 (Model & Exceptions)
- Uses: Developer 3's Viewable interface (controller facade)
- Implements: Controllable interface (view facade)

## Notes
- Implements Controllable interface (signals user actions)
- Uses Viewable interface to invoke controller methods
- Handles all GUI presentation logic

