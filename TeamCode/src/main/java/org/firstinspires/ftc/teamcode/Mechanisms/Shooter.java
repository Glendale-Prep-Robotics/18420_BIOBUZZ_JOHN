package org.firstinspires.ftc.teamcode.Mechanisms;

import static org.firstinspires.ftc.teamcode.Variables.GoalPose.blueAudienceGoal;
import static org.firstinspires.ftc.teamcode.Variables.GoalPose.blueRefereeGoal;
import static org.firstinspires.ftc.teamcode.Variables.GoalPose.redAudienceGoal;
import static org.firstinspires.ftc.teamcode.Variables.GoalPose.redRefereeGoal;

import static dev.nextftc.units.Units.DegreesPerSecond;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.util.Range;

import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.robot.Telemetry;

@Config
public class Shooter implements Mechanism {
    public static double KP = 0.095;
    public double KI = 0;
    public double KD = 0;
    public static double KV = 0.0000087;
    public static double KS =0.009;
    public double KA = 0;
    //F then P then D.


    NextMotor shooterMotor2 = new NextMotor("sm2");
    NextMotor shooterMotor1 = new NextMotor("sm1");

    public enum ShooterMode{
        ON, OFF
    }

    public static ShooterMode mode =ShooterMode.OFF;

    public double val;

    public static double motorVelo = 93000;

    public double realSpeed;


    @Override
    public void periodic(){

        shooterMotor1.follow(shooterMotor2, NextMotor.Direction.REVERSE);
        shooterMotor1.setDirection(NextMotor.Direction.REVERSE);
        shooterMotor2.setZeroPowerBehavior(NextMotor.ZeroPowerBehavior.FLOAT);
        shooterMotor1.setZeroPowerBehavior(NextMotor.ZeroPowerBehavior.FLOAT);
//        shooterMotor1.setAnglePerCount(new Angle(0.22439947142,Radians)); //maybe?
//        shooterMotor2.setAnglePerCount(new Angle(0.22439947142,Radians));
        switch (mode){
            case OFF:
                val = 0;
                break;

            case ON:
                val = motorVelo;
                break;
        }

        double currentSpeed = shooterMotor2.getEncoderVelocity().abs(DegreesPerSecond);
        double power =  ((KP*(val - currentSpeed))+(KV*val)+(KS*Math.signum(val)));

        realSpeed = Range.clip(power,0,1);
        shooterMotor1.setThrottle(realSpeed);
        Telemetry.log("current Speed", currentSpeed);
        Telemetry.log("setPoint", realSpeed);
        Telemetry.log("shooter Error", motorVelo-currentSpeed);
        Telemetry.log("target Speed", motorVelo);
        Telemetry.log("power", shooterMotor2.getThrottle());
//        shooterMotor1.getVelocityConstants()
//                .withP(KP)
//                .withD(KD)
//                .withI(KI)
//                .withV(KV)
//                .withS(KS)
//                .withA(KA);
//        shooterMotor1.setVelocitySetpoint(DegreesPerSecond.of(val));
//// Like kP, other velocity PID constants include:
//// kP, kI, kD, kS, kV, kA
//        AngularVelocity currentVelMeasure = shooterMotor1.getEncoderVelocity();
//        AngularVelocity curentVel2 = shooterMotor2.getEncoderVelocity();
//        double currentRPM = currentVelMeasure.abs(DegreesPerSecond);
//        double cureentRPM2 = curentVel2.abs(DegreesPerSecond);
//        Telemetry.log("Motor Velocity", currentRPM);
//        Telemetry.log("Motor Velocity 2 ", cureentRPM2);


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
