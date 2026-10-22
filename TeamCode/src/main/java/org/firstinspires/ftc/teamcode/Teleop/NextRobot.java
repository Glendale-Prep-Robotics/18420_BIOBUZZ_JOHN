package org.firstinspires.ftc.teamcode.Teleop;
;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.teamcode.Mechanisms.Arm;
import org.firstinspires.ftc.teamcode.Mechanisms.BallBlock;
import org.firstinspires.ftc.teamcode.Mechanisms.DriveTrain;
import org.firstinspires.ftc.teamcode.Mechanisms.Feeder;
import org.firstinspires.ftc.teamcode.Mechanisms.Hood;
import org.firstinspires.ftc.teamcode.Mechanisms.Intake;
import org.firstinspires.ftc.teamcode.Mechanisms.Shooter;
import org.firstinspires.ftc.teamcode.Mechanisms.Turret;

import java.util.Set;

import dev.nextftc.robot.Mechanism;

@Config
public class NextRobot implements dev.nextftc.robot.NextRobot {
      Shooter shooter = new Shooter();
      Turret turret = new Turret();

      Intake intake = new Intake();

      Feeder feeder = new Feeder();

      Hood hood = new Hood();

    BallBlock ballBlock = new BallBlock();

    DriveTrain drive = new DriveTrain();

    Arm arm = new Arm();

    @Override
    public Set<Mechanism> getMechanisms() {
        return Set.of(intake, feeder, arm, drive, shooter, turret, hood, ballBlock);
    }
}
