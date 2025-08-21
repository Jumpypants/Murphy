package com.jumpypants.murphy;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * This class can be used to separate different parts of the robot's state.
 * A task can represent a mechanical movement like moving a slide to a certain position.
 * It can also represent a software process like waiting until a sensor reads a certain value.
 * It is recommended to extend 'Task' within a class that represents a subsystem of the robot.
 * Multiple tasks can be stringed together to perform complicated operations.
 */
public abstract class Task {
    /**
     * How long it has been since the action was initialized.
     */
    protected final ElapsedTime ELAPSED_TIME = new ElapsedTime();
    protected boolean initialized = false;

    /**
     * Call this to run the task. It will call the 'run' method.
     * The 'initialize' method will be called if this is the first time calling 'step'.
     * @param telemetry
     * A 'Telemetry' instance to be used for debugging.
     * @return
     * Returns whether the task wants to be run another time (will return false if the task is finished).
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
     * This method will be called upon the first call of 'step' (when the task is first run).
     * @param telemetry
     * A 'Telemetry' instance to be used for debugging.
     */
    protected abstract void initialize(Telemetry telemetry);

    /**
     * This will be run every time 'step' is called.
     * This serves as the "tick" function of the task.
     * @param telemetry
     * A 'Telemetry' instance to be used for debugging.
     * @return
     * Should return whether or not to run the task again.
     * It should return 'false' when the task is done.
     */
    protected abstract boolean run(Telemetry telemetry);
}
