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
import frc.robot.RobotContainer;
import frc.robot.RobotPreferences;
import frc.robot.commands.Config.DoDelay;
import frc.robot.commands.Drivetrain.DriveMotionProfile;
import frc.robot.commands.Turret.AlignAuto;
import frc.robot.commands.Turret.AlignVisionAuto;
import frc.robot.commands.Turret.ShootCount;

public class Autonomous extends CommandBase {

  DoDelay delay;
  AlignAuto align1;
  AlignVisionAuto align2;
  ShootCount shoot1;
  ShootCount shoot2;
  DriveMotionProfile drive1;
  DriveMotionProfile drive2;

  Command currentCommand;

  // AUTO PLAN:
  // delay - pref
  // shoot? - btn a
  // drive mp and collect - btn b
  // shoot? - btnc
  // drive mp - btn d
  // shoot - btn e

  /**
   * Creates a new Autonomous.
   */
  public Autonomous(DriveMotionProfile d1, DriveMotionProfile d2) {
    // Use addRequirements() here to declare subsystem dependencies.
    // delay
    delay = new DoDelay(RobotPreferences.autoDelay);
    // shoot 1
    align1 = new AlignAuto(RobotPreferences.firstSusanPos, RobotPreferences.firstHoodPos);
    shoot1 = new ShootCount(RobotPreferences.numToShoot);
    // drive MP and collect
    drive1 = d1;
    drive2 = d2;
    // shoot 2
    align2 = new AlignVisionAuto(RobotPreferences.shooterMaxRPM);
    shoot2 = new ShootCount(RobotPreferences.numToShoot);

    addRequirements(RobotContainer.turret);
    addRequirements(RobotContainer.drivetrain);

  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    currentCommand = delay;
    SmartDashboard.putString("Auto Command", "delay");

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
    if (currentCommand == delay) {
      if (RobotContainer.switchBoard.btn_6.get()) {
        switchCommand(align1);
      } else {
        return true;
      }
      SmartDashboard.putString("Auto Command", "align1");

    }

    else if (currentCommand == align1) {
      SmartDashboard.putString("Auto Command", "shoot1");

      switchCommand(shoot1);
      return false;
    } else if (currentCommand == shoot1) {
      SmartDashboard.putString("Auto Command", "drive1");

      if (RobotContainer.switchBoard.btn_4.get()) {
        switchCommand(drive1);
      } else {
        return true;
      }
      return false;
    } else if (currentCommand == drive1) {
      SmartDashboard.putString("Auto Command", "d2");

      switchCommand(drive2);
      return false;
    } else if (currentCommand == drive2) {
      SmartDashboard.putString("Auto Command", "a2");

      if (RobotContainer.switchBoard.btn_2.get()) {
        switchCommand(align2);
      } else {
        return true;
      }
      return false;
    } else if (currentCommand == align2) {
      SmartDashboard.putString("Auto Command", "s2");

      switchCommand(shoot2);
      return false;
    } else if (currentCommand == shoot2) {

      return true;
    }

    return false;
  }
}
