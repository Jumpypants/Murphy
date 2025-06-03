package com.jumpypants.murphy;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * This class is used to manage the states of the robot, and to run them.
 * The opMode should probably have exactly one instance of this class.
 */
public class StateMachine {
    private State currentState;

    /**
     * @param initialState
     * A state instance to be the initial state of the robot.
     * The next state will be the one returned by the initial state, and so on.
     */
    public StateMachine(State initialState) {
        currentState = initialState;
    }

    /**
     * This method should probably be called by the opMode on every iteration.
     * It will call the 'step' method of the current state, and then
     * set the current state to the return value.
     * @param telemetry
     * A 'Telemetry' instance to be used for debugging.
     */
    public void step(Telemetry telemetry) {
        telemetry.addData("State", currentState.getName());
        currentState = currentState.step();
    }
}
