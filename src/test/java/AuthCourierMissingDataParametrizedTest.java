import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.example.couriers.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class AuthCourierMissingDataParametrizedTest {
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

    public AuthCourierMissingDataParametrizedTest(String login, String password, String loginCred, String passwordCred) {
        this.login = login;
        this.password = password;
        this.loginCred = loginCred;
        this.passwordCred = passwordCred;
    }

    @Parameterized.Parameters(name = "creds - {2}, {3}")
    public static Object[][] getTextData() {
        return new Object[][]{
                {"Tasha", "2611", null, "2611"},
                {"Tasha", "2611", "", "2611"},
                {"Tasha", "2611", "Tasha", null},
                {"Tasha", "2611", "Tasha", ""}
        };
    }

    @Before
    public void setUp() {
        courier = new Courier(login, password, "Tasha");
        cred = Credentials.from(courier);
        credCheck = new Credentials(loginCred, passwordCred);

        method.requestCreateCourier(courier);                                   //Before создание
    }

    @After
    public void cleanUp() {
        response = method.requestAuthCourier(cred);                             //After удаление за собой если есть
        if (response.statusCode() == 200) {
            courierId = method.responseAuthCourierOk(response);
            method.requestDeleteCourier(courierId);
        }
    }

    @Test
    @Description("2.4 если какого-то поля нет, запрос возвращает ошибку")
    public void authCourierTest() {
        response = method.requestAuthCourier(credCheck);
        method.responseAuthCourierErrorMissing(response);                   // БАХХХХ статускод 504 (ожид 400) при отсутствии пароля
    }
}
