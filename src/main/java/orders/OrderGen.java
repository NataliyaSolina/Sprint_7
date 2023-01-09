package orders;

import couriers.Courier;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.List;
import java.util.Random;

public class OrderGen {
    public Order def() {
        List<Color> color = List.of(Color.GREY, Color.BLACK);
        return new Order("Tasha", "Sol", "Poyuscheva 5-24", "5", "+7 906 368 00 00", 10,"2023-01-07", "Без комментариев", color);
    }

    public Order random() {
        List<Color>[] color = new List[]{List.of(Color.GREY, Color.BLACK), List.of(Color.GREY), List.of(Color.BLACK), null};
        int randomColor = new Random().nextInt(color.length);
        return new Order(RandomStringUtils.randomAlphanumeric(6, 9), RandomStringUtils.randomAlphanumeric(4, 10), RandomStringUtils.randomAlphanumeric(9, 20), "" + (int) (Math.random() * (237) + 1), "+7" + RandomStringUtils.randomNumeric(10), (int) (Math.random() * 9), "2023-01-07", "Без комментариев", color[randomColor]);
    }

    public Order randomWhithoutColor(List<Color> color) {
        return new Order(RandomStringUtils.randomAlphanumeric(6, 9), RandomStringUtils.randomAlphanumeric(4, 10), RandomStringUtils.randomAlphanumeric(9, 20), "" + (int) (Math.random() * (237) + 1), "+7" + RandomStringUtils.randomNumeric(10), (int) (Math.random() * 9), "2023-01-07", "Без комментариев", color);
    }
}
