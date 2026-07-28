package likelion14th.lte.statistic.service;


import jakarta.persistence.EntityManager;
import likelion14th.lte.global.api.ErrorCode;
import likelion14th.lte.global.exception.GeneralException;
import likelion14th.lte.statistic.dto.StatisticResponse;
import likelion14th.lte.statistic.entity.Statistic;
import likelion14th.lte.statistic.entity.StatWeek;
import likelion14th.lte.todo.repository.TodoDateRepository;
import likelion14th.lte.user.entity.User;
import likelion14th.lte.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;


@Service
@RequiredArgsConstructor
public class StatisticService {

    private final UserRepository userRepository;

    private final TodoDateRepository todoDateRepository;

    private final EntityManager entityManager;


    @Transactional(readOnly = true)
    public StatisticResponse getStatistic(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new GeneralException(ErrorCode.USER_NOT_FOUND)
                );

        return StatisticResponse.from(
                user.getStatistic()
        );
    }


    @Transactional
    public void updateStatistic(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new GeneralException(ErrorCode.USER_NOT_FOUND)
                );

        Statistic statistic = user.getStatistic();

        LocalDate day = LocalDate.now()
                .minusDays(1);


        boolean hasCompleted =
                todoDateRepository.existsByTodo_User_IdAndDateAndCompleted(
                        userId,
                        day,
                        true
                );


        boolean hasFailed =
                todoDateRepository.existsByTodo_User_IdAndDateAndCompleted(
                        userId,
                        day,
                        false
                );


        boolean success =
                hasCompleted && !hasFailed;


        statistic.increaseStreakIfSuccess(success);


        if(success) {

            statistic.getStatWeeks()
                    .stream()
                    .filter(statWeek ->
                            statWeek.getWeek()
                                    .toDayOfWeek()
                                    ==
                                    day.getDayOfWeek()
                    )
                    .findFirst()
                    .ifPresent(
                            StatWeek::increaseCount
                    );
        }


        LocalDate start =
                day.minusDays(30);


        int completeCount =
                todoDateRepository.countByTodo_User_IdAndDateBetweenAndCompleted(
                        userId,
                        start,
                        day,
                        true
                );


        int failedCount =
                todoDateRepository.countByTodo_User_IdAndDateBetweenAndCompleted(
                        userId,
                        start,
                        day,
                        false
                );


        int total =
                completeCount + failedCount;


        int percent = 0;


        if(total != 0) {

            percent =
                    completeCount * 100 / total;
        }


        statistic.updateMonthPercent(percent);
    }


    @Transactional
    public void updateAllStatistics() {

        int page = 0;

        int size = 500;

        Page<User> users;


        do {

            users =
                    userRepository.findAll(
                            PageRequest.of(page, size)
                    );


            for(User user : users.getContent()) {

                updateStatistic(user.getId());

            }


            entityManager.flush();

            entityManager.clear();


            page++;


        } while(users.hasNext());

    }

}