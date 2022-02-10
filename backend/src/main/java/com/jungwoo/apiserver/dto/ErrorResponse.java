package com.jungwoo.apiserver.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * fileName     : ErrorResponse
 * author       : jungwoo
 * description  :
 */
@Getter @Setter
public class ErrorResponse extends BasicResponse{

  private String reason;

  public ErrorResponse(String reason) {
    this.reason = reason;
  }

}
