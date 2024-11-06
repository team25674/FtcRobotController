package org.firstinspires.ftc.teamcode.production;

import com.qualcomm.robotcore.hardware.Servo;

public class Spy {
    private final double UP_POSITION = .7141592;
    private final double DOWN_POSTION = .61415672;
    private final double REJECT_POSITION = .45678;
    private final double INTAKE_POSITION = .1;

    private Servo wheel1;
    private Servo wheel2;
    private Servo upAndDown;

    public Spy (Servo wheel1, Servo wheel2, Servo upAndDown){
        this.wheel1 = wheel1;
        this.wheel2 = wheel2;
        this.upAndDown = upAndDown;
    }
    public void intake() {
        wheel1.setPosition(INTAKE_POSITION);
        wheel2.setPosition(INTAKE_POSITION);

    }
    public void reject() {
        wheel1.setPosition(REJECT_POSITION);
        wheel2.setPosition(REJECT_POSITION);

    }
    public void up() {
        upAndDown.setPosition(UP_POSITION);

    }
    public void down() {
        upAndDown.setPosition(DOWN_POSTION);

    }
}
