package org.firstinspires.ftc.teamcode.Auto;

import static com.pedropathing.api.Paths.line;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;

import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.parallel;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import org.firstinspires.ftc.teamcode.Mechanisms.Arm;
import org.firstinspires.ftc.teamcode.Mechanisms.BallBlock;
import org.firstinspires.ftc.teamcode.Mechanisms.Feeder;
import org.firstinspires.ftc.teamcode.Mechanisms.Hood;
import org.firstinspires.ftc.teamcode.Mechanisms.Intake;
import org.firstinspires.ftc.teamcode.Mechanisms.Shooter;
import org.firstinspires.ftc.teamcode.Teleop.NextRobot;
import org.firstinspires.ftc.teamcode.Variables.GlobalPose;
import org.firstinspires.ftc.teamcode.pedro.Constants;

import dev.nextftc.robot.opmode.NextAutonomous;
import dev.nextftc.robot.opmode.NextOpMode;

@NextAutonomous(name = "BLUE_R")
@Config

public class BlueAuto extends NextOpMode {
    private Follower follower = Constants.create(hardwareMap);
    private final NextRobot robot;

    public BlueAuto(NextRobot robot) {
        super(robot);
        this.robot = robot;
    }
    public static double spinUpTime = 50;
    public static double delayShootTime = 500;
    public static double turretTurnTime = 1000;
    public static double grabBallTime = 700;

    public static double HeadingR = 90;
    public static double HeadingA = 270;

    public static int fieldLengthXY = 144;




    private final Pose startPose = new Pose(fieldLengthXY-55,fieldLengthXY-8,Math.toRadians(HeadingR));
    private final Pose firstShootR = new Pose(fieldLengthXY-55,fieldLengthXY-32,Math.toRadians(HeadingR));
    private final Pose grabGarden = new Pose(fieldLengthXY-6,fieldLengthXY-9,Math.toRadians(HeadingR));
    private final Pose firstTransition = new Pose(fieldLengthXY-36,fieldLengthXY-42,Math.toRadians(HeadingR));
    private final Pose secondTransition = new Pose(fieldLengthXY-36,fieldLengthXY-118,Math.toRadians(HeadingA));
    private final Pose secondShootA = new Pose(fieldLengthXY-59,fieldLengthXY-118,Math.toRadians(HeadingA));
    private final Pose grabFlower = new Pose(fieldLengthXY-47,fieldLengthXY-133,Math.toRadians(HeadingA));
    private final Pose endPose = new Pose(fieldLengthXY-15,fieldLengthXY-115,Math.toRadians(HeadingA));


    private Path getStartGoFirstShootA() {
        return line(startPose, firstShootR).linear(startPose, firstShootR);
    }
    private Path getFirstShootAGoGrabGarden(){
        return line(firstShootR, grabGarden).linear(firstShootR, grabGarden);
    }
    private Path getGrabGardenGoFirstTransition(){
        return line(grabGarden, firstTransition).linear(grabGarden,firstTransition);
    }
    private Path getFirstTransitionGoSecondTransition(){
        return line(firstTransition, secondTransition).linear(firstTransition,secondTransition);
    }
    private Path getSecondTransitionGoSecondShootR(){
        return line(secondTransition, secondShootA).linear(secondTransition, secondShootA);
    }
    private Path getSecondShootRGoGrabFlower(){
        return line(secondShootA, grabFlower).linear(secondShootA, grabFlower);
    }
    private Path getGrabFlowerGoSecondShootR(){
        return line(grabFlower, secondShootA).linear(grabFlower, secondShootA);
    }
    private Path getSecondShootRGoEnd(){
        return line(secondShootA, endPose).linear(secondShootA, endPose);
    }



    public Command enableShoot(){
        return parallel(
                instant(()-> BallBlock.mode = BallBlock.BlockState.BALL_NO),
                instant(()-> Shooter.mode = Shooter.ShooterMode.ON),
                instant(()->Hood.mode = Hood.HoodState.SHOOT)
        );
    }

    public Command disableShoot() {
        return parallel(
                instant(()-> BallBlock.mode = BallBlock.BlockState.BALL_NO),
                instant(()-> Shooter.mode = Shooter.ShooterMode.OFF),
                instant(()-> Intake.mode =  Intake.IntakeMode.OFF),
                instant(()-> Feeder.mode = Feeder.FeederMode.OFF)
        );
    }

    public Command shoot(){
        return parallel(
                instant(()-> Intake.mode = Intake.IntakeMode.ON),
                instant(()-> Feeder.mode = Feeder.FeederMode.ON),
                instant(()-> BallBlock.mode =BallBlock.BlockState.BALL_GO)
        );
    }

    public Command intake(){
        return parallel(
                instant(()-> BallBlock.mode = BallBlock.BlockState.BALL_NO),
                instant(()-> Intake.mode = Intake.IntakeMode.ON),
                instant(()-> Feeder.mode = Feeder.FeederMode.INTAKE)
        );
    }

    public Command intakeNo(){
        return parallel(
                instant(()-> Feeder.mode = Feeder.FeederMode.OFF),
                instant(()-> Intake.mode = Intake.IntakeMode.OFF),
                instant(()-> Arm.mode = Arm.FlowerPiviotState.UP)
        );
    }
    public Command intakeToFlower(){
        return parallel(
                //new FollowPath(path),
                instant(()-> BallBlock.mode = BallBlock.BlockState.BALL_NO),
                instant(()-> Intake.mode = Intake.IntakeMode.ON),
                instant(()-> Arm.mode = Arm.FlowerPiviotState.DOWN),
                instant(()-> Feeder.mode =  Feeder.FeederMode.INTAKE)
        );
    }
    public Command intakeToGarden(){
        return parallel(
                //new FollowPath(path),
                instant(()-> BallBlock.mode = BallBlock.BlockState.BALL_NO),
                instant(()-> Intake.mode = Intake.IntakeMode.ON)
        );
    }


    public Command autoRoutine() {
        return parallel(
                enableShoot(),
                waitMs(spinUpTime),
                shoot(),
                waitMs(delayShootTime),
                follow(follower, getStartGoFirstShootA()),
                waitMs(delayShootTime),
                intake(),
                follow(follower, getFirstShootAGoGrabGarden()),
                waitMs(grabBallTime),
                follow(follower, getGrabGardenGoFirstTransition()),
                intakeNo(),
                follow(follower, getFirstTransitionGoSecondTransition()),
                follow(follower, getSecondTransitionGoSecondShootR()), waitMs(turretTurnTime), shoot(),
                waitMs(delayShootTime),
                intakeToFlower(),
                follow(follower, getSecondShootRGoGrabFlower()),
                intakeNo(),
                follow(follower, getGrabFlowerGoSecondShootR()), shoot(),
                waitMs(delayShootTime),
                follow(follower, getSecondShootRGoEnd()),
                    disableShoot()

        );

    }




    @Override
    public void disabledPeriodic() {
        Scheduler.reset();
        follower.setPose(startPose);
        follower.update();



    }

    @Override
    public void start() {
        schedule(autoRoutine());

    }

    @Override
    public void periodic() {//loop
        follower.update();
        Scheduler.execute();

    }

    @Override
    public void end(){
        GlobalPose.CurrentPose = follower.pose();
        GlobalPose.redAlliance = false;
    }

}








