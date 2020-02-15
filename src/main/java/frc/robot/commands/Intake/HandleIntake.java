/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Intake;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.RobotPreferences;

public class HandleIntake extends CommandBase {
  /**
   * Creates a new HandleIntake.
   */
  private boolean collectionAllowed;

  public HandleIntake() {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(RobotContainer.intake);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    collectionAllowed = true;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    RobotContainer.intake.collectorSetSpeed(0);

    if (RobotContainer.intake.getCollectionSwitch()) { // s1
      RobotContainer.intake.turretGateSetSpeed(1); // m1
      RobotContainer.intake.initialShooterGateSetSpeed(1); // m2
    }
    if (RobotContainer.intake.getStagedSwitch()) { // s3
      RobotContainer.intake.initialShooterGateSetSpeed(0);
    }
    if (RobotContainer.intake.getBottomSwitch() && RobotContainer.intake.getStagedSwitch()) { // s2 & s3
      RobotContainer.intake.turretGateSetSpeed(0); // m1
    }
    if (RobotContainer.intake.getCollectionSwitch() && RobotContainer.intake.getStagedSwitch()
        && RobotContainer.intake.getBottomSwitch()) {
      collectionAllowed = false;
    } else {
      collectionAllowed = true;
    }
    if (RobotContainer.manipulator.btn_4.get() && collectionAllowed) {
      RobotContainer.intake.collectorSetSpeed(RobotPreferences.collectorSpeed.getValue());
    }

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