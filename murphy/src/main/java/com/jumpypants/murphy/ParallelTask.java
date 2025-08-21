package com.jumpypants.murphy;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Executes multiple tasks concurrently.
 */
public class ParallelTask extends Task {
    private final Task[] actions;

    private final boolean stopOnFirstCompletion;

    /**
     * Creates a parallel task that executes multiple tasks simultaneously.
     * @param stopOnFirstCompletion Whether to stop execution when the first task completes,
     *                             or wait for all tasks to complete
     * @param actions Tasks to execute in parallel
     */
    public ParallelTask(boolean stopOnFirstCompletion, Task... actions) {
        if (actions == null) {
            throw new IllegalArgumentException("Actions array cannot be null");
        }
        if (actions.length == 0) {
            throw new IllegalArgumentException("Actions array cannot be empty");
        }
        for (int i = 0; i < actions.length; i++) {
            if (actions[i] == null) {
                throw new IllegalArgumentException("Action at index " + i + " cannot be null");
            }
        }
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
