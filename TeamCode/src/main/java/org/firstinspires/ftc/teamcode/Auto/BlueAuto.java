package org.firstinspires.ftc.teamcode.Auto;

import static com.pedropathing.api.Paths.line;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.groups.Groups.parallel;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import org.firstinspires.ftc.teamcode.Mechanisms.Arm;
import org.firstinspires.ftc.teamcode.Mechanisms.BallBlock;
import org.firstinspires.ftc.teamcode.Mechanisms.Feeder;
import org.firstinspires.ftc.teamcode.Mechanisms.Hood;
import org.firstinspires.ftc.teamcode.Mechanisms.Intake;
import org.firstinspires.ftc.teamcode.Mechanisms.Turret;
import org.firstinspires.ftc.teamcode.Teleop.NextRobot;
import org.firstinspires.ftc.teamcode.pedro.Constants;

import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.robot.opmode.NextAutonomous;
import dev.nextftc.robot.opmode.NextOpMode;

@NextAutonomous(name = "BLUE_R")

public class BlueAuto extends NextOpMode {
    private Follower follower = Constants.create(hardwareMap);
    private final NextRobot robot;

    public BlueAuto(NextRobot robot) {
        super(robot);
        this.robot = robot;
    }

    public static double HeadingR = 90;
    public static double HeadingA = 270;

    public static int fieldLengthXY = 144;

    public static double ballNo = 0.6;
    public static double ballGo = 0.4;



    private final Pose startPose = new Pose(fieldLengthXY-55,fieldLengthXY-8,Math.toRadians(HeadingR));
    private final Pose firstShootR = new Pose(fieldLengthXY-55,fieldLengthXY-32,Math.toRadians(HeadingR));
    private final Pose grabGarden = new Pose(fieldLengthXY-6,fieldLengthXY-9,Math.toRadians(HeadingR));
    private final Pose firstTransition = new Pose(fieldLengthXY-36,fieldLengthXY-42,Math.toRadians(HeadingR));
    private final Pose secondTransition = new Pose(fieldLengthXY-36,fieldLengthXY-118,Math.toRadians(HeadingA));
    private final Pose secondShootA = new Pose(fieldLengthXY-59,fieldLengthXY-118,Math.toRadians(HeadingA));
    private final Pose grabFlower = new Pose(fieldLengthXY-47,fieldLengthXY-133,Math.toRadians(HeadingA));
    private final Pose endPose = new Pose(fieldLengthXY-15,fieldLengthXY-115,Math.toRadians(HeadingA));

    private Path startGoFirstShootA;
    private Path firstShootAGoGrabGarden;
    private Path grabGardenGoFirstTransition;
    private Path firstTransitionGoSecondTransition;
    private Path secondTransitionGoSecondShootR;
    private Path secondShootRGoGrabFlower;
    private Path grabFlowerGoSecondShootR;
    private Path secondShootRGoEnd;

    private Path StartGoFirstShootA() {
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
                instant(()-> Turret.mode = Turret.TurretTracking.ON),
                instant(()-> BallBlock.mode = BallBlock.BlockState.BALL_NO),
                instant(()-> Hood.mode = Hood.HoodState.SHOOT)
        );
    }
    public Command shoot(){
        return sequential(
                //new FollowPath(path),
                instant(()-> Intake.mode = Intake.IntakeMode.ON),
                instant(()-> Feeder.mode = Feeder.FeederMode.ON),
                instant(()-> BallBlock.mode =BallBlock.BlockState.BALL_GO)
        );
    }

    public Command intake(){
        return parallel(
                instant(()-> BallBlock.mode = BallBlock.BlockState.BALL_NO),
                instant(()-> Intake.mode = Intake.IntakeMode.ON),
                instant(()-> Feeder.mode = Feeder.FeederMode.ON)
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
    public Command go(){
        return sequential(



        );
    }
    public Command autoRoutine() {
        return sequential(
                enableShoot(),
                follow(follower, startGoFirstShootA), shoot(),
                intake(),
                follow(follower, firstShootAGoGrabGarden),
                intakeNo(),
                follow(follower, grabGardenGoFirstTransition),
                follow(follower, firstTransitionGoSecondTransition),
                follow(follower, secondTransitionGoSecondShootR), shoot(),
                intakeToFlower(),
                follow(follower, secondShootRGoGrabFlower),
                intakeNo(),
                follow(follower, grabFlowerGoSecondShootR), shoot(),
                follow(follower, secondShootRGoEnd)

        );

    }




    @Override
    public void disabledPeriodic() {


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

}








