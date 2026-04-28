package cn.zhang.eposaiassistant.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: zhanglun @Date: 2026/4/28 17:39
 * @Description: 保全状态
 */
@Getter
@AllArgsConstructor
public enum PosStatusEnum {
	ACCEPTED("已受理"),
	PROCESSING("处理中"),
	SUCCESS("处理成功"),
	FAILED("处理失败"),
	CANCELLED("已取消");

	private final String description;
}
