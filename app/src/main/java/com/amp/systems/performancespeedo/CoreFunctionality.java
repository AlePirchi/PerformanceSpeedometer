package com.amp.systems.performancespeedo;

import android.location.Location;

public class CoreFunctionality extends MainActivity
{
    public static String inputSpeed; //Speed in meters per seconds that comes from the GPS
    public static double groundSpeed; //Speed that will be displayed
    public static double travelledDistance;




    static double convertionRate = 3.6; //Based on location it will convert input speed to groundSpeed

    static double getGroundSpeed(String inputSpeed)
    {
        groundSpeed = Double.parseDouble(inputSpeed) * convertionRate;

        return groundSpeed;
    }

    private double countDistance(double lat1, double lat2, double long1, double long2, double convertionRate)
    {
        double distance;

        Location location1 = new Location("PointA");
        location1.setLatitude(lat1);
        location1.setLongitude(long1);

        Location location2 = new Location("PointB");
        location2.setLatitude(lat2);
        location2.setLongitude(long2);

        distance = location1.distanceTo(location2);

        travelledDistance = travelledDistance + distance;

        return travelledDistance;
    }



}
