# Developer Setup Guide

## Overview
This project is split into 4 separate folders for 4 developers. Each developer should upload their folder as a separate GitHub repository.

## Folder Structure

### 📁 Developer1-Model/
**GitHub Repository:** `sudoku-game-model`
- Contains: Model classes, Exceptions, Main entry point
- **Files:**
  - `model/` package (4 files)
  - `exceptions/` package (3 files)
  - `Main.java`
- **Dependencies:** None (foundation layer)

### 📁 Developer2-View/
**GitHub Repository:** `sudoku-game-view`
- Contains: GUI implementation, View facade
- **Files:**
  - `view/` package (2 files)
  - `facades/Controllable.java`
- **Dependencies:** 
  - Needs `Developer1-Model` (model & exceptions packages)
  - Needs `Developer3-Controller` (Viewable interface)

### 📁 Developer3-Controller/
**GitHub Repository:** `sudoku-game-controller`
- Contains: Controller logic, Controller facade
- **Files:**
  - `controller/` package (3 files)
  - `facades/Viewable.java`
- **Dependencies:**
  - Needs `Developer1-Model` (model & exceptions packages)
  - Needs `Developer4-Utils` (utils package)

### 📁 Developer4-Utils/
**GitHub Repository:** `sudoku-game-utils`
- Contains: Utilities, Storage, Solver, Verifier
- **Files:**
  - `utils/` package (9 files)
- **Dependencies:**
  - Needs `Developer1-Model` (model & exceptions packages)

## Integration Steps

### Step 1: Developer 1 (Model & Exceptions)
1. Upload `Developer1-Model/` to GitHub
2. Create repository: `sudoku-game-model`
3. No dependencies needed

### Step 2: Developer 4 (Utils) - Can work in parallel
1. Upload `Developer4-Utils/` to GitHub
2. Create repository: `sudoku-game-utils`
3. Add Developer 1's repository as dependency/submodule

### Step 3: Developer 3 (Controller)
1. Upload `Developer3-Controller/` to GitHub
2. Create repository: `sudoku-game-controller`
3. Add Developer 1 and Developer 4 repositories as dependencies

### Step 4: Developer 2 (View)
1. Upload `Developer2-View/` to GitHub
2. Create repository: `sudoku-game-view`
3. Add Developer 1 and Developer 3 repositories as dependencies

## Building the Complete Project

To build the complete application, combine all folders:

```
CompleteProject/
├── model/          (from Developer1)
├── exceptions/     (from Developer1)
├── Main.java       (from Developer1)
├── view/           (from Developer2)
├── facades/        (Controllable from Developer2, Viewable from Developer3)
├── controller/     (from Developer3)
└── utils/          (from Developer4)
```

## Important Notes

1. **Package Names:** Do NOT modify package names in any files
2. **Code Structure:** Keep the exact folder structure as provided
3. **Dependencies:** Each developer should reference the required dependencies in their README
4. **Git Submodules:** Consider using Git submodules for dependencies between repositories

## Testing Integration

After all developers upload their code:
1. Clone all 4 repositories
2. Combine into single project structure (as shown above)
3. Compile: `javac -cp . Main.java`
4. Run: `java Main`

