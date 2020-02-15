/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Turret;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotPreferences;
import frc.robot.subsystems.Turret;
import frc.robot.subsystems.Vision;

public class AlignTurretVision extends CommandBase {
  /**
   * Creates a new AlignTurretVision.
   */
  private final Turret turret;
  private final Vision vision;

  public AlignTurretVision(Turret a_turret, Vision a_vision) {
    // Use addRequirements() here to declare subsystem dependencies.
    turret = a_turret;
    vision = a_vision;

    addRequirements(a_turret);
    addRequirements(a_vision);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (vision.visionHasTarget()) {

      turret.setSusanSpeed(vision.getVisionXError() * RobotPreferences.susanVisionP.getValue());
      turret.hoodMoveToDegree(vision.getHoodVisionPosition());
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    turret.setSusanSpeed(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
