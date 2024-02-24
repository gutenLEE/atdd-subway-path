package nextstep.subway.line.presentation;

import java.util.List;

public class PathsResponse {

    private int distance;
    private List<StationDto> stationDtoList;

    public PathsResponse(int distance, List<StationDto> stationDtoList) {
        this.distance = distance;
        this.stationDtoList = stationDtoList;
    }

    public int getDistance() {
        return distance;
    }

    public List<StationDto> getStationDtoList() {
        return stationDtoList;
    }
}
