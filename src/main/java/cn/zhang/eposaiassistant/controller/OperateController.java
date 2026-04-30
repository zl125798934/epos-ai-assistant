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
 * 运营操作控制器。
 *
 * <p>提供保单与保全列表查询接口。</p>
 */
@CrossOrigin
@RestController
@RequestMapping("/api/operate")
@RequiredArgsConstructor
public class OperateController {

  private final OperateService operateService;

  /**
   * 获取保单列表。
   *
   * @return 保单列表
   */
  @GetMapping(value = "/pollist")
  public List<Policy> getPols() {
    return operateService.getPols();
  }

  /**
   * 获取保全列表。
   *
   * @return 保全列表
   */
  @GetMapping(value = "/poslist")
  public List<Pos> getPosList() {
    return operateService.getPos();
  }

}
