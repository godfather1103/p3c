package io.github.godfather1103.settings

import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextArea
import com.intellij.util.ui.FormBuilder
import java.awt.Color
import javax.swing.BorderFactory
import javax.swing.JPanel

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
class P3CSettingsComponent {

    private val myMainPanel: JPanel;

    private val inData: JBTextArea = JBTextArea(20, 20);

    init {
        val border = BorderFactory.createLineBorder(Color.BLACK)
        inData.border = BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(5, 5, 5, 5))
        myMainPanel = FormBuilder.createFormBuilder()
            .addComponent(JBLabel("custom namelist.properties"), 1)
            .addComponent(inData, 1)
            .addComponentFillVertically(JPanel(), 0)
            .panel
    }

    fun getInData(): JBTextArea = inData;

    fun getPanel(): JPanel = myMainPanel;

    fun setInDataText(text: String) {
        inData.text = text
    }

    fun getInDataText(): String {
        return inData.text
    }
}