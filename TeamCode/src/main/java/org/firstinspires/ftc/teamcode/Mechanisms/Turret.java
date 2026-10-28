package org.firstinspires.ftc.teamcode.Mechanisms;

import static org.firstinspires.ftc.teamcode.Variables.GoalPose.blueAudienceGoal;
import static org.firstinspires.ftc.teamcode.Variables.GoalPose.blueRefereeGoal;
import static org.firstinspires.ftc.teamcode.Variables.GoalPose.redAudienceGoal;
import static org.firstinspires.ftc.teamcode.Variables.GoalPose.redRefereeGoal;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.util.Range;

import org.opencv.core.Mat;

import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.hardware.actuators.NextServo;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.robot.Telemetry;

@Config
public class Turret implements Mechanism {
    public enum TurretTracking{
        ON, OFF, INIT, ALL
    }
    public static TurretTracking mode = TurretTracking.INIT;

    public double turretAngle;
    public double trackingAngle;

    public static double DEGREES_PER_TICK = 1./320;


    NextServo turretServo1 = new NextServo("ts1");
    NextServo turretServo2 = new NextServo("ts2");

    @Override
    public void periodic(){
        switch (mode){
            case ON:
                turretAngle = trackingAngle;
                break;

            case OFF:
                turretAngle = 0;
                break;

            case INIT:
                break;
        }

        turretServo1.setDirection(NextMotor.Direction.FORWARD);
        turretServo2.setDirection(NextMotor.Direction.FORWARD);

        turretAngle = Range.clip(turretAngle, 0,1);

        turretServo1.setPosition(turretAngle);
        turretServo2.setPosition(turretAngle);

        Telemetry.log("turretServo1 position", turretServo1.getPosition());
        Telemetry.log("turretServo2 position", turretServo2.getPosition());
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

        double trueAngle = Math.toDegrees(robotHeading - angle);
//        if (trueAngle > 360){
//            trueAngle -= 360;
//        } else if (trueAngle < -360) {
//            trueAngle +=360;
//        }

        trackingAngle = (trueAngle*DEGREES_PER_TICK) +0.5;

        Telemetry.log("target Angle", trueAngle);
        Telemetry.log("robo heading", Math.toDegrees(robotHeading));
        Telemetry.log("calculated angle", Math.toDegrees(angle));



    }
}
