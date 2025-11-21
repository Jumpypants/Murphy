package com.jumpypants.murphy.tasks;

import com.jumpypants.murphy.util.RobotContext;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Abstract base class for encapsulating robot operations and state transitions.
 * Tasks can represent mechanical movements, sensor operations, or other processes.
 * Extend this class within subsystem implementations to create reusable components.
 * Tasks can be composed together to perform complex operations.
 */
public abstract class Task {
    /**
     * Elapsed time since task initialization.
     */
    protected final ElapsedTime ELAPSED_TIME = new ElapsedTime();
    protected boolean initialized = false;
    protected final RobotContext ROBOT_CONTEXT;

    /**
     * Creates a new Task with the provided RobotContext.
     * @param robotContext contains references like telemetry, gamepads, and subsystems
     */
    public Task(RobotContext robotContext) {
        this.ROBOT_CONTEXT = robotContext;
    }

    /**
     * Executes the task. Calls initialize() on first invocation, then run() on each subsequent call.
     * @return true if the task should continue running, false if completed
     */
    public final boolean step() {
        if (ROBOT_CONTEXT == null) {
            throw new IllegalArgumentException("RobotContext cannot be null");
        }
        if (!initialized) {
            ELAPSED_TIME.reset();
            initialize(ROBOT_CONTEXT);
            initialized = true;
        }
        return run(ROBOT_CONTEXT);
    }

    /**
     * Initializes the task. Called once before the first run() invocation.
     * @param robotContext contains references like telemetry, gamepads, and subsystems
     */
    protected abstract void initialize(RobotContext robotContext);

    /**
     * Executes the main task logic. Called on each step() invocation after initialization.
     * @param robotContext contains references like telemetry, gamepads, and subsystems
     * @return true to continue execution, false when task is complete
     */
    protected abstract boolean run(RobotContext robotContext);
}
