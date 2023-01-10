import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.example.orders.*;
import org.junit.Before;
import org.junit.Test;

public class GetOrderByTrackTest {
    OrderMethods method = new OrderMethods();
    OrderGen generator = new OrderGen();
    Order order;
    Response response;
    int track;

    @Before
    public void setUp() {
        order = generator.random();

        response = method.requestCreateOrder(order);
        track = method.responseCreateOrderOk(response);
    }

    @Test
    @Description("d3.1 успешный запрос возвращает объект с заказом")
    public void getOrderByTrackValidDataRezultOk() {
        response = method.requestGetOrderByTrack(track);
        method.responseGetOrderByTrackOk(response);
        method.responseGetOrderByTrackCheckOrder(response, track, order);           //именно тот заказ который создался БАХХХХ плавающий, иногда-часто (не понятно когда) возвращается не тот заказ (трек тот заказ не тот), т е возвращается не тот трек
    }

    @Test
    @Description("d3.2 запрос без номера заказа возвращает ошибку")
    public void getOrderByTrackMissingTrackErrorMissing() {
        response = method.requestGetOrderByTrack(null);
        method.responseGetOrderByTrackErrorMissing(response);
    }

    @Test
    @Description("d3.3 запрос с несуществующим заказом возвращает ошибку")
    public void getOrderByTrackZeroTrackErrorNotFound() {
        response = method.requestGetOrderByTrack(0);
        method.responseGetOrderByTrackErrorNotFound(response);
    }

    @Test
    @Description("d3.3 запрос с несуществующим заказом возвращает ошибку")
    public void getOrderByTrackInvalidTrackErrorNotFound() {
        response = method.requestGetOrderByTrack((int) (Math.random() * (Integer.MAX_VALUE) + 1));      //пальцем в небо не факт что не попадет в рабочий - придумать что-то
        method.responseGetOrderByTrackErrorNotFound(response);
    }
}
