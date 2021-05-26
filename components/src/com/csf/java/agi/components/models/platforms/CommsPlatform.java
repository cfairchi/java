package com.csf.java.agi.components.models.platforms;

import agi.foundation.geometry.Axes;
import agi.foundation.geometry.Point;
import agi.foundation.infrastructure.CopyContext;
import agi.foundation.infrastructure.CopyForAnotherThread;
import agi.foundation.platforms.Platform;
import com.google.common.collect.ImmutableList;

import java.util.*;

/**
 * Models a communication platform as a {@link CustomPlatform} with {@link Antenna} children.
 */
public class CommsPlatform extends CustomPlatform {

    @Override
    public PlatformType getPlatformType() {
        return PlatformType.COMMS_PLATFORM;
    }

    /**
     * Builder for the CommunicationPlatform.
     */
    public static class Builder {
        private final List<Antenna> antennas = new ArrayList<>();
        private String name = UUID.randomUUID().toString();
        private Optional<Point> location = Optional.empty();
        private Optional<Axes> orientation = Optional.empty();

        public CommsPlatform.Builder setName(String name) {
            this.name = name;
            return this;
        }

        public CommsPlatform.Builder setLocation(Point point) {
            this.location = Optional.of(point);
            return this;
        }

        public CommsPlatform.Builder setOrientation(Axes orientation) {
            this.orientation = Optional.of(orientation);
            return this;
        }

        public CommsPlatform.Builder addAntennas(Collection<Antenna> antennas) {
            this.antennas.addAll(antennas);
            return this;
        }

        public CommsPlatform.Builder addAntenna(Antenna antenna) {
            this.antennas.add(antenna);
            return this;
        }

        public CommsPlatform build() {
            CommsPlatform newPlatform = new CommsPlatform(name);
            location.ifPresent(
                    point -> {
                        newPlatform.setLocationPoint(point);
                    });
            orientation.ifPresent(
                    axes -> {
                        newPlatform.setOrientationAxes(axes);
                    });
            newPlatform.getChildren().addAll(antennas);
            return newPlatform;
        }
    }

    @Override
    public final Object clone(CopyContext context) {
        CommsPlatform clone = new CommsPlatform(this, context);
        for (Antenna antenna : this.getAntennas()) {
            clone.addAntenna((Antenna) antenna.clone(context));
        }
        return clone;
    }

    /**
     * Returns ImmutableList of {@link Antenna} child platforms.
     */
    public ImmutableList<Antenna> getAntennas() {
        ImmutableList.Builder<Antenna> antennas = new ImmutableList.Builder<>();
        for (Platform platform : getChildren()) {
            if (platform instanceof Antenna) {
                antennas.add((Antenna) platform.clone(new CopyForAnotherThread()));
            }
        }
        return antennas.build();
    }

    /**
     * Returns true if there is at least one {@link Antenna} attached to this platform
     */
    public boolean hasAntennas() {
        for (Platform platform : getChildren()) {
            if (platform instanceof Antenna) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the {@link Antenna} that has the provided id.
     */
    public Optional<Antenna> findAntennaWithId(String id) {
        for (Platform platform : getChildren()) {
            if (platform instanceof Antenna && ((Antenna) platform).getId().equals(id)) {
                return Optional.of((Antenna) platform.clone(new CopyForAnotherThread()));
            }
        }
        return Optional.empty();
    }

    /**
     * Returns true if there is at least one {@link Antenna} with the provided description text.
     */
    public boolean hasAntennaWithDescription(String description) {
        for (Platform platform : getChildren()) {
            if (platform instanceof Antenna
                    && ((Antenna) platform).getConstantDescriptionText().equals(description)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if there is an {@link Antenna} with the provided antenna id
     */
    public boolean hasAntennaWithId(String antennaId) {
        for (Platform platform : getChildren()) {
            if (platform instanceof Antenna && ((Antenna) platform).getId().equals(antennaId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Removes {@link Antenna} that has the provided Id. Returns true if antenna was removed.
     */
    public boolean removeAntennaWithId(String antennaId) {
        Antenna antennaToRemove = null;
        for (Platform platform : getChildren()) {
            if (platform instanceof Antenna && ((Antenna) platform).getId().equals(antennaId)) {
                antennaToRemove = (Antenna) platform;
                break;
            }
        }
        return removeAntenna(antennaToRemove);
    }

    /**
     * Removes all {@link Antenna}s that have the provided description text. Returns true if at least
     * one antenna was removed.
     */
    public boolean removeAntennasWithDescription(String description) {
        List<Antenna> antennasToRemove = new ArrayList<>();
        for (Platform platform : getChildren()) {
            if (platform instanceof Antenna
                    && ((Antenna) platform).getConstantDescriptionText().equals(description)) {
                antennasToRemove.add((Antenna) platform);
            }
        }
        removeAntennas(antennasToRemove);
        return !antennasToRemove.isEmpty();
    }

    /**
     * Adds provided {@link Antenna} to collection of child platforms. If there is already an {@link
     * Antenna} with the same id, the existing antenna is removed.
     */
    public void addAntenna(Antenna antenna) {
        removeAntennaWithId(antenna.getId());
        getChildren().add(antenna);
    }

    /**
     * Adds collection of {@link Antenna} objects to collection of child platforms. If there is
     * already an {@link Antenna} with the same id, the existing antenna is removed.
     */
    public void addAntennas(Collection<Antenna> antennas) {
        antennas.forEach(antenna -> removeAntennaWithId(antenna.getId()));
        getChildren().addAll(antennas);
    }

    /**
     * Removes provided {@link Antenna} from collection of child platforms.
     */
    public boolean removeAntenna(Antenna antenna) {
        return getChildren().remove(antenna);
    }

    /**
     * Removes collection of {@link Antenna} objects from collection of child platforms.
     */
    public void removeAntennas(Collection<Antenna> antennas) {
        getChildren().removeAll(antennas);
    }

    /**
     * Removes all {@link Antenna} objects from collection of child platforms.
     */
    public void removeAllAntennas() {
        getChildren().clear();
    }

    private CommsPlatform(String name) {
        super(name);
    }

    private CommsPlatform(CommsPlatform existingInstance, CopyContext context) {
        super(existingInstance, context);
    }
}
