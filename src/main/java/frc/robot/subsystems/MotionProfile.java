package frc.robot.subsystems;

import java.io.IOException;

import com.ctre.phoenix.motion.BufferedTrajectoryPointStream;
import com.ctre.phoenix.motion.TrajectoryPoint;

import frc.robot.RobotPreferences;
import frcteam3255.robotbase.SN_MotionProfile;

public class MotionProfile {

    private BufferedTrajectoryPointStream pointsLeft;
    private BufferedTrajectoryPointStream pointsRight;
    private BufferedTrajectoryPointStream[] points = {pointsLeft,pointsRight};

    private String leftFilename;
    private String rightFilename;

    public MotionProfile(String leftName, String rightName){
        pointsLeft = new BufferedTrajectoryPointStream();
        pointsRight = new BufferedTrajectoryPointStream();
        leftFilename = leftName;
        rightFilename = rightName;
        reload();
    }

    public BufferedTrajectoryPointStream[] getPoints() {
        return points;
    }

    public void reload() {

        try {
            initBuffer(pointsLeft, SN_MotionProfile.reader(leftFilename),
                    SN_MotionProfile.count(leftFilename));
        } catch (IOException e) {
            System.out.println("initBuffer failed :(. Is your file in deploy?");
            e.printStackTrace();
        }
        try {
            initBuffer(pointsRight, SN_MotionProfile.reader(rightFilename),
                    SN_MotionProfile.count(rightFilename));
        } catch (IOException e) {
            System.out.println("initBuffer failed :(. Is your file in deploy?");
            e.printStackTrace();
        }

    }


    public void initBuffer(final BufferedTrajectoryPointStream bufferedStream, final double[][] profile,
        final int totalCnt) {
  
      final boolean forward = true; // set to false to drive in opposite direction of profile (not really needed
      // since you can use negative numbers in profile).
  
      final TrajectoryPoint point = new TrajectoryPoint(); // temp for for loop, since unused params are initialized
      // automatically, you can alloc just one
  
      /* clear the buffer, in case it was used elsewhere */
      bufferedStream.Clear();
  
      /* Insert every point into buffer, no limit on size */
      for (int i = 0; i < totalCnt; ++i) {
  
        final double direction = forward ? +1 : -1;
        final double positionRot = profile[i][0];
        final double velocityRPM = profile[i][1];
        final int durationMilliseconds = (int) profile[i][2];
  
        /* for each point, fill our structure and pass it to API */
        point.timeDur = durationMilliseconds;
  
        /* drive part */
        point.position = direction * positionRot * RobotPreferences.motProfSensorUnitsPerRot.getValue(); // Rotations =>
                                                                                                         // sensor units
        point.velocity = direction * velocityRPM * RobotPreferences.motProfSensorUnitsPerRot.getValue() / 600.0; // RPM =>
                                                                                                                 // units
                                                                                                                 // per
                                                                                                                 // 100ms
        point.arbFeedFwd = 0; // good place for kS, kV, kA, etc...
  
        point.profileSlotSelect0 = 0; /* which set of gains would you like to use [0,3]? */
        point.zeroPos = false; /* don't reset sensor, this is done elsewhere since we have multiple sensors */
        point.isLastPoint = ((i + 1) == totalCnt); /* set this to true on the last point */
        point.useAuxPID = false; /* tell MPB that we aren't using both pids */
  
        bufferedStream.Write(point);
      }
    }


}