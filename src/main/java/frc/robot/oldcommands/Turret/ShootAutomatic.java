/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Turret;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;

public class ShootAutomatic extends CommandBase {
    /**
     * Creates a new AlignAndShootToPos. Aligns turret within threshold, then spins
     * up shooter within threshold, then shoots n times
     */

    public ShootAutomatic() {
        // Use addRequirements() here to declare subsystem dependencies.

        addRequirements(RobotContainer.hood);
        addRequirements(RobotContainer.shooter);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        RobotContainer.shooter.configureShooter();
        // RobotContainer.turret.setShooterVelocity(RobotPreferences.shooterMaxRPM.getValue());
        // RobotContainer.turret.shooterVelocity();
        RobotContainer.hood.moveHoodToDegree(RobotContainer.hood.getHoodPosition());
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {

        if (RobotContainer.shooter.isShooterSpedUp()) {

            RobotContainer.shooter.finalShooterGateSetSpeed(-1);

        } else {
            RobotContainer.shooter.finalShooterGateSetSpeed(0);
        }

    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        RobotContainer.shooter.finalShooterGateSetSpeed(0);
        RobotContainer.hood.setHoodSpeed(0);

        // RobotContainer.turret.setShooterSpeed(RobotPreferences.shooterNoSpeed.getValue());
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        // // return true if X error and hood are within tolerance
        return false;
    }
}
