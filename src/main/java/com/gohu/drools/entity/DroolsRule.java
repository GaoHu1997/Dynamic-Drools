package com.gohu.drools.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.junit.platform.commons.util.StringUtils;

import java.util.Date;

/**
 * drools 规则实体类
 */
@Data
@TableName("drool_rule")
public class DroolsRule {

    /**
     * 规则 id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String ruleId;

    /**
     * 规则名称
     */
    private String ruleName;

    /**
     * KieBase 的名字
     * KieBase 就是一个知识仓库，包含了若干的规则、流程、方法等
     * 不同的 KieBase 规则隔离
     */
    private String kieBaseName;
    /**
     * 设置该 KieBase 需要从那个目录下加载文件，这个是一个虚拟的目录，相对于 src/main/resources
     * 比如：kiePackageName=rules 那么当前规则文件写入路径为： kieFileSystem.write("src/main/resources/rules/1.drl")
     */
    private String kiePackageName;
    /**
     * 规则内容，参考 resources/rules/template.drl.bak
     */
    private String ruleContent;
    /**
     * 规则创建时间
     */
    private Date createdTime;
    /**
     * 规则更新时间
     */
    private Date updateTime;

    /**
     * 规则实体校验
     */
    public void validate() {
        if (isBlank(ruleName) || isBlank(kieBaseName) || isBlank(kiePackageName) || isBlank(ruleContent)) {
            throw new RuntimeException("参数有问题");
        }
    }

    private boolean isBlank(String str) {
        return StringUtils.isBlank(str);
    }
}
