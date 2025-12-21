# Developer 3 - Controller Layer

## Responsibilities
- Controller logic
- Game management
- Verification and solving coordination
- Controller facade interface (Viewable)

## Package Structure
```
controller/
  - ControllerA.java (Game management)
  - ControllerB.java (Verification & Solving)
  - ControllerFacade.java (Facade/Adapter Pattern)

facades/
  - Viewable.java
```

## Design Patterns
- **Facade Pattern**: ControllerFacade - Provides simplified interface to complex subsystems
- **Adapter Pattern**: ControllerFacade - Adapts Viewable interface to Controllable interface

## Dependencies
- Requires: Developer 1 (Model & Exceptions)
- Requires: Developer 4 (Utils)
- Uses: Developer 2's Controllable interface (to update view)
- Implements: Viewable interface (exposed to view)

## Notes
- Implements Viewable interface (exposed to view)
- Uses Controllable interface to update view
- Coordinates between ControllerA and ControllerB
- Acts as adapter between Viewable and Controllable interfaces

