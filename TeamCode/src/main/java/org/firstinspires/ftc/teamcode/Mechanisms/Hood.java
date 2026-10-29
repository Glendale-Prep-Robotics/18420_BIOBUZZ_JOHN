package org.firstinspires.ftc.teamcode.Mechanisms;

import com.acmerobotics.dashboard.config.Config;

import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.hardware.actuators.NextServo;
import dev.nextftc.robot.Mechanism;

@Config
public class Hood implements Mechanism {
    NextServo hoodServo1 = new NextServo("hs1");
    //NextServo hoodServo2 = new NextServo("hs2");

    public static double openHoodPosition = 0.225;
    public static double closeHoodPosition = 0;
    public enum HoodState {
        SHOOT, CLOSE, INIT
    }
    public static HoodState mode = HoodState.INIT;

    public static double val;
    @Override
    public void periodic(){
        switch (mode){
            case SHOOT:
                val = openHoodPosition;
                break;

            case CLOSE:
                val = closeHoodPosition;
                break;

            case INIT:
                break;
        }

        hoodServo1.setDirection(NextMotor.Direction.REVERSE);//test
        //hoodServo2.setDirection(NextMotor.Direction.REVERSE);//test


        hoodServo1.setPosition(val);
        //hoodServo2.setPosition(val);


    }
}
