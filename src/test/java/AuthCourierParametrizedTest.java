import couriers.*;
import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class AuthCourierParametrizedTest {
    private final String login;
    private final String password;
    private final String loginCred;
    private final String passwordCred;
    private final String methodParam;

    public AuthCourierParametrizedTest(String login, String password, String loginCred, String passwordCred, String methodParam) {
        this.login = login;
        this.password = password;
        this.loginCred = loginCred;
        this.passwordCred = passwordCred;
        this.methodParam = methodParam;
    }

    @Parameterized.Parameters(name ="creds - {2}, {3}")
    public static Object[][] getTextData() {
        return new Object[][]{
                {"Tasha", "2611", "Tasha", "2611", "Ok"},
                {"Tasha", "2611", "Tasha_" + RandomStringUtils.randomAlphabetic(3), "2611", "NotFound"},
                {"Tasha", "2611", "Tasha", RandomStringUtils.randomNumeric(4), "NotFound"},
                {"Tasha", "2611", null, "2611", "Missing"},
                {"Tasha", "2611", "Tasha", null, "Missing"}
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @Description("2.1 курьер может авторизоваться, 2.2 для авторизации нужно передать все обязательные поля, 2.3 система вернёт ошибку, если неправильно указать логин или пароль, 2.4 если какого-то поля нет, запрос возвращает ошибку, 2.5 если авторизоваться под несуществующим пользователем, запрос возвращает ошибку, 2.6 успешный запрос возвращает id")
    public void authCourierTest() {
        Courier courier = new Courier(login, password, "Tasha");
        Credentials cred = new Credentials(courier.getLogin(), courier.getPassword());
        Credentials credCheck = new Credentials(loginCred, passwordCred);
        CourierMethods method = new CourierMethods();
        Response response;
        int courierId;

        method.requestCreateCourier(courier);                                   //Before создание


        response = method.requestAuthCourier(credCheck);
        switch (methodParam) {
            case "Ok":
                method.responseAuthCourierOk(response);                             //авторизации - сам тест)
                break;
            case "NotFound":
                method.responseAuthCourierErrorNotFound(response);                  //ошибка авторизации - сам тест)
                break;
            case "Missing":
                method.responseAuthCourierErrorMissing(response);                   //ошибка авторизации БАХХХХ статускод 504 (ожид 400) при отсутствии пароля
                break;
        }


        response = method.requestAuthCourier(cred);                             //After удаление за собой если есть
        if (response.statusCode() == 200) {
            courierId = method.responseAuthCourierOk(response);
            response = method.requestDeleteCourier(courierId);
            method.responseDeleteCourierOk(response);
        }
    }
}
