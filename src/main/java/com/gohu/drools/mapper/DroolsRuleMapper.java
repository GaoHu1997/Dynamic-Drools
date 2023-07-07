package com.gohu.drools.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gohu.drools.entity.DroolsRule;
import org.apache.ibatis.annotations.Mapper;

/**
 * 规则持久层
 */
@Mapper
public interface DroolsRuleMapper extends BaseMapper<DroolsRule> {
}
