package com.jumpypants.murphy.tasks;

import com.jumpypants.murphy.RobotContext;

/**
 * Executes multiple tasks in sequential order.
 */
public class SequentialTask extends Task {
    private final Task[] actions;

    private int currentActionIndex = 0;

    /**
     * Creates a sequential task that executes tasks in the specified order.
     * @param robotContext contains references like telemetry, gamepads, and subsystems
     * @param actions Tasks to execute sequentially
     */
    public SequentialTask(RobotContext robotContext, Task... actions) {
        super(robotContext);
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
    }

    @Override
    protected void initialize(RobotContext robotContext) {}

    @Override
    protected boolean run(RobotContext robotContext) {
        // If we've completed all actions, return false to indicate completion
        if (currentActionIndex >= actions.length) {
            return false;
        }

        // Run the current action
        if (!actions[currentActionIndex].step()) {
            // Current action completed, move to next
            currentActionIndex++;
        }

        // Continue running if we haven't completed all actions yet
        return currentActionIndex < actions.length;
    }
}
