# Sudoku Game Project - Multi-Developer Structure

This project is organized into 4 separate folders for 4 developers to work independently and upload to separate GitHub repositories.

## Folder Organization

### Developer 1 - Model & Exceptions (`Developer1-Model/`)
**Responsibilities:** Domain model, exceptions, main entry point
- `model/` - All model classes (Game, Catalog, DifficultyEnum, GameState)
- `exceptions/` - All exception classes
- `Main.java` - Application entry point

**Dependencies:** None (foundation layer)

---

### Developer 2 - View Layer (`Developer2-View/`)
**Responsibilities:** GUI implementation, user interface
- `view/` - GUI classes (SudokuGameGUI, UserAction)
- `facades/Controllable.java` - View facade interface

**Dependencies:** 
- Requires Developer 1 (Model & Exceptions)
- Uses Developer 3's Viewable interface

---

### Developer 3 - Controller Layer (`Developer3-Controller/`)
**Responsibilities:** Controller logic, game management, coordination
- `controller/` - Controller classes (ControllerA, ControllerB, ControllerFacade)
- `facades/Viewable.java` - Controller facade interface

**Dependencies:**
- Requires Developer 1 (Model & Exceptions)
- Requires Developer 4 (Utils)
- Uses Developer 2's Controllable interface

---

### Developer 4 - Utils & Infrastructure (`Developer4-Utils/`)
**Responsibilities:** Utilities, storage, solver, verifier, design patterns
- `utils/` - All utility classes (Storage, Solver, Verifier, etc.)

**Dependencies:**
- Requires Developer 1 (Model & Exceptions)
- Used by Developer 3

---

## Integration Notes

1. **Developer 1** should be completed first (foundation)
2. **Developer 4** can work in parallel with Developer 1
3. **Developer 3** depends on Developers 1 and 4
4. **Developer 2** depends on Developers 1 and 3

## GitHub Upload Instructions

Each developer should:
1. Upload their respective folder as a separate repository
2. Include the README.md file in their folder
3. Maintain package structure exactly as provided
4. Do not modify package names or structure

## Build Instructions

To build the complete project:
1. Combine all 4 folders into a single project structure
2. Ensure all packages are in the correct locations
3. Compile: `javac -cp . Main.java`
4. Run: `java Main`

