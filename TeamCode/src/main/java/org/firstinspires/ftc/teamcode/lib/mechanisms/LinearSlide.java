package org.firstinspires.ftc.teamcode.lib.mechanisms;

import com.qualcomm.robotcore.hardware.DcMotor;

public class LinearSlide {
    static final double COUNTS_PER_INCH = 112.8181;
    public static int POS_LOWER_BASKET_INCHES = 30;
    public static int POS_UPPER_BASKET_INCHES = 45;
    public DcMotor motor;
    int max;

    public LinearSlide(DcMotor motor, int max) {
        this.motor = motor;
        this.max = max;
        motor.setDirection(DcMotor.Direction.FORWARD);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void goToPosition(int positionInches) {
        // Determine new target position, and pass to motor controller
        int newTarget = (int) (positionInches * COUNTS_PER_INCH);
        motor.setTargetPosition(newTarget);

        // Turn On RUN_TO_POSITION
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Start motion
        motor.setPower(1);
    }

    public void goToZero() {
        goToPosition(0);
    }

    public void extend(double speed) {
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor.setPower(speed);
    }
}
