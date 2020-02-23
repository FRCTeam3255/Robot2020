/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.Autonomous.Autonomous;
import frc.robot.commands.Climber.DeployClimberManual;
import frc.robot.commands.Climber.WinchClimber;
import frc.robot.commands.Config.ConfigureTalons;
import frc.robot.commands.Config.ResetDriveEncoder;
import frc.robot.commands.Config.ResetHoodEncoder;
import frc.robot.commands.Config.ResetSusanEncoder;
import frc.robot.commands.ControlPanel.ReloadColorTargets;
import frc.robot.commands.ControlPanel.SpinControlPanelCount;
import frc.robot.commands.ControlPanel.SpinControlPanelManual;
import frc.robot.commands.ControlPanel.SpinToColor;
import frc.robot.commands.ControlPanel.ToggleControlPanel;
import frc.robot.commands.Drivetrain.DriveArcade;
import frc.robot.commands.Drivetrain.DriveMotionProfile;
import frc.robot.commands.Drivetrain.DriveToBall;
import frc.robot.commands.Drivetrain.ReloadMotionProfile;
import frc.robot.commands.Intake.CollectBall;
import frc.robot.commands.Turret.AlignTurretVision;
import frc.robot.commands.Turret.NudgeHood;
import frc.robot.commands.Turret.ResetShooter;
// import frc.robot.commands.Turret.RotateHood;
import frc.robot.commands.Turret.RotateTurret;
import frc.robot.commands.Turret.SetHoodPosition;
// import frc.robot.commands.Turret.SetShooterSpeed;
// import frc.robot.commands.Turret.ShootAutomatic;
import frc.robot.commands.Turret.ShootBall;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.ControlPanel;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Turret;
import frc.robot.subsystems.Vision;
import frcteam3255.robotbase.Joystick.SN_DualActionStick;
import frcteam3255.robotbase.Joystick.SN_Extreme3DStick;
import frcteam3255.robotbase.Joystick.SN_SwitchboardStick;
import edu.wpi.first.wpilibj2.command.Command;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a "declarative" paradigm, very little robot logic should
 * actually be handled in the {@link Robot} periodic methods (other than the
 * scheduler calls). Instead, the structure of the robot (including subsystems,
 * commands, and button mappings) should be declared here.
 */
public class RobotContainer {

  // The robot's subsystems and commands are defined here...
  public static final Drivetrain drivetrain = new Drivetrain();
  public static final Vision vision = new Vision();
  public static final ControlPanel controlPanel = new ControlPanel();
  public static final Turret turret = new Turret();
  public static final Climber climber = new Climber();
  public static final Intake intake = new Intake();

  public static SN_DualActionStick drive = new SN_DualActionStick(0);
  public static SN_Extreme3DStick manipulator = new SN_Extreme3DStick(1);
  public static SN_SwitchboardStick switchBoard = new SN_SwitchboardStick(2);

  public static DriveMotionProfile failMot;
  public static DriveMotionProfile grabBallMot;
  public static DriveMotionProfile getBackMot;
  public static DriveMotionProfile finalMot;

  // private static ShootAutomatic smartShot;
  private static ToggleControlPanel toggleControlPanel;
  private static AlignTurretVision alignTurretVision;
  private static SpinControlPanelCount spinControlPanelCounts;
  private static SpinToColor spinToColor;
  private static DeployClimberManual climberManual;
  private static WinchClimber winchClimber;
  private static SpinControlPanelManual controlPanelLeft;
  private static SpinControlPanelManual controlPanelRight;
  private static RotateTurret turretManual;
  // private static RotateHood hoodManual;
  private static ShootBall shoot;
  // private static SetHoodPosition hoodFar;
  private static CollectBall collect;
  private static Autonomous auto;
  private static DriveToBall driveToBall;
  private static NudgeHood nudgeHoodUp;
  private static NudgeHood nudgeHoodDown;
  private static ResetShooter resetShooter;

  // finsihed
  private static SetHoodPosition hoodMiddleTrench;
  private static SetHoodPosition hoodFrontTrench;
  private static SetHoodPosition hoodInitialization;
  private static SetHoodPosition hoodClose;
  private static SetHoodPosition hoodWallLow;
  private static SetHoodPosition hoodWallHigh;

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {

    // create motion profiles
    failMot = new DriveMotionProfile("failMot_left.csv", "failMot_right.csv");
    grabBallMot = new DriveMotionProfile("grabBallMot_left.csv", "grabBallMot_right.csv");
    getBackMot = new DriveMotionProfile("getBackMot_left.csv", "getBackMot_right.csv");
    finalMot = new DriveMotionProfile("finalMot_left.csv", "finalMot_right.csv");

    // create commands
    resetShooter = new ResetShooter();
    shoot = new ShootBall();
    // smartShot = new ShootAutomatic();
    toggleControlPanel = new ToggleControlPanel();
    alignTurretVision = new AlignTurretVision();
    spinControlPanelCounts = new SpinControlPanelCount(RobotPreferences.spinCount);
    spinToColor = new SpinToColor();
    climberManual = new DeployClimberManual();
    winchClimber = new WinchClimber(RobotPreferences.climberWinchSpeed);
    controlPanelLeft = new SpinControlPanelManual(RobotPreferences.spinSpeedLeft);
    controlPanelRight = new SpinControlPanelManual(RobotPreferences.spinSpeedRight);
    turretManual = new RotateTurret();
    // hoodManual = new RotateHood();
    collect = new CollectBall();
    nudgeHoodUp = new NudgeHood(RobotPreferences.nudgeHoodUp);
    nudgeHoodDown = new NudgeHood(RobotPreferences.nudgeHoodDown);

    hoodMiddleTrench = new SetHoodPosition(RobotPreferences.hoodMiddleTrench, RobotPreferences.shooterMaxRPM);
    hoodFrontTrench = new SetHoodPosition(RobotPreferences.hoodFrontTrench, RobotPreferences.shooterMaxRPM);
    hoodInitialization = new SetHoodPosition(RobotPreferences.hoodInitialization, RobotPreferences.shooterMaxRPM);
    hoodClose = new SetHoodPosition(RobotPreferences.hoodClose, RobotPreferences.shooterCloseRPM);
    hoodWallLow = new SetHoodPosition(RobotPreferences.hoodWallLow, RobotPreferences.shooterWallLowRPM);
    hoodWallHigh = new SetHoodPosition(RobotPreferences.hoodWallHigh, RobotPreferences.shooterWallHighRPM);

    driveToBall = new DriveToBall(false, 100.0, RobotPreferences.ballCount);
    auto = new Autonomous();

    // map buttons to commands
    configureButtonBindings();

    // set default commands on subsystems
    drivetrain.setDefaultCommand(new DriveArcade());
    // intake.setDefaultCommand(new HandleIntake());
    // turret.setDefaultCommand(new ShooterTimeout());

    motionReload();

    SmartDashboard.putData("Reload Motions", new ReloadMotionProfile());
    SmartDashboard.putData("Reload Colors", new ReloadColorTargets());
    SmartDashboard.putData("Reload Talons", new ConfigureTalons());
    SmartDashboard.putData("Reset Drive Encoders", new ResetDriveEncoder());
    SmartDashboard.putData("Reset Hood Encoders", new ResetHoodEncoder());
    SmartDashboard.putData("Reset Susan Encoders", new ResetSusanEncoder());

  }

  public static boolean getModeBtn() {
    // return switchBoard.btn_9.get();
    return true;
  }

  public static void motionReload() {
    failMot.reload();
    grabBallMot.reload();
    getBackMot.reload();
    finalMot.reload();
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be
   * created by instantiating a {@link GenericHID} or one of its subclasses
   * ({@link edu.wpi.first./wpilibj.Joystick} or {@link XboxController}), and then
   * passing it to a {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */

  public static void configureButtonBindings() {
    manipulator.btn_1.whenPressed(shoot);
    manipulator.btn_1.whenReleased(resetShooter);
    manipulator.btn_2.whileHeld(turretManual);
    manipulator.btn_3.whileHeld(alignTurretVision);
    manipulator.btn_4.whenPressed(toggleControlPanel);
    manipulator.btn_5.whileHeld(spinControlPanelCounts);
    manipulator.btn_6.whileHeld(spinToColor);
    manipulator.btn_7.whenPressed(hoodMiddleTrench);
    manipulator.btn_8.whenPressed(hoodWallHigh);
    manipulator.btn_9.whenPressed(hoodFrontTrench);
    manipulator.btn_10.whenPressed(hoodWallLow);
    manipulator.btn_11.whenPressed(hoodInitialization);
    manipulator.btn_12.whenPressed(hoodClose);
    manipulator.POV_North.whenPressed(nudgeHoodUp);
    manipulator.POV_East.whileHeld(controlPanelRight);
    manipulator.POV_South.whenPressed(nudgeHoodDown);
    manipulator.POV_West.whileHeld(controlPanelLeft);

    // drive stuff in arcade drive command
    drive.btn_Y.whileHeld(driveToBall);
    drive.btn_A.whileHeld(auto);
    drive.btn_X.whileHeld(failMot);
    drive.btn_LBump.whileHeld(collect);
    // emergancies
    drive.btn_RTrig.whileHeld(climberManual);
    drive.btn_LTrig.whileHeld(winchClimber);

    // switchboards
    switchBoard.btn_8.whileHeld(winchClimber);
    switchBoard.btn_10.whileHeld(climberManual);
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return auto;
  }
}