/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Drivetrain extends SubsystemBase
{
   // Creates a new ExampleSubsystem

  TalonSRX leftTalon;
  TalonSRX rightTalon;

  public Drivetrain()
  {
    leftTalon = new TalonSRX(0);
    rightTalon = new TalonSRX(2);
  }
  public void arcadeDrive(double speed, double turn)
  {
    leftTalon.set(ControlMode.PercentOutput, speed, DemandType.ArbitraryFeedForward,-turn);
    rightTalon.set(ControlMode.PercentOutput, speed, DemandType.ArbitraryFeedForward, turn);
  }
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
//im a virus >:)  hehe