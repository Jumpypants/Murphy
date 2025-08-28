# Murphy - FTC State Machine Library

A lightweight and intuitive state machine library for FIRST Tech Challenge (FTC) robotics teams. Murphy provides a clean architecture for managing complex robot behaviors through states and tasks, making your autonomous and teleop code more organized and maintainable.

## Installation

### Using JitPack (Recommended)

1. Add JitPack to your project's root `build.gradle` file (This file may have a comment saying you probably don't need to edit it. Don't worry about that):

```gradle
allprojects {
    repositories {
        // ... other repositories
        maven { url 'https://jitpack.io' }
    }
}
```

2. Add the dependency to your `TeamCode/build.gradle` file:

```gradle
dependencies {
    implementation 'com.github.Jumpypants:Murphy:Tag'
    // ... other dependencies
}
```

Replace `Tag` with the latest release version. You can find the latest version on the [releases page](https://github.com/Jumpypants/Murphy/releases).

**Example with specific version:**
```gradle
implementation 'com.github.Jumpypants:Murphy:v1.0.0'
```

3. Sync your project and you're ready to go.

## Overview

Murphy is built around two core concepts:

- **States**: High-level robot behaviors or cycle steps (e.g., "Intake", "Score", "Defend")
- **Tasks**: Lower-level actions that can be composed and executed within the states (e.g., "Move arm to position", "Wait 2 seconds")

The library promotes a clean separation of concerns and makes complex robot behaviors easier to reason about, debug, and maintain.

### Key Features

- Simple state machine implementation
- Task composition (sequential and parallel execution)
- Elapsed time tracking for tasks
- Lightweight with no external dependencies

## Architecture

### State Machine Flow
```
StateMachine → Current State → State.step(RobotContext) → Next State
     ↑                                                      ↓
     └─────────── Update current state ←───────────────────┘
```

### Task Execution Flow
```
Task.step(RobotContext) → initialize() (first call only) → run() → return continue/stop
```

## Best Practices

1. **Keep states focused**: Each state should represent one high-level behavior
2. **Use RobotContext effectively**: Access telemetry and gamepads through the context parameter
3. **Use tasks for reusability**: Create tasks for common actions that can be reused
4. **Task ownership**: Task classes should live inside of their respective subsystem classes (A "MoveClaw" class should exist inside of your Claw class)
5. **Leverage composition**: Use SequentialTask and ParallelTask to build complex behaviors
6. **Handle timeouts**: Consider using timeouts in your tasks to prevent infinite loops
7. **Test incrementally**: Build and test simple states/tasks before composing complex behaviors

## Contributing

Feel free to submit issues and pull requests to improve the library!

## License

This project is open source and available under the MIT License.

---

*Made for the FTC community by Daniel Ben-Tsvi from Fremont High School Infernobots*
