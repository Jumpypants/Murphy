package com.jumpypants.murphy.states;

import com.jumpypants.murphy.RobotContext;

/**
 * Manages robot state execution and transitions.
 * Maintains the current active state and orchestrates state transitions.
 */
public class StateMachine {
    private State currentState;
    private final RobotContext robotContext;

    /**
     * Creates a state machine with the specified initial state.
     * @param initialState The state to begin execution with
     * @param robotContext Context for telemetry, gamepads, etc.
     */
    public StateMachine(State initialState, RobotContext robotContext) {
        if (initialState == null) {
            throw new IllegalArgumentException("Initial state cannot be null");
        }
        currentState = initialState;
        this.robotContext = robotContext;
    }

    /**
     * Executes the current state and handles state transitions.
     * Should be called once per iteration in the main control loop.
     */
    public void step() {
        if (currentState == null) {
            throw new IllegalStateException("Current state is null. StateMachine cannot operate without a valid state.");
        }
        robotContext.telemetry.addData("State", currentState.getName());
        State nextState = currentState.step(robotContext);
        if (nextState == null) {
            throw new IllegalStateException("State '" + currentState.getName() + "' returned null as the next state. States must return a valid State instance.");
        }
        currentState = nextState;
    }
}
