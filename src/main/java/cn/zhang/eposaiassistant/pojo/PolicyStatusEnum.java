package cn.zhang.eposaiassistant.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: zhanglun @Date: 2026/4/28 17:41 @Description:
 */
@Getter
@AllArgsConstructor
public enum PolicyStatusEnum {
	INIT("待生效"),
	NORMAL("生效中"),
	INVALID("已失效");

	private final String description;
}
