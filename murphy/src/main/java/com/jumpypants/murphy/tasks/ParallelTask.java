package com.jumpypants.murphy.tasks;

import com.jumpypants.murphy.util.RobotContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Executes multiple tasks concurrently.
 */
public class ParallelTask extends Task {
    private final Task[] TASKS;
    private final boolean stopOnFirstCompletion;
    private List<Task> activeTasks;

    /**
     * Creates a parallel task that executes multiple tasks simultaneously.
     * @param robotContext contains references like telemetry, gamepads, and subsystems
     * @param stopOnFirstCompletion Whether to stop execution when the first task completes,
     *                             or wait for all tasks to complete
     * @param tasks Tasks to execute in parallel
     */
    public ParallelTask(RobotContext robotContext, boolean stopOnFirstCompletion, Task... tasks) {
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
        this.stopOnFirstCompletion = stopOnFirstCompletion;
    }

    @Override
    protected void initialize(RobotContext robotContext) {
        // Initialize with all tasks active
        activeTasks = new ArrayList<>(Arrays.asList(TASKS));
    }

    @Override
    protected boolean run(RobotContext robotContext) {
        if (activeTasks.isEmpty()) {
            return false;
        }

        // Use iterator to safely remove completed tasks while iterating
        var iterator = activeTasks.iterator();
        while (iterator.hasNext()) {
            Task task = iterator.next();

            if (!task.step()) {
                // Task completed, remove from active list
                iterator.remove();

                // If we should stop on first completion, return false immediately
                if (stopOnFirstCompletion) {
                    return false;
                }
            }
        }

        // Continue running if any tasks are still active
        return !activeTasks.isEmpty();
    }
}
