// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Climber;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;

public class MoveClimber extends CommandBase {
  /** Creates a new MoveClimber. */
  public MoveClimber() {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(RobotContainer.climber);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    // if the switch is ON and the climber is braking, turn off the brakes
    if (RobotContainer.switchBoard.btn_8.get() && RobotContainer.climber.isBrake()) {
      RobotContainer.climber.retractBrake();
    } else

    // if the switch is OFF and the brakes are OFF, turn on the brakes
    if (!RobotContainer.switchBoard.btn_8.get() && !RobotContainer.climber.isBrake()) {
      RobotContainer.climber.deployBrake();
    } else

    // if the switch is ON and the brakes are OFF, manip stick controls climber
    if (RobotContainer.switchBoard.btn_8.get() && !RobotContainer.climber.isBrake()) {
      RobotContainer.climber.setClimbTalon(RobotContainer.manipulator.getYAxis());
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    RobotContainer.climber.deployBrake();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
