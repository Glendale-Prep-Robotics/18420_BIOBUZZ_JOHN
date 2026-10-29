package org.firstinspires.ftc.teamcode.Auto;

import static com.pedropathing.api.Paths.line;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.groups.Groups.parallel;
import static com.pedropathing.ivy.groups.Groups.sequential;

import org.firstinspires.ftc.teamcode.Mechanisms.Arm;
import org.firstinspires.ftc.teamcode.Mechanisms.BallBlock;
import org.firstinspires.ftc.teamcode.Mechanisms.Hood;
import org.firstinspires.ftc.teamcode.Mechanisms.Intake;
import org.firstinspires.ftc.teamcode.Mechanisms.Turret;
import org.firstinspires.ftc.teamcode.Teleop.NextRobot;

import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.robot.opmode.NextAutonomous;
import dev.nextftc.robot.opmode.NextOpMode;

@NextAutonomous(name = "BLUE_R")

public class BlueAuto extends NextOpMode {
    private final NextRobot robot;

    public BlueAuto(NextRobot robot) {
        super(robot);
        this.robot = robot;
    }

    public static double HeadingR = 90;
    public static double HeadingA = 270;

    public static int fieldLengthXY = 144;

    public static double ballNo = 0.6;
    public static double ballGo = 0.4;



    private final Pose startPose = new Pose(fieldLengthXY-55,fieldLengthXY-8,Math.toRadians(HeadingR));
    private final Pose firstShootR = new Pose(fieldLengthXY-55,fieldLengthXY-32,Math.toRadians(HeadingR));
    private final Pose grabGarden = new Pose(fieldLengthXY-6,fieldLengthXY-9,Math.toRadians(HeadingR));
    private final Pose firstTransition = new Pose(fieldLengthXY-36,fieldLengthXY-42,Math.toRadians(HeadingR));
    private final Pose secondTransition = new Pose(fieldLengthXY-36,fieldLengthXY-118,Math.toRadians(HeadingA));
    private final Pose secondShootA = new Pose(fieldLengthXY-59,fieldLengthXY-118,Math.toRadians(HeadingA));
    private final Pose grabFlower = new Pose(fieldLengthXY-47,fieldLengthXY-133,Math.toRadians(HeadingA));
    private final Pose endPose = new Pose(fieldLengthXY-15,fieldLengthXY-115,Math.toRadians(HeadingA));

    private Path startGoFirstShootA;
    private Path firstShootAGoGrabGarden;
    private Path grabGardenGoFirstTransition;
    private Path firstTransitionGoSecondTransition;
    private Path secondTransitionGoSecondShootR;
    private Path secondShootRGoGrabFlower;
    private Path grabFlowerGoSecondShootR;
    private Path secondShootRGoEnd;







}