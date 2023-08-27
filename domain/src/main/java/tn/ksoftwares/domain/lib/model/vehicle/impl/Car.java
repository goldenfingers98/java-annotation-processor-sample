package tn.ksoftwares.domain.lib.model.vehicle.impl;

import tn.ksoftwares.annotation.processors.annotation.Builder;
import tn.ksoftwares.domain.lib.model.vehicle.Vehicle;

import java.util.Objects;

@Builder
public class Car implements Vehicle {

    private final String brandName;
    private final String modelName;

    public Car(String brandName, String modelName) {
        this.brandName = brandName;
        this.modelName = modelName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return Objects.equals(brandName, car.brandName) && Objects.equals(modelName, car.modelName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(brandName, modelName);
    }
}
