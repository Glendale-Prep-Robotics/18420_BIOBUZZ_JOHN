package org.firstinspires.ftc.teamcode.Teleop;


import static org.firstinspires.ftc.teamcode.Mechanisms.GoalPose.blueAudienceGoal;
import static org.firstinspires.ftc.teamcode.Mechanisms.GoalPose.blueRefereeGoal;
import static org.firstinspires.ftc.teamcode.Mechanisms.GoalPose.redRefereeGoal;
import static org.firstinspires.ftc.teamcode.Variables.GlobalPose.redAlliance;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Mechanisms.Arm;
import org.firstinspires.ftc.teamcode.Mechanisms.BallBlock;
import org.firstinspires.ftc.teamcode.Mechanisms.Brake;
import org.firstinspires.ftc.teamcode.Mechanisms.Feeder;
import org.firstinspires.ftc.teamcode.Mechanisms.GoalPose;
import org.firstinspires.ftc.teamcode.Mechanisms.Hood;
import org.firstinspires.ftc.teamcode.Mechanisms.Intake;
import org.firstinspires.ftc.teamcode.Mechanisms.Sensor;
import org.firstinspires.ftc.teamcode.Mechanisms.Shooter;
import org.firstinspires.ftc.teamcode.Mechanisms.Turret;
import org.firstinspires.ftc.teamcode.Variables.GlobalPose;
import org.firstinspires.ftc.teamcode.pedro.Constants;

import dev.nextftc.robot.Telemetry;
import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.opmode.NextTeleop;
import dev.nextftc.robot.triggers.CommandGamepad;

@Config
@NextTeleop(name = "DriverControl")
public class DriverControl extends NextOpMode {
    private final NextRobot robot;
    private final ElapsedTime matchTime = new ElapsedTime();
    public DriverControl(NextRobot robot) {
        super(robot);
        this.robot = robot;
        matchTime.reset();
    }

    CommandGamepad gp1 = new CommandGamepad(gamepad1);
    CommandGamepad gp2 = new CommandGamepad(gamepad2);

    public enum PlayerAmount {
        TWO_PLAYERS,
        ONE_PLAYERS
    }
    PlayerAmount playerAmount = PlayerAmount.TWO_PLAYERS;
    boolean noControls = true;
    Gamepad choosePad;

    public boolean armDown = false;
    public boolean shooterOn = false;
    public boolean brakeOn = false;


    private Follower follower = Constants.create(hardwareMap);


    @Override
    public void disabledPeriodic() {

        double initSeconds = matchTime.seconds();
        boolean bobMouthMagic = (int) initSeconds % 2 == 0;

        if(gamepad1.dpadUpWasPressed()){
            redAlliance = false;
        }
        if(gamepad1.dpadDownWasPressed()){
            redAlliance = true;
        }
        // Little enum that changes amount of players
        if(gamepad1.dpadLeftWasPressed() || gamepad1.dpadRightWasPressed())  {
            if(playerAmount == PlayerAmount.TWO_PLAYERS) {
                playerAmount = PlayerAmount.ONE_PLAYERS;
            } else {
                playerAmount = PlayerAmount.TWO_PLAYERS;
            }
        }

        choosePad = playerAmount == PlayerAmount.TWO_PLAYERS ? gamepad2 : gamepad1;
        // I love unreadable code
        noControls = gamepad1.yWasPressed() != noControls;

        if(noControls) {
            Telemetry.log(bobMouthMagic ? "Hi i am BOB :)" : "Hi i am BOB :D");
            Telemetry.log(bobMouthMagic ? "--> Click Y to see controls!" : "->  Click Y to see controls!");
            Telemetry.log(redAlliance ? "YOU ON RED!!!" : "YOU ON BLUE!!!");
            Telemetry.log("Player Amount:", playerAmount);
        } else {
            Telemetry.log(bobMouthMagic ? "--> Click Y to exit!" : "->  Click Y to exit!");
            Telemetry.log("     ~--------------------~     ");
            Telemetry.log("- press B to zero the robot's position -");
            Telemetry.log("- Dpad down and up to lower and raise the arm -");
            Telemetry.log("- left bumper to turn on and off turret tracking -");
            Telemetry.log("- Press Y, while Opmode active, to turn on flywheel -");
            Telemetry.log("I'll finish this later");
        }

        robot.sensor.allianceIndicator(redAlliance);
    }

    @Override
    public void start(){
        Telemetry.addBackend(FtcDashboard.getInstance().getTelemetry());
        if(GlobalPose.CurrentPose == null){
            Pose newPose = new Pose(0,0,0);
            follower.setPose(newPose);
        }else {
            follower.setPose(GlobalPose.CurrentPose);
        }
    }
    @Override
    public void periodic() {

        /// CHOOSE PAD IS NORMALLY GAMEPAD2

        if(gamepad1.bWasPressed()){
            if (redAlliance){
                follower.setPose(GlobalPose.redZeroPose);
            }
            else{
                follower.setPose(GlobalPose.blueZeroPose);
            }
        }

        if(choosePad.dpadDownWasPressed() && !armDown){
            armDown = true;
        } else if (choosePad.dpadUpWasPressed() && armDown) {
            armDown = false;
        }
        if(armDown){
            Arm.mode = Arm.FlowerPiviotState.DOWN;
        }
        else {
            Arm.mode = Arm.FlowerPiviotState.UP;
        }


        if(choosePad.left_bumper){
            Turret.mode = Turret.TurretTracking.ON;
        }
        else{
            Turret.mode = Turret.TurretTracking.OFF;
        }

        if(choosePad.yWasPressed()){
            shooterOn = !shooterOn;
        }

        if(shooterOn){
            Shooter.mode = Shooter.ShooterMode.ON;
            Hood.mode = Hood.HoodState.SHOOT;

        }
       else{
            Shooter.mode = Shooter.ShooterMode.OFF;
            Hood.mode = Hood.HoodState.CLOSE;
        }

        if(choosePad.left_trigger > 0.5 && robot.shooter.ShooterAtVelocityTeleop()) {
            Feeder.mode = Feeder.FeederMode.ON;
            Intake.mode = Intake.IntakeMode.ON;
            BallBlock.mode = BallBlock.BlockState.BALL_GO;
        } else if (choosePad.right_trigger > 0.5) {
            Feeder.mode = Feeder.FeederMode.INTAKE;
            Intake.mode = Intake.IntakeMode.ON;
            BallBlock.mode = BallBlock.BlockState.BALL_NO;
        }
        else if (choosePad == gamepad2 ? choosePad.b : choosePad.a) {
            Intake.mode = Intake.IntakeMode.REVERSE;
            Feeder.mode = Feeder.FeederMode.REVERSE;
        } else{
            Feeder.mode = Feeder.FeederMode.OFF;
            Intake.mode = Intake.IntakeMode.OFF;
            BallBlock.mode = BallBlock.BlockState.BALL_NO;
        }

        if(choosePad.dpadRightWasPressed()){
            GoalPose.blueAudienceX +=2;
            GoalPose.blueRefereeX +=2;
            GoalPose.redaudienceX +=2;
            GoalPose.redRefereeX  +=2;
        } else if (choosePad.dpadLeftWasPressed()) {
            GoalPose.blueAudienceX -=2;
            GoalPose.blueRefereeX -=2;
            GoalPose.redaudienceX -=2;
            GoalPose.redRefereeX  -=2;
        }

        if(gamepad1.xWasPressed()){
            brakeOn =! brakeOn;
        }

        if(brakeOn){
            Brake.mode = Brake.BrakeState.BRAKE;
        }
        else{
            Brake.mode = Brake.BrakeState.NO_BREAK;
        }
        // YO YO YO MY GABE MAN WHAT IS THIS?
        //YO YO YOU MY TOMMY ITS TO DIASABLE DRIVE WHILE BRAKE IS ENGAGAED
        if(Brake.mode == Brake.BrakeState.BRAKE){

        }
        else{
            follower.manual(
                    -gamepad1.left_stick_y,
                    -gamepad1.left_stick_x,
                    -gamepad1.right_stick_x
            );

        }

        if(robot.sensor.fullIntake()){
            Sensor.mode = Sensor.IndicatorLight.FULL;
        }
        else{
            Sensor.mode = Sensor.IndicatorLight.NOT;
        }

        Pose curentPose = follower.pose();

        robot.turret.getTurretAngle(curentPose, redAlliance);
        robot.shooter.refreshShooterVelo(curentPose, redAlliance);
        robot.hood.refreshHoodVal(curentPose, redAlliance);
        follower.update();

        Telemetry.log("arm toggle", armDown);
        Telemetry.log("arm State", Arm.mode);
       Telemetry.log("Arm Position", Arm.servoPosition);
       Telemetry.log("Turret State", Turret.mode);
       Telemetry.log("ball State", BallBlock.mode);
       Telemetry.log("Hood state", Hood.mode);
       Telemetry.log("Shooter State", Shooter.mode);
       Telemetry.log("current Pose", follower.pose());
       Telemetry.log("blue audience goal", blueAudienceGoal);
       Telemetry.log("blue referee goal", blueRefereeGoal);
       Telemetry.log("red Audience goal", blueAudienceGoal);
       Telemetry.log("red Referee goal", redRefereeGoal);

    }

    @Override
    public void end(){

    }
}
