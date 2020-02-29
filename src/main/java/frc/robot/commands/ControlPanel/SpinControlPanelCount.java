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
import frcteam3255.robotbase.Preferences.SN_DoublePreference;
import frcteam3255.robotbase.Preferences.SN_IntPreference;

public class SpinControlPanelCount extends CommandBase {
  /**
   * Creates a new SpinControlPanelCount.
   */
  private SN_DoublePreference numRotations;
  private SN_IntPreference numColorSamples;
  private int colorTransitions = 0;
  private panelColor currentColor;
  private panelColor priorColor;
  private int numNewColor;

  public SpinControlPanelCount(SN_DoublePreference a_numRotations, SN_IntPreference a_numColorSamples) {
    // Use addRequirements() here to declare subsystem dependencies.
    numRotations = a_numRotations;
    numColorSamples = a_numColorSamples;
    addRequirements(RobotContainer.controlPanel);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    currentColor = RobotContainer.controlPanel.getColor();
    priorColor = currentColor;
    numNewColor = 0;
    colorTransitions = 0;
    RobotContainer.controlPanel.setSpeed(RobotPreferences.controlPanelSpinSpeed.getValue());
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    panelColor newColor = RobotContainer.controlPanel.getColor();

    // ignore color if we don't know what it is
    if (newColor == panelColor.none){
      return;
    }

    // check to see if the new color we are looking at is different than the current pie slice color
    if (newColor != currentColor) {
      // count the number of times we have seen this new color
      if (newColor != priorColor) {
        numNewColor = 1;
      }
      else {
        numNewColor = numNewColor + 1;
      }

      // if we have seen the new color enough times, change the current color we are on, and count a transition
      if (numNewColor >= numColorSamples.getValue()){
        currentColor = newColor;
        numNewColor = 0;
        colorTransitions = colorTransitions + 1;
      }
    }

    // remember the color we just saw for the next time through here
    priorColor = newColor;
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    RobotContainer.controlPanel.setSpeed(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if ((colorTransitions / 8.0) >= numRotations.getValue()) {
      return true;
    }
    return false;
  }
}
