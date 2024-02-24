package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import nextstep.subway.fixture.*;
import nextstep.subway.fixture.AcceptanceTest;
import nextstep.subway.line.presentation.LineResponse;
import nextstep.subway.line.presentation.SectionRequest;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.presentation.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.transaction.annotation.Transactional;

/**
 * Outside In TDD
 * 컨트롤러 구현 이후 서비스 구현 시 바로 기능 구현에 앞서 단위 테스트 먼저 작성
 * 서비스 레이어의 단위 테스트 목적은 비즈니스 플로우를 검증하는 것이며 이 때 협력 객체는 stubbing을 활용하여 대체함
 * 단위 테스트 작성 후 해당 단위 테스트를 만족하는 기능을 구현한 다음 리팩터링 진행
 * 그 다음 기능 구현은 방금 전 사이클에서 stubbing 한 객체를 대상으로 진행하면 수월하게 TDD 사이클을 진행할 수 있음
 * 외부 라이브러리를 활용한 로직을 검증할 때는 가급적 실제 객체를 활용
 * Happy 케이스에 대한 부분만 구현( Side 케이스에 대한 구현은 다음 단계에서 진행)
 */
@AcceptanceTest
@Transactional
public class PathAcceptanceTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("출발역으로부터 도착역까지의 경로에 있는 역 목록이 조회된다")
    @Test
    void searchPath() {

        StationResponse 시청 = StationSteps.createStation("시청");
        StationResponse 을지로입구 = StationSteps.createStation("을지로입구");
        StationResponse 을지로3가 = StationSteps.createStation("을지로3가");
        StationResponse 을지로4가 = StationSteps.createStation("을지로4가");
        StationResponse 동대문역사문화공원 = StationSteps.createStation("동대문역사문화공원");
        LineResponse 이호선 = LineSteps.이호선_생성(시청.getId(), 을지로입구.getId());
        SectionSteps.라인에_구간을_추가한다(이호선.getId(), new SectionRequest(을지로입구.getId(), 을지로3가.getId(), 5));
        SectionSteps.라인에_구간을_추가한다(이호선.getId(), new SectionRequest(을지로3가.getId(), 을지로4가.getId(), 8));
        SectionSteps.라인에_구간을_추가한다(이호선.getId(), new SectionRequest(을지로4가.getId(), 동대문역사문화공원.getId(), 4));

        // given
        PathSteps.getPath(1, 3);

        // when

        // then
    }
}
