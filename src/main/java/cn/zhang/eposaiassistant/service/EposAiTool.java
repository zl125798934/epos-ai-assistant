package cn.zhang.eposaiassistant.service;

import cn.zhang.eposaiassistant.data.Policy;
import cn.zhang.eposaiassistant.data.Pos;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * EPOS AI 工具服务。
 *
 * <p>为 LangChain4j Agent 提供保单查询、保全办理等工具能力。</p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EposAiTool {

  private final OperateService operateService;

  /**
   * 根据保单号查询保单详情。
   *
   * @param policyNo 保单号
   * @return 保单信息
   */
  @Tool(
      name = "query_policy_detail",
      value = "根据保单号查询保单详细信息，包括保单状态、投保人、投保日期等信息（用于客服查询场景）"
  )
  public Policy getPolicy(
      @P(value = "保单号（policy number），由纯数字组成，例如：80001，必须是用户明确提供或系统上下文中已有的保单号")
      final String policyNo
  ) {
    return operateService.getPolicy(policyNo);
  }

  /**
   * 办理退保申请。
   *
   * @param applicant  投保人姓名
   * @param policyNo   保单号
   * @return 办理结果提示
   */
  @Tool(
      name = "apply_surrender",
      value = "为用户办理退保申请，创建保全受理记录。需要提供投保人姓名和保单号。返回受理单号。"
  )
  public String applySurrender(
      @P(value = "投保人姓名，例如：张三")
      final String applicant,
      @P(value = "保单号，例如：80001")
      final String policyNo
  ) {
    log.info("办理退保申请: 申请人={}, 保单号={}", applicant, policyNo);
    int acceptNo = operateService.addPos(applicant, policyNo, "退保");
    return String.format("退保申请已受理，受理号为#%d。后续想了解退保的情况，可以输入'退保进度'来查询。祝您生活愉快！", acceptNo);
  }

  /**
   * 查询保全受理单详情。
   *
   * @param policyNo 保单号（可选）
   * @param acceptNo 受理单号（可选）
   * @return 保全详情文本
   */
  @Tool(
      name = "query_pos_detail",
      value = "根据保单号或受理单号查询保全受理单详情，包括保全类型、当前状态、审核结果等"
  )
  public String queryPosDetail(
      @P(value = "保单号（可选），例如：80001。如果提供了受理单号，此参数可为空")
      final String policyNo,
      @P(value = "受理单号（可选），例如：101。如果提供了保单号，此参数可为空")
      final Integer acceptNo
  ) {
    log.info("查询保全进度: 保单号={}, 受理单号={}", policyNo, acceptNo);
    Pos pos = operateService.getPosByPolicyNoOrAcceptNo(policyNo, acceptNo);
    if (pos == null) {
      return "未查询到相关保全受理记录，请确认保单号或受理单号是否正确。";
    }
    return String.format("保单%s的保全受理单#%d：\n-保全类型：%s\n-当前状态：%s\n-审核结果：%s",
        pos.getPolicyNo(), pos.getAcceptNo(), pos.getPosType(), pos.getPosStatus(),
        pos.getAuditResult() != null ? pos.getAuditResult() : "待审核");
  }

  /**
   * 取消保全受理单。
   *
   * @param policyNo 保单号（可选）
   * @param acceptNo 受理单号（可选）
   * @return 取消结果提示
   */
  @Tool(
      name = "cancel_pos",
      value = "取消已提交的保全受理单（如取消退保）。需要提供保单号或受理单号。"
  )
  public String cancelPos(
      @P(value = "保单号（可选），例如：80001")
      final String policyNo,
      @P(value = "受理单号（可选），例如：101")
      final Integer acceptNo
  ) {
    log.info("取消保全受理单: 保单号={}, 受理单号={}", policyNo, acceptNo);
    Pos pos = operateService.getPosByPolicyNoOrAcceptNo(policyNo, acceptNo);
    if (pos == null) {
      return "未查询到相关保全受理记录，无法取消。";
    }
    operateService.updatePostStatus(pos.getAcceptNo(), "已取消");
    return String.format("您的保单%s，保全受理号#%d，取消成功！", pos.getPolicyNo(), pos.getAcceptNo());
  }

  /**
   * 办理手机号变更。
   *
   * @param applicant  投保人姓名
   * @param policyNo   保单号
   * @param newPhoneNo 新手机号
   * @return 变更结果提示
   */
  @Tool(
      name = "apply_phone_change",
      value = "为用户办理手机号变更申请。需要提供投保人姓名、保单号和新手机号。变更完成后立即生效，无需审批。"
  )
  public String applyPhoneChange(
      @P(value = "投保人姓名，例如：张三")
      final String applicant,
      @P(value = "保单号，例如：80001")
      final String policyNo,
      @P(value = "新手机号，11位数字，例如：13800138001")
      final String newPhoneNo
  ) {
    log.info("办理手机号变更: 申请人={}, 保单号={}, 新手机号={}", applicant, policyNo, newPhoneNo);
    int acceptNo = operateService.addPhoneChangePos(applicant, policyNo, newPhoneNo);
    return String.format("手机号变更已受理并生效，受理号为#%d。您的保单%s手机号已更新为%s。", acceptNo, policyNo, newPhoneNo);
  }
}
