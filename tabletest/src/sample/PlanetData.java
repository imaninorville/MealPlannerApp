package sample;

public class PlanetData {
    String name;
    double radius;
    double flux;
    double distance;

    public PlanetData(String name, double radius, double flux, double distance){
        this.name = name;
        this.radius = radius;
        this.flux = flux;
        this.distance = distance;

    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getFlux() {
        return flux;
    }

    public void setFlux(double flux) {
        this.flux = flux;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}
