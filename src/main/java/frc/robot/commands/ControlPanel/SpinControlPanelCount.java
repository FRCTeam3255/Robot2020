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
  private final ControlPanel controlPanel;
  private SN_IntPreference numRotations;
  private boolean finished;
  private double colorCounter = 0;
  private panelColor initialColor;
  private panelColor currentColor;

  public SpinControlPanelCount(ControlPanel a_controlPanel, SN_IntPreference a_numRotations) {
    // Use addRequirements() here to declare subsystem dependencies.
    numRotations = a_numRotations;
    controlPanel = a_controlPanel;
    addRequirements(a_controlPanel);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    initialColor = controlPanel.getColor();
    currentColor = initialColor;
    finished = false;
    colorCounter = 0;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    /*
      TODO: need to review this logic together. You should only change currentColor when the count increases.
      Also, only call controlPanel.getColor() once, and use it everywhere in the command. When it's spinning during this
      command, you could get different values from each call.
    */
    controlPanel.setSpeed(RobotPreferences.controlPanelSpinSpeed.getValue());
    if ((currentColor != controlPanel.getColor()) && (controlPanel.getColor() != panelColor.none)) {
      colorCounter++;
    }
    if (controlPanel.getColor() != panelColor.none) {
      currentColor = controlPanel.getColor();
    }
    // TODO: Delete the finished variable, and move this check to isFinished.
    if (numRotations.getValue() == colorCounter / 8) {
      finished = true;
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    controlPanel.setSpeed(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return finished;
  }
}
