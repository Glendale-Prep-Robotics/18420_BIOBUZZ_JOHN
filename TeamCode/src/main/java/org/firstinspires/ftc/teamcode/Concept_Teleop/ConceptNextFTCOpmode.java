package org.firstinspires.ftc.teamcode.Concept_Teleop;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.Teleop.NextRobot;

import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.opmode.NextTeleop;
@Disabled
@NextTeleop(name = "nextFTC concept Opmode")
public class ConceptNextFTCOpmode extends NextOpMode {
    private final NextRobot robot;
    public ConceptNextFTCOpmode(NextRobot robot) {
        super(robot);
        this.robot = robot;
    }

    @Override
    public void start(){
        robot.drive .startDrive(gamepad1);
    }
}
