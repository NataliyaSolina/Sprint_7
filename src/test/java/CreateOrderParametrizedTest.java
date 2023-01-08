import io.qameta.allure.Description;
import orders.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

@RunWith(Parameterized.class)
public class CreateOrderParametrizedTest {

    private final List<Color> color;

    public CreateOrderParametrizedTest(List<Color> color) {
        this.color = color;
    }

    @Parameterized.Parameters
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
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @Description("3.1 можно указать один из цветов — BLACK или GREY, 3.2 можно указать оба цвета, 3.3 можно совсем не указывать цвет, 3.4 тело ответа содержит track")
    public void createCourierValidDataRezultOk() {
        OrderMethods method = new OrderMethods();
        OrderClient orderClient = new OrderClient(RandomStringUtils.randomAlphabetic(6), RandomStringUtils.randomAlphabetic(3), RandomStringUtils.randomAlphanumeric(9, 20), "" + (int) (Math.random() * (237) + 1), "+7" + RandomStringUtils.randomNumeric(10), 5, "2023-01-07", "Без комментариев", color);
        Response response;
        int track;

        response = method.requestCreateOrder(orderClient);
        track = method.responseCreateOrderOk(response);
    }
}
