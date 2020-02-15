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

public class SpinToColor extends CommandBase {
  /**
   * Creates a new SpinToColor.
   */
  private panelColor initialColor;
  private panelColor goalColor;
  private double speedCoefficient;
  private boolean finished;

  // TODO: don't pass in the color, have the goal color read from the FMS in
  // initialize
  public SpinToColor(panelColor a_goalColor) {

    // Use addRequirements() here to declare subsystem dependencies.
    goalColor = a_goalColor;
    addRequirements(RobotContainer.controlPanel);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    finished = false;
    speedCoefficient = 1;
    initialColor = RobotContainer.controlPanel.getColor();
    /*
     * TODO: Will need to verify that motor polarity spins the intended direction
     * for this optimization to work. Comment of what the heck this logic was trying
     * to do would have been nice too. :-)
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
    RobotContainer.controlPanel.setSpeed(speedCoefficient * RobotPreferences.controlPanelSpinSpeed.getValue());

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (!(RobotContainer.controlPanel.getColor() != goalColor)) {
      finished = true;

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
    return finished;
  }
}
