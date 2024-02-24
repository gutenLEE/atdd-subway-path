package nextstep.subway.fixture;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import nextstep.subway.line.presentation.LineResponse;
import nextstep.subway.station.presentation.StationResponse;
import org.springframework.http.HttpStatus;

import java.util.List;

public class PathSteps {

    public static List<StationResponse> getPath(int source, int target) {
        // paths?source=1&target=3
        return RestAssured.given().log().all()
                .queryParam("source", source)
                .queryParam("target", target)
                .when()
                .post("/paths")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(new TypeRef<>() {});
    }
}
