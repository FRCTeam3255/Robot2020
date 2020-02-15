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
  private final Turret turret;
  private final SN_DoublePreference speed;
  private boolean instant;

  public SetShooterVelocity(Turret a_turret, SN_DoublePreference a_speed, boolean a_instant) {
    // Use addRequirements() here to declare subsystem dependencies.
    turret = a_turret;
    speed = a_speed;
    instant = a_instant;
    addRequirements(a_turret);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    turret.setShooterVelocity(speed.getValue());

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(final boolean interrupted) {
    if (!instant) {
      turret.setShooterSpeed(0);
    }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (instant) {
      return turret.isShooterSpedUp(speed.getValue());
    } else {
      return false;

    }
  }
}
