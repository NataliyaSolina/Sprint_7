import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.example.orders.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

@RunWith(Parameterized.class)
public class CreateOrderParametrizedTest {
    OrderMethods method = new OrderMethods();
    OrderGen generator = new OrderGen();
    Order order;
    Response response;
    List<Color> color;

    public CreateOrderParametrizedTest(List<Color> color) {
        this.color = color;
    }

    @Parameterized.Parameters(name = "creds - {0}")
    public static Object[][] getTextData() {
        return new Object[][]{
                {List.of(Color.GREY)},
                {List.of(Color.BLACK)},
                {List.of(Color.GREY, Color.BLACK)},
                null
        };
    }

    @Before
    public void setUp() {
        order = generator.randomWithColor(color);
    }

    @Test
    @Description("3.1 можно указать один из цветов — BLACK или GREY, 3.2 можно указать оба цвета, 3.3 можно совсем не указывать цвет, 3.4 тело ответа содержит track")
    public void createCourierValidDataRezultOk() {
        response = method.requestCreateOrder(order);
        method.responseCreateOrderOk(response);
    }
}
