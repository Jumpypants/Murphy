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
    private final int MAX_QUEUE_SIZE;

    /**
     * Creates an empty queue task.
     * @param robotContext contains references like telemetry, gamepads, and subsystems
     */
    public QueueTask(RobotContext robotContext) {
        this(robotContext, -1);
    }


    /**
     * Creates an empty queue task with an optional maximum queue size.
     * @param robotContext contains references like telemetry, gamepads, and subsystems
     * @param stopOnEmpty if true, task stops when queue is empty; if false, task continues running
     * @param maxQueueSize maximum number of tasks allowed in the queue
     */
    public QueueTask(RobotContext robotContext, int maxQueueSize) {
        super(robotContext);
        TASKS = new ArrayList<>();
        this.MAX_QUEUE_SIZE = maxQueueSize;
    }


    /**
     * Adds a task to the end of the queue.
     * @param task Task to add to the queue
     * @return true if task was added successfully, false if queue is full or task is null
     */
    public boolean addTask(Task task) {
        if (task == null) {
            return false;
        }
        if (MAX_QUEUE_SIZE > 0 && size() >= MAX_QUEUE_SIZE) {
            return false;
        }
        TASKS.add(task);
        return true;
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

    /**
     * Clears all tasks from the queue.
     */
    public void clear() {
        TASKS.clear();
    }

    @Override
    protected void initialize(RobotContext robotContext) {}

    @Override
    protected boolean run(RobotContext robotContext) {
        // If queue is empty
        if (TASKS.isEmpty()) {
            // Stop running
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