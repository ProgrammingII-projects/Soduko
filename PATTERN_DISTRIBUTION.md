# Design Pattern Distribution - Fair Allocation

## Summary
Design patterns have been redistributed fairly among the 4 developers.

## Pattern Distribution

### Developer 1 - Model & Exceptions (2 patterns)
- **Singleton Pattern**: `GameStorage.java` - Single storage instance
- **Factory Pattern**: `GameFactory.java` - Creates games based on difficulty

### Developer 2 - View Layer (2 patterns)
- **Observer Pattern**: `GameStateObserver.java` - Interface for observing game state changes
- **Memento Pattern**: `boardHistory` Stack in `SudokuGameGUI.java` - Stores previous board states for undo

### Developer 3 - Controller Layer (2 patterns)
- **Facade Pattern**: `ControllerFacade.java` - Provides simplified interface to complex subsystems
- **Adapter Pattern**: `ControllerFacade.java` - Adapts Viewable interface to Controllable interface

### Developer 4 - Utils & Infrastructure (3 patterns)
- **Iterator Pattern**: `PermutationIterator.java` - Generates permutations for Sudoku solver
- **Flyweight Pattern**: `BoardFlyweight.java` - Efficient board representation
- **Template Method Pattern**: `solve0Thread.java` - Defines validation algorithm structure

## Total: 9 Design Patterns
- Developer 1: 2 patterns
- Developer 2: 2 patterns  
- Developer 3: 2 patterns
- Developer 4: 3 patterns

## Note on GameStateObserver
**GameStateObserver** is defined but **NOT currently used** in the codebase. It's an interface that was created for the Observer pattern but hasn't been implemented yet. The Observer pattern comments in ControllerA, ControllerB, and ControllerFacade refer to the MVC pattern where controllers can update views, not the actual GameStateObserver interface.

