import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.couriers.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class AuthCourierInvalidDataParametrizedTest {
    CourierMethods method = new CourierMethods();
    Courier courier;
    Credentials cred;
    Credentials credCheck;
    Response response;
    int courierId;
    private final String login;
    private final String password;
    private final String loginCred;
    private final String passwordCred;

    public AuthCourierInvalidDataParametrizedTest(String login, String password, String loginCred, String passwordCred) {
        this.login = login;
        this.password = password;
        this.loginCred = loginCred;
        this.passwordCred = passwordCred;
    }

    @Parameterized.Parameters(name = "creds - {2}, {3}")
    public static Object[][] getTextData() {
        return new Object[][]{
                {"Tasha", "2611", RandomStringUtils.randomAlphabetic(3), "2611"},
                {"Tasha", "2611", "Tasha_" + RandomStringUtils.randomAlphabetic(3), "2611"},
                {"Tasha", "2611", "Tasha", RandomStringUtils.randomAlphabetic(4)}
        };
    }

    @Before
    public void setUp() {
        courier = new Courier(login, password, "Tasha");
        cred = Credentials.from(courier);
        credCheck = new Credentials(loginCred, passwordCred);

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
    @Description("2.3 система вернёт ошибку, если неправильно указать логин или пароль, 2.5 если авторизоваться под несуществующим пользователем, запрос возвращает ошибку")
    public void authCourierTest() {
        response = method.requestAuthCourier(credCheck);
        method.responseAuthCourierErrorNotFound(response);
    }
}
