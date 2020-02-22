/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Intake;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.RobotPreferences;
import frcteam3255.robotbase.Preferences.SN_DoublePreference;

public class SetStepServo extends CommandBase {
    /**
     * Creates a new SetStepServo.
     */
    SN_DoublePreference pos;

    public SetStepServo(SN_DoublePreference a_pos) {
        // Use addRequirements() here to declare subsystem dependencies.
        pos = a_pos;
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        RobotContainer.intake.setServoPos(pos.getValue());
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {

    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}