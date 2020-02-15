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

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotMap;
import frc.robot.RobotPreferences;
import frc.robot.commands.ControlPanel.ReloadColorTargets;

public class ControlPanel extends SubsystemBase {
  private TalonFX spinner;
  private final I2C.Port i2cPort = I2C.Port.kOnboard;
  private final ColorSensorV3 m_colorSensor = new ColorSensorV3(i2cPort);

  private ColorTarget redTarget;
  private ColorTarget greenTarget;
  private ColorTarget blueTarget;
  private ColorTarget yellowTarget;
  private DoubleSolenoid controlPanelSolenoid;
  private static final Value controlPanelDeployedValue = Value.kReverse;
  private static final Value controlPanelRetractedValue = Value.kForward;

  public ControlPanel() {
    spinner = new TalonFX(RobotMap.CP_TALON);
    spinner.configFactoryDefault();
    controlPanelSolenoid = new DoubleSolenoid(RobotMap.CONTROL_PANEL_SOLENOID_A, RobotMap.CONTROL_PANEL_SOLENOID_B);

    SmartDashboard.putData("Reload Colors", new ReloadColorTargets());
    reloadColorTargets();

  }

  public static enum panelColor {
    red, green, blue, yellow, none
  };

  public void setSpeed(double speed) {
    spinner.set(ControlMode.PercentOutput, speed);
  }

  public void reloadColorTargets() {
    // TODO: construct the targets in the ControlPanel constructor, then just call
    // the reload method of each target here

    redTarget = new ColorTarget(RobotPreferences.redsRedLow, RobotPreferences.redsRedHigh,
        RobotPreferences.redsGreenLow, RobotPreferences.redsGreenHigh, RobotPreferences.redsBlueLow,
        RobotPreferences.redsBlueHigh);
    greenTarget = new ColorTarget(RobotPreferences.greensRedLow, RobotPreferences.greensRedHigh,
        RobotPreferences.greensGreenLow, RobotPreferences.greensGreenHigh, RobotPreferences.greensBlueLow,
        RobotPreferences.greensBlueHigh);
    blueTarget = new ColorTarget(RobotPreferences.bluesRedLow, RobotPreferences.bluesRedHigh,
        RobotPreferences.bluesGreenLow, RobotPreferences.bluesGreenHigh, RobotPreferences.bluesBlueLow,
        RobotPreferences.bluesBlueHigh);
    yellowTarget = new ColorTarget(RobotPreferences.yellowsRedLow, RobotPreferences.yellowsRedHigh,
        RobotPreferences.yellowsGreenLow, RobotPreferences.yellowsGreenHigh, RobotPreferences.yellowsBlueLow,
        RobotPreferences.yellowsBlueHigh);
  }

  public void deployControlPanel() {
    controlPanelSolenoid.set(controlPanelDeployedValue);
  }

  public void retractControlPanel() {
    controlPanelSolenoid.set(controlPanelRetractedValue);

  }

  public boolean getControlPanelDeployed() {
    return (controlPanelSolenoid.get() == controlPanelDeployedValue);
  }

  public panelColor getColor() {
    Color color = m_colorSensor.getColor();

    if (redTarget.matchesColor(color)) {
      return panelColor.red;
    } else if (greenTarget.matchesColor(color)) {
      return panelColor.green;
    } else if (blueTarget.matchesColor(color)) {
      return panelColor.blue;
    } else if (yellowTarget.matchesColor(color)) {
      return panelColor.yellow;
    } else {
      return panelColor.none;
    }

  }

  public String getStringColor(Color color) {
    if (redTarget.matchesColor(color)) {
      return "r";
    }
    if (greenTarget.matchesColor(color)) {
      return "g";
    }
    if (blueTarget.matchesColor(color)) {
      return "b";
    }
    if (yellowTarget.matchesColor(color)) {
      return "y";
    }
    return "huh";

  }

  @Override
  public void periodic() {

    Color detectedColor = m_colorSensor.getColor();
    double IR = m_colorSensor.getIR();
    int proximity = m_colorSensor.getProximity();

    SmartDashboard.putString("ColorObject", detectedColor.toString());
    SmartDashboard.putNumber("Red", detectedColor.red);
    SmartDashboard.putNumber("Green", detectedColor.green);
    SmartDashboard.putNumber("Blue", detectedColor.blue);
    SmartDashboard.putString("Color Found", getStringColor(detectedColor));
    // TODO: what's the purpose of always saying tw 1?
    SmartDashboard.putNumber("tw", 1);
    SmartDashboard.putNumber("IR", IR);
    SmartDashboard.putNumber("Proximity", proximity);
  }
}
