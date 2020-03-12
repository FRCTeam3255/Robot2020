/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.oldcommands.Turret;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frcteam3255.robotbase.Preferences.SN_DoublePreference;

public class SetHoodPositionAuto extends CommandBase {
  /**
   * Creates a new SetHoodPositionAuto.
   */
  private SN_DoublePreference degrees;
  private SN_DoublePreference velocity;

  public SetHoodPositionAuto(SN_DoublePreference a_degrees, SN_DoublePreference a_velocity) {
    // Use addRequirements() here to declare subsystem dependencies.
    degrees = a_degrees;
    velocity = a_velocity;
    addRequirements(RobotContainer.hood);
    addRequirements(RobotContainer.shooter);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {

    RobotContainer.hood.moveHoodToDegree(degrees.getValue());
    RobotContainer.shooter.setShooterSetpoint(velocity.getValue());
    RobotContainer.shooter.setShooterVelocity();

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
    return RobotContainer.hood.hoodFinished();
  }
}
