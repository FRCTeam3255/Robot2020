/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotPreferences;

public class Vision extends SubsystemBase {
  /**
   * Creates a new ShooterSubsystem.
   */

  UsbCamera camera;
  CvSink cvSink;
  CvSource outputStream;
  CvSource sourceStream;
  Mat source;
  Mat output;
  Mat circles;
  Mat binary;
  Mat mask;
  Mat heirarchy;
  List<MatOfPoint> contours;

  double xPosition;

  Scalar lower = new Scalar(35, 140, 60);
  Scalar upper = new Scalar(35, 140, 60);

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
    // TODO: consider 320x240 to reduce bandwidth for FMS. Don't need quality for this
    // TODO: consider class constants for 640x480
    sourceStream = CameraServer.getInstance().putVideo("src", 640, 480);
    outputStream = CameraServer.getInstance().putVideo("filter", 640, 480);

    source = new Mat();
    output = new Mat();
    circles = new Mat();
    heirarchy = new Mat();
    binary = new Mat();

  }

  public double getX() {
    // TODO: Don't hardcode 80. Even if not a pref, use a variable because it's probably a function of 640x480
    return (xPosition - 80);
  }

  // limelight
  public boolean visionHasTarget() {
    // TODO: Is < 1 always the right check?
    if ((NetworkTableInstance.getDefault().getTable("limelight").getEntry("tv").getDouble(0)) < 1) {
      return false;
    } else {
      return true;
    }
  }

  public double getVisionXError() {
    // TODO: Likely that scaling by innerHoleScalar is more complex than this logic
    return (NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0))
        * RobotPreferences.innerHoleScaler.getValue();
  }

  public double getVisionArea() {
    return NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta").getDouble(0);
  }

  public boolean isXFinished() {
    return ((Math.abs(getVisionXError()) < RobotPreferences.visionXTol.getValue()));
  }

  public double getHoodVisionPosition() {
    // TODO: Let's review this math together
    double position = (1 / (RobotPreferences.hoodVisionP.getValue() * 90 * getVisionArea()));
    position = position * 90;
    if (position > 90) {
      position = 90;
    } else if (position < 0) {
      position = 0;
    }
    return position;
  }

  @Override
  public void periodic() {

    cvSink.grabFrame(source);
    try {

      Imgproc.cvtColor(source, output, Imgproc.COLOR_BGR2HSV); // ! [houghcircles]
      // Core.inRange(source, lower, upper, output);

      Core.inRange(output,
          new Scalar(RobotPreferences.hLow.getValue(), RobotPreferences.sLow.getValue(),
              RobotPreferences.vLow.getValue()),
          new Scalar(RobotPreferences.hHigh.getValue(), RobotPreferences.sHigh.getValue(),
              RobotPreferences.vHigh.getValue()),
          circles);
      // TODO: Should the threshold values (100,255) be preferences?
      Imgproc.threshold(circles, binary, 100, 255, Imgproc.THRESH_BINARY);

      contours = new ArrayList<MatOfPoint>();
      Imgproc.findContours(binary, contours, heirarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

      double maxVal = 0;
      int maxValIdx = 0;

      for (int contourIdx = 0; contourIdx < contours.size(); contourIdx++) {
        double contourArea = Imgproc.contourArea(contours.get(contourIdx));
        if (maxVal < contourArea) {
          maxVal = contourArea;
          maxValIdx = contourIdx;
        }
      }

      mask = new Mat(source.rows(), source.cols(), CvType.CV_8U, Scalar.all(0));
      Imgproc.drawContours(mask, contours, maxValIdx, new Scalar(255), -1);

      // TODO: Let's review this logic together
      Moments m = Imgproc.moments(mask, true);
      double x = m.m10 / m.m00;
      xPosition = x;
      double y = m.m01 / m.m00;
      Point point = new Point(x, y);
      Imgproc.circle(source, point, 5, new Scalar(255, 0, 255), 3, 8, 0);
      SmartDashboard.putNumber("x", getX());
      sourceStream.putFrame(source);
      outputStream.putFrame(mask);
    } catch (Exception e) {
      System.out.println("something went wrong! Is your camera plugged in?");
    }
    // TODO: Let's review the meaning of each of these
    SmartDashboard.putNumber("xerrll", getVisionXError());
    SmartDashboard.putNumber("area err", getVisionArea());
    SmartDashboard.putNumber("given position",
        RobotPreferences.hoodCountsPerDegree.getValue() * RobotPreferences.hoodVisionP.getValue() * getVisionArea());
    SmartDashboard.putNumber("one over given position", getHoodVisionPosition());
    SmartDashboard.putBoolean("Vision Has Target", visionHasTarget());
    SmartDashboard.putBoolean("Vision X Finished", isXFinished());

  }
}
