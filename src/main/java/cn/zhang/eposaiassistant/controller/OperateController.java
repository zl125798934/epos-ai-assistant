package cn.zhang.eposaiassistant.controller;

import cn.zhang.eposaiassistant.data.Policy;
import cn.zhang.eposaiassistant.data.Pos;
import cn.zhang.eposaiassistant.service.OperateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: zhanglun @Date: 2026/4/28 16:51 @Description:
 */
@CrossOrigin
@RestController
@RequestMapping("/api/operate")
@RequiredArgsConstructor
public class OperateController {
	
	private final OperateService operateService;

	/**
	 * 保单列表
	 * @return
	 */
	@CrossOrigin
	@GetMapping(value = "/pollist")
	public List<Policy> getPols() {
		return operateService.getPols();
	}

	/**
	 * 保单列表
	 * @return
	 */
	@CrossOrigin
	@GetMapping(value = "/poslist")
	public List<Pos> getPosList() {
		return operateService.getPos();
	}

}
