package org.firstinspires.ftc.teamcode.Mechanisms;

import com.acmerobotics.dashboard.config.Config;

import dev.nextftc.hardware.actuators.NextRGBIndicator;
import dev.nextftc.hardware.actuators.NextServo;
import dev.nextftc.hardware.sensors.NextDistanceSensor;
import dev.nextftc.robot.Mechanism;

@Config
public class Sensor implements Mechanism {
    NextDistanceSensor rightSensor = new NextDistanceSensor("rs");
    NextDistanceSensor leftSensor = new NextDistanceSensor("ls");

    NextRGBIndicator indicatorLight = new NextRGBIndicator("il");

    public enum IndicatorLight{
        BLUE, RED, FULL, NOT
    }
    public static IndicatorLight mode = IndicatorLight.BLUE;

    public static double BLUE_COLOR = 0.62;//test all colors
    public static double RED_COLOR = 0.28;
    public static double FULL_COLOR = 0.51;
    public static double NOT_COLOR = 0.28;
    double val;

    public static double distance = 3.5;
    @Override
    public void periodic(){
        rightSensor.update();
        leftSensor.update();

       switch (mode){
           case NOT:
               indicatorLight.setColor(NextRGBIndicator.Color.WHITE);
               break;

           case RED:
               indicatorLight.setColor(NextRGBIndicator.Color.RED);
               break;

           case BLUE:
               indicatorLight.setColor(NextRGBIndicator.Color.BLUE);
               break;

           case FULL:
               indicatorLight.setColor(NextRGBIndicator.Color.GREEN);
               break;
       }


    }

    public boolean fullIntake(){
        if(rightSensor.isWithinDistance(distance) || leftSensor.isWithinDistance(distance)){
            return true;
        }
        else{
            return false;
        }
    }

    public void allianceIndicator(boolean areWeRed){
        if(areWeRed){
            indicatorLight.setColor(NextRGBIndicator.Color.RED);
        }
        else{
            indicatorLight.setColor(NextRGBIndicator.Color.BLUE);
        }
    }
}
