package cn.zhang.eposaiassistant.service;

import cn.zhang.eposaiassistant.data.*;
import jakarta.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Author: zhanglun @Date: 2026/4/28 17:26
 * @Description:
 */
@Slf4j
@Service
public class OperateService {

  	private final List<Policy> policyList = new ArrayList<>();
	private final List<Pos> posList = new ArrayList<>();
	
	/**
	 * 获取保单列表
	 * @return
	 */
	public List<Policy> getPols(){
		return policyList
				.stream()
				.sorted((p1, p2) -> p2.getPolicyNo().compareTo(p1.getPolicyNo()))
				.toList();
	}

	/**
	 * 获取保单详情
	 * @param policyNo
	 * @return
	 */
	public Policy getPolicy(String policyNo){
		return policyList.stream().filter(policy -> policy.getPolicyNo().equals(policyNo)).findFirst().orElse(null);
	}
	
	/**
	 * 获取保全列表
	 * @return
	 */
	public List<Pos> getPos(){
		return posList.stream()
				.sorted((p1, p2) -> Integer.compare(p2.getAcceptNo(), p1.getAcceptNo()))
				.toList();
	}

	/**
	 * 添加保全
	 * @param applicant
	 * @param policyNo
	 * @param posType
	 * @return
	 */
	public int addPos(String applicant, String policyNo, String posType) {

		int maxAcceptNo = posList.stream()
				.mapToInt(Pos::getAcceptNo)
				.max()
				.orElse(0);
		int acceptNo = maxAcceptNo+1;

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
	 * 修改保全状态
	 * @param acceptNo
	 * @param posStatus
	 */
	public void updatePostStatus(int acceptNo, String posStatus) {
		Pos pos = posList.stream().filter(p -> p.getAcceptNo() == acceptNo).findFirst().orElse(null);
		if (pos != null) {
			pos.setPosStatus(posStatus);
		}
	}

	/**
	 * 修改保单手机号
	 * @param policyNo
	 * @param phoneNo
	 */
	public void updatePolicyPhoneNo(String policyNo, String phoneNo) {
		Policy policy = getPolicy(policyNo);
		if (policy != null) {
			policy.setPhoneNo(phoneNo);
		}
	}

	/**
	 * 添加手机号变更保全（无需审批，直接成功）
	 * @param applicant
	 * @param policyNo
	 * @param newPhoneNo
	 * @return
	 */
	public int addPhoneChangePos(String applicant, String policyNo, String newPhoneNo) {
		int acceptNo = addPos(applicant, policyNo, PosTypeEnum.PHONE_CHANGE.getDescription());
		// 手机号变更无需审批，直接变更为处理成功
		updatePostStatus(acceptNo, PosStatusEnum.SUCCESS.getDescription());
		// 同步更新保单手机号
		updatePolicyPhoneNo(policyNo, newPhoneNo);
		return acceptNo;
	}

	/**
	 * 根据保单号或受理单号查询保全记录
	 * @param policyNo
	 * @param acceptNo
	 * @return
	 */
	public Pos getPosByPolicyNoOrAcceptNo(String policyNo, Integer acceptNo) {
		if (policyNo != null && !policyNo.isEmpty()) {
			return posList.stream().filter(p -> p.getPolicyNo().equals(policyNo)).findFirst().orElse(null);
		}
		if (acceptNo != null) {
			return posList.stream().filter(p -> p.getAcceptNo() == acceptNo).findFirst().orElse(null);
		}
		return null;
	}

	@PostConstruct
	private void initData() {
		List<String> applicant = List.of("张三", "李四", "王五");
		List<String> phoneNos = List.of("13800138000", "13900139000", "13700137000");

		for (int i = 0; i < 3; i++) {
			Policy policy = new Policy();
			policy.setPolicyNo(String.valueOf(80001+i));
			policy.setApplicant(applicant.get(i % 3));
			policy.setPhoneNo(phoneNos.get(i % 3));
			policy.setPurchaseDate(LocalDate.now().minusDays(i));
			policy.setPolicyStatus(PolicyStatusEnum.NORMAL.getDescription());
			policyList.add(policy);
		}

//		Pos pos = new Pos();
//		pos.setAcceptNo(100);
//		pos.setApplicant("张三");
//		pos.setPolicyNo("80001");
//		pos.setPosType(PosTypeEnum.PROBATIONARY_SURRENDER.getDescription());
//    	pos.setPosStatus(PosStatusEnum.ACCEPTED.getDescription());
//		posList.add(pos);
	}
}
