package com.jumpypants.murphy.tasks;

import com.jumpypants.murphy.util.RobotContext;

import java.util.ArrayList;

/**
 * Executes tasks in queue order (FIFO - First In, First Out).
 * Tasks can be dynamically added to the queue during execution.
 * You should probably place QueueTasks inside of a parallel task with another task that determines when to stop.
 */
public class QueueTask extends Task {
    private final ArrayList<Task> TASKS;

    /**
     * Creates an empty queue task.
     * @param robotContext contains references like telemetry, gamepads, and subsystems
     */
    public QueueTask(RobotContext robotContext) {
        super(robotContext);
        TASKS = new ArrayList<>();
    }

    /**
     * Creates a queue task with initial tasks.
     * @param robotContext contains references like telemetry, gamepads, and subsystems
     * @param initialTasks Initial tasks to add to the queue
     */
    public QueueTask(RobotContext robotContext, ArrayList<Task> initialTasks) {
        super(robotContext);
        if (initialTasks == null) {
            throw new IllegalArgumentException("Initial tasks list cannot be null");
        }
        for (int i = 0; i < initialTasks.size(); i++) {
            if (initialTasks.get(i) == null) {
                throw new IllegalArgumentException("Task at index " + i + " cannot be null");
            }
        }
        TASKS = new ArrayList<>(initialTasks);
    }

    /**
     * Adds a task to the end of the queue.
     * @param task Task to add to the queue
     */
    public void addTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }
        TASKS.add(task);
    }

    /**
     * Returns the number of tasks remaining in the queue.
     * @return Number of tasks in the queue
     */
    public int size() {
        return TASKS.size();
    }

    /**
     * Checks if the queue is empty.
     * @return true if the queue has no tasks, false otherwise
     */
    public boolean isEmpty() {
        return TASKS.isEmpty();
    }

    @Override
    protected void initialize(RobotContext robotContext) {}

    @Override
    protected boolean run(RobotContext robotContext) {
        // If queue is empty, task is complete
        if (TASKS.isEmpty()) {
            return false;
        }

        // Execute the first task in the queue
        if (!TASKS.get(0).step()) {
            // Current task completed, remove from queue
            TASKS.remove(0);
        }

        // Continue running if there are still tasks in the queue
        return !TASKS.isEmpty();
    }
}