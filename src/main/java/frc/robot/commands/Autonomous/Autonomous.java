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
import frc.robot.commands.Drivetrain.DriveToBall;
import frc.robot.commands.Turret.AlignVisionAuto;
import frc.robot.commands.Turret.SetHoodPositionAuto;
import frc.robot.commands.Turret.ShootCount;

public class Autonomous extends CommandBase {

  DoDelay delay;
  SetHoodPositionAuto align1;
  ShootCount shoot1;
  DriveMotionProfile drive1;
  AlignVisionAuto align2;
  ShootCount shoot2;
  DriveMotionProfile rendevouzDrive;
  AlignVisionAuto align3;
  ShootCount shoot3;

  Command currentCommand;

  /**
   * Creates a new Autonomous.
   */
  public Autonomous() {
    // Use addRequirements() here to declare subsystem dependencies.
    // delay = new DoDelay(RobotPreferences.autoDelay);
    // align1 = new SetHoodPositionAuto(a_degrees, a_velocity);
    // shoot1 = new ShootCount(a_numToShoot);
    // drive1 = new DriveMotionProfile(a_leftName, a_rightName);
    // align2 = new AlignVisionAuto(a_hoodPos, a_velocity);
    // shoot2 = new ShootCount(a_numToShoot);
    // rendevouzDrive = new DriveMotionProfile(a_leftName, a_rightName);
    // align3 = new AlignVisionAuto(a_hoodPos, a_velocity);
    // shoot3 = new ShootCount(a_numToShoot);

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
    // if (currentCommand == delay) {
    // switchCommand();

    // }

    // else if (currentCommand == comd2) {
    // SmartDashboard.putString("Auto Command", "failMot");

    // switchCommand(comd);
    // return false;
    // } else if (currentCommand == grabBallMot) {

    // return true;
    // }

    return false;
  }
}
