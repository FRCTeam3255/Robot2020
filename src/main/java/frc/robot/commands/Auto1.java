/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class Auto1 extends CommandBase {
  public enum State {
    DRIVE_FORWARD,
  };

  State myState = State.DRIVE_FORWARD;

  // instantiate all the subcommands
  Command driveForwardCmd;
  Command lineupTurretCmd;
  Command firstShotCmd;
  Command secondShotCmd;

  Command currentCommand;

  /**
   * Creates a new Auto1.
   */
  public Auto1() {
    // Use addRequirements() here to declare subsystem dependencies.
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
  public void end(boolean interrupted) {
  }

  void switchCommand(Command cmd) {
    currentCommand = cmd;
    cmd.initialize();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if(currentCommand.isFinished()) {
      // TODO: set this following check to be against the last command you want to run
      if(currentCommand == secondShotCmd) {
        return true;
      }

      if(currentCommand == driveForwardCmd) {
        // set currentCommand based on ending status of driveForwardCommand and return true or false
        switchCommand(lineupTurretCmd);
        return false;
      }
      else if(currentCommand == lineupTurretCmd) {
//        switch(lineupTurretCmd.finishReason) {
//          case LineupTurretCmd.State.TIMED_OUT:
//        }
      }
    }

    return false;
  }
}
