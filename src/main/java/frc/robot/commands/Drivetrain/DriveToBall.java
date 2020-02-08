/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Drivetrain;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.RobotPreferences;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Vision;

public class DriveToBall extends CommandBase
{
  private final Drivetrain m_drivetrain;
  private final Vision m_vision;

  /**
   * Creates a new DriveToBall.
   **/

  public DriveToBall(Drivetrain drivetrain, Vision vision)
  {
    m_drivetrain = drivetrain;
    m_vision = vision;
    addRequirements(drivetrain);
    addRequirements(vision);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize()
  {

  }

  // Called every time the scheduler runs while the command is scheduled.

  @Override

  public void execute() 
  {
    m_drivetrain.arcadeDrive(.75, m_vision.getX()*RobotPreferences.ballP.getValue());
  }

  // Called once the command ends or is interrupted.

  @Override

  public void end(boolean interrupted)
  {

  }

  // Returns true when the command should end.

  @Override

  public boolean isFinished()
  {
    return false;
  }
}