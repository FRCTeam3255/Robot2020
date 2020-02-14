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
import frcteam3255.robotbase.Preferences.SN_DoublePreference;

public class HandleIntake extends CommandBase {
  /**
   * Creates a new HandleIntake.
   */
  private final Intake m_intake;
  private boolean collectionAllowed;

  public HandleIntake(Intake subsystem) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_intake = subsystem;
    collectionAllowed = true;
    addRequirements(subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    m_intake.collectorSetSpeed(0);

    if (m_intake.getCollectionSwitch()) { // s1
      m_intake.turretGateSetSpeed(1); // m1
      m_intake.initialShooterGateSetSpeed(1); // m2
    }
    if (m_intake.getStagedSwitch()) { // s3
      m_intake.initialShooterGateSetSpeed(0);
    }
    if (m_intake.getBottomSwitch() && m_intake.getStagedSwitch()) { // s2 & s3
      m_intake.turretGateSetSpeed(0); // m1
    }
    if (m_intake.getCollectionSwitch() && m_intake.getStagedSwitch() && m_intake.getBottomSwitch()) {
      collectionAllowed = false;
    } else {
      collectionAllowed = true;
    }
    if (RobotContainer.manipulator.btn_4.get() && collectionAllowed) {
      m_intake.collectorSetSpeed(RobotPreferences.collectorSpeed.getValue());
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
