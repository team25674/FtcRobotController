package org.firstinspires.ftc.teamcode.tutorials;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "Encoder RUN_TO_POSITION Test 1", group = "Tutorials")
public class TutorialDriveToEncoderPosition extends OpMode {

    // Constants
    static final double DRIVE_SPEED = 0.6;
    static final double COUNTS_PER_INCH = 1000; // TODO: Measure this!!

    // Preset Positions
    double positionZero = 0;
    double position1Inches = 8;
    double position2Inches = 18;

    // Motor
    private DcMotor motor = null;

    // Button states for edge detection
    boolean lastButtonStart = false;
    boolean lastButtonA = false;
    boolean lastButtonB = false;
    boolean lastButtonX = false;
    private final ElapsedTime runtime = new ElapsedTime();

    // Tracking states
    boolean firstLoop = true;
    boolean motorActive = false;
    boolean motorEnabled = false;

    // Telemetry Tags
    private static final String TAG_MOTOR_ENABLED = "Enabled";    // Is Motor Enabled
    private static final String TAG_MOTOR_ACTIVE = "Active";      // Is Motor Active
    private static final String TAG_MOTOR_BUSY = "Motor Busy";    // Is Motor Reporting Busy status
    private static final String TAG_MOTOR_POWER = "Motor Power";  // Current motor power
    private static final String TAG_MOTOR_POS = "Motor Pos.";     // Motor position in inches todo
    private static final String TAG_ENCODER_POS = "Encoder Pos."; // Motor encoder raw position
    private static final String TAG_RUNTIME = "Run Time";         // Motor encoder raw position

    @Override
    public void init() {
        // Initialize the drive system variables.
        motor = hardwareMap.get(DcMotor.class, "motor");
        motor.setDirection(DcMotor.Direction.FORWARD);

        // TODO: Find zero ref point? Or is manual stow at zero good enough?
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // reset states
        motorEnabled = false;
        motorActive = false;

        // Prepare motor run mode
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    @Override
    public void loop() {
        // Do on first loop only
        if (firstLoop){
            firstLoop = false;
            runtime.reset();
        }

        // Process button presses
        handleButtons();

        // Handle if motor is active
        if (motorActive) {
            // Reset when motor is no longer busy
            if (!motor.isBusy()) {
                reset();
            }
        }

        // Update telemetry
        updateTelemetry();
    }

    private void handleButtons() {
        // START button
        if (lastButtonStart && !gamepad1.start) {
            // enable or disable the motor
            motorEnabled = !motorEnabled; // toggle motor enablement
            if (motorEnabled) {
                enableMotor();
            } else {
                disableMotor();
            }
        }

        // A button
        if (lastButtonA && !gamepad1.a) {
            // A --> Go to position 1
            start(DRIVE_SPEED, position1Inches, 10);
        }

        // B button
        if (lastButtonB && !gamepad1.b) {
            // B --> Go to position 2
            start(DRIVE_SPEED, position2Inches, 15);
        }

        // X button
        if (lastButtonX && !gamepad1.x) {
            // B --> Go to position zero
            start(DRIVE_SPEED, positionZero, 15);
        }

        // save current button states for next loop
        lastButtonStart = gamepad1.start;
        lastButtonA = gamepad1.a;
        lastButtonB = gamepad1.b;
    }


    private void start(double speed, double leftInches, double timeoutS) {
        // Ensure motor is enabled
        if (!motorEnabled){
            telemetry.log().add("Cannot start, motor not enabled!");
            return;
        }

        // Ensure motor is not already active
        if (motorActive){
            telemetry.log().add("Cannot start, motor already active!");
            return;
        }

        // Determine new target position, and pass to motor controller
        int newTarget = motor.getCurrentPosition() + (int) (leftInches * COUNTS_PER_INCH);
        motor.setTargetPosition(newTarget);

        // Turn On RUN_TO_POSITION
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // reset the timeout time and start motion.
        runtime.reset();
        motor.setPower(Math.abs(speed));

        // Update tracking state
        motorActive = true;
    }

    private void reset() {
        // Stop motor
        motor.setPower(0);

        // Turn off RUN_TO_POSITION
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Reset state
        motorActive = false;
    }

    private void enableMotor() {
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    private void disableMotor() {
        motor.setPower(0);
        motorActive = false;

        // Allow motor to be moved by hand
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    private void updateTelemetry() {
        // Send telemetry message to indicate successful Encoder reset
        telemetry.addData(TAG_MOTOR_ENABLED, "%B", motorEnabled);
        telemetry.addData(TAG_MOTOR_ACTIVE, "%B", motorActive);
        telemetry.addData(TAG_MOTOR_BUSY, "%B", motor.isBusy());
        telemetry.addData(TAG_MOTOR_POWER, "%.5f", motor.getPower());
        telemetry.addData(TAG_MOTOR_POS, "%d", motor.getCurrentPosition());
        telemetry.addData(TAG_ENCODER_POS, "%d", motor.getCurrentPosition());
        telemetry.addData(TAG_RUNTIME, "%d", runtime.seconds());
        telemetry.update();
    }
}
