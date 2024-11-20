package app.namedEntities.entities;

import app.namedEntities.NamedEntity;
import java.util.List;

public class Location extends NamedEntity {
  private double latitude;
  private double longitude;

  public Location(String name, List<String> topics, Integer counts) {
    super(name, "LOCATION", topics, counts); // Initialize superclass fields
  }

  // Getters
  public double getLatitude() {
    return latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  // Setters
  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }
}
