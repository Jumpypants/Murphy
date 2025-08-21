package com.jumpypants.murphy;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

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

    /**
     * Executes the task. Calls initialize() on first invocation, then run() on each subsequent call.
     * @param telemetry Telemetry instance for debugging output
     * @return true if the task should continue running, false if completed
     */
    public final boolean step(Telemetry telemetry) {
        if (telemetry == null) {
            throw new IllegalArgumentException("Telemetry cannot be null");
        }
        if (!initialized) {
            ELAPSED_TIME.reset();
            initialize(telemetry);
            initialized = true;
        }
        return run(telemetry);
    }

    /**
     * Initializes the task. Called once before the first run() invocation.
     * @param telemetry Telemetry instance for debugging output
     */
    protected abstract void initialize(Telemetry telemetry);

    /**
     * Executes the main task logic. Called on each step() invocation after initialization.
     * @param telemetry Telemetry instance for debugging output
     * @return true to continue execution, false when task is complete
     */
    protected abstract boolean run(Telemetry telemetry);
}
