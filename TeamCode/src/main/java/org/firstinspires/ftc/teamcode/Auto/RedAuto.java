package org.firstinspires.ftc.teamcode.Auto;

import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Mechanisms.Arm;
import org.firstinspires.ftc.teamcode.Teleop.NextRobot;

import dev.nextftc.robot.opmode.NextAutonomous;
import dev.nextftc.robot.opmode.NextOpMode;

@NextAutonomous(name = "RED_AUTO")
public class RedAuto extends NextOpMode {
    private final NextRobot robot;

    public RedAuto(NextRobot robot) {
        super(robot);
        this.robot = robot;
    }

    public static double HeadingR = 90;
    public static double HeadingA = 270;

    public static int fieldLengthXY = 144;

    public static double ballNo = 0.6;
    public static double ballGo = 0.4;

    private final Pose startPose = new Pose(55,8,Math.toRadians(270));
    private final Pose firstShootA = new Pose(55,32,Math.toRadians(270));
    private final Pose grabGarden = new Pose(6,9,Math.toRadians(270));
    private final Pose firstTransition = new Pose(36,42,Math.toRadians(270));
    private final Pose secondTransition = new Pose(36,118,Math.toRadians(90));
    private final Pose secondShootR = new Pose(59,118,Math.toRadians(90));
    private final Pose grabFlower = new Pose(47,133,Math.toRadians(90));
    private final Pose endPose = new Pose(15,115,Math.toRadians(90));


    @Override
    public void periodic() {

    }

}
