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
public class Turret implements Mechanism {
    public enum TurretTracking{
        ON, OFF, INIT
    }
    public static TurretTracking mode = TurretTracking.INIT;

    public static double turretAngle;
    public static double noAngle = 0.5;
    public static double trackingAngle;

    public static double DEGREES_PER_TICK = 0.00281;


    NextServo turretServo1 = new NextServo("ts1");
    NextServo turretServo2 = new NextServo("ts2");

    @Override
    public void periodic(){
        switch (mode){
            case ON:
                turretAngle = 1;
                break;

            case OFF:
                turretAngle = noAngle;
                break;

            case INIT:
                break;
        }

        turretServo1.setDirection(NextMotor.Direction.FORWARD);
        turretServo2.setDirection(NextMotor.Direction.FORWARD);

        turretServo1.setPosition(turretAngle);
        turretServo2.setPosition(turretAngle);
    }

    public void getTurretAngle(Pose robotPose, boolean redAllience){
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
        double angle = Math.atan2(yVal,xVal);

        double trueAngle = robotHeading + angle;

        trackingAngle = (trueAngle*DEGREES_PER_TICK) +0.5;



    }
}
