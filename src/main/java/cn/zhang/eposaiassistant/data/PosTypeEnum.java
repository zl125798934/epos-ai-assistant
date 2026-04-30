package cn.zhang.eposaiassistant.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 保全类型枚举。
 */
@Getter
@AllArgsConstructor
public enum PosTypeEnum {

  /** 犹豫期退保。 */
  PROBATIONARY_SURRENDER("犹豫期退保"),

  /** 退保。 */
  SURRENDER("退保"),

  /** 手机号变更。 */
  PHONE_CHANGE("手机号变更"),

  /** 个人信息变更。 */
  PERSONAL_INFO_CHANGE("个人信息变更");

  private final String description;
}
