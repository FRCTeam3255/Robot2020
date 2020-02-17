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
import frc.robot.commands.Autonomous.Auto1;
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
import frc.robot.commands.Intake.HandleIntake;
import frc.robot.commands.Turret.AlignTurretVision;
import frc.robot.commands.Turret.RotateHood;
import frc.robot.commands.Turret.RotateTurret;
import frc.robot.commands.Turret.SetHoodPosition;
import frc.robot.commands.Turret.ShootAutomatic;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.ControlPanel;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Turret;
import frc.robot.subsystems.Vision;
import frc.robot.subsystems.ControlPanel.panelColor;
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

  private static ShootAutomatic smartShot;
  private static ToggleControlPanel toggleControlPanel;
  private static AlignTurretVision alignTurretVision;
  private static SpinControlPanelCount spinControlPanelCounts;
  private static SpinToColor spinToColor;
  private static DeployClimberManual climberManual;
  private static WinchClimber winchClimber;
  private static SpinControlPanelManual controlPanelManual;
  private static RotateTurret turretManual;
  // private static SetHoodPosition hoodMidRange;
  private static RotateHood hoodManual;
  private static SetHoodPosition hoodCloseRange;
  private static CollectBall collect;
  private static Auto1 auto1;
  private static DriveToBall driveToBall;

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
    smartShot = new ShootAutomatic();
    toggleControlPanel = new ToggleControlPanel();
    alignTurretVision = new AlignTurretVision();
    spinControlPanelCounts = new SpinControlPanelCount(RobotPreferences.spinCount);
    spinToColor = new SpinToColor(panelColor.green);
    climberManual = new DeployClimberManual();
    winchClimber = new WinchClimber(RobotPreferences.climberWinchSpeed);
    controlPanelManual = new SpinControlPanelManual();
    turretManual = new RotateTurret();
    // hoodMidRange = new SetHoodPosition(RobotPreferences.hoodMidRangePos);
    hoodCloseRange = new SetHoodPosition(RobotPreferences.hoodCloseRangePos);
    hoodManual = new RotateHood();
    collect = new CollectBall();

    driveToBall = new DriveToBall(false, 100.0, RobotPreferences.ballCount);
    auto1 = new Auto1();

    // map buttons to commands
    configureButtonBindings();

    // set default commands on subsystems
    drivetrain.setDefaultCommand(new DriveArcade());
    intake.setDefaultCommand(new HandleIntake());

    motionReload();

    SmartDashboard.putData("Reload Motions", new ReloadMotionProfile());
    SmartDashboard.putData("Reload Colors", new ReloadColorTargets());
    SmartDashboard.putData("Reload Talons", new ConfigureTalons());
    SmartDashboard.putData("Reset Drive Encoders", new ResetDriveEncoder());
    SmartDashboard.putData("Reset Hood Encoders", new ResetHoodEncoder());
    SmartDashboard.putData("Reset Susan Encoders", new ResetSusanEncoder());

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
   * ({@link edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then
   * passing it to a {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    manipulator.btn_1.whileHeld(smartShot);
    manipulator.btn_2.whileHeld(toggleControlPanel);
    manipulator.btn_3.whileHeld(alignTurretVision);
    manipulator.btn_4.whileHeld(collect);
    manipulator.btn_5.whileHeld(spinControlPanelCounts);
    manipulator.btn_6.whileHeld(spinToColor);
    // manipulator.btn_7.whileHeld(climberManual);
    manipulator.btn_7.whileHeld(new SetHoodPosition(RobotPreferences.hoodTestDegree));
    manipulator.btn_8.whileHeld(winchClimber);
    manipulator.btn_9.whileHeld(controlPanelManual);
    manipulator.btn_10.whileHeld(turretManual);
    // manipulator.btn_11.whileHeld(hoodMidRange);
    manipulator.btn_11.whileHeld(hoodManual);
    manipulator.btn_12.whileHeld(hoodCloseRange);

    // drive stuff in arcade drive command
    drive.btn_Y.whileHeld(driveToBall);
    drive.btn_A.whileHeld(auto1);
    drive.btn_X.whileHeld(failMot);

  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // TODO: Need to have the ability to select different autonomous commands,
    // including do nothing
    return auto1;
  }
}