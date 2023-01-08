import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import orders.OrderClient;
import orders.OrderMethods;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

public class GetOrderByTrackTest {

    OrderMethods method = new OrderMethods();
    OrderClient orderClient = new OrderClient(RandomStringUtils.randomAlphabetic(6), RandomStringUtils.randomAlphabetic(3), RandomStringUtils.randomAlphanumeric(9, 20), "" + (int) (Math.random() * (237) + 1), "+7" + RandomStringUtils.randomNumeric(10), 5, "2023-01-07", "Без комментариев", null);
    Response response;
    int track;
    int orderId;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";

        response = method.requestCreateOrder(orderClient);
        track = method.responseCreateOrderOk(response);
    }

    @Test
    @Description("d3.1 успешный запрос возвращает объект с заказом")
    public void getOrderByTrackValidDataRezultOk() {

        response = method.requestGetOrderByTrack(track);
        orderId = method.responseGetOrderByTrackOk(response);
    }

    @Test
    @Description("d3.2 запрос без номера заказа возвращает ошибку")
    public void getOrderByTrackMissingTrackErrorMissing() {

        response = method.requestGetOrderByTrack(null);
        method.responseGetOrderByTrackErrorMissing(response);
    }

    @Test
    @Description("d3.3 запрос с несуществующим заказом возвращает ошибку")
    public void getOrderByTrackInvalidTrackErrorNotFound() {

        response = method.requestGetOrderByTrack(0);
        method.responseGetOrderByTrackErrorNotFound(response);
    }


}
