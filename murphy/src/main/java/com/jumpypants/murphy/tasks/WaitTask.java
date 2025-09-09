package com.jumpypants.murphy.tasks;

import com.jumpypants.murphy.RobotContext;

public class WaitTask extends Task{

    private final double seconds;

    /**
     * Creates a new Task with the provided RobotContext.
     *
     * @param robotContext contains references like telemetry, gamepads, and subsystems
     * @param seconds      Duration to wait in seconds
     */
    public WaitTask(RobotContext robotContext, double seconds) {
        super(robotContext);
        this.seconds = seconds;
    }

    @Override
    protected void initialize(RobotContext robotContext) {}

    @Override
    protected boolean run(RobotContext robotContext) {
        return ELAPSED_TIME.seconds() < seconds;
    }
}
