import couriers.Courier;
import couriers.CourierMethods;
import couriers.Credentials;
import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static io.restassured.RestAssured.given;

public class DeleteCourierTest {
    CourierMethods method = new CourierMethods();
    Courier courier = new Courier("Tasha_" + RandomStringUtils.randomAlphanumeric(3), "2611", "Tasha");
    Credentials cred = new Credentials(courier.getLogin(), courier.getPassword());
    Response response;
    int courierId;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        method.requestCreateCourier(courier);
        response = method.requestAuthCourier(cred);
        courierId = method.responseAuthCourierOk(response);
    }

    @After
    public void cleanUp() {
        response = method.requestAuthCourier(cred);                              //удаление за собой если есть
        if (response.statusCode() == 200) {
            courierId = method.responseAuthCourierOk(response);
            response = method.requestDeleteCourier(courierId);
            method.responseDeleteCourierOk(response);
        }
    }

    @Test
    @Description("d1.2 успешный запрос возвращает ok: true")
    public void deleteCourierValidDataRezultOk() {
        response = method.requestDeleteCourier(courierId);            //удаление
        method.responseDeleteCourierOk(response);                   //проверка ответа
    }

    @Test
    @Description("d1.1 неуспешный запрос возвращает соответствующую ошибку, d1.3 если отправить запрос без id, вернётся ошибка")
    public void deleteCourierMissingDataRezultErrorMissing() {
        response = method.requestDeleteCourier();
        method.responseDeleteCourierWithoutParamsErrorMissing(response);                   //проверка ответа БАХХХХ статускод 404 message = Not Found. (по тз 400 message = Недостаточно данных для удаления курьера)
    }

    @Test
    @Description("d1.1 неуспешный запрос возвращает соответствующую ошибку, d1.4 если отправить запрос с несуществующим id, вернётся ошибка")
    public void deleteCourierInvalideDataRezultErrorNotFound() {
//        response = method.requestDeleteCourier((int) (Math.random() * (Integer.MAX_VALUE - 2) +1));            //удаление
        response = method.requestDeleteCourier(0);                                                      //удаление
        method.responseDeleteCourierErrorNotFound(response);                                                  //проверка ответа БАХХХХ присутствует символ точка в конце сттроки message (по тз без точки)
    }
}
