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

public class CollectBall extends CommandBase {
    /**
     * Creates a new CollectBall.
     */
    double initialGateSpeed;
    double turretGateSpeed;

    public CollectBall() {
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(RobotContainer.intake);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        turretGateSpeed = 0;
        initialGateSpeed = 0;
        RobotContainer.intake.collectorSetSpeed(RobotPreferences.collectorSpeed.getValue());
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        turretGateSpeed = 1;
        initialGateSpeed = 1;

        if (RobotContainer.intake.getStagedSwitch()) {
            initialGateSpeed = 0;

            if (RobotContainer.intake.getBottomSwitch()) {
                turretGateSpeed = 0;
            }
        }
        RobotContainer.intake.turretGateSetSpeed(turretGateSpeed * RobotPreferences.turretGateSpeed.getValue());
        RobotContainer.intake.initialShooterGateSetSpeed(initialGateSpeed * RobotPreferences.initialGateSpeed.getValue());    
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        RobotContainer.intake.collectorSetSpeed(0);
        RobotContainer.intake.turretGateSetSpeed(0);
        RobotContainer.intake.initialShooterGateSetSpeed(0);    
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}
