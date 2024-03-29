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
import frcteam3255.robotbase.Preferences.SN_DoublePreference;

public class AlignAuto extends CommandBase {
    /**
     * Creates a new AlignAuto.
     */
    private SN_DoublePreference susanPos;
    private SN_DoublePreference hoodPos;
    private Timer timer = new Timer();
    private SN_DoublePreference delay;

    public AlignAuto(SN_DoublePreference a_susanPos, SN_DoublePreference a_hoodPos, SN_DoublePreference a_delay) {
        // Use addRequirements() here to declare subsystem dependencies.

        susanPos = a_susanPos;
        hoodPos = a_hoodPos;
        delay = a_delay;
        // addRequirements(RobotContainer.turret);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        RobotContainer.turret.turnSusanToDegree(susanPos.getValue());

        RobotContainer.turret.moveHoodToDegree(hoodPos.getValue());

        timer.reset();
        timer.start();
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        RobotContainer.turret.turnSusanToDegree(susanPos.getValue());
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        // RobotContainer.turret.setHoodSpeed(0);
        RobotContainer.turret.setSusanSpeed(0);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return RobotContainer.turret.hoodFinished() && RobotContainer.turret.susanFinished()
                && timer.hasPeriodPassed(delay.getValue());
    }
}
