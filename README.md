# Murphy - FTC State Machine Library

A lightweight and intuitive state machine library for FIRST Tech Challenge (FTC) robotics teams. Murphy provides a clean architecture for managing complex robot behaviors through states and tasks, making your autonomous and teleop code more organized and maintainable.

I highly recommend checking out the example project to help you understand how to use the library: [Example Project](https://github.com/Jumpypants/MurphyExample)

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

### The Problem
FTC robots need to perform complex sequences of actions—from autonomous routines that score multiple game pieces to teleop behaviors that coordinate multiple subsystems. As your robot becomes more sophisticated, managing all these behaviors in traditional code becomes messy, hard to debug, and difficult to modify.

### The Solution
Think of Murphy like a **playbook for your robot**. Just like a football team has different plays (offense, defense, special teams) and each play has specific steps, your robot can have different **States** for major behaviors, and each state can execute **Tasks** to get things done.

**Real FTC Example:**
```
Autonomous Routine:
├── "Drive to Game Elements" State
│   ├── Drive forward 24 inches (Task)
│   ├── Turn 90 degrees (Task)
│   └── Wait 0.5 seconds (Task)
├── "Score Game Element" State  
│   ├── Raise arm to scoring position (Task)
│   ├── Release game element (Task)
│   └── Lower arm (Task)
└── "Park" State
    └── Drive to parking zone (Task)
```

### Core Concepts

- **States**: Major robot behaviors or phases (e.g., "Drive to Goal", "Score Game Piece", "Defend")
  - Like different "modes" your robot can be in
  - Each state decides what the robot should do and when to switch to another state
  
- **Tasks**: Individual actions that make up a state (e.g., "Move arm to position", "Drive 12 inches", "Wait 2 seconds")
  - Like individual steps in a recipe
  - Can be reused across different states
  - Can run one after another (sequential) or at the same time (parallel)

### Why This Helps
- **Organized**: Your code mirrors how you think about robot behavior
- **Debuggable**: Easy to see which state/task is running and troubleshoot problems
- **Reusable**: Write a "MoveArm" task once, use it everywhere
- **Flexible**: Change your autonomous by rearranging states, not rewriting everything
- **Team-Friendly**: New programmers can understand and contribute more easily

### Key Features

- 🎯 **Simple to learn** - If you can think through your robot's behavior step-by-step, you can use Murphy
- 🔧 **Task composition** - Run tasks sequentially (one after another) or in parallel (at the same time)
- ⏱️ **Built-in timing** - Every task knows how long it's been running
- 🪶 **Lightweight** - No external dependencies, won't slow down your robot
- 🤝 **FTC-native** - Designed specifically for FTC teams and robot behaviors

## Architecture

Now that you understand the concepts, here's how Murphy works under the hood:

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
