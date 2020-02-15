/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Climber;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frcteam3255.robotbase.Preferences.SN_DoublePreference;

public class DeployClimberPosition extends CommandBase {
  /**
   * Creates a new DeployClimberPosition.
   */
  private SN_DoublePreference position;

  public DeployClimberPosition(SN_DoublePreference a_position) {
    // Use addRequirements() here to declare subsystem dependencies.
    position = a_position;
    addRequirements(RobotContainer.climber);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    RobotContainer.climber.extendToHeight(position.getValue());
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    // TODO: need to set control mode back to percent output.
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;
  }
}
