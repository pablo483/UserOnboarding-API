package sudarshangc.com.UserUnboardingApi.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogOutRequest {

   private String refreshToken;
}
