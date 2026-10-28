package org.firstinspires.ftc.teamcode.Teleop;


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

    public boolean armDown = false;

    public boolean turretTrack = false;

    public boolean hoodOpen = false;

    public boolean redAllience = false;

    private Follower follower = Constants.create(hardwareMap);


    @Override
    public void disabledPeriodic() {
    }

    @Override
    public void start(){
        Telemetry.addBackend(FtcDashboard.getInstance().getTelemetry());
        follower.setPose(GlobalPose.CurrentPose);
        robot.drive.startDrive(gamepad1);
    }
    @Override
    public void periodic() {

        if(gamepad1.aWasPressed()){
            armDown =!armDown;
        }

        if(armDown){
            Arm.mode = Arm.FlowerPiviotState.DOWN;
        }
        else {
            Arm.mode = Arm.FlowerPiviotState.UP;
        }



        if(gamepad1.bWasPressed()){
            turretTrack =! turretTrack;
        }

        if(gamepad1.y){
            Turret.mode = Turret.TurretTracking.ON;
        }
        else{
            Turret.mode = Turret.TurretTracking.OFF;
        }

        if(gamepad1.xWasPressed()){
            hoodOpen =!hoodOpen;
        }

        if(hoodOpen){
            Hood.mode = Hood.HoodState.SHOOT;
        }
       else{
            Hood.mode = Hood.HoodState.CLOSE;
        }

        if(gamepad1.left_trigger > 0.5){
            Shooter.mode = Shooter.ShooterMode.ON;

        }
       else{
            Shooter.mode = Shooter.ShooterMode.OFF;
        }

        if(gamepad1.left_bumper && !gamepad1.right_bumper){
            Feeder.mode = Feeder.FeederMode.ON;
            Intake.mode = Intake.IntakeMode.ON;
            BallBlock.mode = BallBlock.BlockState.BALL_GO;
        } else if (gamepad1.right_bumper) {
            Feeder.mode = Feeder.FeederMode.INTAKE;
            Intake.mode = Intake.IntakeMode.ON;
            BallBlock.mode = BallBlock.BlockState.BALL_NO;
        }else{
            Feeder.mode = Feeder.FeederMode.OFF;
            Intake.mode = Intake.IntakeMode.OFF;
            BallBlock.mode = BallBlock.BlockState.BALL_NO;
        }

        follower.manual(
                -gamepad1.left_stick_y,
                -gamepad1.left_stick_x,
                -gamepad1.right_stick_x
        );
        follower.update();

        robot.turret.getTurretAngle(follower.pose(),redAllience);


        Telemetry.log("arm toggle", armDown);
        Telemetry.log("arm State", Arm.mode);
       Telemetry.log("Arm Position", Arm.servoPosition);
       Telemetry.log("Turret State", Turret.mode);
       Telemetry.log("ball State", BallBlock.mode);
       Telemetry.log("Hood state", Hood.mode);
       Telemetry.log("Shooter State", Shooter.mode);
       Telemetry.log("current Mode", follower.pose());

       Telemetry.update();


    }

    @Override
    public void end(){

    }
}
