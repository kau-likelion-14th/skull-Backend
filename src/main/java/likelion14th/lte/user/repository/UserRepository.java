package likelion14th.lte.user.repository;

import likelion14th.lte.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findById(Long id);
    Page<User> findByUsernameContainingIgnoreCase(String nickName, Pageable pageable);
    Optional<User> findByUserTag(String userTag);

    @Query("SELECT u FROM User u " +
            "WHERE u.id != :userId " +
            "AND NOT EXISTS (SELECT f FROM Follow f WHERE f.fromUser.id = :userId AND f.toUser.id = u.id)")
    Page<User> findCanFollowUser(@Param("userId")Long userId, Pageable pageable);
}

// [추가문제] (필수 X) 이 코드는 인터페이스일 뿐이고 구현체(implements) 클래스가 없습니다.
// 그런데 어떻게 프로그램 실행 시 DB와 통신하는 객체로 동작할 수 있나요?
// 답변:
// JpaRepository는 Spring Data JPA가 제공하는 인터페이스입니다.
// 스프링은 프로그램 실행 시점에 동적 프록시(Dynamic Proxy) 기술을 사용하여
// UserRepository 인터페이스의 구현 객체를 자동으로 생성합니다.
//
// 따라서 개발자가 직접 implements 클래스를 작성하지 않아도
// save(), findById() 같은 기본 CRUD 기능이 자동으로 구현됩니다.
//
// 이는 반복적인 DB 코드를 줄여 개발 생산성을 높이고,
// 인터페이스 기반 설계를 통해 구현체와의 결합도를 낮춰
// 유지보수성과 확장성을 향상시키는 장점이 있습니다.