package com.gohu.drools.controller;

import com.gohu.drools.entity.DroolsRule;
import com.gohu.drools.service.DroolsRuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * 控制层
 */
@Slf4j
@RestController
@RequestMapping("/drools/rule")
public class DroolsRuleController {

    @Resource
    private DroolsRuleService droolsRuleService;

    @PostConstruct
    public void reload() {
        log.debug(">>> 初始化数据库规则");
        droolsRuleService.loadRuleAll();
    }

    /**
     * 查询所有规则
     * @return 规则实体列表
     */
    @GetMapping("/findAll")
    public List<DroolsRule> findAll() {
        return droolsRuleService.findAll();
    }

    /**
     * 添加规则
     * @param droolsRule 规则实体
     * @return
     */
    @PostMapping("/add")
    public Boolean addRule(@RequestBody DroolsRule droolsRule) {
        droolsRuleService.addDroolsRule(droolsRule);
        return true;
    }

    /**
     * 更新规则
     * @param droolsRule 规则实体
     * @return
     */
    @PutMapping("/update")
    public Boolean updateRule(@RequestBody DroolsRule droolsRule) {
        droolsRuleService.updateDroolsRule(droolsRule);
        return true;
    }

    /**
     * 删除规则
     * @param ruleId 规则id
     * @return
     */
    @DeleteMapping("/deleteRule")
    public Boolean deleteRule(@RequestParam String ruleId) {
        droolsRuleService.deleteDroolsRule(ruleId);
        return true;
    }

}
