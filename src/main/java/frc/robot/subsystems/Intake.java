/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotMap;

public class Intake extends SubsystemBase {
  /**
   * Creates a new Intake.
   */

  private TalonSRX collectorTalon;
  private TalonSRX turretGateTalon;
  private TalonSRX finalShooterGateTalon;
  private TalonSRX initialShooterGateTalon;
  private DigitalInput collectionSwitch;
  private DigitalInput bottomSwitch;
  private DigitalInput stagedSwitch;
  private DoubleSolenoid collectorSolenoid;
  private boolean isCollectionLegal;
  
	private static final Value intakeDeployedValue = Value.kReverse;
	private static final Value intakeRetractedValue = Value.kForward;
  
  public Intake() {
    collectorTalon = new TalonSRX(RobotMap.COLLECTOR_TALON);
    turretGateTalon = new TalonSRX(RobotMap.TURRET_GATE_TALON);
    finalShooterGateTalon = new TalonSRX(RobotMap.FINAL_SHOOTER_GATE_TALON);
    initialShooterGateTalon = new TalonSRX(RobotMap.INITIAL_SHOOTER_GATE_TALON);

    collectionSwitch = new DigitalInput(RobotMap.COLLECTOR_SWITCH);
    bottomSwitch = new DigitalInput(RobotMap.BOTTOM_SWITCH);
    stagedSwitch = new DigitalInput(RobotMap.STAGED_SWITCH);

    collectorSolenoid = new DoubleSolenoid(RobotMap.INTAKE_SOLENOID_A,
    RobotMap.INTAKE_SOLENOID_B);

  }

  public boolean getCollectionSwitch() {
    return collectionSwitch.get();
  }
  public boolean getBottomSwitch() {
    return bottomSwitch.get();
  }
  public boolean getStagedSwitch() {
    return stagedSwitch.get();
  }

  public void collectorSetSpeed(double speed){
    collectorTalon.set(ControlMode.PercentOutput, speed);
  }
  public void turretGateSetSpeed(double speed){
    turretGateTalon.set(ControlMode.PercentOutput, speed);
  }
  public void initialShooterGateSetSpeed(double speed){
    initialShooterGateTalon.set(ControlMode.PercentOutput, speed);
  }
  public void finalShooterGateSetSpeed(double speed){
    finalShooterGateTalon.set(ControlMode.PercentOutput, speed);

  }
  public void deployCollector(){
    collectorSolenoid.set(intakeDeployedValue);
  }
  public void retractCollector(){
    collectorSolenoid.set(intakeRetractedValue);

  }
  public boolean getCollectorDeployed(){
    return (collectorSolenoid.get()==intakeDeployedValue);
  }
  public void setCollectionLegality(boolean legality){
    isCollectionLegal = legality;
  }
  public boolean getCollectionLegality(){
    return isCollectionLegal;
  }

  

  @Override
  public void periodic() {
    SmartDashboard.putBoolean("Collection Switch", getCollectionSwitch());
    // This method will be called once per scheduler run
  }
}
