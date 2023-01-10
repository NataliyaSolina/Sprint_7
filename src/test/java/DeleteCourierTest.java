import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.example.couriers.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DeleteCourierTest {
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
        response = method.requestAuthCourier(cred);
        courierId = method.responseAuthCourierOk(response);
    }

    @After
    public void cleanUp() {
        response = method.requestAuthCourier(cred);                              //удаление за собой если есть
        if (response.statusCode() == 200) {
            courierId = method.responseAuthCourierOk(response);
            method.requestDeleteCourier(courierId);
        }
    }

    @Test
    @Description("d1.2 успешный запрос возвращает ok: true")
    public void deleteCourierValidDataRezultOk() {
        response = method.requestDeleteCourier(courierId);
        method.responseDeleteCourierOk(response);
    }

    @Test
    @Description("d1.1 неуспешный запрос возвращает соответствующую ошибку, d1.3 если отправить запрос без id, вернётся ошибка")
    public void deleteCourierMissingDataRezultErrorMissing() {
        response = method.requestDeleteCourier(courierId);
        method.responseDeleteCourierWithoutParamsErrorMissing(response);                   //проверка ответа БАХХХХ статускод 404 message = Not Found. (по тз 400 message = Недостаточно данных для удаления курьера)
    }

    @Test
    @Description("d1.1 неуспешный запрос возвращает соответствующую ошибку, d1.4 если отправить запрос с несуществующим id, вернётся ошибка")
    public void deleteCourierInvalideDataRezultErrorNotFound() {
        response = method.requestDeleteCourier((int) (Math.random() * (Integer.MAX_VALUE - (courierId + 1)) + (courierId + 1)));            //удаление
        method.responseDeleteCourierErrorNotFound(response);                                                  //проверка ответа БАХХХХ присутствует символ точка в конце сттроки message (по тз без точки)
    }
}
