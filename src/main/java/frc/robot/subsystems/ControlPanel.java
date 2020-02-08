/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotMap;
import frc.robot.RobotPreferences;


public class ControlPanel extends SubsystemBase {
  private TalonFX spinner;
  private final I2C.Port i2cPort = I2C.Port.kOnboard;
  private final ColorSensorV3 m_colorSensor = new ColorSensorV3(i2cPort);
  private Servo deployServo;
 
  private ColorTarget redTarget;
  private ColorTarget greenTarget;
  private ColorTarget blueTarget;
  private ColorTarget yellowTarget;

  public ControlPanel() {
    spinner = new TalonFX(RobotMap.CP_TALON);
    deployServo = new Servo(RobotMap.CP_SERVO);
    reloadColorTargets();

  }

  public static enum panelColor {
    red, green, blue, yellow, none
  };

  public void setSpeed(double speed) {
    spinner.set(ControlMode.PercentOutput, speed);
  }

public void reloadColorTargets() {
  redTarget = new ColorTarget(RobotPreferences.redsRedLow, RobotPreferences.redsRedHigh, RobotPreferences.redsGreenLow, RobotPreferences.redsRedHigh, RobotPreferences.redsBlueLow, RobotPreferences.redsBlueHigh);
  greenTarget = new ColorTarget(RobotPreferences.greensRedLow, RobotPreferences.greensRedHigh, RobotPreferences.greensGreenLow, RobotPreferences.greensRedHigh, RobotPreferences.greensBlueLow, RobotPreferences.greensBlueHigh);
  blueTarget = new ColorTarget(RobotPreferences.bluesRedLow, RobotPreferences.bluesRedHigh, RobotPreferences.bluesGreenLow, RobotPreferences.bluesRedHigh, RobotPreferences.bluesBlueLow, RobotPreferences.bluesBlueHigh);
  yellowTarget = new ColorTarget(RobotPreferences.yellowsRedLow, RobotPreferences.yellowsRedHigh, RobotPreferences.yellowsGreenLow, RobotPreferences.yellowsRedHigh, RobotPreferences.yellowsBlueLow, RobotPreferences.yellowsBlueHigh);

}

public panelColor getColor() {
  Color color = m_colorSensor.getColor();


  if(redTarget.matchesColor(color)){
    return panelColor.red;
  } else if(greenTarget.matchesColor(color)){
    return panelColor.green;
  } else if(blueTarget.matchesColor(color)){
    return panelColor.blue;
  } else if(yellowTarget.matchesColor(color)){
    return panelColor.yellow;
  }else{
    return panelColor.none;
  }

}
public String getStringColor() {
    Color color = m_colorSensor.getColor();


    if(redTarget.matchesColor(color)){
      return "r";
    } if(greenTarget.matchesColor(color)){
      return "g";
    } if(blueTarget.matchesColor(color)){
      return "b";
    } if(yellowTarget.matchesColor(color)){
      return "y";
    }
      return "huh";
    

}

  public void deployServo() {
    deployServo.set(1);
  }

  public void retractServo() {
    deployServo.set(0);
  }

  public double getServo() {
    return deployServo.get();
  }

  @Override
  public void periodic() {

    Color detectedColor = m_colorSensor.getColor();
    double IR = m_colorSensor.getIR();
    int proximity = m_colorSensor.getProximity();

    SmartDashboard.putNumber("Red", 255*detectedColor.red);
    SmartDashboard.putString("Color Found", getStringColor());
    SmartDashboard.putNumber("tw", 1);
    SmartDashboard.putNumber("Green", 255*detectedColor.green);
    SmartDashboard.putNumber("Blue", 255*detectedColor.blue);
    SmartDashboard.putNumber("IR", IR);
    SmartDashboard.putNumber("Proximity", proximity);
  }
}
