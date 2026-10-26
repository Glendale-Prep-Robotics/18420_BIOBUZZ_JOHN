package org.firstinspires.ftc.teamcode.Auto;

import static com.pedropathing.api.Paths.line;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.groups.Groups.parallel;
import static com.pedropathing.ivy.groups.Groups.sequential;

import org.firstinspires.ftc.teamcode.Mechanisms.Arm;
import org.firstinspires.ftc.teamcode.Mechanisms.BallBlock;
import org.firstinspires.ftc.teamcode.Mechanisms.Hood;
import org.firstinspires.ftc.teamcode.Mechanisms.Intake;
import org.firstinspires.ftc.teamcode.Mechanisms.Turret;
import org.firstinspires.ftc.teamcode.Teleop.NextRobot;

import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.robot.opmode.NextAutonomous;
import dev.nextftc.robot.opmode.NextOpMode;

@NextAutonomous(name = "RED_AUTO")
public class RedAuto extends NextOpMode {
    private final NextRobot robot;

    public RedAuto(NextRobot robot) {
        super(robot);
        this.robot = robot;
    }

    public static double HeadingR = 90;
    public static double HeadingA = 270;

    public static int fieldLengthXY = 144;

    public static double ballNo = 0.6;
    public static double ballGo = 0.4;

    private final Pose startPose = new Pose(55,8,Math.toRadians(270));
    private final Pose firstShootA = new Pose(55,32,Math.toRadians(270));
    private final Pose grabGarden = new Pose(6,9,Math.toRadians(270));
    private final Pose firstTransition = new Pose(36,42,Math.toRadians(270));
    private final Pose secondTransition = new Pose(36,118,Math.toRadians(90));
    private final Pose secondShootR = new Pose(59,118,Math.toRadians(90));
    private final Pose grabFlower = new Pose(47,133,Math.toRadians(90));
    private final Pose endPose = new Pose(15,115,Math.toRadians(90));

    private Path startGoFirstShootA;
    private Path firstShootAGoGrabGarden;
    private Path grabGardenGoFirstTransition;
    private Path firstTransitionGoSecondTransition;
    private Path secondTransitionGoSecondShootR;
    private Path secondShootRGoGrabFlower;
    private Path grabFlowerGoSecondShootR;
    private Path secondShootRGoEnd;

    private Path StartGoFirstShootA() {
        return line(startPose, firstShootA).linear(startPose, firstShootA);
    }
    private Path getFirstShootAGoGrabGarden(){
        return line(firstShootA, grabGarden).linear(firstShootA, grabGarden);
    }
    private Path getGrabGardenGoFirstTransition(){
        return line(grabGarden, firstTransition).linear(grabGarden,firstTransition);
    }
    private Path getFirstTransitionGoSecondTransition(){
        return line(firstTransition, secondTransition).linear(firstTransition,secondTransition);
    }
    private Path getSecondTransitionGoSecondShootR(){
        return line(secondTransition, secondShootR).linear(secondTransition, secondShootR);
    }
    private Path getSecondShootRGoGrabFlower(){
        return line(secondShootR, grabFlower).linear(secondShootR, grabFlower);
    }
    private Path getGrabFlowerGoSecondShootR(){
        return line(grabFlower, secondShootR).linear(grabFlower, secondShootR);
    }
    private Path getSecondShootRGoEnd(){
        return line(secondShootR, endPose).linear(secondShootR, endPose);
    }



    public Command enableShoot(){
        return parallel(
                instant(()-> Turret.mode = Turret.TurretTracking.ON),
                instant(()-> BallBlock.mode = BallBlock.BlockState.BALL_NO),
                instant(()-> Hood.mode = Hood.HoodState.SHOOT)
        );
    }
    public Command shooot(){
        return sequential(
                //new FollowPath(path),
                instant(()-> Intake.mode = Intake.IntakeMode.ON),
                instant(()-> BallBlock.mode =BallBlock.BlockState.BALL_GO)
        );
    }
    public Command intakeToFlower(){
        return parallel(
                //new FollowPath(path),
                instant(()-> BallBlock.mode = BallBlock.BlockState.BALL_NO),
                instant(()-> Intake.mode = Intake.IntakeMode.ON)

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
                //new FollowPath(path)
        );
    }

    @Override
    public void disabledPeriodic() {


    }

    @Override
    public void start() {

    }

    @Override
    public void periodic() {
        sequential(
        enableShoot()






        );

    }

}
