/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Turret;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.RobotPreferences;

public class AlignAndShoot extends CommandBase {
    /**
     * Creates a new AlignAndShoot. Aligns turret within threshold, then spins up
     * shooter within threshold, then shoots n times
     */
    private int numShotsTodo;
    private int numShots;
    private boolean success;
    private boolean noTarget;
    private boolean aligned;
    private int timeout;
    private boolean hasCounted;

    public enum FinishReason {
        SUCCESS, NO_TARGET, NOT_FINISHED
    };

    // TODO: Need to always set finishReason to NOT_FINISHED in initialize. True for all such commands.
    public FinishReason finishReason = FinishReason.NOT_FINISHED;

    public AlignAndShoot(int a_numShots) {
        // Use addRequirements() here to declare subsystem dependencies.

        numShotsTodo = a_numShots;
        addRequirements(RobotContainer.turret);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        success = false;
        noTarget = false;
        aligned = false;
        timeout = 0;
        hasCounted = false;
        numShots = 0;
        RobotContainer.turret.setShooterVelocity(RobotPreferences.shooterMaxRPM.getValue());
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {

        if (!aligned) {

            if (timeout > RobotPreferences.visionTimeout.getValue()) {

                noTarget = true;
            }
            if (RobotContainer.vision.visionHasTarget()
                    && !(RobotContainer.vision.isXFinished() && RobotContainer.turret.hoodFinished())) {
                timeout = 0;

                RobotContainer.turret.setSusanSpeed(
                        RobotContainer.vision.getVisionXError() * RobotPreferences.susanVisionP.getValue());
                RobotContainer.turret.hoodMoveToDegree(RobotContainer.vision.getHoodVisionPosition());
            } else if (RobotContainer.vision.visionHasTarget()
                    && (RobotContainer.vision.isXFinished() && RobotContainer.turret.hoodFinished())) {
                aligned = true;
            } else {
                timeout++;
            }
        } else {

            if (RobotContainer.turret.isShooterSpedUp(RobotPreferences.shooterMaxRPM.getValue())) {

                if (numShots >= numShotsTodo) {
                    hasCounted = true;
                    success = true;

                }
                if (!hasCounted) {
                    RobotContainer.turret.finalShooterGateSetSpeed(1);

                    numShots++;
                    hasCounted = true;
                } else {
                    if (!RobotContainer.intake.getStagedSwitch()) {
                        hasCounted = false;
                        RobotContainer.turret.finalShooterGateSetSpeed(0);

                    }
                }

            } else {
                RobotContainer.turret.finalShooterGateSetSpeed(0);
            }

        }

    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        RobotContainer.turret.setSusanSpeed(0);
        RobotContainer.turret.finalShooterGateSetSpeed(0);

        RobotContainer.turret.setShooterSpeed(RobotPreferences.shooterNoSpeed.getValue());
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        // // return true if X error and hood are within tolerance
        if (success) {

            finishReason = FinishReason.SUCCESS;

            return true;
        }

        // return true if no vision for timeout
        if (noTarget) {

            finishReason = FinishReason.NO_TARGET;
            return true;
        }

        return false;
    }
}
