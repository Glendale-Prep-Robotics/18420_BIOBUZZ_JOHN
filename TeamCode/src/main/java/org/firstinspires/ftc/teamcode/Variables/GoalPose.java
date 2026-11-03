package org.firstinspires.ftc.teamcode.Variables;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.math.Pose;

@Config
public class GoalPose {


    public static int fieldLengthXY = 144;

    public static double redaudienceX = 58;
    public static double redRefereeX = 58;

    public static double redRefereeY = 75;
    public static double redAudienceY = 60;

    public static double blueRefereeX = 85;
    public static double blueAudienceX = 85;

    public static double blueRefereeY = 75;
    public static double blueAudienceY = 60;

    public static Pose redAudienceGoal = new Pose(redaudienceX,redAudienceY);
    public static Pose redRefereeGoal = new Pose(redRefereeX,redRefereeY);
    public static Pose blueRefereeGoal = new Pose(blueAudienceX,blueAudienceY);
    public static Pose blueAudienceGoal = new Pose(blueRefereeX,blueRefereeY);
}
