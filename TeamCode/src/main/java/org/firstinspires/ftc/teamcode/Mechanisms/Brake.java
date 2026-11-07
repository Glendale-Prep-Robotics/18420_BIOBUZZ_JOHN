package org.firstinspires.ftc.teamcode.Mechanisms;

import com.acmerobotics.dashboard.config.Config;

import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.hardware.actuators.NextServo;
import dev.nextftc.robot.Mechanism;
@Config
public class Brake implements Mechanism {
    NextServo brakeServo1 = new NextServo("bs1");
    NextServo brakeServo2 = new NextServo("bs2");

    public enum BrakeState {
        BRAKE, NO_BREAK, INIT
    }

    public static BrakeState mode = BrakeState.INIT;

    public static double NO_BREAK_POSITION = 0;
    public static double BRAKE_POSITION = 0.225;

    private static double val;

    @Override
    public void periodic(){
        switch (mode){
            case INIT:
                break;

            case BRAKE:
                val = BRAKE_POSITION;
                break;

            case NO_BREAK:
                val = NO_BREAK_POSITION;
                break;
        }

        //test Directions
        brakeServo1.setDirection(NextMotor.Direction.FORWARD);
        brakeServo2.setDirection(NextMotor.Direction.REVERSE);

        //position stuff
        brakeServo1.setPosition(val);
        brakeServo2.setPosition(val);

        //register with nextRobot when reday to use
    }
}
