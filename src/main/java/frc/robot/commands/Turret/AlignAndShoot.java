/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Turret;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotPreferences;
import frc.robot.subsystems.Intake;
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
    private final Intake m_intake;
    private SN_DoublePreference m_speed;
    private int m_numShotsTodo;
    private int m_numShots;
    private boolean timedOut = false;
    private boolean success = false;
    private boolean noTarget = false;
    private boolean aligned = false;
    private int timeout = 0;
    private boolean hasCounted = false;

    public enum FinishReason {
        SUCCESS, NO_TARGET, NOT_FINISHED
    };

    public FinishReason finishReason = FinishReason.NOT_FINISHED;

    public AlignAndShoot(Intake intake, Turret turretSubsystem, Vision visionSubsystem, int numShots) {
        // Use addRequirements() here to declare subsystem dependencies.
        m_turret = turretSubsystem;
        m_vision = visionSubsystem;
        m_intake = intake;
        m_numShotsTodo = numShots;
        addRequirements(turretSubsystem);
        addRequirements(visionSubsystem);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        m_turret.setShooterVelocity(RobotPreferences.shooterMaxRPM.getValue());
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {

        if (!aligned) {

            if (timeout > RobotPreferences.visionTimeout.getValue()) {

                noTarget = true;
            }
            if (m_vision.visionHasTarget() && !(m_vision.isXFinished() && m_turret.hoodFinished())) {

                m_turret.setSusanSpeed(m_vision.getVisionXError() * RobotPreferences.susanVisionP.getValue());
                m_turret.hoodMoveToDegree(m_vision.getHoodVisionPosition());
            } else if (m_vision.visionHasTarget() && (m_vision.isXFinished() && m_turret.hoodFinished())) {
                aligned = true;
            } else {
                timeout++;
            }
        } else {

            if (m_turret.isShooterSpedUp(RobotPreferences.shooterMaxRPM.getValue())) {

                if (m_numShots >= m_numShotsTodo) {
                    hasCounted = true;
                    success = true;

                }
                if (!hasCounted) {
                    m_turret.finalShooterGateSetSpeed(1);

                    m_numShots++;
                    hasCounted = true;
                } else {
                    if (!m_intake.getStagedSwitch()) {
                        hasCounted = false;
                        m_turret.finalShooterGateSetSpeed(0);

                    }
                }

            } else {
                m_turret.finalShooterGateSetSpeed(0);
            }

        }

    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        timedOut = false;
        success = false;
        noTarget = false;
        aligned = false;
        timeout = 0;
        hasCounted = false;
        m_numShots = 0;
        m_turret.setSusanSpeed(0);
        m_turret.finalShooterGateSetSpeed(0);

        m_turret.setShooterSpeed(RobotPreferences.shooterNoSpeed.getValue());
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
