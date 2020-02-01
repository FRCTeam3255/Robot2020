/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

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

   double xPosition;
   
   Scalar lower = new Scalar(35, 140, 60);
   Scalar upper = new Scalar(35, 140, 60);

  public Vision() {
    Thread thread = new Thread(){
        public void run(){
            startStreams();
        }
    };
    thread.start();
    

    }

    //Color sensing for balls
    public void startStreams(){

        camera = CameraServer.getInstance().startAutomaticCapture();

        cvSink = CameraServer.getInstance().getVideo();
        sourceStream = CameraServer.getInstance().putVideo("src", 640, 480);
        outputStream = CameraServer.getInstance().putVideo("filter", 640, 480);
    
        source = new Mat();
        output = new Mat();
        circles = new Mat();
        binary = new Mat();
        
    }


    //limelight
    public double getX(){
        return (xPosition-80);
    }

  
    public boolean visionHasTarget() {
      if ((NetworkTableInstance.getDefault().getTable("limelight").getEntry("tv").getDouble(0)) < 1) {
        return false;
      } else {
        return true;
      }
    }
  
    public double getVisionXError() {
      return (RobotPreferences.visionGoalAngle.getValue()
          - NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0));
    }
  
    public double getVisionAreaError() {
      return (RobotPreferences.visionGoalArea.getValue()
          - NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta").getDouble(0));
    }
  
    public boolean isVisionFinished() {
      return ((Math.abs(getVisionXError()) < RobotPreferences.visionXTol.getValue())
          && (Math.abs(getVisionAreaError()) < RobotPreferences.visionAreaTol.getValue()));
    }


  @Override
  public void periodic() {

    cvSink.grabFrame(source);
    Imgproc.cvtColor(source, output, Imgproc.COLOR_BGR2HSV);        //! [houghcircles]
    // Core.inRange(source, lower, upper, output);

    Core.inRange(output, new Scalar(RobotPreferences.hLow.getValue(), RobotPreferences.sLow.getValue(), RobotPreferences.vLow.getValue()),
            new Scalar(RobotPreferences.hHigh.getValue(), RobotPreferences.sHigh.getValue(), RobotPreferences.vHigh.getValue()), circles);
    Imgproc.threshold( circles, binary, 100,255,Imgproc.THRESH_BINARY );
    
    Moments m = Imgproc.moments(binary,true);
    double x = m.m10/m.m00;
    xPosition = x;
    double y = m.m01/m.m00;
    Point point = new Point(x,y);
    Imgproc.circle(source, point, 5, new Scalar(255,0,255), 3, 8, 0 );
    SmartDashboard.putNumber("x", getX());

            
    // Imgproc.HoughCircles(output, circles, Imgproc.HOUGH_GRADIENT, 1.0,
    //         (double)output.rows()/16, // change this value to detect circles with different distances to each other
    //         100.0, 30.0, 1, 30); // change the last two parameters
    //         // (min_radius & max_radius) to detect larger circles
    // //! [houghcircles]

    // //! [draw]
    // for (int x = 0; x < circles.cols(); x++) {
    //     double[] c = circles.get(0, x);
    //     Point center = new Point(Math.round(c[0]), Math.round(c[1]));
    //     // circle center
    //     Imgproc.circle(source, center, 1, new Scalar(0,100,100), 3, 8, 0 );
    //     // circle outline
    //     int radius = (int) Math.round(c[2]);
    //     Imgproc.circle(source, center, radius, new Scalar(255,0,255), 3, 8, 0 );
    // }


    
    sourceStream.putFrame(source);
    outputStream.putFrame(circles);
  }
}
    