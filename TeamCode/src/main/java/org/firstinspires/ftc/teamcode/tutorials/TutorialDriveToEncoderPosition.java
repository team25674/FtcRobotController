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
    double position1Inches = 8;
    double position2Inches = 18;

    // Motor
    private DcMotor motor = null;

    // Button states for edge detection
    boolean previousStartButtonState = false;
    private final ElapsedTime runtime = new ElapsedTime();

    // Tracking states
    boolean firstLoop = true;
    boolean motorActive = false;
    boolean motorEnabled = false;

    // Telemetry Tags
    private static final String TAG_MOTOR_ENABLED = "Enabled";    // Is Motor Enabled
    private static final String TAG_MOTOR_ACTIVE = "Active";      // Is Motor Active
    private static final String TAG_MOTOR_BUSY = "Motor Busy";        // Is Motor Reporting Busy status
    private static final String TAG_MOTOR_POWER = "Motor Power";  // Current motor power
    private static final String TAG_MOTOR_POS = "Motor Pos.";     // Motor position in inches todo
    private static final String TAG_ENCODER_POS = "Encoder Pos."; // Motor encoder raw position
    private static final String TAG_RUNTIME = "Run Time"; // Motor encoder raw position

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
        // Do anything exactly once (start timer)
        if (firstLoop){
            firstLoop = false;
            runtime.reset();
        }

        // Allow handleMotorEnabled() to run
        handleMotorEnabled();

        // Run motor if enabled
        if (motorEnabled) {

            // Wait for button press / command
            if (!motorActive && gamepad1.a) {
                // A --> Go to position 1
                start(DRIVE_SPEED, position1Inches, 10);
            }
            if (!motorActive && gamepad1.b) {
                // B --> Go to position 2
                start(DRIVE_SPEED, position2Inches, 15);
            }

            // Check on any active routine
            if (motorActive) {
                // Reset when motor is no longer busy
                if (!motor.isBusy()) {
                    reset();
                }
            }
        }

        // Update telemetry
        updateTelemetry();
    }

    private void start(double speed, double leftInches, double timeoutS) {
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

    private void handleMotorEnabled() {
        // wait to let go of start button
        if (previousStartButtonState && !gamepad1.start) {
            // Button was pressed!!
            motorEnabled = !motorEnabled; // toggle motor enablement

            // Perform action
            if (motorEnabled) {
                enableMotor();
            } else {
                disableMotor();
            }
        }

        // save current button state for next loop
        previousStartButtonState = gamepad1.start;
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
