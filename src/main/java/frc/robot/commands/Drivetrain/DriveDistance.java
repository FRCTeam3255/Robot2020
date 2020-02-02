/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Drivetrain;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Drivetrain;
import frcteam3255.robotbase.Preferences.SN_DoublePreference;

public class DriveDistance extends CommandBase
{
  private final Drivetrain m_drivetrain;
  private SN_DoublePreference m_distance;
  /**
   * Creates a new DriveDistance.
   **/

  public DriveDistance(Drivetrain subsystem, SN_DoublePreference distance)
  {
    m_drivetrain = subsystem;
    m_distance = distance;
    addRequirements(subsystem);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize()
  {
    m_drivetrain.resetEncoderCounts();
    m_drivetrain.driveDistance(m_distance.getValue());
  }

  // Called every time the scheduler runs while the command is scheduled.

  @Override

  public void execute() 
  {
  }

  // Called once the command ends or is interrupted.

  @Override

  public void end(boolean interrupted)
  {
    m_drivetrain.resetMotionProfile();
  }

  // Returns true when the command should end.

  @Override

  public boolean isFinished()
  {
    return false;
  }
}