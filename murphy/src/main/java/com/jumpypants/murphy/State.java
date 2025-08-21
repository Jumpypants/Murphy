package com.jumpypants.murphy;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * This is used to represent a state that the robot can be in.
 * For example a robot can be in an 'idle' state or in an 'intaking' state.
 * The robot should have a few of these states, and always be on one of these states.
 * This should be done using a 'StateMachine'.
 * The state can use 'Task's to compartmentalize tasks.
 * <br> <br>
 * The difference between states and tasks is that tasks are always run in a given order,
 * linearly or in parallel, but states are more general, and the robot can flip between
 * states in an order that responds to sensor and driver input.
 */
public interface State {
    /**
     * This is called every frame by the 'StateMachine' to run the state.
     * This is where all the logic for the state should be.
     * @param telemetry
     * A 'Telemetry' instance to be used for debugging.
     * @return
     * Returns the next state to be run. Unless this state only requires one frame to run,
     * it should return itself several times before returning an instance of another state.
     */
    State step(Telemetry telemetry);

    /**
     * @return
     * A string that is the name of the state.
     * This is used by the 'StateMachine' to print the current state name.
     * This should probably just return a hardcoded string.
     */
    String getName();
}
