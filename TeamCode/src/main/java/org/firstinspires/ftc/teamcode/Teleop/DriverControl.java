package org.firstinspires.ftc.teamcode.Teleop;


import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.teamcode.Mechanisms.Arm;
import org.firstinspires.ftc.teamcode.Mechanisms.BallBlock;
import org.firstinspires.ftc.teamcode.Mechanisms.Feeder;
import org.firstinspires.ftc.teamcode.Mechanisms.Hood;
import org.firstinspires.ftc.teamcode.Mechanisms.Intake;
import org.firstinspires.ftc.teamcode.Mechanisms.Shooter;
import org.firstinspires.ftc.teamcode.Mechanisms.Turret;

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

    public boolean ballGo = false;

    public boolean hoodOpen = false;


    @Override
    public void disabledPeriodic() {

    }

    @Override
    public void start(){
        Telemetry.addBackend(FtcDashboard.getInstance().getTelemetry());
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
       if(gamepad1.right_bumper){
           Intake.mode = Intake.IntakeMode.ON;
           Feeder.mode = Feeder.FeederMode.ON;
       }

       else{
           Intake.mode = Intake.IntakeMode.OFF;
           Feeder.mode = Feeder.FeederMode.OFF;
       }

       if(gamepad1.bWasPressed()){
           turretTrack =! turretTrack;
       }

       if(turretTrack){
           Turret.mode = Turret.TurretTracking.ON;
       }
       else{
           Turret.mode = Turret.TurretTracking.OFF;
       }

       if(gamepad1.yWasPressed()){
           ballGo =! ballGo;
       }
       if(ballGo){
           BallBlock.mode = BallBlock.BlockState.BALL_GO;
       }
       else{
           BallBlock.mode = BallBlock.BlockState.BALL_NO;
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

       Telemetry.log("arm toggle", armDown);
       Telemetry.log("arm State", Arm.mode);
       Telemetry.log("Arm Position", Arm.servoPosition);
       Telemetry.log("TurretState", Turret.mode);
       Telemetry.log("ball State", BallBlock.mode);
       Telemetry.log("Hood state", Hood.mode);

       Telemetry.update();


    }

    @Override
    public void end(){

    }
}
