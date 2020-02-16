/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.ControlPanel;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.RobotPreferences;
import frc.robot.subsystems.ControlPanel.panelColor;
import frcteam3255.robotbase.Preferences.SN_IntPreference;

public class SpinControlPanelCount extends CommandBase {
  /**
   * Creates a new SpinControlPanelCount.
   */
  private SN_IntPreference numRotations;
  private double colorCounter = 0;
  private panelColor priorColor;

  public SpinControlPanelCount(SN_IntPreference a_numRotations) {
    // Use addRequirements() here to declare subsystem dependencies.
    numRotations = a_numRotations;
    addRequirements(RobotContainer.controlPanel);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    priorColor = RobotContainer.controlPanel.getColor();
    colorCounter = 0;
    RobotContainer.controlPanel.setSpeed(RobotPreferences.controlPanelSpinSpeed.getValue());
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    panelColor color = RobotContainer.controlPanel.getColor();
    if ((priorColor != color) && (color != panelColor.none)) {
      colorCounter++;
      priorColor = color;
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    RobotContainer.controlPanel.setSpeed(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (numRotations.getValue() == colorCounter / 8) {
      return true;
    }
    return false;
  }
}
