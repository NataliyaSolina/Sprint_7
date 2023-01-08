package orders;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.Is.is;

public class OrderMethods {

    @Step("Send POST request to /api/v1/orders")
    public Response requestCreateOrder(OrderClient orderClient) {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(orderClient)
                        .when()
                        .post("/api/v1/orders");
        System.out.println("requestCreateOrder " + response.body().asString());
        return response;
    }

    @Step("Receive POST response Ok to /api/v1/orders")
    public int responseCreateOrderOk(Response response) {
        response
                .then()
                .assertThat()
                .statusCode(201)
                .and()
                .body("track", notNullValue());
        return response
                .path("track");
    }

    @Step("Send GET request to /api/v1/orders")
    public Response requestListOrdersWithoutParams() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .get("/api/v1/orders");
        System.out.println("requestListOrderWithoutParams " + response.body().asString());
        return response;
    }

    @Step("Receive GET response Ok to /api/v1/orders")
    public void responseListOrdersOk(Response response) {
        response
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("orders", notNullValue())
                .body("orders.id[0]", notNullValue())
                .body("orders.id[1]", notNullValue())
                .body("orders.id[29]", notNullValue())
                .body("orders.track[0]", notNullValue())
                .body("orders.status[0]", notNullValue());
    }

    @Step("Send GET request to /api/v1/orders/track")
    public Response requestGetOrderByTrack(Integer track) {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .queryParam("t", track)
                        .get("/api/v1/orders/track");
        System.out.println("requestGetOrderByTrack " + response.body().asString());
        return response;
    }

    @Step("Receive GET response Ok to /api/v1/orders/track")
    public int responseGetOrderByTrackOk(Response response) {
        response
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("order.id", notNullValue());      //TODO check all body
        return response
                .path("order.id");
    }

    @Step("Receive GET response ErrorMissing to /api/v1/orders/track")
    public void responseGetOrderByTrackErrorMissing(Response response) {
        response
                .then()
                .assertThat()
                .statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для поиска"));
    }

    @Step("Receive GET response ErrorNotFound to /api/v1/orders/track")
    public void responseGetOrderByTrackErrorNotFound(Response response) {
        response
                .then()
                .assertThat()
                .statusCode(404)
                .and()
                .body("message", equalTo("Заказ не найден"));
    }

    @Step("Send PUT request to /api/v1/orders/accept/{orderId}")
    public Response requestAcceptOrderById(Integer orderId, Integer courierId) {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .pathParam("orderId", orderId)
                        .queryParam("courierId", courierId)
                        .put("/api/v1/orders/accept/{orderId}");
        System.out.println("requestAcceptOrderById " + response.body().asString());
        return response;
    }

    @Step("Send PUT request to /api/v1/orders/accept/{orderId}")
    public Response requestAcceptOrderByIdWithoutOrderId(Integer courierId) {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .queryParam("courierId", courierId)
                        .put("/api/v1/orders/accept");
        System.out.println("requestAcceptOrderById " + response.body().asString());
        return response;
    }

    @Step("Receive PUT response Ok to /api/v1/orders/accept/{orderId}")
    public void responseAcceptOrderByIdOk(Response response) {
        response
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("ok", is(true));
    }

    @Step("Receive PUT response NotFoundCour to /api/v1/orders/accept/{orderId}")
    public void responseAcceptOrderByIdNotFoundCour(Response response) {
        response
                .then()
                .assertThat()
                .statusCode(404)
                .and()
                .body("message", equalTo("Курьера с таким id не существует"));
    }

    @Step("Receive PUT response NotFoundOrder to /api/v1/orders/accept/{orderId}")
    public void responseAcceptOrderByIdNotFoundOrder(Response response) {
        response
                .then()
                .assertThat()
                .statusCode(404)
                .and()
                .body("message", equalTo("Заказа с таким id не существует"));
    }

    @Step("Receive PUT response Missing to /api/v1/orders/accept/{orderId}")
    public void responseAcceptOrderByIdMissing(Response response) {
        response
                .then()
                .assertThat()
                .statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для поиска"));
    }

}
