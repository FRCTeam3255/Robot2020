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

public class SetShooterVelocity extends CommandBase {
  /**
   * Creates a new SetShooterVelocity.
   */
  private final Turret m_turret;
  private final SN_DoublePreference m_speed;
  private boolean m_instant;

  public SetShooterVelocity(final Turret subsystem, final SN_DoublePreference speed, boolean instant) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_turret = subsystem;
    m_speed = speed;
    m_instant = instant;
    addRequirements(subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_turret.setShooterVelocity(m_speed.getValue());

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(final boolean interrupted) {
    if (!m_instant) {
      m_turret.setShooterSpeed(0);
    }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (m_instant) {
      return m_turret.isShooterSpedUp(m_speed.getValue());
    } else {
      return false;

    }
  }
}
