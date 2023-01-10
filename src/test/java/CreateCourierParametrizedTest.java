import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.example.couriers.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class CreateCourierParametrizedTest {
    CourierMethods method = new CourierMethods();
    Courier courier;
    Response response;
    private final String login;
    private final String password;

    public CreateCourierParametrizedTest(String login, String password) {
        this.login = login;
        this.password = password;
    }

    @Parameterized.Parameters(name = "creds - {0}, {1}")
    public static Object[][] getTextData() {
        return new Object[][]{
                {null, "2611"},
                {"", "2611"},
                {"Tasha", null},
                {"Tasha", ""}
        };
    }

    @Before
    public void setUp() {
        courier = new Courier(login, password, "Tasha");
    }

    @Test
    @Description("1.6 если одного из полей нет, запрос возвращает ошибку")
    public void createCourierMissingRequiredFieldTest() {
        response = method.requestCreateCourier(courier);
        method.responseCreateCourierErrorMissing(response);
    }
}
