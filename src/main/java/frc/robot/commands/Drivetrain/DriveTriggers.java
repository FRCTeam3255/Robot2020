// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Drivetrain;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.RobotPreferences;

public class DriveTriggers extends CommandBase {
  /** Creates a new DriveTriggers. */
  public DriveTriggers() {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(RobotContainer.drivetrain);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    RobotContainer.drivetrain.arcadeDriveInit();

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    int sAxis = RobotPreferences.steeringAxis.getValue();

    // sAxis should probably only ever be 0 or 4 on an F310, 0 or 2 on a DualAction

    // RobotContainer.drivetrain.arcadeDriveTriggers(RobotContainer.driveF.getAxisRT()
    // - RobotContainer.driveF.getAxisLT(),
    // RobotContainer.driveF.getAxisVar(sAxis));
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}