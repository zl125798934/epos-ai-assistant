package cn.zhang.eposaiassistant.service;

import cn.zhang.eposaiassistant.data.*;
import jakarta.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 运营业务服务。
 *
 * <p>提供保单与保全的查询、新增及状态变更能力。</p>
 */
@Slf4j
@Service
public class OperateService {

  private final List<Policy> policyList = new ArrayList<>();
  private final List<Pos> posList = new ArrayList<>();

  /**
   * 获取保单列表。
   *
   * @return 按保单号降序排列的保单列表
   */
  public List<Policy> getPols() {
    return policyList
        .stream()
        .sorted((final Policy p1, final Policy p2) -> p2.getPolicyNo().compareTo(p1.getPolicyNo()))
        .toList();
  }

  /**
   * 获取保单详情。
   *
   * @param policyNo 保单号
   * @return 保单信息，未找到时返回 {@code null}
   */
  public Policy getPolicy(final String policyNo) {
    return policyList.stream().filter(policy -> policy.getPolicyNo().equals(policyNo)).findFirst().orElse(null);
  }

  /**
   * 获取保全列表。
   *
   * @return 按受理单号降序排列的保全列表
   */
  public List<Pos> getPos() {
    return posList.stream()
        .sorted((final Pos p1, final Pos p2) -> Integer.compare(p2.getAcceptNo(), p1.getAcceptNo()))
        .toList();
  }

  /**
   * 添加保全。
   *
   * @param applicant 申请人
   * @param policyNo  保单号
   * @param posType   保全类型
   * @return 受理单号
   */
  public int addPos(final String applicant, final String policyNo, final String posType) {

    int maxAcceptNo = posList.stream()
        .mapToInt(Pos::getAcceptNo)
        .max()
        .orElse(0);
    int acceptNo = maxAcceptNo + 1;

    Pos pos = new Pos();
    pos.setAcceptNo(acceptNo);
    pos.setApplicant(applicant);
    pos.setPolicyNo(policyNo);
    pos.setPosType(posType);
    pos.setPosStatus(PosStatusEnum.ACCEPTED.getDescription());
    posList.add(pos);

    return acceptNo;
  }


  /**
   * 修改保全状态。
   *
   * @param acceptNo  受理单号
   * @param posStatus 保全状态
   */
  public void updatePostStatus(final int acceptNo, final String posStatus) {
    Pos pos = posList.stream().filter(p -> p.getAcceptNo() == acceptNo).findFirst().orElse(null);
    if (pos != null) {
      pos.setPosStatus(posStatus);
    }
  }

  /**
   * 修改保单手机号。
   *
   * @param policyNo 保单号
   * @param phoneNo  手机号
   */
  public void updatePolicyPhoneNo(final String policyNo, final String phoneNo) {
    Policy policy = getPolicy(policyNo);
    if (policy != null) {
      policy.setPhoneNo(phoneNo);
    }
  }

  /**
   * 添加手机号变更保全（无需审批，直接成功）。
   *
   * @param applicant  申请人
   * @param policyNo   保单号
   * @param newPhoneNo 新手机号
   * @return 受理单号
   */
  public int addPhoneChangePos(final String applicant, final String policyNo, final String newPhoneNo) {
    int acceptNo = addPos(applicant, policyNo, PosTypeEnum.PHONE_CHANGE.getDescription());
    // 手机号变更无需审批，直接变更为处理成功
    updatePostStatus(acceptNo, PosStatusEnum.SUCCESS.getDescription());
    // 同步更新保单手机号
    updatePolicyPhoneNo(policyNo, newPhoneNo);
    return acceptNo;
  }

  /**
   * 根据保单号或受理单号查询保全记录。
   *
   * @param policyNo 保单号
   * @param acceptNo 受理单号
   * @return 保全记录，未找到时返回 {@code null}
   */
  public Pos getPosByPolicyNoOrAcceptNo(final String policyNo, final Integer acceptNo) {
    if (policyNo != null && !policyNo.isEmpty()) {
      return posList.stream().filter(p -> p.getPolicyNo().equals(policyNo)).findFirst().orElse(null);
    }
    if (acceptNo != null) {
      return posList.stream().filter(p -> p.getAcceptNo() == acceptNo).findFirst().orElse(null);
    }
    return null;
  }

  /**
   * 初始化演示数据。
   */
  @PostConstruct
  private void initData() {
    List<String> applicant = List.of("张三", "李四", "王五");
    List<String> phoneNos = List.of("13800138000", "13900139000", "13700137000");

    for (int i = 0; i < 3; i++) {
      Policy policy = new Policy();
      policy.setPolicyNo(String.valueOf(80001 + i));
      policy.setApplicant(applicant.get(i % 3));
      policy.setPhoneNo(phoneNos.get(i % 3));
      policy.setPurchaseDate(LocalDate.now().minusDays(i));
      policy.setPolicyStatus(PolicyStatusEnum.NORMAL.getDescription());
      policyList.add(policy);
    }
  }
}
