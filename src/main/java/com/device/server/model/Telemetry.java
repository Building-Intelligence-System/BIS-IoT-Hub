package com.device.server.model;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "telemetries")
public class Telemetry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String imei;
    private Instant time;

    private Boolean valid;

    private Double latitude;
    private Double longitude;
    private Short altitude;
    private Short speed;
    private Short course;
    private Short satellitesCount;

    public Telemetry() {

    }

    //region GETTERS

    public Integer getId() {
        return id;
    }

    public String getImei() {
        return imei;
    }

    public Instant getTime() {
        return time;
    }

    public Boolean getValid() {
        return valid;
    }

    public Boolean isValid() {
        return valid;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Short getAltitude() {
        return altitude;
    }

    public Short getSpeed() {
        return speed;
    }

    public Short getCourse() {
        return course;
    }

    public Short getSatellitesCount() {
        return satellitesCount;
    }

    //endregion GETTERS

    //region SETTERS

    public Builder editor() {
        return this.new Builder(this);
    }

    //endregion SETTERS

    //region BUILDER

    public static Builder builder() {
        return new Telemetry().new Builder();
    }

    public final class Builder {

        private Builder() {
        }

        private Builder(final Telemetry telemetry) {
            if (telemetry == null) {
                throw new IllegalArgumentException("Telemetry cannot be null");
            }
        }

        public Builder withImei(final String imei) {
            Telemetry.this.imei = imei;
            return this;
        }

        public Builder withFixTime(final Instant fixTime) {
            Telemetry.this.time = fixTime;
            return this;
        }

        public Builder withValid(final Boolean valid) {
            Telemetry.this.valid = valid;
            return this;
        }

        public Builder withLatitude(final Double latitude) {
            Telemetry.this.latitude = latitude;
            return this;
        }

        public Builder withLongitude(final Double longitude) {
            Telemetry.this.longitude = longitude;
            return this;
        }

        public Builder withAltitude(final Short altitude) {
            Telemetry.this.altitude = altitude;
            return this;
        }

        public Builder withSpeed(final Short speed) {
            Telemetry.this.speed = speed;
            return this;
        }

        public Builder withCourse(final Short course) {
            Telemetry.this.course = course;
            return this;
        }

        public Builder withSatellitesCount(final Short satellitesCount) {
            Telemetry.this.satellitesCount = satellitesCount;
            return this;
        }

        public Telemetry build() {
            return Telemetry.this;
        }
    }

    //endregion BUILDER
}
