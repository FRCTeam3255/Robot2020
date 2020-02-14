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
import frc.robot.subsystems.Drivetrain;

public class Auto1 extends CommandBase {
  DriveMotionProfile mot1;
  DriveMotionProfile mot2;
  DriveMotionProfile mot3;

  Command currentCommand;

  /**
   * Creates a new Auto1.
   */
  public Auto1(Drivetrain dt, DriveMotionProfile cmd1, DriveMotionProfile cmd2, DriveMotionProfile cmd3) {
    // Use addRequirements() here to declare subsystem dependencies.
    mot1 = cmd1;
    mot2 = cmd2;
    mot3 = cmd3;
    addRequirements(dt);

  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    currentCommand = mot1;

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
    currentCommand.end(false);
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
    if (currentCommand == mot1) {
      // set currentCommand based on ending status of driveForwardCommand and return
      // true or false
      switchCommand(mot2);
      return false;
    } else if (currentCommand == mot2) {

      switchCommand(mot3);
      return false;
    } else if (currentCommand == mot3) {
      // switch(lineupTurretCmd.finishReason) {
      // case LineupTurretCmd.State.TIMED_OUT:
      return true;
      // }
    }

    return false;
  }
}
