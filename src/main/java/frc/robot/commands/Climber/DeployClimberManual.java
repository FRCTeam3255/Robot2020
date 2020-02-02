/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Climber;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climber;
import frcteam3255.robotbase.Preferences.SN_DoublePreference;

public class DeployClimberManual extends CommandBase {
  /**
   * Creates a new DeployClimberManual.
   */
  private final Climber m_climber;
  private SN_DoublePreference m_speed;
  public DeployClimberManual(Climber subsystem, SN_DoublePreference speed) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_climber = subsystem;
    m_speed = speed;
    addRequirements(subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_climber.setSpeed(m_speed.getValue());
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;
  }
}
