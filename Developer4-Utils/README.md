# Developer 4 - Utils & Infrastructure Layer

## Responsibilities
- Utility classes
- Storage system
- Solver implementation
- Verifier implementation
- Design pattern implementations (Iterator, Flyweight, Factory, etc.)

## Package Structure
```
utils/
  - BoardFlyweight.java (Flyweight Pattern)
  - PermutationIterator.java (Iterator Pattern)
  - RandomPairs.java
  - solve0Thread.java (Template Method Pattern)
  - SudokuSolver.java
  - SudokuVerifier.java
```

## Design Patterns
- **Iterator Pattern**: PermutationIterator - Generates permutations for Sudoku solver
- **Flyweight Pattern**: BoardFlyweight - Efficient board representation for solver
- **Template Method Pattern**: solve0Thread - Defines validation algorithm structure

## Dependencies
- Requires: Developer 1 (Model & Exceptions)
- Used by: Developer 3 (Controller)

## Notes
- Contains solver and verifier utilities
- Implements 3 design patterns focused on solver algorithms

