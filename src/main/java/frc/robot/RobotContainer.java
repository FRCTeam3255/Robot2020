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
import frc.robot.commands.Drivetrain.DriveArcade;
import frc.robot.commands.Drivetrain.DriveMotionProfile;
import frc.robot.commands.Drivetrain.DriveToBall;
import frc.robot.commands.Drivetrain.ReloadMotionProfile;
import frc.robot.commands.Intake.HandleIntake;
import frc.robot.commands.Turret.AlignAndShoot;
import frc.robot.commands.Turret.AlignAndShootToPos;
import frc.robot.commands.Turret.AlignTurretVision;
import frc.robot.commands.Turret.Shoot;
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

  public static DriveMotionProfile failMot = new DriveMotionProfile(drivetrain, "failMot_left.csv",
      "failMot_right.csv");
  public static DriveMotionProfile grabBallMot = new DriveMotionProfile(drivetrain, "grabBallMot_left.csv",
      "grabBallMot_right.csv");
  public static DriveMotionProfile getBackMot = new DriveMotionProfile(drivetrain, "getBackMot_left.csv",
      "getBackMot_right.csv");
  public static DriveMotionProfile finalMot = new DriveMotionProfile(drivetrain, "finalMot_left.csv",
      "finalMot_right.csv");

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // Configure the button bindings

    configureButtonBindings();

    drivetrain.setDefaultCommand(new DriveArcade(drivetrain));
    intake.setDefaultCommand(new HandleIntake(intake));
    motionReload();
    SmartDashboard.putData("Reload Motions", new ReloadMotionProfile());
  }

  public static void motionReload() {
    failMot.reload();
    grabBallMot.reload();
    getBackMot.reload();
    finalMot.reload();
  }

  public static void colorReload() {
    controlPanel.reloadColorTargets();
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be
   * created by instantiating a {@link GenericHID} or one of its subclasses
   * ({@link edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then
   * passing it to a {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    drive.btn_Y.whenPressed(
        new DriveToBall(drivetrain, vision, intake, true, RobotPreferences.ballTimeout, RobotPreferences.ballCount));
    drive.btn_A.whileHeld(new Auto1(drivetrain, new AlignAndShoot(intake, turret, vision, 3), failMot,
        new AlignAndShootToPos(intake, turret, 3, RobotPreferences.failHoodPos, RobotPreferences.failHoodPos),
        grabBallMot, getBackMot, new AlignAndShoot(intake, turret, vision, 4),
        new DriveToBall(drivetrain, vision, intake, true, RobotPreferences.ballTimeout, RobotPreferences.ballCount),
        finalMot, new AlignAndShoot(intake, turret, vision, 2)));
    drive.btn_X.whileHeld(failMot);
    manipulator.btn_1.whileHeld(new Shoot(turret));
    manipulator.btn_2.whenPressed(new SpinControlPanelCount(controlPanel, RobotPreferences.spinCount));
    manipulator.btn_3.whileHeld(new AlignTurretVision(turret, vision));

    manipulator.btn_7.whileHeld(new WinchClimber(climber, RobotPreferences.climberWinchSpeed));
    manipulator.btn_8.whileHeld(new DeployClimberManual(climber, RobotPreferences.climberUpSpeed));
    manipulator.btn_10.whileHeld(new DeployClimberManual(climber, RobotPreferences.climberDownSpeed));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    return new Auto1(drivetrain, new AlignAndShoot(intake, turret, vision, 3), failMot,
        new AlignAndShootToPos(intake, turret, 3, RobotPreferences.failHoodPos, RobotPreferences.failHoodPos),
        grabBallMot, getBackMot, new AlignAndShoot(intake, turret, vision, 4),
        new DriveToBall(drivetrain, vision, intake, true, RobotPreferences.ballTimeout, RobotPreferences.ballCount),
        finalMot, new AlignAndShoot(intake, turret, vision, 2));
  }
}