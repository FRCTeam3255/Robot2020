/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Turret;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.RobotPreferences;
import frcteam3255.robotbase.Preferences.SN_DoublePreference;

public class AlignVisionAuto extends CommandBase {
    /**
     * Creates a new AlignVisionAuto. Aligns turret within threshold, then spins up
     * shooter within threshold, then shoots n times
     */
    private SN_DoublePreference hoodPos;
    private Timer timer = new Timer();
    private boolean aligned;
    public FinishReason finishReason = FinishReason.NOT_FINISHED;
    private SN_DoublePreference velocity;

    public enum FinishReason {
        SUCCESS, NO_TARGET, NOT_FINISHED
    };

    public AlignVisionAuto(SN_DoublePreference a_hoodPos, SN_DoublePreference a_velocity) {
        // Use addRequirements() here to declare subsystem dependencies.
        hoodPos = a_hoodPos;
        addRequirements(RobotContainer.turret);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        finishReason = FinishReason.NOT_FINISHED;

        RobotContainer.turret.setShooterVelocity();
        RobotContainer.turret.moveHoodToDegree(hoodPos.getValue());

        aligned = false;
        timer.reset();
        timer.start();

    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {

        if (RobotContainer.vision.visionHasTarget() && !(RobotContainer.vision.isXFinished())) {

            timer.reset();
            timer.start();

            RobotContainer.turret
                    .setSusanSpeed(RobotContainer.vision.getVisionXError() * RobotPreferences.susanVisionP.getValue());
        }

    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        RobotContainer.turret.setSusanSpeed(0);
        RobotContainer.turret.setHoodSpeed(0);
        RobotContainer.turret.finalShooterGateSetSpeed(0);
        // RobotContainer.turret.setShooterSpeefd(0);

    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        // // return true if X error and hood are within tolerance
        if (RobotContainer.vision.isXFinished()) {
            finishReason = FinishReason.SUCCESS;

            return true;
        }

        // return true if no vision for timeout
        if (timer.hasPeriodPassed(RobotPreferences.visionTimeout.getValue())) {

            finishReason = FinishReason.NO_TARGET;
            return true;
        }

        return false;
    }
}
