/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Turret;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frcteam3255.robotbase.Preferences.SN_DoublePreference;

public class NudgeHood extends CommandBase {
  /**
   * Creates a new NudgeHood.
   */
  private SN_DoublePreference degrees;

  public NudgeHood(SN_DoublePreference a_degrees) {
    // Use addRequirements() here to declare subsystem dependencies.
    degrees = a_degrees;
    addRequirements(RobotContainer.turret);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {

    RobotContainer.turret.hoodMoveToDegree(RobotContainer.turret.getHoodPosition() + degrees.getValue());

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
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
