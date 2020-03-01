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
import frc.robot.commands.Drivetrain.DriveMotionProfile;
import frc.robot.commands.Turret.AlignAuto;
import frc.robot.commands.Turret.AlignVisionAuto;
import frc.robot.commands.Turret.ShootCount;
import frc.robot.commands.Turret.AlignVisionAuto.FinishReason;

public class Autonomous extends CommandBase {

  AlignAuto auto1Align1;
  AlignAuto auto2Align1;
  AlignAuto align1;
  AlignVisionAuto align2;
  AlignVisionAuto align3;
  ShootCount shoot1;
  ShootCount shoot2;
  ShootCount shoot3;
  DriveMotionProfile auto1Drive1;
  DriveMotionProfile auto1Drive2;
  DriveMotionProfile auto1Drive3;
  DriveMotionProfile auto2Drive1;
  DriveMotionProfile auto2Drive2;
  DriveMotionProfile auto2Drive3;
  DriveMotionProfile drive1;
  DriveMotionProfile drive2;
  DriveMotionProfile drive3;

  Command currentCommand;

  // AUTO PLAN:
  // turn on collector at start
  // align turret, hood, and delay (using preferences)
  // shoot - (button can disable shoot)
  // drive mp1 & mp2 - (button can disable drive and force exit)
  // align with vision & shoot - (button can disable shoot, and won't shoot if no
  // vision)
  // drive mp3 - (button can disable drive and force exit)
  // align with vision & shoot - (button can disalbe shoot, and won't shoot if no
  // vision)
  // turn off collector at end

  /**
   * Creates a new Autonomous.
   */
  public Autonomous() {
    // create motion profiles
    auto1Drive1 = new DriveMotionProfile("auto1D1_left.csv", "auto1D1_right.csv");
    auto1Drive2 = new DriveMotionProfile("auto1D2_left.csv", "auto1D2_right.csv");
    auto1Drive3 = new DriveMotionProfile("auto1D3_left.csv", "auto1D3_right.csv");

    auto2Drive1 = new DriveMotionProfile("auto2D1_left.csv", "auto2D1_right.csv");
    auto2Drive2 = new DriveMotionProfile("auto2D2_left.csv", "auto2D2_right.csv");
    auto2Drive3 = new DriveMotionProfile("auto2D3_left.csv", "auto2D3_right.csv");

    // create initial alignment commands
    auto1Align1 = new AlignAuto(RobotPreferences.auto1SusanPos, RobotPreferences.auto1HoodPos,
        RobotPreferences.auto1Delay);
    auto2Align1 = new AlignAuto(RobotPreferences.auto2SusanPos, RobotPreferences.auto2HoodPos,
        RobotPreferences.auto2Delay);

    // shoot 1
    shoot1 = new ShootCount(RobotPreferences.numToShoot);

    // shoot 2
    align2 = new AlignVisionAuto(RobotPreferences.shooterMaxRPM);
    shoot2 = new ShootCount(RobotPreferences.numToShoot);

    // shoot 3
    align3 = new AlignVisionAuto(RobotPreferences.shooterMaxRPM);
    shoot3 = new ShootCount(RobotPreferences.numToShoot);

    addRequirements(RobotContainer.turret);
    addRequirements(RobotContainer.drivetrain);
  }

  // reloadMotionProfiles causes all internal motion profiles to be reloaded from
  // their file
  public void reloadMotionProfiles() {
    auto1Drive1.reload();
    auto1Drive2.reload();
    auto1Drive3.reload();

    auto2Drive1.reload();
    auto2Drive2.reload();
    auto2Drive3.reload();
  }

  // displays a command on the dashboard of the current command name
  private void updateDashboard(String cmdName) {
    SmartDashboard.putString("Auto Command", cmdName);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    // turn on the collector
    RobotContainer.intake.collectorSetSpeed(RobotPreferences.collectorSpeed.getValue());

    // determine which set of motion profiles and alignment commands to use based on
    // switchboard button 1
    if (RobotContainer.switchBoard.btn_1.get() == false) {
      align1 = auto1Align1;

      drive1 = auto1Drive1;
      drive2 = auto1Drive2;
      drive3 = auto1Drive3;
    } else {
      align1 = auto2Align1;

      drive1 = auto2Drive1;
      drive2 = auto2Drive2;
      drive3 = auto2Drive3;
    }

    // run the delay command
    currentCommand = align1;
    updateDashboard("align1");
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
    currentCommand.end(interrupted);
    updateDashboard("Auto Over");

    // turn off the collector
    RobotContainer.intake.collectorSetSpeed(0);
  }

  void switchCommand(final Command cmd) {
    currentCommand.end(false);

    currentCommand = cmd;

    cmd.initialize();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    // if the current command isn't finished, then return false
    if (currentCommand.isFinished() == false) {
      return false;
    }

    // determine what to do if the align1 command just ended
    if (currentCommand == align1) {
      // determine whether we should perform shoot1 or not
      if (RobotContainer.switchBoard.btn_2.get()) {
        // run the shoot1 command
        updateDashboard("shoot1");
        switchCommand(shoot1);
        return false;
      } else {
        // if drive1 & 2 is enabled, then perform drive1 command, else exit
        if (RobotContainer.switchBoard.btn_3.get()) {
          // run the drive1 command
          updateDashboard("drive1");
          switchCommand(drive1);
          return false;
        } else {
          return true;
        }
      }
    }

    // determine what to do if the shoot1 command just ended
    if (currentCommand == shoot1) {
      // if drive1 & 2 is enabled, then perform drive1 command, else exit
      if (RobotContainer.switchBoard.btn_3.get()) {
        // run the drive1 command
        updateDashboard("drive1");
        switchCommand(drive1);
        return false;
      } else {
        return true;
      }
    }

    // determine what to do if the drive1 command just ended
    if (currentCommand == drive1) {
      // switch to the drive2 command
      updateDashboard("drive2");
      switchCommand(drive2);
      return false;
    }

    // determine what to do if the drive2 command just ended
    if (currentCommand == drive2) {
      // if shoot2 is enabled, then perform align2 command
      if (RobotContainer.switchBoard.btn_4.get()) {
        updateDashboard("align2");
        switchCommand(align2);
        return false;
      } else {
        // if drive3 is enabled, then perform drive3, else exit
        if (RobotContainer.switchBoard.btn_5.get()) {
          updateDashboard("drive3");
          switchCommand(drive3);
          return false;
        } else {
          return true;
        }
      }
    }

    // determine what to do if the align2 command just ended
    if (currentCommand == align2) {
      // if the align2 command finished successfully, switch to the shoot2 command
      if (align2.finishReason == FinishReason.SUCCESS) {
        updateDashboard("shoot2");
        switchCommand(shoot2);
        return false;
      }

      // if drive3 is enabled, then perform drive3, else exit
      if (RobotContainer.switchBoard.btn_5.get()) {
        updateDashboard("drive3");
        switchCommand(drive3);
        return false;
      } else {
        return true;
      }
    }

    // determine what to do if the shoot2 command just ended
    if (currentCommand == shoot2) {
      // if drive3 is enabled, then perform drive3, else exit
      if (RobotContainer.switchBoard.btn_5.get()) {
        updateDashboard("drive3");
        switchCommand(drive3);
        return false;
      } else {
        return true;
      }
    }

    // determine what to do if the drive3 command just ended
    if (currentCommand == drive3) {
      // if shoot3 is enabled, then perform align3, else exit
      if (RobotContainer.switchBoard.btn_6.get()) {
        updateDashboard("align3");
        switchCommand(align3);
        return false;
      } else {
        return true;
      }
    }

    // determine what to do if the align3 command just ended
    if (currentCommand == align3) {
      // if the align3 command finished successfully, switch to the shoot3 command
      if (align3.finishReason == FinishReason.SUCCESS) {
        updateDashboard("shoot3");
        switchCommand(shoot3);
        return false;
      }

      return true;
    }

    // determine what to do if the shoot3 command just ended
    if (currentCommand == shoot3) {
      // exit autonomous
      return true;
    }

    return false;
  }
}
