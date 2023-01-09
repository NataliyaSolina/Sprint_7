import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import orders.Order;
import orders.OrderGen;
import orders.OrderMethods;
import org.junit.Before;
import org.junit.Test;

public class GetOrderByTrackTest {
    OrderMethods method = new OrderMethods();
    OrderGen generator = new OrderGen();
    Order order = generator.random();
    Response response;
    int track;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";

        response = method.requestCreateOrder(order);
        track = method.responseCreateOrderOk(response);
    }

    @Test
    @Description("d3.1 успешный запрос возвращает объект с заказом")
    public void getOrderByTrackValidDataRezultOk() {
        response = method.requestGetOrderByTrack(track);
        method.responseGetOrderByTrackOk(response);
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
        response = method.requestGetOrderByTrack((int) (Math.random() * (Integer.MAX_VALUE) + 1));      //пальцем в небо не факт что не попадет в рабочий - придумать что-то
//        response = method.requestGetOrderByTrack(0);
        method.responseGetOrderByTrackErrorNotFound(response);
    }
}
