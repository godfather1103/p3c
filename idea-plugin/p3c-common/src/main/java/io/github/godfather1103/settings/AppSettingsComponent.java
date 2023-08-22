// Copyright 2000-2022 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

package io.github.godfather1103.settings;

import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextArea;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * <p>Title:        Godfather1103's Github</p>
 * <p>Copyright:    Copyright (c) 2023</p>
 * <p>Company:      https://github.com/godfather1103</p>
 *
 * @author 作者: Jack Chu E-mail: chuchuanbao@gmail.com
 * @version 1.0
 * @date 创建时间：2023/8/22 09:43
 * @since 1.0
 */
public class AppSettingsComponent {

    private final JPanel myMainPanel;

    private final JBTextArea inData = new JBTextArea(20, 20);

    public AppSettingsComponent() {
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        inData.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        myMainPanel = FormBuilder.createFormBuilder()
                .addComponent(new JBLabel("custom namelist.properties"), 1)
                .addComponent(inData, 1)
                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();
    }

    public JBTextArea getInData() {
        return inData;
    }

    public JPanel getPanel() {
        return myMainPanel;
    }

    public void setInDataText(@NotNull String text) {
        inData.setText(text);
    }

    public String getInDataText() {
        return inData.getText();
    }
}
