import couriers.*;
import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class CreateCourierParametrizedTest {
    private final String login;
    private final String password;

    public CreateCourierParametrizedTest(String login, String password) {
        this.login = login;
        this.password = password;
    }

    @Parameterized.Parameters
    public static Object[][] getTextData() {
        return new Object[][]{
                {null, "2611"},
                {"Tasha", null}
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @Description("1.6 если одного из полей нет, запрос возвращает ошибку")
    public void createCourierMissingRequiredFieldTest() {
        CourierMethods method = new CourierMethods();
        Courier courier = new Courier(login, password, "Tasha");
        Response response;

        response = method.requestCreateCourier(courier);
        method.responseCreateCourierErrorMissing(response);              //проверка ответа ERROR
    }
}
