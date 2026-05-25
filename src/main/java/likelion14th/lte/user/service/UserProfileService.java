package likelion14th.lte.user.service;

import org.springframework.transaction.annotation.Transactional;
import likelion14th.lte.global.api.ErrorCode;
import likelion14th.lte.global.exception.GeneralException;
import likelion14th.lte.user.dto.request.CreateTestUserRequest;
import likelion14th.lte.user.dto.response.UserProfileResponse;
import likelion14th.lte.user.entity.User;
import likelion14th.lte.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class UserProfileService {

    // [Q5. Service 안에서 new UserRepository() 로 객체를 직접 생성하지 않고,
    // 외부에서 의존성 주입(DI)을 받는 이유는 무엇인가요? (결합도와 단위 테스트 관점)]
    // 답변:
    // 객체를 직접 생성하면 Service가 특정 Repository 구현체에 강하게 의존하게 되어
    // 결합도가 높아집니다. DI를 사용하면 필요한 객체를 외부에서 주입받기 때문에
    // 구현체를 쉽게 교체할 수 있고 유지보수가 편해집니다.
    // 또한 단위 테스트 시 실제 Repository 대신 Mock 객체를 주입할 수 있어
    // DB 없이도 독립적인 테스트가 가능합니다.
    private final UserRepository userRepository;

    // [Q6. (코딩 문제) 만약 클래스 위의 @RequiredArgsConstructor를 지운다면,
    // 우리가 직접 작성해야 할 의존성 주입용 자바 '생성자' 코드는 어떤 모습일까요? 아래에 직접 코딩해 보세요.]
    /*
       여기에 생성자 코드 작성:

       protected UserProfileService(UserRepository userRepository){
           this.userRepository = userRepository;
       }

    */

    @Transactional
    public UserProfileResponse createTestUser(CreateTestUserRequest request){

        // [Q7. 일반적인 생성자 new User(name, intro, tag) 방식을 쓰지 않고,
        // User.builder()...build() 라는 '빌더 패턴'을 사용하여 객체를 조립했을 때 얻는 장점은 무엇인가요?]
        // 답변:
        // 빌더 패턴은 매개변수가 많을 때 가독성이 좋아지고,
        // 어떤 값이 어떤 필드에 들어가는지 명확하게 알 수 있습니다.
        // 또한 매개변수 순서를 헷갈릴 위험이 줄어들며,
        // 필요한 값만 선택적으로 넣을 수 있어 객체 생성이 유연해집니다.
        User newUser = User.builder()
                .username(request.getUsername())
                .userTag(request.getUserTag())
                .introduction(request.getIntroduction())
                .build();

        User savedUser;
        try{

            // [Q8. 데이터를 저장하는 이 메서드 위에 @Transactional이 반드시 붙어야 하는 이유는 무엇인가요?
            // (저장 도중 DB 서버가 끊겼을 때의 상황을 가정해서 설명하세요)]
            // 답변:
            // @Transactional은 작업 도중 문제가 발생했을 때
            // 이전 작업들을 모두 롤백(취소)하여 데이터의 일관성을 보장합니다.
            // 예를 들어 DB 저장 중 서버 연결이 끊기면,
            // 일부 데이터만 저장되는 비정상 상태를 막고
            // 전체 작업을 원래 상태로 되돌릴 수 있습니다.
            savedUser = userRepository.save(newUser);
        }
        catch(Exception e){
            throw new GeneralException(ErrorCode.BAD_REQUEST);
        }
        return UserProfileResponse.from(savedUser);
    }

    @Transactional(readOnly = true)
    public UserProfileResponse getUserProfile(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new GeneralException(ErrorCode.USER_NOT_FOUND));

        return UserProfileResponse.from(user);
    }
}
