/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.ControlPanel;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ControlPanel;
import frcteam3255.robotbase.Preferences.SN_DoublePreference;

public class RotateControlPanel extends CommandBase {
  /**
   * Creates a new ControlPanelRotate.
   */
  private SN_DoublePreference m_speed;
  private final ControlPanel m_controlPanel;
  public RotateControlPanel(ControlPanel subsystem, SN_DoublePreference speed) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_speed = speed;
    m_controlPanel = subsystem;
    addRequirements(subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_controlPanel.setSpeed(m_speed.getValue());
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
