import couriers.*;
import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CreateCourierTest {
    CourierMethods method = new CourierMethods();
    Courier courier = new Courier("Tasha_" + RandomStringUtils.randomAlphanumeric(3), "2611", "Tasha");
    Credentials cred = new Credentials(courier.getLogin(), courier.getPassword());
    Response response;
    int courierId;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";

        response = method.requestAuthCourier(cred);                         //Проверка может есть такой и дел если что
        if (response.statusCode() == 200) {
            courierId = method.responseAuthCourierOk(response);
            response = method.requestDeleteCourier(courierId);
            method.responseDeleteCourierOk(response);
        }
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
    @Description("1.1 курьера можно создать, 1.3 чтобы создать курьера, нужно передать в ручку все обязательные поля, 1.4 запрос возвращает правильный код ответа, 1.5 успешный запрос возвращает ok: true")
    public void createCourierValidDataRezultOk() {
        response = method.requestCreateCourier(courier);            //создание
        method.responseCreateCourierOk(response);                   //проверка ответа
    }

    @Test
    @Description("1.2 нельзя создать двух одинаковых курьеров, 1.7 если создать пользователя с логином, который уже есть, возвращается ошибка")
    public void createCourierDoubleDataRezultErrorDouble() {
        response = method.requestCreateCourier(courier);            //создание подготовка
        method.responseCreateCourierOk(response);                   //проверка ответа

        response = method.requestCreateCourier(courier);            //создание второй раз (те же данные) - сам тест
        method.responseCreateCourierErrorDouble(response);          //проверка ответа БАХХХХ присутствует символы (. Попробуйте другой.) в конце сттроки message (по тз не должно)
    }
}
