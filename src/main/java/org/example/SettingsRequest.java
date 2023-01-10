package org.example;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import io.restassured.http.ContentType;

public class SettingsRequest {
    protected final String BASE_URI = "https://qa-scooter.praktikum-services.ru";
    protected final String API_PREFIX = "/api/v1";

    protected RequestSpecification getSpec() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(BASE_URI)
                .setBasePath(API_PREFIX)
                .log(LogDetail.URI)
                .log(LogDetail.BODY)
                .build();
    }


}
