package org.firstinspires.ftc.teamcode.Mechanisms;

import com.acmerobotics.dashboard.config.Config;

import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.robot.Mechanism;

@Config
public class Feeder implements Mechanism {

    NextMotor FeederMotor = new NextMotor("fm");

    public enum FeederMode{
        ON,OFF,INIT, INTAKE, REVERSE

    }

    public static double FEEDER_ON_SHOOT = 0.7;


    public static double FEEDER_ON_INTAKE = 0.3;

    public static FeederMode mode = FeederMode.INIT;

    public double val2;

    @Override
    public void periodic(){
        switch (mode){
            case INIT:
                break;

            case ON:
                val2 = FEEDER_ON_SHOOT;
                break;

            case OFF:
                val2 = 0;
                break;

            case INTAKE:
                val2 = FEEDER_ON_INTAKE;
                break;

            case REVERSE:
                val2 = -FEEDER_ON_INTAKE;
        }


        FeederMotor.setDirection(NextMotor.Direction.FORWARD);
        FeederMotor.setThrottle(val2);
    }
}
