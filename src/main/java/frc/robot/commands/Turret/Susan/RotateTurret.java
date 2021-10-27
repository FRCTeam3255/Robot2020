/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Turret.Susan;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.RobotPreferences;

public class RotateTurret extends CommandBase {
  /**
   * Creates a new RotateTurret.
   */

  public RotateTurret() {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(RobotContainer.susan);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    RobotContainer.susan.configureLazySusan();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    RobotContainer.susan
        .setSusanSpeed(RobotPreferences.turretManualSpeed.getValue() * RobotContainer.manipulator.getTwistAxis());
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    RobotContainer.susan.setSusanSpeed(0);

  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
