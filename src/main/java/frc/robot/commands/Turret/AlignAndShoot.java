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

public class AlignAndShoot extends CommandBase {
    /**
     * Creates a new AlignAndShoot. Aligns turret within threshold, then spins up
     * shooter within threshold, then shoots n times
     */
    private int numShotsTodo;
    private int numShots;
    private Timer timer = new Timer();
    private boolean aligned;
    private boolean hasCounted;
    public FinishReason finishReason = FinishReason.NOT_FINISHED;

    public enum FinishReason {
        SUCCESS, NO_TARGET, NOT_FINISHED
    };

    public AlignAndShoot(int a_numShots) {
        // Use addRequirements() here to declare subsystem dependencies.

        numShotsTodo = a_numShots;
        addRequirements(RobotContainer.turret);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        finishReason = FinishReason.NOT_FINISHED;

        aligned = false;
        hasCounted = false;
        numShots = 0;
        timer.reset();
        timer.start();
        // RobotContainer.turret.setShooterVelocity(RobotPreferences.shooterMaxRPM.getValue());
        // RobotContainer.turret.setShooterSpeefd(RobotContainer);
        ;

    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {

        if (!aligned) {
            if (RobotContainer.vision.visionHasTarget()
                    && !(RobotContainer.vision.isXFinished() && RobotContainer.turret.hoodFinished())) {

                timer.reset();
                timer.start();

                RobotContainer.turret.setSusanSpeed(
                        RobotContainer.vision.getVisionXError() * RobotPreferences.susanVisionP.getValue());
                RobotContainer.turret.moveHoodToDegree(RobotContainer.vision.getHoodVisionPosition());
            } else if (RobotContainer.vision.visionHasTarget()
                    && (RobotContainer.vision.isXFinished() && RobotContainer.turret.hoodFinished())) {
                aligned = true;
            }
        } else {
            timer.stop();
            if (RobotContainer.turret.isShooterSpedUp()) {

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
        RobotContainer.turret.setHoodSpeed(0);
        RobotContainer.turret.finalShooterGateSetSpeed(0);
        // RobotContainer.turret.setShooterSpeefd(0);

    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        // // return true if X error and hood are within tolerance
        if (numShots >= numShotsTodo) {

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
