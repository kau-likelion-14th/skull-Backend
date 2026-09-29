package likelion14th.lte.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserIntroRequest {

    private String introduce;

    public String getIntroduce() {
        return introduce;
    }
}