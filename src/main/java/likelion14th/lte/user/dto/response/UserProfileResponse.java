package likelion14th.lte.user.dto.response;


import likelion14th.lte.user.entity.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class UserProfileResponse {
    private String username;
    private String profileImageUrl;
    private String introduction;

    // [Q4. Controller가 DB에서 꺼낸 원본 Entity(User)를 클라이언트 화면에 그대로 반환하지 않고,
    // 굳이 from() 메서드를 통해 DTO로 한번 변환해서 내보내는 핵심적인 이유 2가지는 무엇인가요?]
    // 답변:
    // 첫 번째 이유는 보안성과 데이터 보호 때문입니다.
    // Entity에는 비밀번호, DB 식별자(id), 내부 관리용 데이터 등
    // 클라이언트에게 노출되면 안 되는 정보가 포함될 수 있습니다.
    // DTO를 사용하면 화면에 필요한 데이터만 선택적으로 전달할 수 있어
    // 불필요한 정보 노출을 방지할 수 있습니다.
    //
    // 두 번째 이유는 계층 간의 역할 분리와 유지보수성 향상 때문입니다.
    // Entity는 데이터베이스 테이블 구조를 표현하는 객체이고,
    // DTO는 클라이언트 화면에 필요한 데이터 형태를 표현하는 객체입니다.
    // 만약 Entity를 그대로 반환하면 화면 요구사항 변경이 DB 구조 변경으로 이어질 수 있습니다.
    // DTO로 변환하면 DB 구조와 API 응답 구조를 분리할 수 있어
    // 유지보수성과 확장성이 향상됩니다.

    public static UserProfileResponse from (User user){
        return new UserProfileResponse(
                user.getUsername() + "#" + user.getUserTag(),
                user.getProfileImage(),
                user.getIntroduction()
        );
    }
}
