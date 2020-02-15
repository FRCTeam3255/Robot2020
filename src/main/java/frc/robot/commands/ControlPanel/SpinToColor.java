/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.ControlPanel;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotPreferences;
import frc.robot.subsystems.ControlPanel;
import frc.robot.subsystems.ControlPanel.panelColor;

public class SpinToColor extends CommandBase {
  /**
   * Creates a new SpinToColor.
   */
  private final ControlPanel controlPanel;
  private panelColor initialColor;
  private panelColor goalColor;
  private double speedCoefficient = 1;
  private boolean finished = false;

  // TODO: don't pass in the color, have the goal color read from the FMS in initialize
  public SpinToColor(ControlPanel a_controlPanel, panelColor a_goalColor) {
    // Use addRequirements() here to declare subsystem dependencies.
    goalColor = a_goalColor;
    controlPanel = a_controlPanel;
    addRequirements(a_controlPanel);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    initialColor = controlPanel.getColor();
    /*
     TODO: Will need to verify that motor polarity spins the intended direction for this optimization to work.
     Comment of what the heck this logic was trying to do would have been nice too. :-)
    */
    if (initialColor == panelColor.red && goalColor == panelColor.yellow) {
      speedCoefficient = -1;
    } else if (initialColor == panelColor.green && goalColor == panelColor.red) {
      speedCoefficient = -1;
    } else if (initialColor == panelColor.blue && goalColor == panelColor.green) {
      speedCoefficient = -1;
    } else if (initialColor == panelColor.yellow && goalColor == panelColor.blue) {
      speedCoefficient = -1;
    } else {
      speedCoefficient = 1;
    }
    SmartDashboard.putString("init color", controlPanel.getColor().toString());
    SmartDashboard.putNumber("coeff", speedCoefficient);
    controlPanel.setSpeed(speedCoefficient * RobotPreferences.controlPanelSpinSpeed.getValue());
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // TODO: move this to isFinished and get rid of the finished variable
    if (!(controlPanel.getColor() != goalColor)) {
      finished = true;

    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    controlPanel.setSpeed(0);
    finished = false;

  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return finished;
  }
}
