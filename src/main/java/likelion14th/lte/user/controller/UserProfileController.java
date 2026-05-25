package likelion14th.lte.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import likelion14th.lte.global.api.ApiResponse;
import likelion14th.lte.global.api.SuccessCode;
import likelion14th.lte.user.dto.request.CreateTestUserRequest;
import likelion14th.lte.user.dto.response.UserProfileResponse;
import likelion14th.lte.user.service.UserProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/profile")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class UserProfileController {
    public final UserProfileService userProfileService;

    // [Q9. Controller 내부에서 userRepository.findById()를 직접 호출해서 유저를 찾지 않고,
    // 반드시 userProfileService를 호출하여 작업을 위임해야 하는 이유는 무엇인가요? (단일 책임 원칙 관점)]
    // 답변:
    // Controller는 HTTP 요청과 응답을 처리하는 역할만 담당해야 합니다.
    // DB 조회나 비즈니스 로직까지 직접 처리하면 역할이 많아져 유지보수가 어려워집니다.
    // 따라서 실제 비즈니스 로직은 Service에 위임하여
    // Controller는 요청 전달, Service는 비즈니스 처리라는 식으로
    // 책임을 분리하는 것이 단일 책임 원칙(SRP)에 맞습니다.

    @GetMapping
    @Operation(summary = "유저 프로필 조회",description = "유저아이디를 받아 유저 프로필을 반환하는 api 입니다.")
    public ApiResponse<UserProfileResponse> getUserProfile(
            @RequestParam Long userId
    ){
        UserProfileResponse userProfileResponse = userProfileService.getUserProfile(userId);

        return ApiResponse.onSuccess(SuccessCode.OK,userProfileResponse);

    }
    @PostMapping
    @Operation(summary = "테스트 유저를 생성",description = "이름, 한줄소개, 유저 태그를 받아 유저를")
    public ApiResponse<UserProfileResponse> createTestUserProfile(

            // [Q10. 클라이언트가 보낸 JSON 텍스트 데이터가 어떻게 자바 객체인 CreateTestUserRequest로
            // 변환 되는지앞의 어노테이션과 연관 지어 설명해 보세요.]
            // 답변:
            // @RequestBody 어노테이션은 HTTP 요청 Body에 담긴 JSON 데이터를 읽어옵니다.
            // 이후 Spring의 HttpMessageConverter(Jackson 라이브러리)가
            // JSON의 key-value 값을 분석하여
            // CreateTestUserRequest 객체의 필드에 자동으로 매핑(변환)해줍니다.
            // 그래서 개발자는 직접 JSON을 파싱하지 않아도
            // 자바 객체 형태로 바로 사용할 수 있습니다.
            @RequestBody CreateTestUserRequest createTestUserRequest
    ){
        UserProfileResponse response = userProfileService.createTestUser(createTestUserRequest);
        return ApiResponse.onSuccess(SuccessCode.CREATED,response);
    }
}
