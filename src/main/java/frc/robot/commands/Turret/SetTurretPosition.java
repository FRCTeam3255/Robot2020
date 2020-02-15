/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Turret;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Turret;
import frcteam3255.robotbase.Preferences.SN_DoublePreference;

public class SetTurretPosition extends CommandBase {
  /**
   * Creates a new SetTurretPosition.
   */
  private final Turret turret;
  private SN_DoublePreference degrees;

  public SetTurretPosition(Turret a_turret, SN_DoublePreference a_degrees) {
    // Use addRequirements() here to declare subsystem dependencies.
    turret = a_turret;
    degrees = a_degrees;
    addRequirements(a_turret);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    turret.susanTurnToDegree(degrees.getValue());
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
