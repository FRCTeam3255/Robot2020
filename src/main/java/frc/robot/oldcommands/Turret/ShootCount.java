/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.oldcommands.Turret;

import edu.wpi.first.wpilibj.Timer;
// import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.RobotPreferences;
// import frcteam3255.robotbase.Preferences.SN_DoublePreference;
import frcteam3255.robotbase.Preferences.SN_IntPreference;

public class ShootCount extends CommandBase {
  /**
   * Creates a new ShootCount.
   */
  private Timer timer = new Timer();

  private enum stateType {
    SPINNING, SHOOTING, STAGING, NONE
  };

  private stateType state = stateType.NONE;
  private int numShot;
  private SN_IntPreference numToShoot;

  public ShootCount(SN_IntPreference a_numToShoot) {
    // Use addRequirements() here to declare subsystem dependencies.
    numToShoot = a_numToShoot;
    addRequirements(RobotContainer.shooter);
    addRequirements(RobotContainer.intake);

  }

  // Called when the command is initially scheduled
  @Override
  public void initialize() {

    numShot = 0;
    startStaging();
  }

  public void startStaging() {
    RobotContainer.shooter.finalShooterGateSetSpeed(0);
    state = stateType.STAGING;
    RobotContainer.shooter.setShooterVelocity();
    RobotContainer.intake.turretGateSetSpeed(1 * RobotPreferences.turretGateSpeed.getValue());
    RobotContainer.intake.initialShooterGateSetSpeed(1 * RobotPreferences.initialGateSpeed.getValue());
    timer.reset();
    timer.start();
    // RobotContainer.turret.setShooterSpeefd();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    RobotContainer.shooter.setShooterSpeed(0);
    RobotContainer.shooter.finalShooterGateSetSpeed(0);
    RobotContainer.intake.turretGateSetSpeed(0);
    RobotContainer.intake.initialShooterGateSetSpeed(0);

  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (state == stateType.STAGING) {
      if (RobotContainer.intake.getStagedSwitch()
          || timer.hasPeriodPassed(RobotPreferences.stagingTimeout.getValue())) {
        timer.reset();
        timer.start();
        RobotContainer.intake.turretGateSetSpeed(0);
        RobotContainer.intake.initialShooterGateSetSpeed(0);
        state = stateType.SPINNING;
      }
    } else if (state == stateType.SPINNING) {
      if (RobotContainer.shooter.isShooterSpedUp() || timer.hasPeriodPassed(RobotPreferences.spinupTimeout.getValue())) {
        state = stateType.SHOOTING;
        RobotContainer.shooter.finalShooterGateSetSpeed(-1);
        timer.reset();
        timer.start();
      }
    } else if (state == stateType.SHOOTING) {
      if (timer.hasPeriodPassed(RobotPreferences.shootingTimeout.getValue())) {
        RobotContainer.shooter.finalShooterGateSetSpeed(0);
        numShot = numShot + 1;
        if (numShot < numToShoot.getValue()) {
          startStaging();
        } else {
          return true;
        }
      }
    }
    return false;
  }
}
