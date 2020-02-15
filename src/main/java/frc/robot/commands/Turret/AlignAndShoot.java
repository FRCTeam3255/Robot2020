/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Turret;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotPreferences;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Turret;
import frc.robot.subsystems.Vision;

public class AlignAndShoot extends CommandBase {
    /**
     * Creates a new AlignAndShoot. Aligns turret within threshold, then spins up
     * shooter within threshold, then shoots n times
     */
    private final Turret turret;
    private final Vision vision;
    private final Intake intake;
    private int numShotsTodo;
    private int numShots;
    private boolean success = false;
    private boolean noTarget = false;
    private boolean aligned = false;
    private int timeout = 0;
    private boolean hasCounted = false;

    public enum FinishReason {
        SUCCESS, NO_TARGET, NOT_FINISHED
    };

    public FinishReason finishReason = FinishReason.NOT_FINISHED;

    public AlignAndShoot(Intake a_intake, Turret a_turret, Vision a_vision, int a_numShots) {
        // Use addRequirements() here to declare subsystem dependencies.
        turret = a_turret;
        vision = a_vision;
        intake = a_intake;
        numShotsTodo = a_numShots;
        addRequirements(a_intake);
        addRequirements(a_turret);
        addRequirements(a_vision);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        turret.setShooterVelocity(RobotPreferences.shooterMaxRPM.getValue());
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {

        if (!aligned) {

            if (timeout > RobotPreferences.visionTimeout.getValue()) {

                noTarget = true;
            }
            if (vision.visionHasTarget() && !(vision.isXFinished() && turret.hoodFinished())) {
                timeout = 0;

                turret.setSusanSpeed(vision.getVisionXError() * RobotPreferences.susanVisionP.getValue());
                turret.hoodMoveToDegree(vision.getHoodVisionPosition());
            } else if (vision.visionHasTarget() && (vision.isXFinished() && turret.hoodFinished())) {
                aligned = true;
            } else {
                timeout++;
            }
        } else {

            if (turret.isShooterSpedUp(RobotPreferences.shooterMaxRPM.getValue())) {

                if (numShots >= numShotsTodo) {
                    hasCounted = true;
                    success = true;

                }
                if (!hasCounted) {
                    turret.finalShooterGateSetSpeed(1);

                    numShots++;
                    hasCounted = true;
                } else {
                    if (!intake.getStagedSwitch()) {
                        hasCounted = false;
                        turret.finalShooterGateSetSpeed(0);

                    }
                }

            } else {
                turret.finalShooterGateSetSpeed(0);
            }

        }

    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        success = false;
        noTarget = false;
        aligned = false;
        timeout = 0;
        hasCounted = false;
        numShots = 0;
        turret.setSusanSpeed(0);
        turret.finalShooterGateSetSpeed(0);

        turret.setShooterSpeed(RobotPreferences.shooterNoSpeed.getValue());
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
