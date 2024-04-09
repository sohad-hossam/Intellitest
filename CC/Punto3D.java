package unisa.gps.etour.util;

/**
 * Bean that contains the coordinates of a point on the surface of the earth "and
 * That it realizes the calculation of the distance from the system. The values of
 * Coordinates must be represented in radians and must fall in
 * Target range: 0 to greek-Pi / 4 for the latitude south of the equator
 * 0 to + Pi greek / 4 for the latitude north of the equator from 0 to Pi-greek /
 * 2 for the meridian of longitude west of Greenwitch greek from 0 to + Pi / 2
 * For the meridian of longitude east of Greenwitch
 *
 * @ Author Mauro Miranda
 * @ Version 0.1 2007 eTour Project - Copyright by SE @ SA Lab DMI University
 * Of Salerno
 */

public class Point3D {
    // Radius of the earth
    final double EARTH_RADIUS = 6371.0;
    private double latitude, longitude, altitude;

    public Point3D() {
        latitude = longitude = altitude = 0;
    }

    public Point3D(double latitude, double longitude, double altitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }

    /**
     * Returns the latitude
     *
     * @ Return
     * /
    public double getLatitude() {
        return latitude;
    }

    /**
     * Sets the latitude
     *
     * @ Param latitude
     * /
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Returns the longitude
     *
     * @ Return
     * /
    public double getLongitude() {
        return longitude;
    }

    /**
     * Sets the longitude
     *
     * @ Param longitude
     * /
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * Returns the altitude
     *
     * @ Return
     * /
    public double getAltitude() {
        return altitude;
    }

    /**
     * Sets the altitude
     *
     * @ Param altitude
     * /
    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    /**
     * Calculate the distance between the point and another point given as argument
     *
     * @ Param p
     * @ Return
     * /
    public double distance(Point3D p) {
        double differenceLongitude = this.longitude - p.longitude;
        double part1 = Math.pow(Math.cos(p.latitude) * Math.sin(differenceLongitude), 2);
        double part2 = Math.pow(Math.cos(latitude) * Math.sin(p.latitude)
                - Math.sin(latitude) * Math.cos(p.latitude) * Math.cos(differenceLongitude), 2);
        double part3 = Math.sin(latitude) * Math.sin(p.latitude)
                + Math.cos(latitude) * Math.cos(p.latitude) * Math.cos(differenceLongitude);
        return (Math.atan(Math.sqrt(part1 + part2) / part3) * EARTH_RADIUS);
    }

    /**
     * Method which creates a 3D point from coordinates measured in degrees. The
     * 3D point instead represents the coordinates in radians
     *
     * @ Param latitude latitude in degrees
     * @ Param longitude Longitude in degrees *
     * @ Param altitude
     * @ Return Point3D with the coordinates in radians
     * /
    public static Point3D degreesToRadians(double latitude, double longitude, double altitude) {
        Point3D point = new Point3D();
        point.setLatitude(latitude * Math.PI / 180);
        point.setLongitude(longitude * Math.PI / 180);
        point.setAltitude(altitude);
        return point;
    }
}
