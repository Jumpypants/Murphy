package com.jumpypants.murphy.util;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Contains shared context and resources used throughout the robot control system.
 * This class provides a centralized way to pass common resources like telemetry and gamepads
 * to States, Tasks, and other components without requiring multiple parameters.
 *
 * <p>This class can be extended to include additional shared resources as needed.
 */
public abstract class RobotContext {
    /**
     * Telemetry instance for sending debug and status information to the driver station.
     * Used by States and Tasks to display real-time robot status, sensor values,
     * and operational feedback.
     */
    public final Telemetry telemetry;

    /**
     * Primary gamepad controller (typically the driver controller).
     * Provides access to joystick inputs, button states, and trigger values
     * for robot movement and primary control functions.
     */
    public final Gamepad gamepad1;

    /**
     * Secondary gamepad controller (typically the operator controller).
     * Used for auxiliary controls such as subsystem operations, preset positions,
     * and advanced robot functions that don't interfere with primary driving.
     */
    public final Gamepad gamepad2;

    /**
     * Creates a new RobotContext with the specified telemetry and gamepad references.
     * All parameters are required and cannot be null.
     *
     * @param telemetry the telemetry instance for driver station communication
     * @param gamepad1  the primary gamepad controller
     * @param gamepad2  the secondary gamepad controller
     * @throws IllegalArgumentException if any parameter is null
     */
    public RobotContext(Telemetry telemetry, Gamepad gamepad1, Gamepad gamepad2) {
        if (telemetry == null) {
            throw new IllegalArgumentException("Telemetry cannot be null");
        }
        if (gamepad1 == null) {
            throw new IllegalArgumentException("Gamepad1 cannot be null");
        }
        if (gamepad2 == null) {
            throw new IllegalArgumentException("Gamepad2 cannot be null");
        }

        this.telemetry = telemetry;
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
    }
}