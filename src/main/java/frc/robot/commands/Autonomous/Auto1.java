/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Autonomous;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.commands.Drivetrain.DriveMotionProfile;
import frc.robot.commands.Turret.AlignTurretVision;
import frc.robot.commands.Turret.ShootAndReload;

public class Auto1 extends CommandBase {
  // TODO - declare all the subcommands
  DriveMotionProfile driveForwardCmd;
  AlignTurretVision lineupTurretCmd;
  ShootAndReload firstShotCmd;
  ShootAndReload secondShotCmd;

  Command currentCommand;

  /**
   * Creates a new Auto1.
   */
  public Auto1() {
    // Use addRequirements() here to declare subsystem dependencies.

    // TODO - instantiate all the subcommands
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    currentCommand = driveForwardCmd;

    currentCommand.initialize();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    currentCommand.execute();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(final boolean interrupted) {
  }

  void switchCommand(final Command cmd) {
    currentCommand.end(false);

    currentCommand = cmd;

    cmd.initialize();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (currentCommand.isFinished() == false) {
      return false;
    }

    if (currentCommand == driveForwardCmd) {
      // set currentCommand based on ending status of driveForwardCommand and return
      // true or false
      switchCommand(lineupTurretCmd);
      return false;
    } else if (currentCommand == lineupTurretCmd) {
      switch (lineupTurretCmd.finishReason) {
        case SUCCESS:
          break;
        case TIMEOUT:
          break;
        case NO_TARGET:
          break;
        case NOT_FINISHED:
          break;
      }
    } else if (currentCommand == lineupTurretCmd) {
      // switch(lineupTurretCmd.finishReason) {
      // case LineupTurretCmd.State.TIMED_OUT:
      // }
    } else if (currentCommand == secondShotCmd) {
      return true;
    }

    return false;
  }
}
