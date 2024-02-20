package nextstep.subway.line.domain;

import nextstep.subway.line.exception.DuplicateSectionException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.exception.StationNotFoundException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;
import java.util.stream.Collectors;

@Embeddable
public class LineSections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new LinkedList<>();

    protected LineSections() {
    }



    public Optional<Section> find(long stationId) {
        return sections.stream()
                .filter(section -> section.getDownStation().getId() == stationId)
                .findFirst();
    }

    public void remove(Section deleteSection) {
        Station lastStation = getLastStation();
        if (!lastStation.equals(deleteSection.getDownStation())) {
            throw new IllegalStateException("하행 종점만 삭제 가능");
        }

        this.sections = sections.stream()
                .filter(it -> !it.getDownStation().equals(lastStation))
                .collect(Collectors.toList());
    }

    public boolean deletable() {
        return this.sections.size() > 1;
    }

    public void add(Section newSection) {

        if (iaAppend(newSection)) {
            this.sections.add(newSection);
            return;
        }
        insertSection(newSection);
    }

    private boolean iaAppend(Section newSection) {
        return isEmptySection() || isAppendToLast(newSection);
    }

    private void insertSection(Section newSection) {
        if (hasDuplicateSection(newSection)) {
            throw new DuplicateSectionException(newSection.toString());
        }

        Section asis = findDivideTarget(newSection);
        int pos = this.sections.indexOf(asis);
        this.sections.remove(pos);
        sections.addAll(pos, asis.divide(newSection));
    }

    public List<Station> getStations() {
        Set<Station> set = new LinkedHashSet<>();
        for (Section section : sections) {
            set.add(section.getUpStation());
            set.add(section.getDownStation());
        }
        return List.copyOf(set);
    }


    public Station getFirstStation() {
        return this.sections.get(0).getUpStation();
    }


    public Station getLastStation() {
        return this.sections.get(sections.size() - 1).getDownStation();
    }


    private Section findDivideTarget(Section newSection) {
        return this.sections.stream()
                .filter(section -> section.getUpStation().equals(newSection.getUpStation()))
                .findFirst()
                .orElseThrow(() -> new StationNotFoundException(newSection.getUpStation().toString()));
    }


    private boolean hasDuplicateSection(Section newSection) {
        return this.sections.stream().anyMatch(asis -> asis.equalSection(newSection));
    }


    private boolean isAppendToLast(Section newSection) {
        return getLastStation().equals(newSection.getUpStation());
    }


    private boolean isEmptySection() {
        return this.sections.isEmpty();
    }
}
