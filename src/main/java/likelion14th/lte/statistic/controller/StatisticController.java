package likelion14th.lte.statistic.controller;

import likelion14th.lte.statistic.dto.StatisticResponse;
import likelion14th.lte.statistic.service.StatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/statistic")
public class StatisticController {

    private final StatisticService statisticService;


    @GetMapping
    public ResponseEntity<StatisticResponse> getStatistic(
            @RequestParam Long userId
    ) {

        return ResponseEntity.ok(
                statisticService.getStatistic(userId)
        );
    }
}
