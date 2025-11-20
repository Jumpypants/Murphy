package com.jumpypants.murphy.states;

/**
 * Represents a discrete robot state with associated behavior and transitions.
 * States define robot behavior modes such as idle, intake, or scoring operations.
 * The robot maintains exactly one active state at any time, managed by a StateMachine.
 * States can utilize Tasks to modularize complex operations.
 * <br> <br>
 * States differ from Tasks in that Tasks execute in predetermined sequences,
 * while States allow dynamic transitions based on sensor input and driver commands.
 */
public interface State {
    /**
     * Executes the state logic and determines the next state transition.
     * Called by the StateMachine on each iteration.
     * @return The next state to execute (typically returns itself until transition conditions are met)
     */
    State step();

    /**
     * Returns the display name of this state.
     * Used by the StateMachine for telemetry output.
     * @return Human-readable state name
     */
    String getName();
}
