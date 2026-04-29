package cn.zhang.eposaiassistant.data;

import lombok.Data;

import java.time.LocalDate;

/**
 * @Author: zhanglun @Date: 2026/4/28 17:30
 * @Description: 保单
 */
@Data
public class Policy {

	// 保单号
	private String policyNo;
	// 投保人
	private String applicant;
	// 投保日期
	private LocalDate purchaseDate;
	// 保单状态
	private String policyStatus;
	// 变更原因
	private String changeReason;
	// 手机号
	private String phoneNo;
}
