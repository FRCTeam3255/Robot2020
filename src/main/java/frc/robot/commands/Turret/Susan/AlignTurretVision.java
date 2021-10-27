/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Turret.Susan;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.RobotPreferences;

public class AlignTurretVision extends CommandBase {
  /**
   * Creates a new AlignTurretVision.
   */

  public AlignTurretVision() {
    // Use addRequirements() here to declare subsystem dependencies.

    // addRequirements(RobotContainer.turret);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    RobotContainer.shooter.setGoalVelocity(RobotPreferences.shooterMaxRPM.getValue());

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (RobotContainer.vision.visionHasTarget()) {

      RobotContainer.susan.turnSusanToDegree(RobotContainer.susan.getSusanPosition()
          + RobotContainer.vision.getVisionXError() + RobotContainer.vision.getVisionInnerOffset());

      if (RobotContainer.switchBoard.btn_9.get()) {
        RobotContainer.hood.moveHoodToDegree(RobotContainer.vision.getHoodAngle());
      }
    } else {

      // RobotContainer.turret.setHoodSpeed(0);
      RobotContainer.susan.setSusanSpeed(0);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    // RobotContainer.turret.setHoodSpeed(0);
    RobotContainer.susan.setSusanSpeed(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
