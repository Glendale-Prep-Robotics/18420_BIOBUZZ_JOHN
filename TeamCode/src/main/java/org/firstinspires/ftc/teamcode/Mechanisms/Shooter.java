package org.firstinspires.ftc.teamcode.Mechanisms;

import static org.firstinspires.ftc.teamcode.Mechanisms.GoalPose.blueAudienceGoal;
import static org.firstinspires.ftc.teamcode.Mechanisms.GoalPose.blueRefereeGoal;
import static org.firstinspires.ftc.teamcode.Mechanisms.GoalPose.redAudienceGoal;
import static org.firstinspires.ftc.teamcode.Mechanisms.GoalPose.redRefereeGoal;

import static dev.nextftc.units.Units.DegreesPerSecond;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.math.Pose;

import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.robot.Telemetry;
import dev.nextftc.units.Units;
import dev.nextftc.units.measuretypes.AngularVelocity;

@Config
public class Shooter implements Mechanism {
    public static double KP = 0.00003;
    public static double KI = 0;
    public static double KD = 0;
    //F then P then D.


    NextMotor shooterMotor2 = new NextMotor("sm2");
    NextMotor shooterMotor1 = new NextMotor("sm1");

    public enum ShooterMode{
        ON, OFF
    }

    public static ShooterMode mode =ShooterMode.OFF;

    public static double val;

    public static double motorVelo = 20000;


    @Override
    public void periodic(){

        shooterMotor2.follow(shooterMotor1, NextMotor.Direction.FORWARD);
        shooterMotor1.setDirection(NextMotor.Direction.REVERSE);
        shooterMotor2.setZeroPowerBehavior(NextMotor.ZeroPowerBehavior.FLOAT);
        shooterMotor1.setZeroPowerBehavior(NextMotor.ZeroPowerBehavior.FLOAT);

        switch (mode){
            case OFF:
                val = 0;
                break;

            case ON:
                val = motorVelo;
                break;
        }
        shooterMotor1.getVelocityConstants()
                .withP(KP)
                .withD(KD)
                .withI(KI);
        shooterMotor1.setVelocitySetpoint(DegreesPerSecond.of(val));
// Like kP, other velocity PID constants include:
// kP, kI, kD, kS, kV, kA
        AngularVelocity currentVelMeasure = shooterMotor1.getEncoderVelocity();
        double currentRPM = currentVelMeasure.abs(DegreesPerSecond);
        Telemetry.log("Motor Velocity", currentRPM);


    }

    public void refreshShooterVelo(Pose robotPose, boolean redAllience){
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

        double velo = distance;//write function here

        motorVelo = velo;
    }


}
