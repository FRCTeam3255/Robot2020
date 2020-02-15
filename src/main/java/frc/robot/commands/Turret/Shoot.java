/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Turret;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.RobotPreferences;

public class Shoot extends CommandBase {
  /**
   * Creates a new Shoot.
   */
  public Shoot() {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(RobotContainer.turret);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    RobotContainer.turret.setShooterVelocity(RobotPreferences.shooterMaxRPM.getValue());

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (RobotContainer.turret.isShooterSpedUp(RobotPreferences.shooterMaxRPM.getValue())) {
      RobotContainer.turret.finalShooterGateSetSpeed(1);

    } else {
      RobotContainer.turret.finalShooterGateSetSpeed(0);

    }

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    RobotContainer.turret.finalShooterGateSetSpeed(0);
    RobotContainer.turret.setShooterSpeed(RobotPreferences.shooterNoSpeed.getValue());

  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    // return ! intake.getStagedSwitch();
    return false;
  }
}
