package org.firstinspires.ftc.teamcode.Mechanisms;

import com.acmerobotics.dashboard.config.Config;

import dev.nextftc.hardware.actuators.NextServo;
import dev.nextftc.robot.Mechanism;

@Config
public class BallBlock implements Mechanism {
    NextServo ballStop = new NextServo("bs");

    public enum BlockState {
        BALL_NO,
        BALL_GO,
        INIT
    }

    public static BlockState mode = BlockState.INIT;

    public static double OPEN_POSITION = 0.5;

    public static double CLOSE_POSITION = 0.8;

    public static double POSITION;

    @Override
    public void periodic(){
        switch (mode) {
            case BALL_NO:
                POSITION = CLOSE_POSITION;
                break;

            case BALL_GO:
               POSITION = OPEN_POSITION;
                break;

            case INIT:
                break;
        }
        if(ballStop.getPosition() == POSITION) {

        }else{
            ballStop.setPosition(POSITION);
        }
    }


}
