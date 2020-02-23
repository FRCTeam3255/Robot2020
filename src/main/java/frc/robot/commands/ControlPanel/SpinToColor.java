/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.ControlPanel;

import edu.wpi.first.wpilibj.DriverStation;
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
  String gameData;

  public SpinToColor() {

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(RobotContainer.controlPanel);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    finished = false;
    speedCoefficient = 1;

    gameData = DriverStation.getInstance().getGameSpecificMessage();
    if (gameData.length() > 0) {
      switch (gameData.charAt(0)) {
      case 'B':
        // Blue case code
        initialColor = panelColor.blue;

        break;
      case 'G':
        // Green case code
        initialColor = panelColor.green;

        break;
      case 'R':
        // Red case code

        break;
      case 'Y':
        // Yellow case code
        initialColor = panelColor.yellow;

        break;
      default:
        // This is corrupt data
        finished = true;
        break;
      }
    } else {
      // Code for no data received yet
    }
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
