package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.hardware.DcMotor;

public class LinearSlideClass {
    DcMotor motor;
    //Defining position values
    int minPos;
    int maxPos;
    int curPos;
    //Constrction function for te linear slide
    public LinearSlideClass(DcMotor motor, int maxPos){
        this.motor = motor;
        this.maxPos = maxPos;
        minPos = 0;
    }
}
