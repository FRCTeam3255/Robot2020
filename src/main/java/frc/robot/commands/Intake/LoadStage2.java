/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Intake;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;
import frcteam3255.robotbase.Preferences.SN_DoublePreference;

public class LoadStage2 extends CommandBase {
  /**
   * Creates a new LoadStage2.
   */
  private final Intake m_intake;
  private SN_DoublePreference m_speed;
  private boolean m_finished;
  public LoadStage2(Intake subsystem) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_intake = subsystem;
    m_speed = speed;
    addRequirements(subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_intake.initialShooterGateSetSpeed(1);
    if(m_intake.getStagedSwitch()){
      m_finished = true;
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return m_finished;
  }
}
