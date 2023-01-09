package couriers;

import org.apache.commons.lang3.RandomStringUtils;

public class CourierGen {
    public Courier def() {
        return new Courier("TashaS", "2611", "Tasha");
    }

    public Courier random() {
        return new Courier(RandomStringUtils.randomAlphanumeric(6, 9), RandomStringUtils.randomAlphanumeric(4, 10), RandomStringUtils.randomAlphanumeric(3, 9));
    }
}
