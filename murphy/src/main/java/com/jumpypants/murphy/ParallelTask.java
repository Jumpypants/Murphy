package com.jumpypants.murphy;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * An action that runs multiple actions in parallel.
 */
public class ParallelTask extends Task {
    private final Task[] actions;

    private final boolean stopOnFirstCompletion;

    /**
     * @param stopOnFirstCompletion
     * Defines weather or not to stop when one of the actions is completed.
     * The alternative is waiting until all actions are completed to stop.
     * @param actions
     * The actions to run.
     */
    public ParallelTask(boolean stopOnFirstCompletion, Task... actions) {
        this.actions = actions;
        this.stopOnFirstCompletion = stopOnFirstCompletion;
    }

    @Override
    protected void initialize(Telemetry telemetry) {}

    @Override
    protected boolean run(Telemetry telemetry) {
        boolean runAgain = false;

        for (Task action : actions) {
            if (action.step(telemetry)) {
                runAgain = true;
                if (stopOnFirstCompletion) {
                    break;
                }
            }
        }

        return runAgain;
    }
}
