/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.commands.Drivetrain.DriveArcade;
import frc.robot.commands.Drivetrain.DriveDistance;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.ControlPanel;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Turret;
import frc.robot.subsystems.Vision;
import frcteam3255.robotbase.Joystick.SN_DualActionStick;
import frcteam3255.robotbase.Joystick.SN_Extreme3DStick;
import edu.wpi.first.wpilibj2.command.Command;

/**
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  public static final Drivetrain m_drivetrain = new Drivetrain();
  public static final Vision m_vision = new Vision();
  public static final ControlPanel m_controlPanel = new ControlPanel();
  public static final Turret m_turret = new Turret();
  public static final Climber m_climber = new Climber();
  public static final Intake m_intake = new Intake();

  public static SN_DualActionStick drive = new SN_DualActionStick(0);
  public static SN_Extreme3DStick manipulator = new SN_Extreme3DStick(1);

  /**
   * The container for the robot.  Contains subsystems, OI devices, and commands.
   */
  public RobotContainer()
  {
    // Configure the button bindings

    configureButtonBindings();

    m_drivetrain.setDefaultCommand(new DriveArcade(m_drivetrain));
  }

  public static void motionReload(){
    
  }
  public static void colorReload(){
    m_controlPanel.reloadColorTargets();
  }
  /**
   * Use this method to define your button->command mappings.  Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a
   * {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    drive.btn_A.whileHeld(new DriveDistance(m_drivetrain, RobotPreferences.driveDistance));
    drive.btn_B.whileHeld(new DriveDistance(m_drivetrain, RobotPreferences.driveDistance2));
  }


  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    return null;
  }
}  