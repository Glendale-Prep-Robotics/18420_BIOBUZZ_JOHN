package org.firstinspires.ftc.teamcode.Mechanisms;

import dev.nextftc.hardware.actuators.NextServo;
import dev.nextftc.hardware.sensors.NextDistanceSensor;
import dev.nextftc.robot.Mechanism;

public class Sensor implements Mechanism {
    NextDistanceSensor rightSensor = new NextDistanceSensor("rs");
    NextDistanceSensor leftSensor = new NextDistanceSensor("ls");

    NextServo indicatorLight = new NextServo("il");

    public enum IndicatorLight{
        BLUE, RED, FULL, NOT
    }
    public static IndicatorLight mode = IndicatorLight.BLUE;

    public static double BLUE_COLOR = 0.3;//test all colors
    public static double RED_COLOR = 0.5;
    public static double FULL_COLOR = 0.56;
    public static double NOT_COLOR = 0.75;
    double val;
    @Override
    public void periodic(){
        rightSensor.update();
        leftSensor.update();

        switch (mode){
            case RED:
                val = RED_COLOR;
                break;

            case NOT:
                val = NOT_COLOR;
                break;

            case BLUE:
                val = BLUE_COLOR;
                break;

            case FULL:
                val = FULL_COLOR;
                break;
        }

        indicatorLight.setPosition(val);


    }
}
