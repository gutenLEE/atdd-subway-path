package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.persistance.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.presentation.SectionResponse;
import nextstep.subway.line.exception.LineNotFoundException;
import nextstep.subway.line.presentation.SectionRequest;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.exception.StationNotFoundException;
import nextstep.subway.station.persistance.StationRepository;
import org.springframework.stereotype.Service;

@Service
public class SectionService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    public SectionService(LineRepository lineRepository,
                          StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }


    public SectionResponse createSection(long lineId, SectionRequest sectionRequest) {

        Line line = getLine(lineId);
        Station downStation = getStation(sectionRequest.getDownStationId());
        Station upStation = getStation(sectionRequest.getUpStationId());
        Section section = new Section(upStation, downStation, sectionRequest.getDistance(), line);
        line.addSection(section);
        Line saved = lineRepository.save(line);

        return new SectionResponse(saved.getUpStation().getId(), saved.getDownStation().getId(), saved.getDistance());
    }

    public void deleteSection(long lineId, long stationId) {

        Line line = getLine(lineId);
        line.removeSection(stationId);

        if(line.deletableSection()) {
            throw new IllegalStateException("구간이 1개여서 역을 삭제할 수 없다");
        }

        line.removeSection(stationId);
        lineRepository.save(line);
    }

    private Line getLine(long lineId) {
        return lineRepository.findById(lineId)
                .orElseThrow(() -> new LineNotFoundException(lineId));
    }

    private Station getStation(long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new StationNotFoundException(Long.toString(stationId)));
    }
}