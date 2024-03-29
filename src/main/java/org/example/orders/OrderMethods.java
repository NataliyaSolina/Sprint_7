package org.example.orders;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.example.SettingsRequest;

import static io.restassured.RestAssured.given;
import static java.util.Objects.nonNull;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;

public class OrderMethods extends SettingsRequest {

    @Step("Send POST request to /api/v1/orders")
    public Response requestCreateOrder(Order order) {
        Response response =
                given()
                        .spec(getSpec())
                        .and()
                        .body(order)
                        .when()
                        .post("/orders");
        System.out.println("requestCreateOrder " + response.body().asString());
        return response;
    }

    @Step("Receive POST response Ok to /api/v1/orders")
    public int responseCreateOrderOk(Response response) {
        response
                .then()
                .assertThat()
                .statusCode(SC_CREATED)
                .and()
                .body("track", greaterThan(0));
        return response
                .path("track");
    }

    @Step("Send GET request to /api/v1/orders")
    public Response requestListOrdersWithoutParams() {
        Response response =
                given()
                        .spec(getSpec())
                        .when()
                        .get("/orders");
        System.out.println("requestListOrderWithoutParams " + response.body().asString());
        return response;
    }

    @Step("Receive GET response Ok to /api/v1/orders")
    public void responseListOrdersOk(Response response) {
        response
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("orders", notNullValue())
                .body("orders.id[0]", notNullValue())
                .body("orders.id[0]", greaterThan(0))
                .body("orders.id[1]", notNullValue())
                .body("orders.id[1]", greaterThan(0))
                .body("orders.id[29]", notNullValue())
                .body("orders.id[29]", greaterThan(0))
                .body("orders.track[0]", notNullValue())
                .body("orders.track[0]", greaterThan(0))
                .body("orders.status[0]", notNullValue());
    }

    @Step("Send GET request to /api/v1/orders/track")
    public Response requestGetOrderByTrack(Integer track) {
        Response response;
        if (track != null) {
            response =
                    given()
                            .spec(getSpec())
                            .queryParam("t", track)
                            .when()
                            .get("/orders/track");
            System.out.println("requestGetOrderByTrack " + response.body().asString());
        } else {
            response =
                    given()
                            .spec(getSpec())
                            .when()
                            .get("/orders/track");
        }
        return response;
    }

    @Step("Receive GET response Ok to /api/v1/orders/track")
    public int responseGetOrderByTrackOk(Response response) {
        response
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("order", notNullValue())
                .body("order.id", notNullValue())
                .body("order.id", greaterThan(0))
                .body("order.track", notNullValue())
                .body("order.track", greaterThan(0));
        return response
                .path("order.id");
    }

    @Step("Receive GET response Ok to /api/v1/orders/track")
    public void responseGetOrderByTrackCheckOrder(Response response, Integer track, Order order) {
        response
                .then()
                .assertThat()
                .body("order.id", greaterThan(0))
                .body("order.firstName", equalTo(order.getFirstName()))
                .body("order.lastName", equalTo(order.getLastName()))
                .body("order.address", equalTo(order.getAddress()))
                .body("order.metroStation", equalTo(order.getMetroStation()))
                .body("order.phone", equalTo(order.getPhone()))
                .body("order.rentTime", equalTo(order.getRentTime()))
                .body("order.deliveryDate", containsString(order.getDeliveryDate()))
                .body("order.track", equalTo(track))
                .body("order.comment", equalTo(order.getComment()))
                .body("order.comment", equalTo(order.getComment()))
                .body("order.comment", equalTo(order.getComment()))
                .body("order.comment", equalTo(order.getComment()));
        if (nonNull(order.getColor())) {
            assertEquals(response
                    .then()
                    .extract()
                    .path("order.color").toString(), (order.getColor().toString()));
        } else {
            response
                    .then()
                    .assertThat()
                    .body("order.color", nullValue());
        }
    }

    @Step("Receive GET response ErrorMissing to /api/v1/orders/track")
    public void responseGetOrderByTrackErrorMissing(Response response) {
        response
                .then()
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для поиска"));
    }

    @Step("Receive GET response ErrorNotFound to /api/v1/orders/track")
    public void responseGetOrderByTrackErrorNotFound(Response response) {
        response
                .then()
                .assertThat()
                .statusCode(SC_NOT_FOUND)
                .and()
                .body("message", equalTo("Заказ не найден"));
    }

    @Step("Send PUT request to /api/v1/orders/accept/{orderId}")
    public Response requestAcceptOrderById(Integer orderId, Integer courierId) {
        Response response;
        if (orderId != null) {
            response =
                    given()
                            .spec(getSpec())
                            .pathParam("orderId", orderId)
                            .queryParam("courierId", courierId)
                            .when()
                            .put("/orders/accept/{orderId}");
        } else {
            response =
                    given()
                            .spec(getSpec())
                            .queryParam("courierId", courierId)
                            .when()
                            .put("/orders/accept");
        }
        System.out.println("requestAcceptOrderById " + response.body().asString());
        return response;
    }

    @Step("Receive PUT response Ok to /api/v1/orders/accept/{orderId}")
    public void responseAcceptOrderByIdOk(Response response) {
        response
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("ok", is(true));
    }

    @Step("Receive PUT response NotFoundCour to /api/v1/orders/accept/{orderId}")
    public void responseAcceptOrderByIdNotFoundCour(Response response) {
        response
                .then()
                .assertThat()
                .statusCode(SC_NOT_FOUND)
                .and()
                .body("message", equalTo("Курьера с таким id не существует"));
    }

    @Step("Receive PUT response NotFoundOrder to /api/v1/orders/accept/{orderId}")
    public void responseAcceptOrderByIdNotFoundOrder(Response response) {
        response
                .then()
                .assertThat()
                .statusCode(SC_NOT_FOUND)
                .and()
                .body("message", equalTo("Заказа с таким id не существует"));
    }

    @Step("Receive PUT response Missing to /api/v1/orders/accept/{orderId}")
    public void responseAcceptOrderByIdMissing(Response response) {
        response
                .then()
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для поиска"));
    }
}
