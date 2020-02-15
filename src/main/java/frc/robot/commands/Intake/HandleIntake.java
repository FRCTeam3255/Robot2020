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
import frc.robot.subsystems.Intake;

public class HandleIntake extends CommandBase {
  /**
   * Creates a new HandleIntake.
   */
  private final Intake intake;
  private boolean collectionAllowed = true;

  public HandleIntake(Intake a_intake) {
    // Use addRequirements() here to declare subsystem dependencies.
    intake = a_intake;
    addRequirements(a_intake);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    intake.collectorSetSpeed(0);

    if (intake.getCollectionSwitch()) { // s1
      intake.turretGateSetSpeed(1); // m1
      intake.initialShooterGateSetSpeed(1); // m2
    }
    if (intake.getStagedSwitch()) { // s3
      intake.initialShooterGateSetSpeed(0);
    }
    if (intake.getBottomSwitch() && intake.getStagedSwitch()) { // s2 & s3
      intake.turretGateSetSpeed(0); // m1
    }
    if (intake.getCollectionSwitch() && intake.getStagedSwitch() && intake.getBottomSwitch()) {
      collectionAllowed = false;
    } else {
      collectionAllowed = true;
    }
    if (RobotContainer.manipulator.btn_4.get() && collectionAllowed) {
      intake.collectorSetSpeed(RobotPreferences.collectorSpeed.getValue());
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
