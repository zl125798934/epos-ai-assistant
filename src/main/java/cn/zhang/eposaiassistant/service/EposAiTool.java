package cn.zhang.eposaiassistant.service;

import cn.zhang.eposaiassistant.data.Policy;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Author: zhanglun @Date: 2026/4/28 16:28 @Description:
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EposAiTool {

	private final OperateService operateService;

	@Tool(
			name = "query_policy_detail",
			value = "根据保单号查询保单详细信息，包括保单状态、投保人、投保日期等信息（用于客服查询场景）"
	)
	public Policy getPolicy(
			@P(value = "保单号（policy number），由纯数字组成，例如：80001，必须是用户明确提供或系统上下文中已有的保单号")
			String policyNo
	) {
		return operateService.getPolicy(policyNo);
	}
}
