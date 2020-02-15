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

  // TODO: Need a lighting system that uses LEDs to alert driver when they have vision lock on goal or ball

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
    // TODO: We probably need more reload commands on the dashboard
    // TODO: Need reset encoder buttons on the dashboard
  }

  public static void motionReload() {
    failMot.reload();
    grabBallMot.reload();
    getBackMot.reload();
    finalMot.reload();
  }

  // TODO: why have this in RobotContainer since it can be called directly?
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
    // TODO: Don't have an autonomous command tied to a button
    // TODO: Don't create the same command twice. Create it in constrcutor, and then assign it and/or use it in multiple places
    drive.btn_A.whileHeld(new Auto1(drivetrain, new AlignAndShoot(intake, turret, vision, 3), failMot,
        new AlignAndShootToPos(intake, turret, 3, RobotPreferences.failHoodPos, RobotPreferences.failHoodPos),
        grabBallMot, getBackMot, new AlignAndShoot(intake, turret, vision, 4),
        new DriveToBall(drivetrain, vision, intake, true, RobotPreferences.ballTimeout, RobotPreferences.ballCount),
        finalMot, new AlignAndShoot(intake, turret, vision, 2)));
    // TODO: Don't have an autonomous command tied to a button
    drive.btn_X.whileHeld(failMot);
    manipulator.btn_1.whileHeld(new Shoot(turret));
    manipulator.btn_2.whenPressed(new SpinControlPanelCount(controlPanel, RobotPreferences.spinCount));
    manipulator.btn_3.whileHeld(new AlignTurretVision(turret, vision));

    manipulator.btn_7.whileHeld(new WinchClimber(climber, RobotPreferences.climberWinchSpeed));
    manipulator.btn_8.whileHeld(new DeployClimberManual(climber, RobotPreferences.climberUpSpeed));
    manipulator.btn_10.whileHeld(new DeployClimberManual(climber, RobotPreferences.climberDownSpeed));

    /*
      TODO: Need to have ability to reconfigure button bindings when we enter climb mode, control panel mode, or based on other switchboard switches.
      This is another reason why it's better to create commands once in constructor, and then reference them in configureButtonBindings.
    */

    /*
      TODO: Need the following controls
        Climber: 
          extend: manipulator 8 DONE
          retract: manipulator 10 DONE
          climb: manipulator 12
          set position high: manipulator 9 (climb mode)
          set position medium: manipulator 11 (climb mode)
          set position low: (don't need this)
          climb mode on/off: switchboard 10

        Control Panel:
          deploy: manipulator 2 (toggle)
          retract: manipulator 2 (toogle)
          spin manual: manipulator 7/8
          spin to color: manipulator 6
          spin number of times: manipulator 5

        DriveTrain:
          Drive arcade: driveTrain default command DONE
          drive to ball: driver Y DONE
          high speed/low speed (default low speed): driver right button
          Defensive spin move: driver b

        Collector:
          deploy: smart dashboard
          retract: (not mechanically possible?)
          collect manually: manipulator 4 DONE
          collect until detected: ??
          reverse spin collector (only for anomoly): ??

        Intake:
          feed to turret manually: ??
          feed to turret automatically: default command for intake
          reverse: (not mechanically possible?)

        Turret:
          align rotation with vision: manipulator 3
          align hood with vision: manipulator 3
          turn turret manually: manipulator 9 and 10 (not climb mode)
          hood to midrange: manipulator 11 (not climb mode)
          hood to close shot: manipulator 12 (not climb mode)
          move hood manually: potentiomter on switchboard??
          spin up shooter: switchboard 9 (defaults on)
          spin down shooter: switchboard 9 (same spin up)
          set shooter speed: (preference only?, if we lose hood motor, speed could be good, but we could drive to position)
          shoot a ball automatically: trigger
          shoot a ball manually (doesn't rely on sesnors): trigger with switchboard 7 enabled
    */
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // TODO: Need to have the ability to select different autonomous commands, including do nothing
    return new Auto1(drivetrain, new AlignAndShoot(intake, turret, vision, 3), failMot,
        new AlignAndShootToPos(intake, turret, 3, RobotPreferences.failHoodPos, RobotPreferences.failHoodPos),
        grabBallMot, getBackMot, new AlignAndShoot(intake, turret, vision, 4),
        new DriveToBall(drivetrain, vision, intake, true, RobotPreferences.ballTimeout, RobotPreferences.ballCount),
        finalMot, new AlignAndShoot(intake, turret, vision, 2));
  }
}