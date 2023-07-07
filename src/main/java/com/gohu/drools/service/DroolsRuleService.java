package com.gohu.drools.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.gohu.drools.entity.DroolsRule;

import java.util.List;

/**
 * 规则 crud
 */
public interface DroolsRuleService extends IService<DroolsRule> {

    /**
     * 加载所有规则
     */
    Integer loadRuleAll();

    /**
     * 查询所有规则
     */
    List<DroolsRule> findAll();

    /**
     * 添加drools规则
     * @param droolsRule 规则实体
     */
    void addDroolsRule(DroolsRule droolsRule);

    /**
     * 修改drools 规则
     * @param droolsRule 规则实体
     */
    void updateDroolsRule(DroolsRule droolsRule);

    /**
     * 删除drools规则
     * @param ruleId 规则id
     */
    void deleteDroolsRule(String ruleId);
}
