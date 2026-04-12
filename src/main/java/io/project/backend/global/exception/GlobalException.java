package io.project.backend.global.exception;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
public class GlobalException extends RuntimeException {

  private final ErrorCode errorCode;
  private final Map<String, Object> details;

  /**
   * <p>상세 에러 정보가 없는 경우</p>
   * 간단한 에러 체크
   *
   * @param errorCode 에러 코드
   */
  public GlobalException(ErrorCode errorCode) {
    super(errorCode.getDescription());
    this.errorCode = errorCode;
    this.details = new HashMap<>();
  }

  /**
   * <p>상세 에러 정보가 있는 경우</p>
   * 자세한 에러 체크
   *
   * @param errorCode 에러 코드
   * @param details 에러 상세
   */
  public GlobalException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode.getDescription());
    this.errorCode = errorCode;
    this.details = details;
  }
}
