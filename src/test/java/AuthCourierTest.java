import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.example.couriers.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AuthCourierTest {
    CourierMethods method = new CourierMethods();
    CourierGen generator = new CourierGen();
    Courier courier;
    Credentials cred;
    Response response;
    int courierId;

    @Before
    public void setUp() {
        courier = generator.random();
        cred = Credentials.from(courier);

        method.requestCreateCourier(courier);
    }

    @After
    public void cleanUp() {
        response = method.requestAuthCourier(cred);                             // удаление за собой если есть
        if (response.statusCode() == 200) {
            courierId = method.responseAuthCourierOk(response);
            method.requestDeleteCourier(courierId);
        }
    }

    @Test
    @Description("2.1 курьер может авторизоваться, 2.2 для авторизации нужно передать все обязательные поля, 2.6 успешный запрос возвращает id")
    public void authCourierTest() {
        response = method.requestAuthCourier(cred);
        method.responseAuthCourierOk(response);
    }
}