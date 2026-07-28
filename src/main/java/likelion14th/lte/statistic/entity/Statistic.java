package likelion14th.lte.statistic.entity;

import jakarta.persistence.*;
import likelion14th.lte.Entity.BaseEntity;
import likelion14th.lte.todo.entity.WeekEnum;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Statistic extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long statisticId;

    private int streak;

    private int monthPercent;

    @OneToMany(
            mappedBy = "statistic",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<StatWeek> statWeeks = new ArrayList<>();

    public static Statistic create() {
        Statistic statistic = new Statistic();
        statistic.streak = 0;
        statistic.monthPercent = 0;

        statistic.initializeWeeks();

        return statistic;
    }

    private void initializeWeeks() {
        for (WeekEnum week : WeekEnum.values()) {
            statWeeks.add(StatWeek.create(this, week));
        }
    }

    public WeekEnum getMostTodoWeek() {
        return statWeeks.stream()
                .max((statWeek1, statWeek2) ->
                        Integer.compare(statWeek1.getCount(), statWeek2.getCount()))
                .map(StatWeek::getWeek)
                .orElse(null);
    }

    public void increaseStreakIfSuccess(boolean success) {
        if (success) {
            this.streak++;
        } else {
            this.streak = 0;
        }
    }

    public void updateMonthPercent(int monthPercent) {
        this.monthPercent = monthPercent;
    }
}