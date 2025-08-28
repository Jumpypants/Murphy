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

## API Reference

### RobotContext

A centralized container for shared resources used throughout the robot control system.
You extend this abstract class to include other references if you want to (subsystems, hardware, etc.).

```java
// Define your concrete context (add subsystem fields as needed)
public class MyRobotContext extends RobotContext {
    // public final Arm arm;
    // public final DriveTrain drive;

    public MyRobotContext(Telemetry telemetry, Gamepad gamepad1, Gamepad gamepad2) {
        super(telemetry, gamepad1, gamepad2);
        // arm = new Arm(...);
        // drive = new DriveTrain(...);
    }
}

// Usage
RobotContext robotContext = new MyRobotContext(telemetry, gamepad1, gamepad2);
```

**Fields:**
- `telemetry` - Telemetry instance for driver station communication
- `gamepad1` - Primary gamepad controller (typically driver)
- `gamepad2` - Secondary gamepad controller (typically operator)

**Constructor (for subclasses):**
- `RobotContext(Telemetry telemetry, Gamepad gamepad1, Gamepad gamepad2)` - All parameters required and cannot be null. Since RobotContext is abstract, instantiate a subclass (e.g., `MyRobotContext`).

### StateMachine

The main controller that manages state transitions and execution.

```java
StateMachine stateMachine = new StateMachine(initialState, robotContext);

// In your OpMode loop
stateMachine.step();
```

**Methods:**
- `StateMachine(State initialState, RobotContext robotContext)` - Constructor with starting state and context
- `step()` - Execute current state and handle transitions

### State Interface

Implement this interface to create custom robot states.

```java
public class IntakingState implements State {
    @Override
    public State step(RobotContext robotContext) {
        // Access telemetry and gamepads through robotContext
        robotContext.telemetry.addData("State", "Intaking game element");
        
        // Your state logic here
        if (gameElementDetected) {
            return new HoldingElementState();
        }
        return this; // Continue in this state
    }
    
    @Override
    public String getName() {
        return "Intaking";
    }
}
```

**Methods:**
- `step(RobotContext robotContext)` - Called every loop iteration, returns next state
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
    protected void initialize(RobotContext robotContext) {
        // Setup code (runs once)
        robot.arm.setTargetPosition(targetPosition);
        robotContext.telemetry.addData("Arm Target", targetPosition);
    }
    
    @Override
    protected boolean run(RobotContext robotContext) {
        // Execution code (runs every loop)
        robotContext.telemetry.addData("Arm Position", robot.arm.getCurrentPosition());
        
        // Access gamepad inputs if needed
        if (robotContext.gamepad1.a) {
            // Manual stop logic
        }
        
        return !robot.arm.isAtTarget(); // Return false when done
    }
}
```

**Protected Fields:**
- `ELAPSED_TIME` - ElapsedTime instance for timing operations
- `initialized` - Boolean flag for initialization state

**Methods:**
- `step(RobotContext robotContext)` - Main execution method (call this)
- `initialize(RobotContext robotContext)` - Override for setup code
- `run(RobotContext robotContext)` - Override for main execution logic

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
        RobotContext robotContext = new MyRobotContext(telemetry, gamepad1, gamepad2);
        StateMachine stateMachine = new StateMachine(new StartState(), robotContext);
        
        waitForStart();
        
        while (opModeIsActive()) {
            stateMachine.step();
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
    protected void initialize(RobotContext robotContext) {}
    
    @Override
    protected boolean run(RobotContext robotContext) {
        return scoreTask.step(robotContext);
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
    public State step(RobotContext robotContext) {
        robotContext.telemetry.addData("Autonomous", "Running sequence");
        
        if (!taskStarted || autonomousSequence.step(robotContext)) {
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

### TeleOp with Gamepad Integration

```java
public class TeleOpDriveState implements State {
    @Override
    public State step(RobotContext robotContext) {
        // Access both gamepads through the context
        double drive = -robotContext.gamepad1.left_stick_y;
        double strafe = robotContext.gamepad1.left_stick_x;
        double turn = robotContext.gamepad1.right_stick_x;
        
        // Drive the robot
        robot.drivetrain.drive(drive, strafe, turn);
        
        // Operator controls on gamepad2
        if (robotContext.gamepad2.a) {
            return new IntakeState();
        }
        if (robotContext.gamepad2.b) {
            return new ScoreState();
        }
        
        robotContext.telemetry.addData("Drive", "%.2f", drive);
        robotContext.telemetry.addData("Strafe", "%.2f", strafe);
        robotContext.telemetry.addData("Turn", "%.2f", turn);
        
        return this;
    }
    
    @Override
    public String getName() {
        return "TeleOp Drive";
    }
}
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
