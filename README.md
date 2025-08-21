# Murphy - FTC State Machine Library

A lightweight and intuitive state machine library for FIRST Tech Challenge (FTC) robotics teams. Murphy provides a clean architecture for managing complex robot behaviors through states and tasks, making your autonomous and teleop code more organized and maintainable.

## Installation

### Using JitPack (Recommended)

1. Add JitPack to your project's root `build.gradle` file:

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
    implementation 'com.github.Jumpypants:Murphy:1.0.0'
    // ... other dependencies
}
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
StateMachine → Current State → State.step() → Next State
     ↑                                           ↓
     └─────────── Update current state ←────────┘
```

### Task Execution Flow
```
Task.step() → initialize() (first call only) → run() → return continue/stop
```

##  API Reference

### StateMachine

The main controller that manages state transitions and execution.

```java
StateMachine stateMachine = new StateMachine(initialState);

// In your OpMode loop
stateMachine.step(telemetry);
```

**Methods:**
- `StateMachine(State initialState)` - Constructor with starting state
- `step(Telemetry telemetry)` - Execute current state and handle transitions

### State Interface

Implement this interface to create custom robot states.

```java
public class DriveToPositionState implements State {
    @Override
    public State step() {
        // Your state logic here
        if (taskComplete) {
            return new NextState();
        }
        return this; // Continue in this state
    }
    
    @Override
    public String getName() {
        return "Drive To Position";
    }
}
```

**Methods:**
- `step()` - Called every loop iteration, returns next state
- `getName()` - Returns state name for telemetry display

### Task (Abstract Class)

Base class for all tasks. Extend this to create reusable actions.

```java
public class MoveArmTask extends Task {
    private final double targetPosition;
    
    public MoveArmTask(double target) {
        this.targetPosition = target;
    }
    
    @Override
    protected void initialize(Telemetry telemetry) {
        // Setup code (runs once)
        robot.arm.setTargetPosition(targetPosition);
    }
    
    @Override
    protected boolean run(Telemetry telemetry) {
        // Execution code (runs every loop)
        telemetry.addData("Arm Position", robot.arm.getCurrentPosition());
        return !robot.arm.isAtTarget(); // Return false when done
    }
}
```

**Protected Fields:**
- `ELAPSED_TIME` - ElapsedTime instance for timing operations
- `initialized` - Boolean flag for initialization state

**Methods:**
- `step(Telemetry telemetry)` - Main execution method (call this)
- `initialize(Telemetry telemetry)` - Override for setup code
- `run(Telemetry telemetry)` - Override for main execution logic

### SequentialTask

Executes multiple tasks in order, waiting for each to complete before starting the next.

```java
Task sequence = new SequentialTask(
    new MoveArmTask(1000),
    new WaitTask(2.0),
    new MoveArmTask(0)
);
```

**Constructor:**
- `SequentialTask(Task... actions)` - Variable number of tasks to execute in order

### ParallelTask

Executes multiple tasks simultaneously.

```java
Task parallel = new ParallelTask(
    false, // Wait for ALL tasks to complete
    new MoveArmTask(1000),
    new DriveTask(24.0),
    new IntakeTask()
);

Task raceCondition = new ParallelTask(
    true, // Stop when FIRST task completes
    new DriveUntilLineTask(),
    new TimeoutTask(5.0)
);
```

**Constructor:**
- `ParallelTask(boolean stopOnFirstCompletion, Task... actions)`
  - `stopOnFirstCompletion`: If true, stops when any task completes; if false, waits for all tasks

## Usage Examples

### Basic State Machine

```java
@Autonomous
public class MyAutonomous extends LinearOpMode {
    @Override
    public void runOpMode() {
        StateMachine stateMachine = new StateMachine(new StartState());
        
        waitForStart();
        
        while (opModeIsActive()) {
            stateMachine.step(telemetry);
            telemetry.update();
        }
    }
}
```

### Complex Task Composition

```java
public class ScoreSequence extends Task {
    private final SequentialTask scoreTask;
    
    public ScoreSequence() {
        scoreTask = new SequentialTask(
            new ParallelTask(false,
                new MoveArmTask(HIGH_POSITION),
                new ExtendSlideTask(EXTENDED_POSITION)
            ),
            new WaitTask(0.5),
            new ReleaseClawTask(),
            new WaitTask(1.0),
            new ParallelTask(false,
                new MoveArmTask(REST_POSITION),
                new RetractSlideTask()
            )
        );
    }
    
    @Override
    protected void initialize(Telemetry telemetry) {}
    
    @Override
    protected boolean run(Telemetry telemetry) {
        return scoreTask.step(telemetry);
    }
}
```

### State with Task Integration

```java
public class AutonomousState implements State {
    private final Task autonomousSequence;
    private boolean taskStarted = false;
    
    public AutonomousState() {
        autonomousSequence = new SequentialTask(
            new DriveToPositionTask(24, 0, 0),
            new ScoreSequence(),
            new DriveToPositionTask(0, 0, 0)
        );
    }
    
    @Override
    public State step() {
        if (!taskStarted || autonomousSequence.step(telemetry)) {
            taskStarted = true;
            return this;
        }
        return new IdleState(); // Task completed
    }
    
    @Override
    public String getName() {
        return "Autonomous";
    }
}
```

## Best Practices

1. **Keep states focused**: Each state should represent one high-level behavior
2. **Use tasks for reusability**: Create tasks for common actions that can be reused
3. **Task ownership**: Task classes should live inside of their respective subsytem classes (A "MoveClaw" class should exist inside of your Claw class)
4. **Leverage composition**: Use SequentialTask and ParallelTask to build complex behaviors
5. **Handle timeouts**: Consider using timeouts in your tasks to prevent infinite loops
6. **Test incrementally**: Build and test simple states/tasks before composing complex behaviors

## Contributing

Feel free to submit issues and pull requests to improve the library!

## License

This project is open source and available under the MIT License.

---

*Made for the FTC community by Daniel Ben-Tsvi from Fremont High School Infernobots*
