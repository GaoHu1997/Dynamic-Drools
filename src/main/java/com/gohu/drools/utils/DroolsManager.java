package com.gohu.drools.utils;

import com.gohu.drools.entity.DroolsRule;
import lombok.extern.slf4j.Slf4j;
import org.drools.compiler.kie.builder.impl.InternalKieModule;
import org.drools.compiler.kie.builder.impl.KieContainerImpl;
import org.junit.platform.commons.util.StringUtils;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.builder.model.KieBaseModel;
import org.kie.api.builder.model.KieModuleModel;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.Collection;
import java.util.List;

/**
 * drools 加载规则的核心类
 */
@Component
@Slf4j
public class DroolsManager {

    // 此类本身就是单例的
    private final KieServices kieServices = KieServices.get();

    // kie文件系统，需要缓存，如果每次添加规则都是重新建一个的话，则可能出现之前加到文件系统中的规则没有了
    private final KieFileSystem kieFileSystem = kieServices.newKieFileSystem();

    // 可以理解为构建 kmodule.xml
    private final KieModuleModel kieModuleModel = kieServices.newKieModuleModel();

    // 全局唯一
    // 如果每次加个规则都新创建一个，那么旧需要销毁之前创建的kieContainer，如果此时有正在使用的KieSession，则可能有问题
    private KieContainer kieContainer;

    /**
     * 销毁前调用
     */
    @PreDestroy
    public void destroy() {
        if (null != kieContainer) {
            kieContainer.dispose();
        }
    }

    /**
     * 判断该 kbase 是否存在
     *
     * @param kieBaseName 规则库名称
     * @return Boolean
     */
    public Boolean existsKieBase(String kieBaseName) {
        if (null == kieContainer) {
            return false;
        }
        Collection<String> kieBaseNames = kieContainer.getKieBaseNames();
        if (kieBaseNames.contains(kieBaseName)) {
            return true;
        }
        log.info(">>> 需要创建KieBase:{}", kieBaseName);
        return false;
    }

    /**
     * 删除 kbase 中的规则
     *
     * @param droolsRule 规则实体
     */
    public void deleteDroolsRule(DroolsRule droolsRule) {
        String kieBaseName = droolsRule.getKieBaseName();
        String packageName = droolsRule.getKiePackageName();
        String ruleName = droolsRule.getRuleName();
        if (existsKieBase(kieBaseName)) {
//            KieBase kieBase = kieContainer.getKieBase(kieBaseName);
//            kieBase.removeRule(packageName, ruleName);
            String file = "src/main/resources/" + droolsRule.getKiePackageName() + "/" + droolsRule.getRuleId() + ".drl";
            kieFileSystem.delete(file);
            buildKieContainer();
            log.info(">>> 删除kieBase:[{}]包:[{}]下的规则:[{}]", kieBaseName, packageName, ruleName);
        }
    }

    /**
     * 添加或更新 drools 规则
     *
     * @param droolsRule 规则实体类
     */
    public void addOrUpdateRule(DroolsRule droolsRule) {
        // 获取 kbase 的名称
        String kieBaseName = droolsRule.getKieBaseName();
        // 判断该 kbase 是否存在
        boolean existsKieBase = existsKieBase(kieBaseName);
        // 该对象对应 kmodule.xml 中的 kbase 标签
        KieBaseModel kieBaseModel = null;
        if (!existsKieBase) {
            // 创建一个 kbase
            kieBaseModel = kieModuleModel.newKieBaseModel(kieBaseName);
            // 不是默认的 kieBase
            kieBaseModel.setDefault(false);
            // 设置该 KieBase 需要加载的包路径
            kieBaseModel.addPackage(droolsRule.getKiePackageName());
            // 设置 kieSession
            kieBaseModel
                    // 名称
                    .newKieSessionModel(kieBaseName + "-session")
                    // 不是默认 session
                    .setDefault(false);
        } else {
            // 获取到已经存在的 kbase 对象
            kieBaseModel = kieModuleModel.getKieBaseModels().get(kieBaseName);
            // 获取到 packages
            List<String> packages = kieBaseModel.getPackages();
            if (!packages.contains(droolsRule.getKiePackageName())) {
                kieBaseModel.addPackage(droolsRule.getKiePackageName());
                log.info(">>> kieBase:{}添加一个新的包:{}", kieBaseName, droolsRule.getKiePackageName());
            } else {
                kieBaseModel = null;
            }
        }

        // 写入路径
        // src/main/resources：固定写法
        // droolsRule.getKiePackageName()：与动态构建的 kmodule.xml 文件中包加载路径保持一致
        // droolsRule.getRuleId().drl：规则文件名称，使用规则实体的id
        String file = "src/main/resources/" + droolsRule.getKiePackageName() + "/" + droolsRule.getRuleId() + ".drl";
        log.info(">>> 加载虚拟规则文件:{}【{}】", file,droolsRule.getRuleName());
        // 写入规则
        kieFileSystem.write(file, droolsRule.getRuleContent());

        if (kieBaseModel != null) {
            String kmoduleXml = kieModuleModel.toXML();
            log.info(">>> 加载kmodule.xml:[\n{}]", kmoduleXml);
            kieFileSystem.writeKModuleXML(kmoduleXml);
        }

        buildKieContainer();
    }

    /**
     * 构建KieContainer
     */
    private void buildKieContainer() {
        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
        // 通过KieBuilder构建KieModule下所有的KieBase
        kieBuilder.buildAll();
        // 获取构建过程中的结果
        Results results = kieBuilder.getResults();
        // 获取错误信息
        List<Message> messages = results.getMessages(Message.Level.ERROR);
        if (null != messages && !messages.isEmpty()) {
            for (Message message : messages) {
                log.error(message.getText());
            }
            throw new RuntimeException("加载规则出现异常");
        }
        if (null == kieContainer) {
            // KieContainer 只有第一次时才需要创建
            kieContainer = kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());
        } else {
            // 更新 KieContainer
            // 将新的 KieModule 应用到已经存在的 KieContainer 对象上
            // 如果 KieSession 对应已经存在，那么新的规则对 KieSession 是可见的。
            ((KieContainerImpl) kieContainer).updateToKieModule((InternalKieModule) kieBuilder.getKieModule());
        }
    }

    /**
     * 触发规则
     *
     * @param kieBaseName 规则库名称
     * @param group       分组
     * @param obj         fact 对象
     * @return fact 对象
     */
    public Object fireRule(String kieBaseName, String group, Object obj) {
        // 创建 kieSession
        KieSession kieSession = kieContainer.newKieSession(kieBaseName + "-session");

        // 设置焦点，对应agenda-group分组中的规则才可能被触发
        if (StringUtils.isNotBlank(group)) {
            kieSession.getAgenda().getAgendaGroup(group).setFocus();
        }

        // 添加 fact
        kieSession.insert(obj);
        // 触发所有规则
        kieSession.fireAllRules();
        kieSession.dispose();
        // 返回信息
        return obj;
    }
}
