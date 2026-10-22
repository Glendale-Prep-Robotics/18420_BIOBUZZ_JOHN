package org.firstinspires.ftc.teamcode.Mechanisms;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.Gamepad;

import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.robot.drive.DriveCommands;

@Config
public class DriveTrain implements Mechanism {
    public final NextMotor frontLeft = new NextMotor("fl");
    public final NextMotor frontRight = new NextMotor("fr");
    public final NextMotor backLeft = new NextMotor("bl");
    public final NextMotor backRight = new NextMotor("br");

    public void startDrive(Gamepad gamepad) {
        DriveCommands.mecanumDrive(frontLeft, frontRight, backLeft, backRight, gamepad).schedule();
    }

    @Override
    public void periodic(){
        frontLeft.setDirection(NextMotor.Direction.REVERSE);
        frontRight.setDirection(NextMotor.Direction.FORWARD);
        backLeft.setDirection(NextMotor.Direction.REVERSE);
        backRight.setDirection(NextMotor.Direction.FORWARD);
        //direction are all good

    }
}