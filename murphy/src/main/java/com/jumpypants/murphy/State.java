package com.jumpypants.murphy;

/**
 * This is used to represent a state that the robot can be in.
 * For example a robot can be in an 'idle' state or in an 'intaking' state.
 * The robot should have a few of these states, and always be on one of these states.
 * This should be done using a 'com.fremontrobotics.murphy.MurphyStateMachine'.
 * The state can use 'MurphyAction's to compartmentalize tasks.
 * <br> <br>
 * The difference between states and actions is that actions are always ran in a given order,
 * linearly or in parallel, but states are more general, and the robot can flip between
 * states in an order that responds to sensor and driver input.
 */
public interface State {
    /**
     * This is called every frame by the 'com.fremontrobotics.murphy.MurphyStateMachine' to run the state.
     * This is where all the logic for the state should be.
     * @return
     * Returns the next state to be ran. Unless this state only requires one frame to run,
     * it should return itself several times before returning an instance of another state.
     */
    State step();

    /**
     * @return
     * A string that is the name of the state.
     * This is used by the 'com.fremontrobotics.murphy.MurphyStateMachine' to print the current state name.
     * This should probably just return a hardcoded string.
     */
    String getName();
}
