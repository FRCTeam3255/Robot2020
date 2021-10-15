// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Turret;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.RobotPreferences;
import frc.robot.subsystems.Turret.Direction;

public class RotateTurretButton extends CommandBase {

  Direction dir;

  /** Creates a new RotateTurretButton. */
  public RotateTurretButton(Direction a_dir) {
    // Use addRequirements() here to declare subsystem dependencies
    addRequirements(RobotContainer.turret);

    dir = a_dir;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    RobotContainer.turret.configureLazySusan();

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (dir.equals(Direction.WEST)) {
      RobotContainer.turret.setSusanSpeed(RobotPreferences.turretManualSpeed.getValue());
    } else if (dir.equals(Direction.EAST)) {
      RobotContainer.turret.setSusanSpeed(-RobotPreferences.turretManualSpeed.getValue());
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    RobotContainer.turret.setSusanSpeed(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
