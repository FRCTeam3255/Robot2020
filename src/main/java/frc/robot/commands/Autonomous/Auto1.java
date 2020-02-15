/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Autonomous;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.commands.Drivetrain.DriveMotionProfile;
import frc.robot.commands.Drivetrain.DriveToBall;
import frc.robot.commands.Turret.AlignAndShoot;
import frc.robot.commands.Turret.AlignAndShootToPos;
import frc.robot.subsystems.Drivetrain;

public class Auto1 extends CommandBase {
  AlignAndShoot initialLineupAndShoot;
  DriveMotionProfile failMot;
  DriveMotionProfile grabBallMot;
  DriveMotionProfile getBackMot;
  DriveMotionProfile finalMot;

  AlignAndShootToPos failShoot;
  AlignAndShoot stage2LineupAndShoot;
  DriveToBall rendevouzBalls;
  AlignAndShoot stage3LineupAndShoot;

  Command currentCommand;

  /**
   * Creates a new Auto1.
   */
  public Auto1(Drivetrain a_drivetrain, AlignAndShoot a_initialLineupAndShoot, DriveMotionProfile a_failMot,
      AlignAndShootToPos a_failShoot, DriveMotionProfile a_grabBallMot, DriveMotionProfile a_getBackMot,
      AlignAndShoot a_stage2LineupAndShoot, DriveToBall a_rendevouzBalls, DriveMotionProfile a_finalMot,
      AlignAndShoot a_stage3LineupAndShoot) {
    // Use addRequirements() here to declare subsystem dependencies.
    initialLineupAndShoot = a_initialLineupAndShoot;
    failMot = a_failMot;
    failShoot = a_failShoot;
    grabBallMot = a_grabBallMot;
    getBackMot = a_getBackMot;
    stage2LineupAndShoot = a_stage2LineupAndShoot;
    rendevouzBalls = a_rendevouzBalls;
    finalMot = a_finalMot;
    stage3LineupAndShoot = a_stage3LineupAndShoot;
    addRequirements(a_drivetrain);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    currentCommand = initialLineupAndShoot;
    SmartDashboard.putString("Auto Command", "initialLineupAndShoot");

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
    SmartDashboard.putString("Auto Command", "Auto Over");

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
    if (currentCommand == initialLineupAndShoot) {
      // set currentCommand based on ending status of driveForwardCommand and return
      // true or false

      switch (initialLineupAndShoot.finishReason) {
      case NO_TARGET:
        SmartDashboard.putString("Auto Command", "failShoot");

        switchCommand(failShoot);
        return false;
      case SUCCESS:
        SmartDashboard.putString("Auto Command", "grabBallMot");

        switchCommand(grabBallMot);
        return false;
      case NOT_FINISHED:
        SmartDashboard.putString("Auto Command", "failShoot");

        switchCommand(failShoot);
        return false;
      }

    } else if (currentCommand == failShoot) {
      SmartDashboard.putString("Auto Command", "failMot");

      switchCommand(failMot);
      return false;
    } else if (currentCommand == grabBallMot) {
      SmartDashboard.putString("Auto Command", "getBackMot");

      switchCommand(getBackMot);
      return false;
    } else if (currentCommand == failMot) {
      return true;
    } else if (currentCommand == getBackMot) {
      SmartDashboard.putString("Auto Command", "stage2LineupAndShoot");

      switchCommand(stage2LineupAndShoot);
      return false;
    } else if (currentCommand == stage2LineupAndShoot) {

      switch (stage2LineupAndShoot.finishReason) {
      case NO_TARGET:
        return true;
      case SUCCESS:
        SmartDashboard.putString("Auto Command", "rendevouzBalls");

        switchCommand(rendevouzBalls);
        return false;
      case NOT_FINISHED:
        return true;
      }
    } else if (currentCommand == rendevouzBalls) {

      switch (rendevouzBalls.finishReason) {
      case TIMED_OUT:
        return true;
      case SUCCESS:
        SmartDashboard.putString("Auto Command", "finalMot");

        switchCommand(finalMot);
        return false;
      case NOT_FINISHED:
        return true;
      }
    } else if (currentCommand == finalMot) {
      SmartDashboard.putString("Auto Command", "stage3LineupAndShoot");

      switchCommand(stage3LineupAndShoot);
      return false;
    } else if (currentCommand == stage3LineupAndShoot) {
      return true;

    }

    return false;
  }
}
