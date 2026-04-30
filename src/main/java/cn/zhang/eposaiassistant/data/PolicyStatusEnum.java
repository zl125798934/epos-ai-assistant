package cn.zhang.eposaiassistant.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 保单状态枚举。
 */
@Getter
@AllArgsConstructor
public enum PolicyStatusEnum {

  /** 待生效。 */
  INIT("待生效"),

  /** 生效中。 */
  NORMAL("生效中"),

  /** 已失效。 */
  INVALID("已失效");

  private final String description;
}
