package org.firstinspires.ftc.teamcode.Mechanisms;

import com.acmerobotics.dashboard.config.Config;

import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.robot.Mechanism;

@Config
public class Intake implements Mechanism {


    NextMotor intakeMotor1 = new NextMotor("im1");

    public static double intakeThrottle = 1;

    public static double val;

    public enum IntakeMode{
        ON, OFF, INIT
    }

    public static IntakeMode mode = IntakeMode.INIT;


    @Override
    public void periodic(){
        switch (mode) {
            case OFF:
                val = 0;
                break;

            case ON:
                val = intakeThrottle;
                break;

            case INIT:
                break;
        }

        intakeMotor1.setDirection(NextMotor.Direction.REVERSE);

        intakeMotor1.setThrottle(val);

    }

}
