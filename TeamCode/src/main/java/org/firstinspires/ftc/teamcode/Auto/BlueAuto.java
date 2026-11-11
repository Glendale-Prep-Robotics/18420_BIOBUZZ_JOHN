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

import static org.firstinspires.ftc.teamcode.Auto.RedAuto.delayShootTime;
import static org.firstinspires.ftc.teamcode.Auto.RedAuto.disableShoot;
import static org.firstinspires.ftc.teamcode.Auto.RedAuto.enableShoot;
import static org.firstinspires.ftc.teamcode.Auto.RedAuto.grabBallTime;
import static org.firstinspires.ftc.teamcode.Auto.RedAuto.intake;
import static org.firstinspires.ftc.teamcode.Auto.RedAuto.intakeNo;
import static org.firstinspires.ftc.teamcode.Auto.RedAuto.intakeToFlower;
import static org.firstinspires.ftc.teamcode.Auto.RedAuto.shoot;
import static org.firstinspires.ftc.teamcode.Auto.RedAuto.turnTime;
import static org.firstinspires.ftc.teamcode.Auto.RedAuto.turretTurnTime;

import org.firstinspires.ftc.teamcode.Mechanisms.Sensor;
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




    private final Pose startPose = new Pose(fieldLengthXY-55,fieldLengthXY-9,Math.toRadians(90)+pi);
    private final Pose firstShootR = new Pose(fieldLengthXY - 55,fieldLengthXY - 32,Math.toRadians(270) + pi);
    private final Pose grabGarden = new Pose(fieldLengthXY-3,fieldLengthXY - 11,Math.toRadians(270) + pi);
    private final Pose firstTransition = new Pose(fieldLengthXY-34,fieldLengthXY-42,Math.toRadians(270)+pi);
    private final Pose secondTransition = new Pose(fieldLengthXY-34,fieldLengthXY-118,Math.toRadians(90)+pi);
    private final Pose transitionShootA = new Pose(fieldLengthXY-59,fieldLengthXY-118,Math.toRadians(0)+pi);
    private final Pose grabFlower = new Pose(fieldLengthXY-50,fieldLengthXY-129,Math.toRadians(90)+pi);
    private final Pose endPose = new Pose(fieldLengthXY-15,fieldLengthXY-115,Math.toRadians(90)+pi);
    private final Pose beforeFlowerGrab = new Pose(fieldLengthXY-50,fieldLengthXY-118,Math.toRadians(90)+pi);
    private final Pose secondShootA = new Pose(fieldLengthXY-59,fieldLengthXY-136,Math.toRadians(0)+pi);



    private Path StartGoFirstShootR() {
        return line(startPose, firstShootR).linear(startPose, firstShootR);
    }
    private Path FirstShootAGoGrabGarden(){
        return line(firstShootR, grabGarden).linear(firstShootR, grabGarden);
    }
    private Path GrabGardenGoFirstTransition(){
        return line(grabGarden, firstTransition).linear(grabGarden,firstTransition);
    }
    private Path FirstTransitionGoSecondTransition(){
        return line(firstTransition, secondTransition).linear(firstTransition,secondTransition);
    }
    private Path SecondTransitionGoTransitionShootR(){
        return line(secondTransition, transitionShootA).linear(secondTransition, transitionShootA);
    }
    private Path transitionShootRGoSecondShootR(){
        return line(transitionShootA, secondShootA).constant(transitionShootA);
    }
    private Path secondShootRGoTransitionShootR() {
        return line(secondShootA, transitionShootA).linear(secondShootA, transitionShootA);
    }
    private Path transitionShootRGoBeforeGrabFlower() {
        return line(transitionShootA,beforeFlowerGrab).linear(transitionShootA,beforeFlowerGrab);
    }
    private Path beforeGrabFlowerGoTransitionShootR() {
        return line(beforeFlowerGrab,transitionShootA).linear(beforeFlowerGrab,transitionShootA);
    }
    private Path beforeGrabFlowerGoGrabFlower() {
        return line(beforeFlowerGrab,grabFlower).linear(beforeFlowerGrab,grabFlower);
    }
    private Path GrabFlowerGoBeforeGrabFlower(){
        return line(grabFlower, beforeFlowerGrab).linear(grabFlower, beforeFlowerGrab);
    }
    private Path transitionShootRGoEnd(){
        return line(transitionShootA, endPose).linear(transitionShootA, endPose);
    }

    public Command spinUp() {
        return sequential(
                waitUntil(() -> robot.shooter.ShooterAtVelocity())
        );
    }

    public Command autoRoutine() {
        return parallel(
                instant(()-> Turret.mode = Turret.TurretTracking.ON),
                sequential(
                        enableShoot(),
                        spinUp(),
                        shoot(),
                        follow(follower, StartGoFirstShootR()),
                        intake(),
                        follow(follower, FirstShootAGoGrabGarden()),
                        waitMs(grabBallTime),
                        follow(follower, GrabGardenGoFirstTransition()),
                        intakeNo(),
                        follow(follower, FirstTransitionGoSecondTransition()),
                        follow(follower, SecondTransitionGoTransitionShootR()), waitMs(turretTurnTime),
                        follow(follower, transitionShootRGoSecondShootR()),spinUp(), shoot(), waitMs(delayShootTime),
                        follow(follower, secondShootRGoTransitionShootR()),
                        follow(follower, transitionShootRGoBeforeGrabFlower()),
                        intakeToFlower(), waitMs(500),
                        follow(follower, beforeGrabFlowerGoGrabFlower()), waitMs(1500),
                        follow(follower, GrabFlowerGoBeforeGrabFlower()), intakeNo(),
                        follow(follower, beforeGrabFlowerGoTransitionShootR()),
                        follow(follower, transitionShootRGoSecondShootR()), waitMs(turnTime),
                        spinUp(),shoot(), waitMs(delayShootTime),
                        follow(follower, secondShootRGoTransitionShootR()),
                        follow(follower, transitionShootRGoEnd()),
                        disableShoot()

                ));
    }

    @Override
    public void disabledPeriodic() {
        Scheduler.reset();
        follower.setPose(startPose);
        follower.update();
        GlobalPose.redAlliance = false;
        Sensor.mode = Sensor.IndicatorLight.BLUE;



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









