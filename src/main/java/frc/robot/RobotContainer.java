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
import frc.robot.commands.ControlPanel.SpinControlPanelCount;
import frc.robot.commands.ControlPanel.SpinControlPanelManual;
import frc.robot.commands.ControlPanel.SpinToColor;
import frc.robot.commands.ControlPanel.ToggleControlPanel;
import frc.robot.commands.Drivetrain.DriveArcade;
import frc.robot.commands.Drivetrain.DriveMotionProfile;
import frc.robot.commands.Drivetrain.DriveToBall;
import frc.robot.commands.Drivetrain.ReloadMotionProfile;
import frc.robot.commands.Intake.HandleIntake;
import frc.robot.commands.Turret.AlignAndShoot;
import frc.robot.commands.Turret.AlignAndShootToPos;
import frc.robot.commands.Turret.AlignTurretVision;
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
import frcteam3255.robotbase.SN_MotionProfile;
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
  private static SetHoodPosition hoodMidRange;
  private static SetHoodPosition hoodCloseRange;
  private static Auto1 auto1;
  private static DriveToBall driveToBall;

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // Configure the button bindings
    // TODO: This only gets called at construction. Would be called in start motion profile.
    SN_MotionProfile.setSensorUnitsPerTick(RobotPreferences.motProfSensorUnitsPerFt.getValue());

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
    hoodMidRange = new SetHoodPosition(RobotPreferences.hoodMidRangePos);
    hoodCloseRange = new SetHoodPosition(RobotPreferences.hoodCloseRangePos);

    driveToBall = new DriveToBall(true, RobotPreferences.ballTimeout, RobotPreferences.ballCount);
    auto1 = new Auto1(new AlignAndShoot(3), failMot,
        new AlignAndShootToPos(3, RobotPreferences.failHoodPos, RobotPreferences.failHoodPos), grabBallMot, getBackMot,
        new AlignAndShoot(4), new DriveToBall(true, RobotPreferences.ballTimeout, RobotPreferences.ballCount), finalMot,
        new AlignAndShoot(2));
    
    // map buttons to commands
    configureButtonBindings();


    // set default commands on subsystems
    drivetrain.setDefaultCommand(new DriveArcade());
    intake.setDefaultCommand(new HandleIntake());

    motionReload();

    SmartDashboard.putData("Reload Motions", new ReloadMotionProfile());
    // TODO: We probably need more reload commands on the dashboard
    // TODO: Need reset encoder buttons on the dashboard
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
    // button 4 collects ball - found in intakeHandler cmd
    manipulator.btn_5.whileHeld(spinControlPanelCounts);
    manipulator.btn_6.whileHeld(spinToColor);
    manipulator.btn_7.whileHeld(climberManual);
    manipulator.btn_8.whileHeld(winchClimber);
    manipulator.btn_9.whileHeld(controlPanelManual);
    manipulator.btn_10.whileHeld(turretManual);
    manipulator.btn_11.whileHeld(hoodMidRange);
    manipulator.btn_12.whileHeld(hoodCloseRange);

    drive.btn_Y.whileHeld(driveToBall);
    // TODO: Use the switchboard to have a mode for testing autonomous. It can remap button bindings.
    drive.btn_A.whileHeld(auto1);
    drive.btn_X.whileHeld(failMot);

    /*
     * TODO: MAY need to have ability to reconfigure button bindings when we enter
     * climb mode, control panel mode, or based on other switchboard switches. This
     * is another reason why it's better to create commands once in constructor, and
     * then reference them in configureButtonBindings.
     */

    /*
     * TODO: Need the following controls Climber: extend: manipulator 8 DONE
     * retract: manipulator 10 DONE climb: manipulator 12 set position high:
     * manipulator 9 (climb mode) set position medium: manipulator 11 (climb mode)
     * set position low: (don't need this) climb mode on/off: switchboard 10
     * 
     * Control Panel: deploy: manipulator 2 (toggle) retract: manipulator 2 (toogle)
     * spin manual: manipulator 7/8 spin to color: manipulator 6 spin number of
     * times: manipulator 5
     * 
     * DriveTrain: Drive arcade: driveTrain default command DONE drive to ball:
     * driver Y DONE high speed/low speed (default low speed): driver right button
     * Defensive spin move: driver b
     * 
     * Collector: deploy: smart dashboard retract: (not mechanically possible?)
     * collect manually: manipulator 4 DONE collect until detected: ?? reverse spin
     * collector (only for anomoly): ??
     * 
     * Intake: feed to turret manually: ?? feed to turret automatically: default
     * command for intake reverse: (not mechanically possible?)
     * 
     * Turret: align rotation with vision: manipulator 3 align hood with vision:
     * manipulator 3 turn turret manually: manipulator 9 and 10 (not climb mode)
     * hood to midrange: manipulator 11 (not climb mode) hood to close shot:
     * manipulator 12 (not climb mode) move hood manually: potentiomter on
     * switchboard?? spin up shooter: switchboard 9 (defaults on) spin down shooter:
     * switchboard 9 (same spin up) set shooter speed: (preference only?, if we lose
     * hood motor, speed could be good, but we could drive to position) shoot a ball
     * automatically: trigger shoot a ball manually (doesn't rely on sesnors):
     * trigger with switchboard 7 enabled
     */
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