package cn.zhang.eposaiassistant.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: zhanglun @Date: 2026/4/28 17:39
 * @Description: 保全类型
 */
@Getter
@AllArgsConstructor
public enum PosTypeEnum {
	PROBATIONARY_SURRENDER("犹豫期退保"),
	SURRENDER("退保"),
	PHONE_CHANGE("手机号变更"),
	PERSONAL_INFO_CHANGE("个人信息变更");

	private final String description;
}
