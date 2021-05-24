package com.csf.java.utils;

import java.util.ArrayList;

public class GeoUtils {

    public static double DEG2RAD = 0.01745329251994;
    public static double RAD2DEG = 57.29577951308;
    public static double EARTHRADKM = 6371;
    public static double EARTHRADM = EARTHRADKM * 1000;
    public static double KNOTSTOKMPS = 0.0005144444;
    public static double KNOTSTOMPS = 0.514444;

    public static double[] calcDestLatLon_GreatArc(double theLat, double theLon, double theRangeMeters, double theBearing_deg) {
        return destVincenty(theLat, theLon, theBearing_deg, theRangeMeters);
        //double lat = theLat * DEG2RAD;
        //double lon = theLon * DEG2RAD;
        //double b = theBearing_deg * DEG2RAD;
        //double radius = (theRangeMeters / EARTHRADKM); //R);
        //CSFTODO what is R

        //double lat2 = Math.asin(Math.sin(lat) * Math.cos(radius) + Math.cos(lat) * Math.sin(radius) * Math.cos(b));
        //double lon2 = lon + Math.atan2(Math.sin(b) * Math.sin(radius) * Math.cos(lat), Math.cos(radius) - Math.sin(lat) * Math.sin(lat2));
        //return new double[]{lat2,lon2};
    }


    public static double[] calcNextGreatArcPoint(double[] theLatLon, double theHeadingDeg, double theSpeedKnots, TimeSpan theTimeSpan) {
        double distMeters = theSpeedKnots * KNOTSTOKMPS * 1000 * theTimeSpan.Seconds();
        return destVincenty(theLatLon[0], theLatLon[1], theHeadingDeg, distMeters);
        //double speedMPS = theSpeedKnots * KNOTSTOMPS;
        //double rangeMeters = speedMPS * theTimeSpan.Seconds();
        //return calcDestLatLon_GreatArc(theLatLon[0], theLatLon[1], rangeMeters, theHeadingDeg);//CSFTODO
    }

    //returns distance in meters between 2 lat/lon pairs using Haversine Formula
    //origin [lat,lon] in decimal format
    //destination [lat,lon] in decimal format
    public static double distanceMeters(double[] theLatLon1, double[] theLatLon2) {
        double lat1 = theLatLon1[0] * DEG2RAD;
        double lon1 = theLatLon1[1] * DEG2RAD;
        double lat2 = theLatLon2[0] * DEG2RAD;
        double lon2 = theLatLon2[1] * DEG2RAD;

        double dLat = (lat2 - lat1);
        double dLon = (lon2 - lon1);
        double sin2Lat = Math.sin(dLat / 2) * Math.sin(dLat / 2);
        double sin2Lon = Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double a = sin2Lat + Math.cos(lat1) * Math.cos(lat2) * sin2Lon;
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTHRADM * c;
    }


    public static boolean isPointInPoly(int theNumVertex,
                                        ArrayList<Double> theLats,
                                        ArrayList<Double> theLons,
                                        double theLat,
                                        double theLon) {
        boolean oddNodes = false;

        int j = theNumVertex - 1;
        for (int i = 0; i < theNumVertex - 1; ) {
            if ((theLons.get(i) < theLon && theLons.get(j) >= theLon || theLons.get(j) < theLon && theLons.get(i) >= theLon) &&
                    (theLats.get(i) <= theLat || theLats.get(j) <= theLat)) {
                oddNodes = (theLats.get(i) + (theLons.get(i)) / (theLons.get(j) - theLons.get(i)) * (theLats.get(j) - theLats.get(i)) < theLat);
            }
            i = i + 1;
            j = i;
        }
        return oddNodes;
    }

    public static boolean isLatLonInBox(double[] theNW_LatLon, double[] theSE_LatLon, double[] theLL) {
        boolean inBox = false;
        double lat = theLL[0];
        double lon = theLL[1];
        if (lat <= theNW_LatLon[0] && lat > theSE_LatLon[0]) {
            if (lon >= theNW_LatLon[1] && lon < theSE_LatLon[1]) {
                inBox = true;
            }
        }
        return inBox;
    }

    public static double[] convertDMStoDD(String[] theLatLonDMS) {
        double[] llDD = {0, 0};
        String lat = theLatLonDMS[0];
        String lon = theLatLonDMS[1];

        if (lat.length() > 6 && lon.length() > 7) {
            try {

                int degLat = Integer.parseInt(lat.substring(0, 2));
                int degLon = Integer.parseInt(lon.substring(0, 3));
                double minLat = Integer.parseInt(lat.substring(2, 2));
                double minLon = Integer.parseInt(lon.substring(3, 2));
                double secLat = Integer.parseInt(lat.substring(4, 2));
                double secLon = Integer.parseInt(lon.substring(5, 2));
                String latHemisphere = lat.substring(6, 1);
                String lonHemisphere = lon.substring(7, 1);

                llDD[0] = degLat + minLat / 60 + secLat / 3600;
                llDD[1] = degLon + minLon / 60 + secLon / 3600;

                if (latHemisphere.equals("S")) llDD[0] = llDD[0] * -1;
                if (lonHemisphere.equals("W")) llDD[1] = llDD[1] * -1;

                //llDD[0] = Math.round(llDD[0],5);
                //llDD[1] = Math.round(llDD[1],5);
            } catch (Exception ex) {
                llDD = null;
            }
        } else {
            llDD = null;
        }
        return llDD;
    }

    public static String[] convertDDtoDMS(double[] theLatLonDD) {
        String[] llDMS = {"000000N", "0000000E"};
        if (theLatLonDD[0] >= -90 && theLatLonDD[0] <= 90 && theLatLonDD[1] >= -180 && theLatLonDD[1] <= 180) {
            double lat = Math.abs(theLatLonDD[0]);
            double lon = Math.abs(theLatLonDD[1]);

            Integer degLat = (int) lat;
            Integer degLon = (int) lon;

            Double latRemainder = lat - degLat;
            Double lonRemainder = lon - degLon;

            Integer latMinutes = (int) (latRemainder * 60);
            Integer lonMinutes = (int) (lonRemainder * 60);

            Double latMinRem = (latRemainder * 60) - latMinutes;
            Double lonMinRem = (lonRemainder * 60) - lonMinutes;

            Integer latSec = (int) Math.round(latMinRem * 60);
            Integer lonSec = (int) Math.round(lonMinRem * 60);

            String latHemisphere = (theLatLonDD[0] < 0) ? "S" : "N";
            String lonHemisphere = (theLatLonDD[1] < 0) ? "W" : "E";


            llDMS[0] = String.format("%02d", degLat) +
                    String.format("%02d", latMinutes) +
                    String.format("%02d", latSec) +
                    latHemisphere;

            llDMS[1] = String.format("%03d", degLon) +
                    String.format("%02d", lonMinutes) +
                    String.format("%02d", lonSec) +
                    lonHemisphere;
        } else {
            llDMS = null;
        }
        return llDMS;
    }


    // Calculates destination point given start point lat/long, bearing & distance,
    // using Vincenty inverse formula for ellipsoids
    //
    // @param   {Number} lat1, lon1: first point in decimal degrees
    // @param   {Number} brng: initial bearing in decimal degrees
    // @param   {Number} dist: distance along bearing in metres
    // @returns (LatLon} destination point
    //
    public static double[] destVincenty(double lat1, double lon1, double brng, double dist) {
        int a = 6378137;
        double b = 6356752.3142;
        double f = 1 / 298.257223563;  // WGS-84 ellipsiod
        double alpha1 = DEG2RAD * brng;
        double sinAlpha1 = Math.sin(alpha1);
        double cosAlpha1 = Math.cos(alpha1);

        double tanU1 = (1 - f) * Math.tan(lat1 * DEG2RAD);
        double cosU1 = 1 / Math.sqrt((1 + tanU1 * tanU1)), sinU1 = tanU1 * cosU1;
        double sigma1 = Math.atan2(tanU1, cosAlpha1);
        double sinAlpha = cosU1 * sinAlpha1;
        double cosSqAlpha = 1 - sinAlpha * sinAlpha;
        double uSq = cosSqAlpha * (a * a - b * b) / (b * b);
        double A = 1 + uSq / 16384 * (4096 + uSq * (-768 + uSq * (320 - 175 * uSq)));
        double B = uSq / 1024 * (256 + uSq * (-128 + uSq * (74 - 47 * uSq)));

        double sinSigma = 0;
        double cosSigma = 0;
        double cos2SigmaM = 0;

        double sigma = dist / (b * A), sigmaP = 2 * Math.PI;
        while (Math.abs(sigma - sigmaP) > 1e-12) {
            cos2SigmaM = Math.cos(2 * sigma1 + sigma);
            sinSigma = Math.sin(sigma);
            cosSigma = Math.cos(sigma);
            double deltaSigma = B * sinSigma * (cos2SigmaM + B / 4 * (cosSigma * (-1 + 2 * cos2SigmaM * cos2SigmaM) -
                    B / 6 * cos2SigmaM * (-3 + 4 * sinSigma * sinSigma) * (-3 + 4 * cos2SigmaM * cos2SigmaM)));
            sigmaP = sigma;
            sigma = dist / (b * A) + deltaSigma;
        }

        double tmp = sinU1 * sinSigma - cosU1 * cosSigma * cosAlpha1;
        double lat2 = Math.atan2(sinU1 * cosSigma + cosU1 * sinSigma * cosAlpha1, (1 - f) * Math.sqrt(sinAlpha * sinAlpha + tmp * tmp));
        double lambda = Math.atan2(sinSigma * sinAlpha1, cosU1 * cosSigma - sinU1 * sinSigma * cosAlpha1);
        double C = f / 16 * cosSqAlpha * (4 + f * (4 - 3 * cosSqAlpha));
        double L = lambda - (1 - C) * f * sinAlpha * (sigma + C * sinSigma * (cos2SigmaM + C * cosSigma * (-1 + 2 * cos2SigmaM * cos2SigmaM)));
        double lon2 = (lon1 * DEG2RAD + L + 3 * Math.PI) % (2 * Math.PI) - Math.PI;  // normalise to -180...+180

        //double revAz = Math.atan2(sinAlpha, -tmp);  // final bearing, if required

        return new double[]{lat2 * RAD2DEG, lon2 * RAD2DEG};
    }


}
