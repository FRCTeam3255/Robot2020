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

public class AlignAndShootToPos extends CommandBase {
    /**
     * Creates a new AlignAndShootToPos. Aligns turret within threshold, then spins
     * up shooter within threshold, then shoots n times
     */
    private int numShotsTodo;
    private int numShots;
    private boolean aligned;
    private boolean hasCounted;
    private Timer timer;
    SN_DoublePreference hoodPos;
    SN_DoublePreference turretPos;
    public FinishReason finishReason = FinishReason.NOT_FINISHED;

    public enum FinishReason {
        SUCCESS, NO_TARGET, NOT_FINISHED
    };

    public AlignAndShootToPos(int a_numShots, SN_DoublePreference a_hoodPos, SN_DoublePreference a_turretPos) {
        // Use addRequirements() here to declare subsystem dependencies.

        hoodPos = a_hoodPos;
        turretPos = a_turretPos;
        numShotsTodo = a_numShots;
        addRequirements(RobotContainer.turret);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        aligned = false;
        hasCounted = false;
        numShots = 0;
        timer.reset();
        timer.start();
        // RobotContainer.turret.setShooterVelocity(RobotPreferences.shooterMaxRPM.getValue());
        // RobotContainer.turret.shooterVelocity();

    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {

        if (!aligned) {

            timer.reset();
            timer.start();
            if (!(RobotContainer.turret.susanFinished() && RobotContainer.turret.hoodFinished())) {

                RobotContainer.turret.susanTurnToDegree(turretPos.getValue());
                RobotContainer.turret.hoodMoveToDegree(hoodPos.getValue());
            } else if ((RobotContainer.turret.susanFinished() && RobotContainer.turret.hoodFinished())) {
                aligned = true;
            }
        } else {

            if (RobotContainer.turret.isShooterSpedUp()) {

                timer.reset();
                timer.start();
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
        RobotContainer.turret.setHoodSpeed(0);
        RobotContainer.turret.setSusanSpeed(0);
        RobotContainer.turret.finalShooterGateSetSpeed(0);

        // RobotContainer.turret.setShooterSpeed(RobotPreferences.shooterNoSpeed.getValue());
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        if (timer.hasPeriodPassed(RobotPreferences.toPosTimeout.getValue())) {
            finishReason = FinishReason.NO_TARGET;
            return true;

        }
        if (numShots >= numShotsTodo) {
            finishReason = FinishReason.SUCCESS;
            return true;
        }
        return false;
    }
}
