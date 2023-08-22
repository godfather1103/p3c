// Copyright 2000-2023 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package io.github.godfather1103.settings;

import com.alibaba.p3c.idea.config.P3cConfig;
import com.alibaba.p3c.pmd.lang.java.util.namelist.NameListConfig;
import com.intellij.openapi.options.Configurable;
import io.github.godfather1103.service.BaseNameListServiceExt;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * <p>Title:        Godfather1103's Github</p>
 * <p>Copyright:    Copyright (c) 2023</p>
 * <p>Company:      https://github.com/godfather1103</p>
 *
 * @author 作者: Jack Chu E-mail: chuchuanbao@gmail.com
 * @version 1.0
 * @date 创建时间：2023/8/22 09:44
 * @since 1.0
 */
public class AppSettingsConfigurable implements Configurable {

    private AppSettingsComponent mySettingsComponent;

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "P3C Configure";
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return mySettingsComponent.getInData();
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        mySettingsComponent = new AppSettingsComponent();
        return mySettingsComponent.getPanel();
    }

    @Override
    public boolean isModified() {
        return !P3cConfig.getInstance()
                .getCustomNamelistProperties()
                .equals(mySettingsComponent.getInDataText());
    }

    @Override
    public void apply() {
        var inData = mySettingsComponent.getInDataText();
        P3cConfig.getInstance()
                .setCustomNamelistProperties(inData);
        var service = NameListConfig.NAME_LIST_SERVICE;
        if (service instanceof BaseNameListServiceExt) {
            BaseNameListServiceExt base = ((BaseNameListServiceExt) service);
            if (!base.getOldData().equals(inData)) {
                base.resetData(inData);
            }
        }
    }

    @Override
    public void reset() {
        mySettingsComponent
                .setInDataText(P3cConfig.getInstance().getCustomNamelistProperties());
    }

    @Override
    public void disposeUIResources() {
        mySettingsComponent = null;
    }

}
