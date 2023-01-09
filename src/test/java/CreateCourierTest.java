import couriers.*;
import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CreateCourierTest {
    CourierMethods method = new CourierMethods();
    CourierGen generator = new CourierGen();
    Courier courier = generator.random();
    Credentials cred = Credentials.from(courier);
    Response response;
    int courierId;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";

        response = method.requestAuthCourier(cred);                         //Проверка может есть такой и дел если что
        if (response.statusCode() == 200) {
            courierId = method.responseAuthCourierOk(response);
            method.requestDeleteCourier(courierId);
        }
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
    @Description("1.1 курьера можно создать, 1.3 чтобы создать курьера, нужно передать в ручку все обязательные поля, 1.4 запрос возвращает правильный код ответа, 1.5 успешный запрос возвращает ok: true")
    public void createCourierValidDataRezultOk() {
        response = method.requestCreateCourier(courier);
        method.responseCreateCourierOk(response);
    }

    @Test
    @Description("1.2 нельзя создать двух одинаковых курьеров, 1.7 если создать пользователя с логином, который уже есть, возвращается ошибка")
    public void createCourierDoubleDataRezultErrorDouble() {
        response = method.requestCreateCourier(courier);            //создание подготовка
        method.responseCreateCourierOk(response);

        response = method.requestCreateCourier(courier);            //создание второй раз (те же данные) - сам тест
        method.responseCreateCourierErrorDouble(response);          //проверка ответа БАХХХХ присутствует символы (. Попробуйте другой.) в конце сттроки message (по тз не должно)
    }
}
