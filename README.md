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
implementation 'com.github.Jumpypants:Murphy:v3.1.1'
```

3. Sync your project and you're ready to go.

## Overview

Murphy is built around three core concepts:

- **RobotContext**: A shared container for your robot's subsystems, telemetry, and gamepads
- **States**: High-level robot behaviors or cycle steps (e.g., "Intake", "Score", "Defend")
- **Tasks**: Lower-level actions that can be composed and executed within the states (e.g., "Move arm to position", "Wait 2 seconds")

The library promotes a clean separation of concerns and makes complex robot behaviors easier to reason about, debug, and maintain.

### Key Features

- Simple state machine implementation
- Task composition (sequential and parallel execution)
- Elapsed time tracking for tasks
- Centralized resource management through RobotContext
- Lightweight with no external dependencies

### Basic Usage Example

Here's how you'd set up Murphy in a typical FTC OpMode:

```java
@TeleOp(name="My TeleOp")
public class MyTeleOp extends OpMode {
    private StateMachine stateMachine;

    @Override
    public void init() {
        // 1. Create your robot context with subsystems
        MyRobot robotContext = new MyRobot(telemetry, gamepad1, gamepad2, /* your subsystems */);
        
        // 2. Create initial state and state machine
        stateMachine = new StateMachine(new IdleState(robotContext), robotContext);
    }

    @Override
    public void loop() {
        // 3. Step the state machine each loop
        stateMachine.step();
    }
}
```

## Architecture

### Core Components

#### 1. RobotContext
Your team creates a class extending `RobotContext` to hold your robot's subsystems:

```java
public class MyRobot extends RobotContext {
    public final DriveTrain driveTrain;
    public final Arm arm;
    public final Claw claw;

    public MyRobot(Telemetry telemetry, Gamepad gamepad1, Gamepad gamepad2, 
                   DriveTrain driveTrain, Arm arm, Claw claw) {
        super(telemetry, gamepad1, gamepad2);
        this.driveTrain = driveTrain;
        this.arm = arm;
        this.claw = claw;
    }
}
```

#### 2. States
States represent high-level robot behaviors and handle state transitions:

```java
public class IntakeState implements State {
    private final MyRobot robot;
    private final Task intakeTask;

    public IntakeState(MyRobot robot) {
        this.robot = robot;
        this.intakeTask = new SequentialTask(robot,
            robot.arm.new MoveToPosition(robot, ARM_DOWN),
            robot.claw.new OpenClaw(robot)
        );
    }

    @Override
    public State step() {
        // Handle driving
        robot.driveTrain.drive(robot.gamepad1.left_stick_x, robot.gamepad1.left_stick_y);
        
        // Run intake task
        if (!intakeTask.step()) {
            // Task finished, transition to next state
            return new ScoreState(robot);
        }
        
        return this; // Stay in this state
    }

    @Override
    public String getName() {
        return "Intake";
    }
}
```

#### 3. Tasks
Tasks encapsulate reusable actions and are typically inner classes of subsystems:

```java
public class Arm {
    // ... hardware setup ...

    public class MoveToPosition extends Task {
        private final double targetPosition;

        public MoveToPosition(RobotContext robotContext, double targetPosition) {
            super(robotContext);
            this.targetPosition = targetPosition;
        }

        @Override
        protected void initialize(RobotContext robotContext) {
            // Set up PID controller or motion profile
        }

        @Override
        protected boolean run(RobotContext robotContext) {
            // Move arm and return true while moving, false when done
            return Math.abs(getCurrentPosition() - targetPosition) > TOLERANCE;
        }
    }
}
```

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
2. **Extend RobotContext**: Create your own robot class extending RobotContext to include your subsystems
3. **Use RobotContext effectively**: Access telemetry and gamepads through the context parameter
4. **Use tasks for reusability**: Create tasks for common actions that can be reused across states
5. **Task ownership**: Task classes should live inside of their respective subsystem classes (A "MoveClaw" class should exist inside of your Claw class)
6. **Leverage composition**: Use SequentialTask and ParallelTask to build complex behaviors
7. **Handle timeouts**: Consider using timeouts in your tasks to prevent infinite loops
8. **Test incrementally**: Build and test simple states/tasks before composing complex behaviors
9. **Return patterns**: States return `this` to stay in current state, or a new state to transition. Tasks return `true` to continue, `false` when complete
10. **Use ELAPSED_TIME**: Tasks have built-in `ELAPSED_TIME` for implementing timeouts and time-based logic

### Task Composition Examples

**Sequential Tasks** (one after another):
```java
new SequentialTask(robotContext,
    arm.new MoveToPosition(robotContext, HIGH_POSITION),
    claw.new OpenClaw(robotContext),
    new WaitTask(robotContext, 1.0) // Wait 1 second
);
```

**Parallel Tasks** (simultaneously):
```java
new ParallelTask(robotContext, false, // false = wait for all to complete
    arm.new MoveToPosition(robotContext, HIGH_POSITION),
    claw.new RotateWrist(robotContext, SCORING_ANGLE)
);
```

**Nested Composition**:
```java
new SequentialTask(robotContext,
    // First: Move arm and rotate wrist simultaneously
    new ParallelTask(robotContext, false,
        arm.new MoveToPosition(robotContext, HIGH_POSITION),
        claw.new RotateWrist(robotContext, SCORING_ANGLE)
    ),
    // Then: Release the game piece
    claw.new OpenClaw(robotContext)
);
```

## Contributing

Feel free to submit issues and pull requests to improve the library!

## License

This project is open source and available under the MIT License.

---

*Made for the FTC community by Daniel Ben-Tsvi from Fremont High School Infernobots*
