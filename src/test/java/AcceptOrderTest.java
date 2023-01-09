import couriers.Courier;
import couriers.CourierGen;
import couriers.CourierMethods;
import couriers.Credentials;
import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import orders.Order;
import orders.OrderGen;
import orders.OrderMethods;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AcceptOrderTest {
    CourierMethods methodCour = new CourierMethods();
    OrderMethods methodOrder = new OrderMethods();
    CourierGen generatorCour = new CourierGen();
    Courier courier = generatorCour.random();
    Credentials cred = Credentials.from(courier);
    OrderGen generatorOrder = new OrderGen();
    Order order = generatorOrder.random();
    Response response;
    int courierId;
    int orderId;
    int track;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";

        methodCour.requestCreateCourier(courier);                           //создать курьера
        response = methodCour.requestAuthCourier(cred);
        courierId = methodCour.responseAuthCourierOk(response);             //получить ид

        response = methodOrder.requestCreateOrder(order);                   //создать заказ
        track = methodOrder.responseCreateOrderOk(response);                //получить track

        response = methodOrder.requestGetOrderByTrack(track);
        orderId = methodOrder.responseGetOrderByTrackOk(response);          //получитьб ид по траку
    }

    @After
    public void cleanUp() {
        response = methodCour.requestAuthCourier(cred);                     //удаление за собой если есть
        if (response.statusCode() == 200) {
            courierId = methodCour.responseAuthCourierOk(response);
            methodCour.requestDeleteCourier(courierId);
        }
    }

    @Test
    @Description("d2.1 успешный запрос возвращает ok: true")
    public void acceptOrderValidDataRezultOk() {
        response = methodOrder.requestAcceptOrderById(orderId, courierId);
        methodOrder.responseAcceptOrderByIdOk(response);
    }

    @Test
    @Description("d2.2 если не передать id курьера, запрос вернёт ошибку")
    public void acceptOrderMissingCourierIdErrorMissing() {
        response = methodOrder.requestAcceptOrderById(orderId, null);
        methodOrder.responseAcceptOrderByIdMissing(response);
    }

    @Test
    @Description("d2.3 если передать неверный id курьера, запрос вернёт ошибку")
    public void acceptOrderInvalidCourierIdErrorNotFound() {
        response = methodOrder.requestAcceptOrderById(orderId, (int) (Math.random() * (Integer.MAX_VALUE - (courierId + 1)) + (courierId + 1)));
        methodOrder.responseAcceptOrderByIdNotFoundCour(response);
    }

    @Test
    @Description("d2.4 если не передать номер заказа, запрос вернёт ошибку")
    public void acceptOrderMissingOrderIdErrorMissing() {
        response = methodOrder.requestAcceptOrderById(null, courierId);
        methodOrder.responseAcceptOrderByIdMissing(response);                   //БАХХХХ статускод 404 message = Not Found. (по тз 400 message = Недостаточно данных для поиска)
    }

    @Test
    @Description("d2.5 если передать неверный номер заказа, запрос вернёт ошибку")
    public void acceptOrderInvalidOrderIdErrorNotFound() {
        response = methodOrder.requestAcceptOrderById((int) (Math.random() * (Integer.MAX_VALUE - (orderId + 1)) + (orderId + 1)), courierId);
        methodOrder.responseAcceptOrderByIdNotFoundOrder(response);
    }
}
