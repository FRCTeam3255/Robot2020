/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Turret;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.RobotPreferences;
import frcteam3255.robotbase.Preferences.SN_DoublePreference;

public class Shoot extends CommandBase {
  /**
   * Creates a new Shoot.
   */
  private Timer timer = new Timer();
  private SN_DoublePreference rpm;
  private boolean empty;

  public Shoot() {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(RobotContainer.turret);

  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    empty = false;
    // RobotContainer.turret.setShooterSpeefd();

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (RobotContainer.turret.isShooterSpedUp()) {
      if (!empty) {
        if (RobotContainer.intake.getStagedSwitch()) {
          RobotContainer.turret.finalShooterGateSetSpeed(-1);
        } else {
          empty = true;
          timer.reset();
          timer.start();

        }

      }
    }

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    RobotContainer.turret.finalShooterGateSetSpeed(0);

  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    // return ! intake.getStagedSwitch();
    return timer.hasPeriodPassed(RobotPreferences.shootDelay.getValue());
  }
}
