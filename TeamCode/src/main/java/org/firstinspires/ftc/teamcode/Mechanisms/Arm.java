package org.firstinspires.ftc.teamcode.Mechanisms;

import com.acmerobotics.dashboard.config.Config;

import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.hardware.actuators.NextServo;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.robot.Telemetry;

@Config
public class Arm implements Mechanism {

    NextServo flowerArm = new NextServo("pa");

    public static double DOWN_POSITION = 0.225;

    //public static double DOWN_POSITION = 0.25;

    public enum FlowerPiviotState{
        DOWN,UP,INIT
    }

    double val; //this should be local

    public static FlowerPiviotState mode = FlowerPiviotState.INIT;

    public static double servoPosition;
    @Override
    public void periodic(){


        switch (mode){
            case INIT:
                break;

            case DOWN:
                val = DOWN_POSITION;
                break;

            case UP:
                val = 0;
                break;
        }
        Telemetry.log("Down Position",DOWN_POSITION);
        Telemetry.log("val", val);


        flowerArm.setDirection(NextMotor.Direction.REVERSE);
        flowerArm.setPosition(val);

        servoPosition = flowerArm.getPosition();

    }
}
