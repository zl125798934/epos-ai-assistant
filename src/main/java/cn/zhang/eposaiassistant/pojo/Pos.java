package cn.zhang.eposaiassistant.pojo;

import lombok.Data;

/**
 * @Author: zhanglun @Date: 2026/4/28 17:34 @Description:
 */
@Data
public class Pos {
	// 受理单号
	private int acceptNo;
	// 申请人
	private String applicant;
	// 保单号
	private String policyNo;
	// 保全类型
	private String posType;
	// 保全状态
	private String posStatus;
	// 审核结果（通过/拒绝）
	private String auditResult;
}
