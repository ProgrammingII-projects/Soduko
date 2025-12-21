# Developer 1 - Model & Exceptions Layer

## Responsibilities
- Model layer (domain objects)
- Exception classes
- Main entry point

## Package Structure
```
model/
  - Catalog.java
  - DifficultyEnum.java
  - Game.java
  - GameState.java

exceptions/
  - InvalidGameException.java
  - NotFoundException.java
  - SolutionInvalidException.java

utils/
  - GameStorage.java (Singleton Pattern)
  - GameFactory.java (Factory Pattern)

Main.java
```

## Design Patterns
- **Singleton Pattern**: GameStorage - Single storage instance
- **Factory Pattern**: GameFactory - Creates games based on difficulty

## Dependencies
- No dependencies on other developers' code
- Pure domain model, exceptions, and foundational utilities

## Notes
- This is the foundation layer
- Other developers depend on these model classes
- Main.java initializes the application

