/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.ControlPanel;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotPreferences;
import frc.robot.subsystems.ControlPanel;
import frc.robot.subsystems.ControlPanel.panelColor;
import frcteam3255.robotbase.Preferences.SN_IntPreference;

public class SpinControlPanelCount extends CommandBase {
  /**
   * Creates a new SpinControlPanelCount.
   */
  private final ControlPanel m_controlPanel;
  private SN_IntPreference m_numRotations;
  private boolean m_finished;
  private double m_colorCounter = 0;
  private panelColor m_initialColor;
  private panelColor m_currentColor;

  public SpinControlPanelCount(ControlPanel subsystem, SN_IntPreference numRotations) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_numRotations = numRotations;
    m_controlPanel = subsystem;
    addRequirements(subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_initialColor = m_controlPanel.getColor();
    m_currentColor = m_initialColor;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_controlPanel.setSpeed(RobotPreferences.controlPanelSpinSpeed.getValue());
    if((m_currentColor != m_controlPanel.getColor())&&(m_controlPanel.getColor()!=panelColor.none)){
      m_colorCounter++;
    }
    if(m_controlPanel.getColor()!=panelColor.none){
      m_currentColor = m_controlPanel.getColor();
    }
    if(m_numRotations.getValue() == m_colorCounter/8){
      m_finished = true;
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_controlPanel.setSpeed(0);
    m_finished = false;
    m_colorCounter=0;
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return m_finished;
  }
}
