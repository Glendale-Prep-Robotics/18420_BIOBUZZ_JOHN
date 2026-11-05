package org.firstinspires.ftc.teamcode.Mechanisms;

import static org.firstinspires.ftc.teamcode.Variables.GoalPose.blueAudienceGoal;
import static org.firstinspires.ftc.teamcode.Variables.GoalPose.blueRefereeGoal;
import static org.firstinspires.ftc.teamcode.Variables.GoalPose.redAudienceGoal;
import static org.firstinspires.ftc.teamcode.Variables.GoalPose.redRefereeGoal;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.math.Pose;

import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.hardware.actuators.NextServo;
import dev.nextftc.robot.Mechanism;

@Config
public class Hood implements Mechanism {
    NextServo hoodServo1 = new NextServo("hs1");
    //NextServo hoodServo2 = new NextServo("hs2");

    public static double openHoodPosition = 0.26;
    public static double closeHoodPosition = 0;
    public enum HoodState {
        SHOOT, CLOSE, INIT
    }
    public static HoodState mode = HoodState.INIT;

    public static double val;

    public static double hoodVal = 0;
    @Override
    public void periodic(){
        switch (mode){
            case SHOOT:
                val = openHoodPosition;
                break;

            case CLOSE:
                val = hoodVal;
                break;

            case INIT:
                break;
        }

        hoodServo1.setDirection(NextMotor.Direction.REVERSE);//test
        //hoodServo2.setDirection(NextMotor.Direction.REVERSE);//test


        hoodServo1.setPosition(val);
        //hoodServo2.setPosition(val);


    }

    public void refreshHoodVal(Pose robotPose, boolean redAllience){
        double robotX = robotPose.x();
        double robotY = robotPose.y();
        double robotHeading = robotPose.heading();
        Pose goalPose;
        if(redAllience){
            if(robotY > 70){
                goalPose = redRefereeGoal;
            }
            else{
                goalPose = redAudienceGoal;
            }
        }
        else{
            if(robotY > 70){
                goalPose = blueRefereeGoal;
            }
            else{
                goalPose = blueAudienceGoal;
            }
        }
        double goalX = goalPose.x();
        double goalY = goalPose.y();

        double xVal = goalX - robotX;
        double yVal = goalY - robotY;

        double distance = Math.sqrt((xVal*xVal)+(yVal*yVal));

        double hoodvelo = (-0.0152419*distance) + 0.869677;//write function here

        hoodVal = hoodvelo;
    }
}
