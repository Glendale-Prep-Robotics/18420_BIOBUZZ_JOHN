package org.firstinspires.ftc.teamcode.Teleop;


import static org.firstinspires.ftc.teamcode.Variables.GlobalPose.redAllience;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.follower.Follower;

import org.firstinspires.ftc.teamcode.Mechanisms.Arm;
import org.firstinspires.ftc.teamcode.Mechanisms.BallBlock;
import org.firstinspires.ftc.teamcode.Mechanisms.Feeder;
import org.firstinspires.ftc.teamcode.Mechanisms.Hood;
import org.firstinspires.ftc.teamcode.Mechanisms.Intake;
import org.firstinspires.ftc.teamcode.Mechanisms.Shooter;
import org.firstinspires.ftc.teamcode.Mechanisms.Turret;
import org.firstinspires.ftc.teamcode.Variables.GlobalPose;
import org.firstinspires.ftc.teamcode.Variables.GoalPose;
import org.firstinspires.ftc.teamcode.pedro.Constants;

import dev.nextftc.robot.Telemetry;
import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.opmode.NextTeleop;
import dev.nextftc.robot.triggers.CommandGamepad;

@Config
@NextTeleop(name = "DriverControl")
public class DriverControl extends NextOpMode {
    private final NextRobot robot;

    public DriverControl(NextRobot robot) {
        super(robot);
        this.robot = robot;
    }

    CommandGamepad gp1 = new CommandGamepad(gamepad1);
    CommandGamepad gp2 = new CommandGamepad(gamepad2);

    public enum PlayerAmount {
        TWO_PLAYERS,
        ONE_PLAYERS
    }
    PlayerAmount playerAmount = PlayerAmount.TWO_PLAYERS;
    Gamepad choosePad;

    public boolean armDown = false;

    public boolean shooterOn = false;

    private Follower follower = Constants.create(hardwareMap);

    public enum ShooterOnMode{
        ON, OFF
    }

    public ShooterOnMode shooter = ShooterOnMode.OFF;


    @Override
    public void disabledPeriodic() {
        if(gamepad1.dpadUpWasPressed()){
            redAllience  = false;
        }
        if(gamepad1.dpadDownWasPressed()){
            redAllience = true;
        }
        // Little enum that changes amount of players
        if(gamepad1.dpadLeftWasPressed() || gamepad1.dpadRightWasPressed())  {
            if(playerAmount == PlayerAmount.TWO_PLAYERS) {
                playerAmount = PlayerAmount.ONE_PLAYERS;
            } else {
                playerAmount = PlayerAmount.TWO_PLAYERS;
            }
        }

        String colorMessage = redAllience ? "YOU ON RED!!!" : "YOU ON BLUE!!!";
        Telemetry.log(colorMessage);

        //YO YO YO MY GABE MAN YOU CAN CHANGE IF YOU WANT
        //Telemetry.log("red alliance boolean", redAllience);

        Telemetry.log("Player Amount:",playerAmount);
        choosePad = playerAmount == PlayerAmount.TWO_PLAYERS ? gamepad2 : gamepad1;
    }

    @Override
    public void start(){
        Telemetry.addBackend(FtcDashboard.getInstance().getTelemetry());
        follower.setPose(GlobalPose.CurrentPose);
        robot.drive.startDrive(gamepad1);
    }
    @Override
    public void periodic() {

        /// CHOOSEPAD IS NORMALLY GAMEPAD2

        if(gamepad1.bWasPressed()){
            if (redAllience){
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
            shooterOn =!shooterOn;
        }

        if(shooterOn){
            Shooter.mode = Shooter.ShooterMode.ON;
            Hood.mode = Hood.HoodState.SHOOT;
            shooter = ShooterOnMode.ON;

        }
       else{
            Shooter.mode = Shooter.ShooterMode.OFF;
            Hood.mode = Hood.HoodState.CLOSE;
            shooter = ShooterOnMode.OFF;
        }

        if(choosePad.left_trigger > 0.5) {
            switch (shooter) {
                case ON:
                    Feeder.mode = Feeder.FeederMode.ON;
                    Intake.mode = Intake.IntakeMode.ON;
                    BallBlock.mode = BallBlock.BlockState.BALL_GO;
                    break;

                case OFF:
                    Feeder.mode = Feeder.FeederMode.INTAKE;
                    Intake.mode = Intake.IntakeMode.ON;
                    BallBlock.mode = BallBlock.BlockState.BALL_NO;
                    break;
            }
            //why is this else if?

        }else if (choosePad == gamepad2 ? choosePad.b : choosePad.a) {
            Intake.mode = Intake.IntakeMode.REVERSE;
            Feeder.mode = Feeder.FeederMode.REVERSE;
        } else{
            Feeder.mode = Feeder.FeederMode.OFF;
            Intake.mode = Intake.IntakeMode.OFF;
            BallBlock.mode = BallBlock.BlockState.BALL_NO;
        }

        if(gamepad2.dpadRightWasPressed()){

            GoalPose.blueAudienceX +=2;
            GoalPose.blueRefereeX +=2;
            GoalPose.redaudienceX +=2;
            GoalPose.redRefereeX  +=2;
        } else if (gamepad2.dpadLeftWasPressed()) {
            GoalPose.blueAudienceX -=2;
            GoalPose.blueRefereeX -=2;
            GoalPose.redaudienceX -=2;
            GoalPose.redRefereeX  -=2;
        }

        robot.turret.getTurretAngle(follower.pose(),redAllience);

        follower.manual(
                -gamepad1.left_stick_y,
                -gamepad1.left_stick_x,
                -gamepad1.right_stick_x
        );

        follower.update();

        Telemetry.log("red Allience Boolean", redAllience);
        Telemetry.log("arm toggle", armDown);
        Telemetry.log("arm State", Arm.mode);
       Telemetry.log("Arm Position", Arm.servoPosition);
       Telemetry.log("Turret State", Turret.mode);
       Telemetry.log("ball State", BallBlock.mode);
       Telemetry.log("Hood state", Hood.mode);
       Telemetry.log("Shooter State", Shooter.mode);
       Telemetry.log("current Pose", follower.pose());

    }

    @Override
    public void end(){

    }
}
