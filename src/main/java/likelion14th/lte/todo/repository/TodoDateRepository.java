package likelion14th.lte.todo.repository;

import likelion14th.lte.todo.entity.TodoDate;
import likelion14th.lte.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.*;

public interface TodoDateRepository extends JpaRepository<TodoDate,Long> {

    Optional<TodoDate> findByTodo_IdAndDate(Long todoId, LocalDate date);

    List<TodoDate> findByTodo_User_IdAndDate(Long userId, LocalDate date);

    List<TodoDate> findAllByTodo_IdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);

    void deleteAllByTodo_IdAndDateGreaterThanEqual(Long todoId, LocalDate from);

    List<TodoDate> findAllByTodo_User_IdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);


    List<TodoDate> findAllByTodo_User(User todoUser);

    boolean existsByTodo_User_IdAndDateAndCompleted(
            Long userId,
            LocalDate date,
            boolean completed
    );


    int countByTodo_User_IdAndDateBetweenAndCompleted(
            Long userId,
            LocalDate startDate,
            LocalDate endDate,
            boolean completed
    );
}
