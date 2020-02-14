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

// TODO - make this have a timeout
public class Shoot extends CommandBase {
  /**
   * Creates a new Shoot.
   */
  private final Turret m_turret;

  public Shoot(Turret subsystem) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_turret = subsystem;
    addRequirements(subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_turret.finalShooterGateSetSpeed(1);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_turret.finalShooterGateSetSpeed(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    // return !m_intake.getStagedSwitch();
    return false;
  }
}
