package frc.robot;

import frcteam3255.robotbase.Preferences.SN_DoublePreference;
import frcteam3255.robotbase.Preferences.SN_IntPreference;

public class RobotPreferences {
        // Vision
        public static final SN_DoublePreference visionDriveP = new SN_DoublePreference("visionDriveP", .4);
        public static final SN_DoublePreference innerHoleScaler = new SN_DoublePreference("innerHoleScaler", .98);
        public static final SN_DoublePreference visionSteerP = new SN_DoublePreference("visionSpeedP", .04);
        public static final SN_DoublePreference visionGoalArea = new SN_DoublePreference("visionGoalArea", 1.5);
        public static final SN_DoublePreference visionXTol = new SN_DoublePreference("visionXTol", 1);
        public static final SN_DoublePreference visionAreaTol = new SN_DoublePreference("visionAreaTol", .5);
        public static final SN_IntPreference visionTimeout = new SN_IntPreference("visionTimeout", 5);
        public static final SN_DoublePreference ballP = new SN_DoublePreference("ballP", 1 / 80);
        public static final SN_DoublePreference hLow = new SN_DoublePreference("hLow", 0);
        public static final SN_DoublePreference sLow = new SN_DoublePreference("sLow", 0);
        public static final SN_DoublePreference vLow = new SN_DoublePreference("vLow", 0);
        public static final SN_DoublePreference hHigh = new SN_DoublePreference("hHigh", 255);
        public static final SN_DoublePreference sHigh = new SN_DoublePreference("sHigh", 255);
        public static final SN_DoublePreference vHigh = new SN_DoublePreference("vHigh", 255);
        public static final SN_IntPreference ballCount = new SN_IntPreference("ballCount", 2);

        // Drivetrain
        public static final SN_IntPreference motProfSensorUnitsPerFt = new SN_IntPreference("motProfSensorUnitsPerFt",
                        11700);
        public static final SN_DoublePreference motProfNeutralDeadband = new SN_DoublePreference(
                        "motProfNeutralDeadband", 0.001);
        public static final SN_DoublePreference motProfP = new SN_DoublePreference("motProfP", 0.1);
        public static final SN_DoublePreference motProfI = new SN_DoublePreference("motProfI", 0.0);
        public static final SN_DoublePreference motProfD = new SN_DoublePreference("motProfD", 0.0);
        // public static final SN_DoublePreference motProfF = new
        // SN_DoublePreference("motProfF", 1023.0/6800.0);
        public static final SN_DoublePreference motProfF = new SN_DoublePreference("motProfF", 0);
        public static final SN_DoublePreference motProfIz = new SN_DoublePreference("motProfIz", 400);
        public static final SN_DoublePreference motProfPeakOut = new SN_DoublePreference("motProfPeakOut", 1.0);
        public static final SN_DoublePreference driveDistance = new SN_DoublePreference("driveDistance", 5.0);
        public static final SN_DoublePreference driveDistance2 = new SN_DoublePreference("driveDistance2", -5.0);
        public static final SN_IntPreference ballTimeout = new SN_IntPreference("ballTimeout", 20);
        // Climber
        public static final SN_DoublePreference climberCountsPerInches = new SN_DoublePreference(
                        "climberCountsPerInches", 400);
        public static final SN_DoublePreference climberP = new SN_DoublePreference("climberP", 1.0);
        public static final SN_DoublePreference climberI = new SN_DoublePreference("climberI", 0.0);
        public static final SN_DoublePreference climberD = new SN_DoublePreference("climberD", 0.0);
        public static final SN_DoublePreference climberF = new SN_DoublePreference("climberF", 1023.0 / 6800.0);
        public static final SN_DoublePreference climberIz = new SN_DoublePreference("climberIz", 400);
        public static final SN_IntPreference climberHeight = new SN_IntPreference("climberHeight", 30);
        public static final SN_DoublePreference climberWinchSpeed = new SN_DoublePreference("climberWinchSpeed", -.5);
        public static final SN_DoublePreference climberUpSpeed = new SN_DoublePreference("climberUpSpeed", .5);
        public static final SN_DoublePreference climberDownSpeed = new SN_DoublePreference("climberDownSpeed", -.5);
        // Turret
        public static final SN_DoublePreference susanCountsPerDegree = new SN_DoublePreference("susanCountsPerDegree",
                        84);
        public static final SN_DoublePreference hoodCountsPerDegree = new SN_DoublePreference("hoodCountsPerDegree",
                        87);
        public static final SN_DoublePreference susanVisionP = new SN_DoublePreference("susanVisionP", 0.0025 * .2);
        public static final SN_DoublePreference susanSpeedBackwards = new SN_DoublePreference("susanSpeedBackwards",
                        -0.2);
        public static final SN_DoublePreference hoodVisionP = new SN_DoublePreference("susanVisionP", 90 / 20);
        public static final SN_DoublePreference susanP = new SN_DoublePreference("susanP", 0.2);
        public static final SN_DoublePreference susanI = new SN_DoublePreference("susanI", 0);
        public static final SN_DoublePreference susanD = new SN_DoublePreference("susanD", 0);
        public static final SN_DoublePreference shooterMaxRPM = new SN_DoublePreference("shooterMaxRPM", 2000);
        public static final SN_DoublePreference shooterP = new SN_DoublePreference("shooterP", 10e-5);
        public static final SN_DoublePreference shooterI = new SN_DoublePreference("shooterI", 1e-6);
        public static final SN_DoublePreference shooterD = new SN_DoublePreference("shooterD", 0);
        public static final SN_DoublePreference shooterFF = new SN_DoublePreference("shooterFF", 0);
        public static final SN_DoublePreference hoodP = new SN_DoublePreference("hoodP", .3);
        public static final SN_DoublePreference hoodI = new SN_DoublePreference("hoodI", 0);
        public static final SN_DoublePreference hoodD = new SN_DoublePreference("hoodD", 0);
        public static final SN_DoublePreference shooterFullSpeed = new SN_DoublePreference("shooterFullSpeed", 1);
        public static final SN_DoublePreference shooterNoSpeed = new SN_DoublePreference("shooterNoSpeed", 0);
        public static final SN_DoublePreference shooterTolerance = new SN_DoublePreference("shooterToleranceSpeed",
                        100);
        // ControlPanel
        public static final SN_IntPreference spinCount = new SN_IntPreference("spinCount", 1);

        // Intake
        public static final SN_DoublePreference collectorSpeed = new SN_DoublePreference("collectorSpeed", .6);
        // reds
        public static final SN_DoublePreference redsRedLow = new SN_DoublePreference("redsRedLow", 100);
        public static final SN_DoublePreference redsRedHigh = new SN_DoublePreference("redsRedHigh", 130);
        public static final SN_DoublePreference redsGreenLow = new SN_DoublePreference("redsGreenLow", 85);
        public static final SN_DoublePreference redsGreenHigh = new SN_DoublePreference("redsGreenHigh", 105);
        public static final SN_DoublePreference redsBlueLow = new SN_DoublePreference("redsBlueLow", 25);
        public static final SN_DoublePreference redsBlueHigh = new SN_DoublePreference("redsBlueHigh", 55);
        // greens
        public static final SN_DoublePreference greensRedLow = new SN_DoublePreference("greensRedLow", 15);
        public static final SN_DoublePreference greensRedHigh = new SN_DoublePreference("greensRedHigh", 90);
        public static final SN_DoublePreference greensGreenLow = new SN_DoublePreference("greensGreenLow", 60);
        public static final SN_DoublePreference greensGreenHigh = new SN_DoublePreference("greensGreenHigh", 255);
        public static final SN_DoublePreference greensBlueLow = new SN_DoublePreference("greensBlueLow", 20);
        public static final SN_DoublePreference greensBlueHigh = new SN_DoublePreference("greensBlueHigh", 100);
        // blues
        public static final SN_DoublePreference bluesRedLow = new SN_DoublePreference("bluesRedLow", 0);
        public static final SN_DoublePreference bluesRedHigh = new SN_DoublePreference("bluesRedHigh", 100);
        public static final SN_DoublePreference bluesGreenLow = new SN_DoublePreference("bluesGreenLow", 50);
        public static final SN_DoublePreference bluesGreenHigh = new SN_DoublePreference("bluesGreenHigh", 204);
        public static final SN_DoublePreference bluesBlueLow = new SN_DoublePreference("bluesBlueLow", 55);
        public static final SN_DoublePreference bluesBlueHigh = new SN_DoublePreference("bluesBlueHigh", 155);
        // yellows
        public static final SN_DoublePreference yellowsRedLow = new SN_DoublePreference("yellowsRedLow", 25);
        public static final SN_DoublePreference yellowsRedHigh = new SN_DoublePreference("yellowsRedHigh", 165);
        public static final SN_DoublePreference yellowsGreenLow = new SN_DoublePreference("yellowsGreenLow", 55);
        public static final SN_DoublePreference yellowsGreenHigh = new SN_DoublePreference("yellowsGreenHigh", 205);
        public static final SN_DoublePreference yellowsBlueLow = new SN_DoublePreference("yellowsBlueLow", 0);
        public static final SN_DoublePreference yellowsBlueHigh = new SN_DoublePreference("yellowsBlueHigh", 120);
        // speed
        public static final SN_DoublePreference controlPanelSpinSpeed = new SN_DoublePreference("controlPanelSpinSpeed",
                        .5);
}