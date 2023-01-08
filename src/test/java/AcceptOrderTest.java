import couriers.Courier;
import couriers.CourierMethods;
import couriers.Credentials;
import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import orders.OrderClient;
import orders.OrderMethods;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AcceptOrderTest {
    CourierMethods methodCour = new CourierMethods();
    OrderMethods methodOrder = new OrderMethods();
    Courier courier = new Courier("Tasha_" + RandomStringUtils.randomAlphanumeric(3), "2611", "Tasha");
    Credentials cred = new Credentials(courier.getLogin(), courier.getPassword());
    OrderClient order = new OrderClient(RandomStringUtils.randomAlphabetic(6), RandomStringUtils.randomAlphabetic(3), RandomStringUtils.randomAlphanumeric(9, 20), "" + (int) (Math.random() * (237) + 1), "+7" + RandomStringUtils.randomNumeric(10), 5, "2023-01-07", "Без комментариев", null);
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
            response = methodCour.requestDeleteCourier(courierId);
            methodCour.responseDeleteCourierOk(response);
        }
    }

    @Test
    @Description("d2.1 успешный запрос возвращает ok: true")
    public void acceptOrderValidDataRezultOk() {
        response = methodOrder.requestAcceptOrderById(orderId, courierId);  //акцептнуть
        methodOrder.responseAcceptOrderByIdOk(response);
    }

    @Test
    @Description("d2.2 если не передать id курьера, запрос вернёт ошибку")
    public void acceptOrderMissingCourierIdErrorMissing() {
        response = methodOrder.requestAcceptOrderById(orderId, null);  //акцептнуть
        methodOrder.responseAcceptOrderByIdMissing(response);
    }

    @Test
    @Description("d2.3 если передать неверный id курьера, запрос вернёт ошибку")
    public void acceptOrderInvalidCourierIdErrorNotFound() {
        response = methodOrder.requestAcceptOrderById(orderId, 0);  //акцептнуть
        methodOrder.responseAcceptOrderByIdNotFoundCour(response);
    }

    @Test
    @Description("d2.4 если не передать номер заказа, запрос вернёт ошибку")
    public void acceptOrderMissingOrderIdErrorMissing() {
        response = methodOrder.requestAcceptOrderByIdWithoutOrderId(courierId);  //акцептнуть
        methodOrder.responseAcceptOrderByIdMissing(response);                   //БАХХХХ статускод 404 message = Not Found. (по тз 400 message = Недостаточно данных для поиска)
    }

    @Test
    @Description("d2.5 если передать неверный номер заказа, запрос вернёт ошибку")
    public void acceptOrderInvalidOrderIdErrorNotFound() {
        response = methodOrder.requestAcceptOrderById(0, courierId);  //акцептнуть
        methodOrder.responseAcceptOrderByIdNotFoundOrder(response);
    }
}
