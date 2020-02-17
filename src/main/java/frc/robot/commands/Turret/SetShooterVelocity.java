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

public class SetShooterVelocity extends CommandBase {
  /**
   * Creates a new SetShooterVelocity.
   */
  private final SN_DoublePreference speed;
  private boolean instant;

  public SetShooterVelocity(SN_DoublePreference a_speed, boolean a_instant) {
    // Use addRequirements() here to declare subsystem dependencies.
    speed = a_speed;
    instant = a_instant;
    addRequirements(RobotContainer.turret);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    RobotContainer.turret.setShooterVelocity(speed.getValue());
    RobotContainer.turret.shooterVelocity();

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(final boolean interrupted) {
    if (!instant) {
    }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (instant) {
      /*
       * TODO: Seems like we should check to see if the velocity is what we set it to.
       * speed.getvalue could have changed between when we set it, and when we get it.
       * I think the shooter shouldn't have to be passed a goalSpeed. I bet you can
       * find out from the talon what the setpoint was, rather than having to provide
       * the setpoint again. Since there is no method, just cache the variable and
       * check against that.
       */
      return RobotContainer.turret.isShooterSpedUp();
    } else {
      return false;

    }
  }
}
