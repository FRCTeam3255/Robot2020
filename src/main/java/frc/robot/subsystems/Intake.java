/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotMap;

public class Intake extends SubsystemBase {
  /**
   * Creates a new Intake.
   */

  private TalonFX collectorTalon;
  private TalonFX turretGateTalon;
  private TalonFX innerShooterGateTalon;
  private TalonFX outerShooterGateTalon;
  
  public Intake() {
    collectorTalon = new TalonFX(RobotMap.COLLECTOR_TALON);
    turretGateTalon = new TalonFX(RobotMap.TURRET_GATE_TALON);
    innerShooterGateTalon = new TalonFX(RobotMap.INNER_SHOOTER_GATE_TALON);
    outerShooterGateTalon = new TalonFX(RobotMap.OUTER_SHOOTER_GATE_TALON);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
