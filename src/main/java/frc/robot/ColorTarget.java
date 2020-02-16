package frc.robot;

import edu.wpi.first.wpilibj.util.Color;
import frcteam3255.robotbase.Preferences.SN_DoublePreference;

public class ColorTarget {
    // TODO: (optional) typically, these would be privates. Can use m_ as a
    // convention for member variables rather than a_
    private double lowRed;
    private double highRed;
    private double lowGreen;
    private double highGreen;
    private double lowBlue;
    private double highBlue;

    private SN_DoublePreference lowRedPref;
    private SN_DoublePreference highRedPref;
    private SN_DoublePreference lowGreenPref;
    private SN_DoublePreference highGreenPref;
    private SN_DoublePreference lowBluePref;
    private SN_DoublePreference highBluePref;

    public ColorTarget(SN_DoublePreference a_lowRedPref, SN_DoublePreference a_highRedPref,
            SN_DoublePreference a_lowGreenPref, SN_DoublePreference a_highGreenPref, SN_DoublePreference a_lowBluePref,
            SN_DoublePreference a_highBluePref) {
        lowRedPref = a_lowRedPref;
        highRedPref = a_highRedPref;
        lowGreenPref = a_lowGreenPref;
        highGreenPref = a_highGreenPref;
        lowBluePref = a_lowBluePref;
        highBluePref = a_highBluePref;
        reloadValues();

    }

    public void reloadValues() {
        lowRed = lowRedPref.getValue();
        highRed = highRedPref.getValue();
        lowGreen = lowGreenPref.getValue();
        highGreen = highGreenPref.getValue();
        lowBlue = lowBluePref.getValue();
        highBlue = highBluePref.getValue();
    }

    public boolean matchesColor(Color color) {
        boolean redTarget = (lowRed < color.red) && (color.red < highRed);
        boolean greenTarget = (lowGreen < color.green) && (color.green < highGreen);
        boolean blueTarget = (lowBlue < color.blue) && (color.blue < highBlue);
        if (redTarget && greenTarget && blueTarget) {
            return true;
        }
        return false;
    }
}