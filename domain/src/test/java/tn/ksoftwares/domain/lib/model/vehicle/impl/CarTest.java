package tn.ksoftwares.domain.lib.model.vehicle.impl;

import tn.ksoftwares.domain.lib.model.vehicle.impl.CarBuilder;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CarTest {

    private static final String BRAND_NAME = "Lamborghini";
    private static final String MODEL_NAME = "Aventador S";

    private final Car expectedCar = new Car(BRAND_NAME, MODEL_NAME);

    @Test
    public void testCarBuilder() {
        final Car actualCar = new CarBuilder()
                .setBrandName(BRAND_NAME)
                .setModelName(MODEL_NAME)
                .build();
        assertEquals("The actual car instance should be equal to the expected one.", expectedCar, actualCar);
    }
}