import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import orders.OrderMethods;
import org.junit.Before;
import org.junit.Test;

public class ListOrdersTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @Description("4 Проверь, что в тело ответа возвращается список заказов.")
    public void listOrdersTest() {
        OrderMethods method = new OrderMethods();
        Response response;

        response = method.requestListOrdersWithoutParams();
        method.responseListOrdersOk(response);
    }
}