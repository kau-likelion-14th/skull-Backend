package likelion14th.lte.user.entity;

import jakarta.persistence.*;
import likelion14th.lte.Entity.BaseEntity;
import likelion14th.lte.follow.entity.Follow;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
// [Q1. @NoArgsConstructor는 매개변수가 없는 기본 생성자를 만듭니다.
// 그런데 왜 누구나 쓸 수 있게 PUBLIC으로 열어두지 않고, 굳이 PROTECTED로 막아두었을까요? (객체 생성의 안전성과 JPA 관점)]
// 답변:
// JPA는 데이터베이스에서 데이터를 조회할 때 리플렉션 기술을 사용하여 기본 생성자로 객체를 생성합니다.
// 따라서 매개변수가 없는 기본 생성자가 반드시 필요합니다.
// 하지만 생성자를 public으로 열어두면 외부에서 무분별하게 객체를 생성할 수 있어 객체의 일관성이 깨질 위험이 있습니다.
// protected로 제한하면 JPA 내부에서는 객체 생성이 가능하면서도,
// 외부에서는 직접 생성하지 못하게 하여 객체 생성의 안정성과 캡슐화를 유지할 수 있습니다.
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // [Q2. @Column(nullable = false) 어노테이션이 DB와 자바 코드 사이에서 하는 역할은 무엇인가요?]
    // 답변:
    // nullable = false는 해당 컬럼에 null 값이 저장되지 못하도록 제한하는 옵션입니다.
    // 이를 통해 DB 레벨에서 데이터 무결성을 보장할 수 있습니다.
    // 또한 자바 코드에서도 "username은 반드시 존재해야 하는 값"이라는 의미를 명확하게 전달하여
    // 개발자의 실수를 줄이고 안정적인 데이터 관리를 가능하게 합니다.
    @Column(nullable = false)
    private String username;

    @Column(length = 16,nullable = false,unique = true)
    private String userTag;

    @Column(columnDefinition = "TEXT")
    private String introduction;

    @Column(columnDefinition = "TEXT")
    private String profileImage;

    @Column(columnDefinition = "TEXT")
    private String s3ImageKey;

    @OneToMany(mappedBy = "toUser",fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Follow> followers;

    @OneToMany(mappedBy = "fromUser",fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Follow> followings;




    @Builder(access = AccessLevel.PUBLIC)
    private User (String username, String userTag, String introduction){
        this.username = username;
        this.userTag = userTag;
        this.introduction = introduction;
        this.followers = new ArrayList<>();
        this.followings = new ArrayList<>();
    }

    // [Q3. @Setter를 위 @Getter 처럼 사용하면 모든 멤버들에 setIntroduction() 같은 setter 메서드가 생성됩니다.
    // 하지만 왜 @Setter를 쓰지 않고 updateIntroduction() 이라는 명확한 메서드를 만든 객체지향적인 이유는 무엇인가요?]
    // 답변:
    // @Setter를 사용하면 모든 필드 값이 외부에서 자유롭게 변경될 수 있어 객체의 캡슐화가 약해집니다.
    // 반면 updateIntroduction()처럼 목적이 명확한 메서드를 사용하면
    // "자기소개를 수정한다"라는 행위를 객체 스스로 책임지게 만들 수 있습니다.
    // 이는 객체지향 설계의 캡슐화 원칙을 지키고,
    // 이후 수정 로직에 검증이나 추가 기능이 필요해져도 메서드 내부에서 관리할 수 있어 유지보수성이 향상됩니다.
    public void updateIntroduction(String introduction){
        this.introduction = introduction;
    }

}
