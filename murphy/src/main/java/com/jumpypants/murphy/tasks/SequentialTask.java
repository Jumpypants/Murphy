package com.jumpypants.murphy.tasks;

import com.jumpypants.murphy.util.RobotContext;

/**
 * Executes multiple tasks in sequential order.
 */
public class SequentialTask extends Task {
    private final Task[] TASKS;

    private int currentTaskIndex = 0;

    /**
     * Creates a sequential task that executes tasks in the specified order.
     * @param robotContext contains references like telemetry, gamepads, and subsystems
     * @param tasks Tasks to execute sequentially
     */
    public SequentialTask(RobotContext robotContext, Task... tasks) {
        super(robotContext);
        if (tasks == null) {
            throw new IllegalArgumentException("Tasks array cannot be null");
        }
        if (tasks.length == 0) {
            throw new IllegalArgumentException("Tasks array cannot be empty");
        }
        for (int i = 0; i < tasks.length; i++) {
            if (tasks[i] == null) {
                throw new IllegalArgumentException("Task at index " + i + " cannot be null");
            }
        }
        this.TASKS = tasks;
    }

    @Override
    protected void initialize(RobotContext robotContext) {}

    @Override
    protected boolean run(RobotContext robotContext) {
        // If we've completed all tasks, return false to indicate completion
        if (currentTaskIndex >= TASKS.length) {
            return false;
        }

        // Run the current task
        if (!TASKS[currentTaskIndex].step()) {
            // Current task completed, move to next
            currentTaskIndex++;
        }

        // Continue running if we haven't completed all tasks yet
        return currentTaskIndex < TASKS.length;
    }
}
