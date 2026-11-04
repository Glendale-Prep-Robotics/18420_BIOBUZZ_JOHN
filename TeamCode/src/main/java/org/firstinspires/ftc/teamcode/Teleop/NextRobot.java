package org.firstinspires.ftc.teamcode.Teleop;
;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.teamcode.Mechanisms.Arm;
import org.firstinspires.ftc.teamcode.Mechanisms.BallBlock;
import org.firstinspires.ftc.teamcode.Mechanisms.Brake;
import org.firstinspires.ftc.teamcode.Mechanisms.DriveTrain;
import org.firstinspires.ftc.teamcode.Mechanisms.Feeder;
import org.firstinspires.ftc.teamcode.Mechanisms.Hood;
import org.firstinspires.ftc.teamcode.Mechanisms.Intake;
import org.firstinspires.ftc.teamcode.Mechanisms.Sensor;
import org.firstinspires.ftc.teamcode.Mechanisms.Shooter;
import org.firstinspires.ftc.teamcode.Mechanisms.Turret;

import java.util.Set;

import dev.nextftc.robot.Mechanism;

@Config
public class NextRobot implements dev.nextftc.robot.NextRobot {
      public Shooter shooter = new Shooter();

      public Turret turret = new Turret();

      public Intake intake = new Intake();

      public Feeder feeder = new Feeder();

      public Hood hood = new Hood();

    public BallBlock ballBlock = new BallBlock();

    public DriveTrain drive = new DriveTrain();

    public Arm arm = new Arm();

    public Brake brake = new Brake();

    public Sensor sensor = new Sensor();

    @Override
    public Set<Mechanism> getMechanisms() {
        return Set.of(intake, feeder, arm, drive, shooter, turret, hood, ballBlock, brake, sensor);
    }
}
//Gabe is good at programming