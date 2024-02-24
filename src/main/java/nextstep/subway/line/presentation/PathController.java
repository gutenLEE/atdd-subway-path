package nextstep.subway.line.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("paths/source={source}&target={target}")
    public ResponseEntity<PathsResponse> getPaths(
            @PathVariable int source, @PathVariable int target) {
        return ResponseEntity.ok(pathService.searchPath(source, target));
    }
}
