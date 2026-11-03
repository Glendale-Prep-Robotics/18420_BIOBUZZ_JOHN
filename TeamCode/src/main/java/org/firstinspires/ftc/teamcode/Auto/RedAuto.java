package org.firstinspires.ftc.teamcode.Auto;

import static com.pedropathing.api.Paths.line;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;

import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.groups.Groups.parallel;
import static com.pedropathing.ivy.groups.Groups.sequential;

import org.firstinspires.ftc.teamcode.Mechanisms.Arm;
import org.firstinspires.ftc.teamcode.Mechanisms.BallBlock;
import org.firstinspires.ftc.teamcode.Mechanisms.Feeder;
import org.firstinspires.ftc.teamcode.Mechanisms.Hood;
import org.firstinspires.ftc.teamcode.Mechanisms.Intake;
import org.firstinspires.ftc.teamcode.Mechanisms.Shooter;
import org.firstinspires.ftc.teamcode.Mechanisms.Turret;
import org.firstinspires.ftc.teamcode.Teleop.NextRobot;
import org.firstinspires.ftc.teamcode.Variables.GlobalPose;
import org.firstinspires.ftc.teamcode.pedro.Constants;

import dev.nextftc.robot.Telemetry;
import dev.nextftc.robot.opmode.NextAutonomous;
import dev.nextftc.robot.opmode.NextOpMode;

@Config
@NextAutonomous(name = "RED_A")
public class RedAuto extends NextOpMode {
    private NextRobot robot;

    public RedAuto(NextRobot robot) {
        super(robot);
        this.robot = robot;
    }

    //public static Follower follower = Constants.create(HardwareMap);
    private Follower follower = Constants.create(hardwareMap);
    public static double HeadingR = 90;
    public static double HeadingA = 270;


    public static double spinUpTime = 1000;
    public static double delayShootTime = 500;
    public static double turretTurnTime = 1000;
    public static double grabBallTime = 700;

  

    public static double ballNo = 0.6;
    public static double ballGo = 0.4;

    private final Pose startPose = new Pose(55,8,Math.toRadians(270));
    private final Pose firstShootA = new Pose(55,32,Math.toRadians(270));
    private final Pose grabGarden = new Pose(6,9,Math.toRadians(270));
    private final Pose firstTransition = new Pose(36,42,Math.toRadians(270));
    private final Pose secondTransition = new Pose(36,118,Math.toRadians(90));
    private final Pose secondShootR = new Pose(59,118,Math.toRadians(0));
    private final Pose grabFlower = new Pose(47,133,Math.toRadians(90));
    private final Pose endPose = new Pose(15,115,Math.toRadians(90));


    private Path getStartGoFirstShootA() {
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
                instant(()->Turret.mode = Turret.TurretTracking.ON),
                sequential(
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

        ));

    }






    @Override
    public void disabledPeriodic() {

        Scheduler.reset();
        follower.setPose(startPose);
        follower.update();
        GlobalPose.redAllience = true;


    }
    @Override
    public void start() {
        schedule(autoRoutine());
    }

    @Override
    public void periodic() {//loop
        follower.update();
        Scheduler.execute();
        robot.turret.getTurretAngle(follower.pose(), GlobalPose.redAllience);

        Telemetry.log("current Pose", follower.pose());
        Telemetry.log("current turret Angle", robot.turret.turretAngle);


    }

    @Override
    public void end(){
        GlobalPose.CurrentPose = follower.pose();
        GlobalPose.redAllience = true;
    }

}
