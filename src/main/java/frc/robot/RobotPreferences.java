package frc.robot;

import frcteam3255.robotbase.Preferences.SN_DoublePreference;
import frcteam3255.robotbase.Preferences.SN_IntPreference;

public class RobotPreferences {

    //Vision
    public static final SN_DoublePreference visionDriveP = new SN_DoublePreference("visionDriveP", .4);
    public static final SN_DoublePreference visionSteerP = new SN_DoublePreference("visionSpeedP", .04);
    public static final SN_DoublePreference visionGoalArea = new SN_DoublePreference("visionGoalArea", 1.5);
    public static final SN_DoublePreference visionGoalAngle = new SN_DoublePreference("visionGoalAngle", 0);
    public static final SN_DoublePreference visionXTol = new SN_DoublePreference("visionXTol", 0);
    public static final SN_DoublePreference visionAreaTol = new SN_DoublePreference("visionAreaTol", 0);
    public static final SN_DoublePreference ballP = new SN_DoublePreference("ballP", 1/80);
    public static final SN_DoublePreference hLow = new SN_DoublePreference("hLow", 0);
    public static final SN_DoublePreference sLow = new SN_DoublePreference("sLow", 0);
    public static final SN_DoublePreference vLow = new SN_DoublePreference("vLow", 0);
    public static final SN_DoublePreference hHigh = new SN_DoublePreference("hHigh", 255);
    public static final SN_DoublePreference sHigh = new SN_DoublePreference("sHigh", 255);
    public static final SN_DoublePreference vHigh = new SN_DoublePreference("vHigh", 255);

    //Drivetrain
    public static final SN_IntPreference motProfSensorUnitsPerRot = new SN_IntPreference("motProfSensorUnitsPerRot", 659);
    public static final SN_DoublePreference motProfNeutralDeadband = new SN_DoublePreference("motProfNeutralDeadband", 0.001);
    public static final SN_DoublePreference motProfP = new SN_DoublePreference("motProfP", 1.0);
    public static final SN_DoublePreference motProfI = new SN_DoublePreference("motProfI", 0.0);
    public static final SN_DoublePreference motProfD = new SN_DoublePreference("motProfD", 0.0);
    public static final SN_DoublePreference motProfF = new SN_DoublePreference("motProfF", 1023.0/6800.0);
    public static final SN_DoublePreference motProfIz = new SN_DoublePreference("motProfIz", 400);
    public static final SN_DoublePreference motProfPeakOut = new SN_DoublePreference("motProfPeakOut", 1.0);

    //Turret
    public static final SN_DoublePreference susanCountsPerDegree = new SN_DoublePreference("susanCountsPerDegree", 400);
    public static final SN_DoublePreference hoodCountsPerDegree = new SN_DoublePreference("hoodCountsPerDegree", 400);
    public static final SN_DoublePreference susanP = new SN_DoublePreference("susanP", 1.0);
    public static final SN_DoublePreference susanI = new SN_DoublePreference("susanI", 0);
    public static final SN_DoublePreference susanD = new SN_DoublePreference("susanD", 0);
    public static final SN_DoublePreference shooterMaxRPM = new SN_DoublePreference("shooterMaxRPM", 5700);
    public static final SN_DoublePreference shooterP = new SN_DoublePreference("shooterP", 10e-5);
    public static final SN_DoublePreference shooterI = new SN_DoublePreference("shooterI", 1e-6);
    public static final SN_DoublePreference shooterD = new SN_DoublePreference("shooterD", 0);
    public static final SN_DoublePreference shooterFF = new SN_DoublePreference("shooterFF", 0);
    public static final SN_DoublePreference hoodP = new SN_DoublePreference("hoodP", 10e-5);
    public static final SN_DoublePreference hoodI = new SN_DoublePreference("hoodI", 1e-6);
    public static final SN_DoublePreference hoodD = new SN_DoublePreference("hoodD", 0);



}