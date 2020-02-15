/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Climber;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;

public class DeployClimberManual extends CommandBase {
  /**
   * Creates a new DeployClimberManual.
   */

  public DeployClimberManual() {
    // Use addRequirements() here to declare subsystem dependencies.

    /*
     * TODO: Don't delete this todo!! Need to discuss. Because addRequirements will
     * call end(true) on any running climber command, but this command is registered
     * twice for the same climber, if set speed 0 when interrupted is asynchronous,
     * and executes after the set speed in initialize, we could get stuck. These
     * types of issues have happened in prior WPIlibs.
     */
    addRequirements(RobotContainer.climber);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    RobotContainer.climber.setSpeed(RobotContainer.manipulator.getYAxis());

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    RobotContainer.climber.setSpeed(0);

  }

  // Returns false when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
