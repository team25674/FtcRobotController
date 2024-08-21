package org.firstinspires.ftc.teamcode.tutorials;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name="SkidSteer_DriveMode")
public class  TutorialSkidSteerWithMode extends OpMode {

    DcMotor left_motor;
    DcMotor right_motor;

    // drive mode
    boolean slow_mode_enabled; // option 1: use boolean for 2 states
    DriveMode drive_mode; // option 2: use an enum to store many states
    boolean previous_a;

    @Override
    public void init() {
        // get motors
        left_motor = hardwareMap.get(DcMotor.class, "left_motor");
        right_motor = hardwareMap.get(DcMotor.class, "right_motor");

        // initialize motor direction
        left_motor.setDirection(DcMotorSimple.Direction.FORWARD);
        right_motor.setDirection(DcMotorSimple.Direction.REVERSE);

        // initialize states
        slow_mode_enabled = false;
        drive_mode = DriveMode.NORMAL;
    }

    @Override
    public void loop() {
        // read inputs

        if (previous_a && !gamepad1.a){
            if (drive_mode == DriveMode.SLOW) {
                drive_mode = DriveMode.NORMAL;
            }else {
                drive_mode = DriveMode.SLOW;
            }
        }


        double left_power_input = gamepad1.left_stick_y;
        double right_power_input = gamepad1.right_stick_y;
        double left_power = left_power_input;
        double right_power = right_power_input;

        if (drive_mode == DriveMode.SLOW){
            left_power = left_power/2;
            right_power = right_power/2;

        }

        // update motors
        left_motor.setPower(left_power);
        right_motor.setPower(right_power);




        // update telemetry
        telemetry.addData("left_power", left_power);
        telemetry.addData("right_power", right_power);
        telemetry.addData("mode",drive_mode);
        telemetry.update();

        previous_a = gamepad1.a;


    }
}

// Normally, this would go in its own file as a "public enum"
enum DriveMode {
    NORMAL,
    SLOW,
}
