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
import static com.pedropathing.ivy.commands.Commands.waitUntil;
import static com.pedropathing.ivy.groups.Groups.parallel;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import static org.firstinspires.ftc.teamcode.Auto.RedAuto.disableShoot;
import static org.firstinspires.ftc.teamcode.Auto.RedAuto.enableShoot;
import static org.firstinspires.ftc.teamcode.Auto.RedAuto.grabBallTime;
import static org.firstinspires.ftc.teamcode.Auto.RedAuto.intake;
import static org.firstinspires.ftc.teamcode.Auto.RedAuto.intakeNo;
import static org.firstinspires.ftc.teamcode.Auto.RedAuto.intakeToFlower;
import static org.firstinspires.ftc.teamcode.Auto.RedAuto.shoot;
import static org.firstinspires.ftc.teamcode.Auto.RedAuto.turnTime;

import org.firstinspires.ftc.teamcode.Mechanisms.Turret;
import org.firstinspires.ftc.teamcode.Teleop.NextRobot;
import org.firstinspires.ftc.teamcode.Variables.GlobalPose;
import org.firstinspires.ftc.teamcode.pedro.Constants;

import dev.nextftc.robot.opmode.NextAutonomous;
import dev.nextftc.robot.opmode.NextOpMode;

@Config
@NextAutonomous(name = "BLUE_R")
public class BlueAuto extends NextOpMode {
    private NextRobot robot;

    public BlueAuto(NextRobot robot) {
        super(robot);
        this.robot = robot;
    }

    private Follower follower = Constants.create(hardwareMap);

    public static int fieldLengthXY = 144;

    double pi = Math.PI;




    private final Pose startPose = new Pose(fieldLengthXY-54,fieldLengthXY-8,Math.toRadians(90)+pi);
    private final Pose firstShootR = new Pose(fieldLengthXY - 55,fieldLengthXY - 32,Math.toRadians(270) + pi);
    private final Pose grabGarden = new Pose(fieldLengthXY-3,fieldLengthXY - 11.5,Math.toRadians(270) + pi);
    private final Pose firstTransition = new Pose(fieldLengthXY-30,fieldLengthXY-42,Math.toRadians(270)+pi);
    private final Pose secondTransition = new Pose(fieldLengthXY-30,fieldLengthXY-118,Math.toRadians(90)+pi);
    private final Pose transitionShootA = new Pose(fieldLengthXY-59,fieldLengthXY-118,Math.toRadians(0)+pi);
    private final Pose grabFlower = new Pose(fieldLengthXY-54,fieldLengthXY-129,Math.toRadians(90)+pi);
    private final Pose endPose = new Pose(fieldLengthXY-15,fieldLengthXY-115,Math.toRadians(90)+pi);
    private final Pose beforeFlowerGrab = new Pose(fieldLengthXY-50,fieldLengthXY-118,Math.toRadians(90)+pi);
    private final Pose secondShootA = new Pose(fieldLengthXY-59,fieldLengthXY-133,Math.toRadians(0)+pi);



    private Path StartGoFirstShootR() {
        return line(startPose, firstShootR).linear(startPose, firstShootR);
    }
    private Path FirstShootRGoGrabGarden(){
        return line(firstShootR, grabGarden).linear(firstShootR, grabGarden);
    }
    private Path GrabGardenGoFirstTransition(){
        return line(grabGarden, firstTransition).linear(grabGarden,firstTransition);
    }
    private Path FirstTransitionGoSecondTransition(){
        return line(firstTransition, secondTransition).linear(firstTransition,secondTransition);
    }
    private Path SecondTransitionGoTransitionShootA(){
        return line(secondTransition, transitionShootA).linear(secondTransition, transitionShootA);
    }
    private Path transitionShootAGoSecondShootA(){
        return line(transitionShootA, secondShootA).constant(transitionShootA);
    }
    private Path secondShootAGoTransitionShootA() {
        return line(secondShootA, transitionShootA).linear(secondShootA, transitionShootA);
    }
    private Path transitionShootAGoBeforeGrabFlower() {
        return line(transitionShootA,beforeFlowerGrab).linear(transitionShootA,beforeFlowerGrab);
    }
    private Path beforeGrabFlowerGoTransitionShootA() {
        return line(beforeFlowerGrab,transitionShootA).linear(beforeFlowerGrab,transitionShootA);
    }
    private Path beforeGrabFlowerGoGrabFlower() {
        return line(beforeFlowerGrab,grabFlower).linear(beforeFlowerGrab,grabFlower);
    }
    private Path GrabFlowerGoBeforeGrabFlower(){
        return line(grabFlower, beforeFlowerGrab).linear(grabFlower, beforeFlowerGrab);
    }
    private Path transitionShootAGoEnd(){
        return line(transitionShootA, endPose).linear(transitionShootA, endPose);
    }

    public Command spinUp() {
        return sequential(
                waitUntil(() -> robot.shooter.ShooterAtVelocity())
        );
    }

    public Command autoRoutine() {
        return sequential(
                parallel(
                instant(()-> Turret.mode = Turret.TurretTracking.ON),
                sequential(
                        enableShoot(),
                        spinUp(),
                        shoot(),
                        waitMs(500),
                        follow(follower, StartGoFirstShootR()),
                        intake(),
                        follow(follower, FirstShootRGoGrabGarden()),
                        waitMs(grabBallTime),
                        follow(follower, GrabGardenGoFirstTransition())
                        )),
                        instant(()-> Turret.mode = Turret.TurretTracking.OFF),
                        intakeNo(),
                        follow(follower, FirstTransitionGoSecondTransition()),
                        follow(follower, SecondTransitionGoTransitionShootA()), waitMs(500),
                        parallel(
                                instant(()->Turret.mode = Turret.TurretTracking.ON),
                                sequential(
                        follow(follower, transitionShootAGoSecondShootA()),spinUp(), waitMs(1000), shoot(), waitMs(1000),
                        follow(follower, secondShootAGoTransitionShootA()),
                        follow(follower, transitionShootAGoBeforeGrabFlower()),
                        intakeToFlower(), waitMs(500),
                        follow(follower, beforeGrabFlowerGoGrabFlower()), waitMs(1500),
                        follow(follower, GrabFlowerGoBeforeGrabFlower()), intakeNo(),
                        follow(follower, beforeGrabFlowerGoTransitionShootA()),
                        follow(follower, transitionShootAGoSecondShootA()), waitMs(turnTime),
                        spinUp(),shoot(), waitMs(1000),
                        follow(follower, secondShootAGoTransitionShootA()),
                        follow(follower, transitionShootAGoEnd()),
                        disableShoot()

                )));
    }

    @Override
    public void disabledPeriodic() {
        Scheduler.reset();
        follower.setPose(startPose);
        follower.update();
        GlobalPose.redAlliance = false;
        robot.turret.goToZero();
        robot.sensor.allianceIndicator(GlobalPose.redAlliance);
        robot.ballBlock.ballBlockOut();



    }

    @Override
    public void start() {
        schedule(autoRoutine());

    }

    @Override
    public void periodic() {//loop
        follower.update();
        Scheduler.execute();

        robot.shooter.refreshShooterVelo(follower.pose(), GlobalPose.redAlliance);
        robot.hood.refreshHoodVal(follower.pose(), GlobalPose.redAlliance);
        robot.turret.getTurretAngle(follower.pose(), GlobalPose.redAlliance);

    }

    @Override
    public void end(){
        GlobalPose.CurrentPose = follower.pose();
        GlobalPose.redAlliance = false;
    }
}









