/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Turret;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotPreferences;
import frc.robot.subsystems.Turret;

public class Shoot extends CommandBase {
  /**
   * Creates a new Shoot.
   */
  private final Turret turret;

  public Shoot(Turret a_turret) {
    // Use addRequirements() here to declare subsystem dependencies.
    turret = a_turret;
    addRequirements(a_turret);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    turret.setShooterVelocity(RobotPreferences.shooterMaxRPM.getValue());

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (turret.isShooterSpedUp(RobotPreferences.shooterMaxRPM.getValue())) {
      turret.finalShooterGateSetSpeed(1);

    } else {
      turret.finalShooterGateSetSpeed(0);

    }

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    turret.finalShooterGateSetSpeed(0);
    turret.setShooterSpeed(RobotPreferences.shooterNoSpeed.getValue());

  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    // return ! intake.getStagedSwitch();
    return false;
  }
}
