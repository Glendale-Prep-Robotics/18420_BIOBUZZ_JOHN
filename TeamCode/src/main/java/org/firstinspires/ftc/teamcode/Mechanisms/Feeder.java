package org.firstinspires.ftc.teamcode.Mechanisms;

import com.acmerobotics.dashboard.config.Config;

import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.robot.Mechanism;

@Config
public class Feeder implements Mechanism {

    NextMotor FeederMotor = new NextMotor("fm");

    public enum FeederMode{
        ON,OFF,INIT

    }

    public static double FEEDER_ON = 1;

    public static FeederMode mode = FeederMode.INIT;

    public static double val2;

    @Override
    public void periodic(){
        switch (mode){
            case INIT:
                break;

            case ON:
                val2 = FEEDER_ON;
                break;

            case OFF:
                val2 = 0;
                break;
        }


        FeederMotor.setDirection(NextMotor.Direction.FORWARD);
        FeederMotor.setThrottle(val2);
    }
}
