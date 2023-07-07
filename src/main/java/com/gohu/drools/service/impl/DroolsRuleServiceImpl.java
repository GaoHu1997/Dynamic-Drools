package com.gohu.drools.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gohu.drools.utils.DroolsManager;
import com.gohu.drools.entity.DroolsRule;
import com.gohu.drools.mapper.DroolsRuleMapper;
import com.gohu.drools.service.DroolsRuleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * 规则 crud 实现
 */
@Service
public class DroolsRuleServiceImpl extends ServiceImpl<DroolsRuleMapper,DroolsRule> implements DroolsRuleService {

    @Resource
    private DroolsManager droolsManager;

    /**
     * 加载所有规则
     */
    @Override
    public Integer loadRuleAll() {
        List<DroolsRule> list = this.findAll();

        for (DroolsRule rule: list) {
            droolsManager.addOrUpdateRule(rule);
        }
        return list.size();
    }

    /**
     * 查询所有规则
     * @return
     */
    @Override
    public List<DroolsRule> findAll() {
        return this.list();
    }

    /**
     * 添加规则
     * @param droolsRule 规则实体
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addDroolsRule(DroolsRule droolsRule) {
        droolsRule.validate();
        droolsRule.setCreatedTime(new Date());
        this.save(droolsRule);

        droolsManager.addOrUpdateRule(droolsRule);
    }

    /**
     * 修改规则
     * @param droolsRule 规则实体
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDroolsRule(DroolsRule droolsRule) {
        droolsRule.validate();
        droolsRule.setUpdateTime(new Date());
        this.updateById(droolsRule);
        droolsManager.addOrUpdateRule(droolsRule);
    }

    /**
     * 删除规则
     * @param ruleId 规则id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDroolsRule(String ruleId) {
        DroolsRule droolsRule = this.getById(ruleId);
        if (null != droolsRule) {
            this.removeById(ruleId);
            droolsManager.deleteDroolsRule(droolsRule);
        }
    }
}
