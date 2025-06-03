package com.jumpypants.murphy;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * This class can be used to separate different parts of the robot's state.
 * An action can represent a mechanical movement like moving a slide to a certain position.
 * It can also represent a software process like waiting until a sensor reads a certain value.
 * It is recommended to extend 'MurphyAction' within a class that represents a subsystem of the robot.
 * Multiple actions can be stringed together to perform complicated tasks.
 */
public abstract class Task {
    /**
     * How long it has been since the action was initialized.
     */
    protected final ElapsedTime ELAPSED_TIME = new ElapsedTime();
    protected boolean initialized = false;

    /**
     * Call this to run the action. It will call the 'run' method.
     * The 'initialize' method will be called if this is the first time calling 'step'.
     * @param telemetry
     * A 'Telemetry' instance to be used for debugging.
     * @return
     * Returns weather the action wants to be run another time (will return false if the action is finished).
     */
    public final boolean step(Telemetry telemetry) {
        if (!initialized) {
            ELAPSED_TIME.reset();
            initialize(telemetry);
            initialized = true;
        }
        return run(telemetry);
    }

    /**
     * This method will be called upon the first call of 'step' (when the action is first ran).
     * @param telemetry
     * A 'Telemetry' instance to be used for debugging.
     */
    protected abstract void initialize(Telemetry telemetry);

    /**
     * This will be ran every time 'step' is called.
     * This serves as the "tick" function of the action.
     * @param telemetry
     * A 'Telemetry' instance to be used for debugging.
     * @return
     * Should return weather or not to run the action again.
     * It should return 'false' when the action is done.
     */
    protected abstract boolean run(Telemetry telemetry);
}
