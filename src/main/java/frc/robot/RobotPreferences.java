package frc.robot;

import frcteam3255.robotbase.Preferences.SN_DoublePreference;
import frcteam3255.robotbase.Preferences.SN_IntPreference;

public class RobotPreferences {
        // Auto
        public static final SN_DoublePreference auto1SusanPos = new SN_DoublePreference("auto1SusanPos", 0);
        public static final SN_DoublePreference auto1HoodPos = new SN_DoublePreference("auto1HoodPos", 0);
        public static final SN_DoublePreference auto1Delay = new SN_DoublePreference("auto1Delay", 0);
        public static final SN_DoublePreference auto2SusanPos = new SN_DoublePreference("auto2SusanPos", 0);
        public static final SN_DoublePreference auto2HoodPos = new SN_DoublePreference("auto2HoodPos", 0);
        public static final SN_DoublePreference auto2Delay = new SN_DoublePreference("auto2Delay", 0);
        public static final SN_DoublePreference failHoodPos = new SN_DoublePreference("failHoodPos", 50);
        public static final SN_DoublePreference failTurretPos = new SN_DoublePreference("failTurretPos", 20);
        public static final SN_DoublePreference ballNewness = new SN_DoublePreference("ballNewness", 0);
        public static final SN_IntPreference toPosTimeout = new SN_IntPreference("toPosTimeout", 50);
        public static final SN_IntPreference numToShoot = new SN_IntPreference("numToShoot", 2);

        // Vision
        public static final SN_DoublePreference visionDriveP = new SN_DoublePreference("visionDriveP", .4);
        public static final SN_DoublePreference innerHoleScaler = new SN_DoublePreference("innerHoleScaler", .98);
        public static final SN_DoublePreference visionSteerP = new SN_DoublePreference("visionSpeedP", .04);
        public static final SN_DoublePreference visionGoalArea = new SN_DoublePreference("visionGoalArea", 1.5);
        public static final SN_DoublePreference visionXTol = new SN_DoublePreference("visionXTol", 1);
        public static final SN_IntPreference visionTimeout = new SN_IntPreference("visionTimeout", 5);
        public static final SN_DoublePreference ballP = new SN_DoublePreference("ballP", .005);
        public static final SN_DoublePreference hLow = new SN_DoublePreference("hLow", 10);
        public static final SN_DoublePreference sLow = new SN_DoublePreference("sLow", 100);
        public static final SN_DoublePreference vLow = new SN_DoublePreference("vLow", 0);
        public static final SN_DoublePreference hHigh = new SN_DoublePreference("hHigh", 100);
        public static final SN_DoublePreference sHigh = new SN_DoublePreference("sHigh", 255);
        public static final SN_DoublePreference vHigh = new SN_DoublePreference("vHigh", 255);
        public static final SN_IntPreference ballCount = new SN_IntPreference("ballCount", 2);
        public static final SN_DoublePreference ballSpeed = new SN_DoublePreference("ballSpeed", .5);

        // Drivetrain
        public static final SN_IntPreference motProfSensorUnitsPerFt = new SN_IntPreference("motProfSensorUnitsPerFt",
                        11700);
        public static final SN_DoublePreference motProfNeutralDeadband = new SN_DoublePreference(
                        "motProfNeutralDeadband", 0.001);
        public static final SN_DoublePreference motProfP = new SN_DoublePreference("motProfP", 0.1);
        public static final SN_DoublePreference motProfI = new SN_DoublePreference("motProfI", 0.0);
        public static final SN_DoublePreference motProfD = new SN_DoublePreference("motProfD", 0.0);
        public static final SN_DoublePreference motProfF = new SN_DoublePreference("motProfF", 1023.0 / 6800.0);
        public static final SN_DoublePreference motProfIz = new SN_DoublePreference("motProfIz", 400);
        public static final SN_DoublePreference motProfPeakOut = new SN_DoublePreference("motProfPeakOut", 1.0);
        public static final SN_DoublePreference drivetrainTurnDeadband = new SN_DoublePreference(
                        "drivetrainTurnDeadband", .2);
        public static final SN_DoublePreference drivetrainDeadband = new SN_DoublePreference("drivetrainDeadband", 0);
        public static final SN_DoublePreference drivetrainHighSpeed = new SN_DoublePreference("drivetrainHighSpeed", 1);
        public static final SN_DoublePreference drivetrainHighTurnSpeed = new SN_DoublePreference("drivetrainTurnSpeed",
                        1);
        public static final SN_DoublePreference drivetrainLowSpeed = new SN_DoublePreference("drivetrainLowSpeed", .4);
        public static final SN_DoublePreference drivetrainTurnLowSpeed = new SN_DoublePreference(
                        "drivetrainTurnLowSpeed", .2);
        public static final SN_DoublePreference drivetrainRampTime = new SN_DoublePreference("drivetrainRampTime", 0);
        public static final SN_DoublePreference drivetrainLowTurnSpeed = new SN_DoublePreference("drivetrainTurnSpeed",
                        .2);
        public static final SN_DoublePreference ballTimeout = new SN_DoublePreference("ballTimeout", 5);
        // Climber
        public static final SN_DoublePreference climberCountsPerInches = new SN_DoublePreference(
                        "climberCountsPerInches", 400);
        public static final SN_DoublePreference climberP = new SN_DoublePreference("climberP", 1.0);
        public static final SN_DoublePreference climberI = new SN_DoublePreference("climberI", 0.0);
        public static final SN_DoublePreference climberD = new SN_DoublePreference("climberD", 0.0);
        public static final SN_DoublePreference climberF = new SN_DoublePreference("climberF", 1023.0 / 6800.0);
        public static final SN_DoublePreference climberIz = new SN_DoublePreference("climberIz", 400);
        public static final SN_IntPreference climberHeight = new SN_IntPreference("climberHeight", 30);
        public static final SN_DoublePreference climberWinchSpeed = new SN_DoublePreference("climberWinchSpeed", .5);
        public static final SN_DoublePreference climberUpSpeed = new SN_DoublePreference("climberUpSpeed", .5);
        public static final SN_DoublePreference climberDownSpeed = new SN_DoublePreference("climberDownSpeed", -.5);
        // Turret
        public static final SN_DoublePreference susanMinDegree = new SN_DoublePreference("susanMinDegree", -90);
        public static final SN_DoublePreference susanMaxDegree = new SN_DoublePreference("susanMaxDegree", 90);
        public static final SN_DoublePreference susanHardstopTol = new SN_DoublePreference("susanHardstopTol", 10);
        public static final SN_DoublePreference susanTol = new SN_DoublePreference("susanTol", .5);
        public static final SN_DoublePreference shootDelay = new SN_DoublePreference("shootDelay", .01);
        public static final SN_DoublePreference hoodTol = new SN_DoublePreference("hoodTol", .5);

        public static final SN_DoublePreference susanCountsPerDegree = new SN_DoublePreference("susanCountsPerDegree",
                        84);
        public static final SN_DoublePreference hoodCountsPerDegree = new SN_DoublePreference("hoodCountsPerDegree",
                        40);
        public static final SN_DoublePreference hoodTestDegree = new SN_DoublePreference("hoodTestDegree", 40);
        public static final SN_DoublePreference susanVisionP = new SN_DoublePreference("susanVisionP", 0.13);
        public static final SN_DoublePreference susanSpeedBackwards = new SN_DoublePreference("susanSpeedBackwards",
                        -0.2);
        public static final SN_DoublePreference turretManualSpeed = new SN_DoublePreference("turretManualSpeed", 0.5);
        public static final SN_DoublePreference hoodVisionP = new SN_DoublePreference("hoodVisionP", .01);
        public static final SN_DoublePreference susanMaxSpeed = new SN_DoublePreference("susanMaxSpeed", 0.3);
        public static final SN_DoublePreference hoodMaxSpeed = new SN_DoublePreference("hoodMaxSpeed", 0.2);
        public static final SN_DoublePreference hoodMinDegree = new SN_DoublePreference("hoodMinDegree", 10);
        public static final SN_DoublePreference hoodMaxDegree = new SN_DoublePreference("hoodMaxDegree", 80);
        public static final SN_DoublePreference susanP = new SN_DoublePreference("susanP", 0.2);
        public static final SN_DoublePreference susanI = new SN_DoublePreference("susanI", 0);
        public static final SN_DoublePreference susanD = new SN_DoublePreference("susanD", 0);
        public static final SN_DoublePreference susanF = new SN_DoublePreference("susanF", 0.01);
        public static final SN_DoublePreference shooterP = new SN_DoublePreference("shooterP", 0.005);
        public static final SN_DoublePreference shooterI = new SN_DoublePreference("shooterI", 0);
        public static final SN_DoublePreference shooterD = new SN_DoublePreference("shooterD", 0);
        public static final SN_DoublePreference shooterFF = new SN_DoublePreference("shooterFF", 1.71E-4);
        public static final SN_DoublePreference hoodP = new SN_DoublePreference("hoodP", 10);
        public static final SN_DoublePreference hoodI = new SN_DoublePreference("hoodI", 0);
        public static final SN_DoublePreference hoodD = new SN_DoublePreference("hoodD", 0);
        public static final SN_DoublePreference spinupTimeout = new SN_DoublePreference("spinupTimeout", 5);
        public static final SN_DoublePreference stagingTimeout = new SN_DoublePreference("stagingTimeout", 1);
        public static final SN_DoublePreference shootingTimeout = new SN_DoublePreference("shootingTimeout", 1);
        public static final SN_DoublePreference shooterFullSpeed = new SN_DoublePreference("shooterFullSpeed", 1);
        public static final SN_DoublePreference shooterNoSpeed = new SN_DoublePreference("shooterNoSpeed", 0);
        public static final SN_DoublePreference shooterTolerance = new SN_DoublePreference("shooterToleranceSpeed",
                        200);

        // Hood positions
        public static final SN_DoublePreference shooterMaxRPM = new SN_DoublePreference("shooterMaxRPM", 5000);
        public static final SN_DoublePreference shooterCloseRPM = new SN_DoublePreference("shooterCloseRPM", 4000);
        public static final SN_DoublePreference shooterWallLowRPM = new SN_DoublePreference("shooterWallLowRPM", 1500);
        public static final SN_DoublePreference shooterWallHighRPM = new SN_DoublePreference("shooterWallHighRPM",
                        2500);
        public static final SN_DoublePreference nudgeHoodUp = new SN_DoublePreference("nudgeHoodUp", 2);
        public static final SN_DoublePreference nudgeHoodDown = new SN_DoublePreference("nudgeHoodDown", -2);
        public static final SN_DoublePreference hoodMiddleTrench = new SN_DoublePreference("hoodMiddleTrench", 62);
        public static final SN_DoublePreference hoodFrontTrench = new SN_DoublePreference("hoodFrontTrench", 58);
        public static final SN_DoublePreference hoodInitialization = new SN_DoublePreference("hoodInitialization", 52);
        public static final SN_DoublePreference hoodClose = new SN_DoublePreference("hoodClose", 42);
        public static final SN_DoublePreference hoodWallLow = new SN_DoublePreference("hoodWallLow", 80);
        public static final SN_DoublePreference hoodWallHigh = new SN_DoublePreference("hoodWallHigh", 10);
        public static final SN_DoublePreference shooterTimeout = new SN_DoublePreference("shooterTimeout", 5);

        // ControlPanel
        public static final SN_DoublePreference spinCount = new SN_DoublePreference("spinCount", 3.5);
        public static final SN_IntPreference numColorSamples = new SN_IntPreference("numColorSamples", 1);
        public static final SN_DoublePreference spinSpeedRight = new SN_DoublePreference("spinSpeedRight", 1);
        public static final SN_DoublePreference spinSpeedLeft = new SN_DoublePreference("spinSpeedLeft", -1);

        // Intake
        public static final SN_DoublePreference servoPosRetract = new SN_DoublePreference("servoPosRetract", 0);
        public static final SN_DoublePreference servoPosDeploy = new SN_DoublePreference("servoPosDeploy", 1);
        public static final SN_DoublePreference collectorSpeed = new SN_DoublePreference("collectorSpeed", .3);
        public static final SN_DoublePreference turretGateSpeed = new SN_DoublePreference("turretGateSpeed", 1);
        public static final SN_DoublePreference initialGateSpeed = new SN_DoublePreference("initialGateSpeed", 1);
        // reds
        public static final SN_DoublePreference redsRedLow = new SN_DoublePreference("redsRedLow", .2);
        public static final SN_DoublePreference redsRedHigh = new SN_DoublePreference("redsRedHigh", .6);
        public static final SN_DoublePreference redsGreenLow = new SN_DoublePreference("redsGreenLow", .3);
        public static final SN_DoublePreference redsGreenHigh = new SN_DoublePreference("redsGreenHigh", .45);
        public static final SN_DoublePreference redsBlueLow = new SN_DoublePreference("redsBlueLow", .1);
        public static final SN_DoublePreference redsBlueHigh = new SN_DoublePreference("redsBlueHigh", .27);
        // greens
        public static final SN_DoublePreference greensRedLow = new SN_DoublePreference("greensRedLow", .15);
        public static final SN_DoublePreference greensRedHigh = new SN_DoublePreference("greensRedHigh", .3);
        public static final SN_DoublePreference greensGreenLow = new SN_DoublePreference("greensGreenLow", .48);
        public static final SN_DoublePreference greensGreenHigh = new SN_DoublePreference("greensGreenHigh", .6);
        public static final SN_DoublePreference greensBlueLow = new SN_DoublePreference("greensBlueLow", .15);
        public static final SN_DoublePreference greensBlueHigh = new SN_DoublePreference("greensBlueHigh", .35);
        // blues
        public static final SN_DoublePreference bluesRedLow = new SN_DoublePreference("bluesRedLow", .15);
        public static final SN_DoublePreference bluesRedHigh = new SN_DoublePreference("bluesRedHigh", .3);
        public static final SN_DoublePreference bluesGreenLow = new SN_DoublePreference("bluesGreenLow", .43);
        public static final SN_DoublePreference bluesGreenHigh = new SN_DoublePreference("bluesGreenHigh", .48);
        public static final SN_DoublePreference bluesBlueLow = new SN_DoublePreference("bluesBlueLow", .27);
        public static final SN_DoublePreference bluesBlueHigh = new SN_DoublePreference("bluesBlueHigh", .4);
        // yellows
        public static final SN_DoublePreference yellowsRedLow = new SN_DoublePreference("yellowsRedLow", .1);
        public static final SN_DoublePreference yellowsRedHigh = new SN_DoublePreference("yellowsRedHigh", .5);
        public static final SN_DoublePreference yellowsGreenLow = new SN_DoublePreference("yellowsGreenLow", .4);
        public static final SN_DoublePreference yellowsGreenHigh = new SN_DoublePreference("yellowsGreenHigh", .6);
        public static final SN_DoublePreference yellowsBlueLow = new SN_DoublePreference("yellowsBlueLow", 0);
        public static final SN_DoublePreference yellowsBlueHigh = new SN_DoublePreference("yellowsBlueHigh", .2);
        // speed
        public static final SN_DoublePreference controlPanelSpinSpeed = new SN_DoublePreference("controlPanelSpinSpeed",
                        .5);
}