package cn.zhang.eposaiassistant.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 保全状态枚举。
 */
@Getter
@AllArgsConstructor
public enum PosStatusEnum {

  /** 已受理。 */
  ACCEPTED("已受理"),

  /** 处理中。 */
  PROCESSING("处理中"),

  /** 处理成功。 */
  SUCCESS("处理成功"),

  /** 处理失败。 */
  FAILED("处理失败"),

  /** 已取消。 */
  CANCELLED("已取消");

  private final String description;
}
