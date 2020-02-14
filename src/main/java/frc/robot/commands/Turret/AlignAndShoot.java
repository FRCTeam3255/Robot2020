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
import frcteam3255.robotbase.Preferences.SN_DoublePreference;

public class AlignAndShoot extends CommandBase {
    /**
     * Creates a new AlignAndShoot. Aligns turret within threshold, then spins up
     * shooter within threshold, then shoots n times
     */
    // TODO:add shooting
    private final Turret m_turret;
    private final Vision m_vision;
    private SN_DoublePreference m_speed;
    private int m_numShots;
    private boolean timedOut = false;
    private boolean success = false;
    private boolean noTarget = false;
    private boolean aligned = false;
    private int timeout = 0;

    public enum FinishReason {
        SUCCESS, NO_TARGET, NOT_FINISHED
    };

    public FinishReason finishReason = FinishReason.NOT_FINISHED;

    public AlignAndShoot(Turret turretSubsystem, Vision visionSubsystem, SN_DoublePreference speed, int numShots) {
        // Use addRequirements() here to declare subsystem dependencies.
        m_turret = turretSubsystem;
        m_vision = visionSubsystem;
        m_speed = speed;
        m_numShots = numShots;
        addRequirements(turretSubsystem);
        addRequirements(visionSubsystem);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        m_turret.setShooterSpeed(RobotPreferences.shooterFullSpeed.getValue());
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {

        if (!aligned) {

            if (timeout > RobotPreferences.visionTimeout.getValue()) {
                noTarget = true;
            }
            if (m_vision.visionHasTarget() && !m_vision.isVisionFinished()) {
                m_turret.setSusanSpeed(
                        m_speed.getValue() * m_vision.getVisionXError() * RobotPreferences.susanVisionP.getValue());
                m_turret.hoodMoveToDegree(m_vision.getHoodVisionPosition());
            } else if (m_vision.isVisionFinished()) {
                aligned = true;
            } else {
                timeout++;
            }
        } else {
            if (m_turret.isShooterSpedUp(RobotPreferences.shooterFullSpeed.getValue())) {
                m_turret.finalShooterGateSetSpeed(1);
            } else {
                m_turret.finalShooterGateSetSpeed(0);
                // TODO: add finish condition utilizing numshots
            }
        }

    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        m_turret.setSusanSpeed(0);
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
