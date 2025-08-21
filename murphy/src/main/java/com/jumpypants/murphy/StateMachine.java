package com.jumpypants.murphy;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Manages robot state execution and transitions.
 * Maintains the current active state and orchestrates state transitions.
 */
public class StateMachine {
    private State currentState;

    /**
     * Creates a state machine with the specified initial state.
     * @param initialState The state to begin execution with
     */
    public StateMachine(State initialState) {
        if (initialState == null) {
            throw new IllegalArgumentException("Initial state cannot be null");
        }
        currentState = initialState;
    }

    /**
     * Executes the current state and handles state transitions.
     * Should be called once per iteration in the main control loop.
     * @param telemetry Telemetry instance for debugging output
     */
    public void step(Telemetry telemetry) {
        if (currentState == null) {
            throw new IllegalStateException("Current state is null. StateMachine cannot operate without a valid state.");
        }
        telemetry.addData("State", currentState.getName());
        State nextState = currentState.step(telemetry);
        if (nextState == null) {
            throw new IllegalStateException("State '" + currentState.getName() + "' returned null as the next state. States must return a valid State instance.");
        }
        currentState = nextState;
    }
}
