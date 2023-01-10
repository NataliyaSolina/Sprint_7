import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.example.orders.OrderMethods;
import org.junit.Test;

public class ListOrdersTest {
    OrderMethods method = new OrderMethods();
    Response response;

    @Test
    @Description("4 Проверь, что в тело ответа возвращается список заказов.")
    public void listOrdersTest() {
        response = method.requestListOrdersWithoutParams();
        method.responseListOrdersOk(response);
    }
}
