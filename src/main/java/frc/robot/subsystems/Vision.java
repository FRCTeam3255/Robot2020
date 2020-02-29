/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotContainer;
import frc.robot.RobotPreferences;

public class Vision extends SubsystemBase {
  /**
   * Creates a new ShooterSubsystem.
   */

  UsbCamera camera;
  CvSink cvSink;

  boolean ledsForcedOn = false;
  private boolean isUsingVision = false;
  double xPosition = 0;
  int camWidth = 320;
  int camHeight = 240;

  // Scalar lower = new Scalar(35, 140, 60);
  // Scalar upper = new Scalar(35, 140, 60);

  public Vision() {
    Thread thread = new Thread() {
      public void run() {
        startStreams();
      }
    };
    thread.start();

  }

  // Color sensing for balls
  public void startStreams() {

    camera = CameraServer.getInstance().startAutomaticCapture();
    cvSink = CameraServer.getInstance().getVideo();

  }

  public void setUsingVision(boolean a_vision) {
    isUsingVision = a_vision;
  }

  // limelight
  public boolean visionHasTarget() {
    if ((NetworkTableInstance.getDefault().getTable("limelight").getEntry("tv").getDouble(0)) < .9) {
      return false;
    } else {
      return true;
    }
  }

  // public void setForcedLEDs(boolean bool){
  // forcedl
  // }

  public void turnOnLimelight() {
    NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setDouble(0);
  }

  public void turnOffLimelight() {
    NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setDouble(1);
  }

  public double getVisionXError() {
    return (NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0));
  }

  public double getVisionInnerOffset() {
    // Plugged into a exponential regression line calc.
    if (RobotContainer.manipulator.getDialAxis() < 0.5) {

      double ts = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ts").getDouble(0);
      double angle;
      if (Math.abs(ts) > 45) {
        angle = Math.abs(ts + 90);
      } else {
        angle = -1 * Math.abs(ts);
      }
      if (angle < 0) {
        return (68.167 + (3.42 - 68.17) / 1 + Math.pow((Math.abs(angle) / 13.4), 8.35));
      } else {
        return -(68.167 + (3.42 - 68.17) / 1 + Math.pow((Math.abs(angle) / 13.4), 8.35));
      }
    } else {
      return 0;
    }

  }

  public double getDistanceToTarget() {
    return 70 / (Math
        .tan(Math.toRadians(NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0) + 26)));
  }

  public double getHoodAngle() {
    return getNewness(getDistanceToTarget())
        + ((63.34171 + (-80.46378 - 63.34171) / (1 + Math.pow((getDistanceToTarget() / 45.55441), 2.088429))));
  }

  public double getNewness(double dist) {
    return RobotPreferences.ballNewness.getValue();
  }

  public double getVisionArea() {
    return NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta").getDouble(0);
  }

  public boolean isXFinished() {
    return ((Math.abs(getVisionXError()) < RobotPreferences.visionXTol.getValue()));
  }

  public double getHoodVisionPosition() {
    double position = (1 / (RobotPreferences.hoodVisionP.getValue() * 90 * getVisionArea()));

    // hard stop at 90 deg
    position = position * 90;
    if (position > 90) {
      position = 90;

    }
    // hard stop at 0 deg
    else if (position < 0) {
      position = 0;
    }
    return position;

  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("LL X Error", getVisionXError());
    SmartDashboard.putNumber("LL Area Error", getVisionArea());
    SmartDashboard.putBoolean("LL Has Target", visionHasTarget());
    SmartDashboard.putBoolean("LL X Finished", isXFinished());
    SmartDashboard.putNumber("LL X skew", getVisionInnerOffset());
    SmartDashboard.putNumber("LL Distance", getDistanceToTarget());
    SmartDashboard.putNumber("LL Hood Angle", getHoodAngle());
    SmartDashboard.putNumber("LL TS",
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("ts").getDouble(0));

  }
}
