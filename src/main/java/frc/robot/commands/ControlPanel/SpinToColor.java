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

public class SpinToColor extends CommandBase {
  /**
   * Creates a new SpinToColor.
   */
  private final ControlPanel m_controlPanel;
  private panelColor m_initialColor;
  private panelColor m_goalColor;
  private double m_speedCoefficient = 1;
  private boolean m_finished = false;
  public SpinToColor(ControlPanel subsystem, panelColor goalColor) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_goalColor = goalColor;
    m_controlPanel = subsystem;
    addRequirements(subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    if(m_initialColor == panelColor.red && m_goalColor == panelColor.yellow){
      m_speedCoefficient = -1;
    }else if(m_initialColor == panelColor.green && m_goalColor == panelColor.red){
      m_speedCoefficient = -1;
    }else if(m_initialColor == panelColor.blue && m_goalColor == panelColor.green){
      m_speedCoefficient = -1;
    }else if(m_initialColor == panelColor.yellow && m_goalColor == panelColor.blue){
      m_speedCoefficient = -1;
    }
    
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(m_controlPanel.getColor()!=m_goalColor){
      m_controlPanel.setSpeed(m_speedCoefficient*RobotPreferences.controlPanelSpinSpeed.getValue());
    }else{
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
