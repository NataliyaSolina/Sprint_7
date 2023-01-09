package couriers;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.Is.is;

public class CourierMethods {
    @Step("Send POST request to /api/v1/courier")
    public Response requestCreateCourier(Courier courier) {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(courier)
                        .when()
                        .post("/api/v1/courier");
        System.out.println("requestCreateCourier " + response.body().asString());
        return response;
    }

    @Step("Receive POST response Ok to /api/v1/courier")
    public void responseCreateCourierOk(Response response) {
        response
                .then()
                .assertThat()
                .statusCode(SC_CREATED)
                .and()
                .body("ok", is(true));
    }

    @Step("Receive POST response ErrorDouble to /api/v1/courier")
    public void responseCreateCourierErrorDouble(Response response) {
        response
                .then()
                .assertThat()
                .statusCode(SC_CONFLICT)
                .and()
                .body("message", equalTo("Этот логин уже используется"));
    }

    @Step("Receive POST response ErrorMissing to /api/v1/courier")
    public void responseCreateCourierErrorMissing(Response response) {
        response
                .then()
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Step("Send POST request to /api/v1/courier/login")
    public Response requestAuthCourier(Credentials cred) {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(cred)
                        .when()
                        .post("/api/v1/courier/login");
        System.out.println("requestAuthCourier " + response.body().asString());
        return response;
    }

    @Step("Receive POST response Ok to /api/v1/courier/login")
    public int responseAuthCourierOk(Response response) {
        response
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("id", notNullValue());
        return response
                .path("id");
    }

    @Step("Receive POST response NotFound to /api/v1/courier/login")
    public void responseAuthCourierErrorNotFound(Response response) {
        response
                .then()
                .assertThat()
                .statusCode(SC_NOT_FOUND)
                .and()
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Step("Receive POST response ErrorMissing to /api/v1/courier/login")
    public void responseAuthCourierErrorMissing(Response response) {
        response
                .then()
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Step("Send DELETE request to /api/v1/courier/{courierId}")
    public Response requestDeleteCourier(Integer courierId) {
        Response response;
        if (courierId != null) {
            response =
                    given()
//                        .spec()
//                        .log().all()
                            .header("Content-type", "application/json")
                            .when()
                            .pathParams("courierId", courierId)
                            .delete("/api/v1/courier/{courierId}");
        } else {
            response =
                    given()
                            .header("Content-type", "application/json")
                            .when()
                            .delete("/api/v1/courier/");
        }
        System.out.println("requestDeleteCourier " + response.body().asString());
        return response;
    }

//    @Step("Send DELETE request to /api/v1/courier")
//    public Response requestDeleteCourierWithoutParams() {
//        Response response =
//                given()
//                        .header("Content-type", "application/json")
//                        .when()
//                        .delete("/api/v1/courier/");
//        System.out.println("requestDeleteCourier " + response.body().asString());
//        return response;
//    }

    @Step("Receive DELETE response Ok to /api/v1/courier/{courierId}")
    public void responseDeleteCourierOk(Response response) {
        response
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("ok", is(true));
    }

    @Step("Receive DELETE response ErrorMissing to /api/v1/courier")
    public void responseDeleteCourierWithoutParamsErrorMissing(Response response) {
        response
                .then()
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для удаления курьера"));
    }

    @Step("Receive DELETE response NotFound to /api/v1/courier/{courierId}")
    public void responseDeleteCourierErrorNotFound(Response response) {
        response
                .then()
                .assertThat()
                .statusCode(SC_NOT_FOUND)
                .and()
                .body("message", equalTo("Курьера с таким id нет"));
    }
}
