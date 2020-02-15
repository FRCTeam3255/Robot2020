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
import frcteam3255.robotbase.Preferences.SN_DoublePreference;

public class AlignAndShootToPos extends CommandBase {
    /**
     * Creates a new AlignAndShootToPos. Aligns turret within threshold, then spins
     * up shooter within threshold, then shoots n times
     */
    private final Turret turret;
    private final Intake intake;
    private int numShotsTodo;
    private int numShots;
    private boolean success = false;
    private boolean timedOut = false;
    private int timeout = 0;
    private boolean aligned = false;
    private boolean hasCounted = false;
    SN_DoublePreference hoodPos;
    SN_DoublePreference turretPos;

    public AlignAndShootToPos(Intake a_intake, Turret a_turret, int a_numShots, SN_DoublePreference a_hoodPos,
            SN_DoublePreference a_turretPos) {
        // Use addRequirements() here to declare subsystem dependencies.
        turret = a_turret;
        intake = a_intake;
        hoodPos = a_hoodPos;
        turretPos = a_turretPos;
        numShotsTodo = a_numShots;
        addRequirements(a_turret);
        addRequirements(a_intake);
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

            if (timeout > RobotPreferences.toPosTimeout.getValue()) {

                timedOut = true;
            }
            if (!(turret.susanFinished() && turret.hoodFinished())) {

                turret.susanTurnToDegree(turretPos.getValue());
                turret.hoodMoveToDegree(hoodPos.getValue());
            } else if ((turret.susanFinished() && turret.hoodFinished())) {
                aligned = true;
            }
        } else {

            if (timeout > RobotPreferences.toPosTimeout.getValue()) {

                timedOut = true;
            }
            if (turret.isShooterSpedUp(RobotPreferences.shooterMaxRPM.getValue())) {
                timeout = 0;
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
                timeout++;
            }

        }

    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        success = false;
        aligned = false;
        hasCounted = false;
        timedOut = false;
        timeout = 0;
        numShots = 0;
        turret.setSusanSpeed(0);
        turret.finalShooterGateSetSpeed(0);

        turret.setShooterSpeed(RobotPreferences.shooterNoSpeed.getValue());
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        // // return true if X error and hood are within tolerance
        return success || timedOut;
    }
}
