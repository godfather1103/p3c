/*
 * Copyright 1999-2017 Alibaba Group.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.p3c.pmd.lang.java.rule.naming;

import com.alibaba.p3c.pmd.I18nResources;
import com.alibaba.p3c.pmd.lang.java.rule.AbstractPojoRule;
import com.alibaba.p3c.pmd.lang.java.util.VariableUtils;
import com.alibaba.p3c.pmd.lang.java.util.ViolationUtils;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTFieldDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTRecordComponent;
import net.sourceforge.pmd.lang.java.ast.ASTRecordDeclaration;
import org.jaxen.JaxenException;

import java.util.List;

/**
 * [Mandatory] Do not add 'is' as prefix while defining Boolean variable, since it may cause a serialization exception
 * in some Java Frameworks.
 *
 * @author changle.lq
 * @date 2017/04/16
 */
public class BooleanPropertyShouldNotStartWithIsRule extends AbstractPojoRule {

    @Override
    public Object visit(ASTClassOrInterfaceDeclaration node, Object data) {
        try {
            List<Node> fields = node.findChildNodesWithXPath(
                    "ClassOrInterfaceBody/ClassOrInterfaceBodyDeclaration/FieldDeclaration");
            for (Node fieldNode : fields) {
                ASTFieldDeclaration field = (ASTFieldDeclaration) fieldNode;
                String typeName = field.getType().getName();
                if (typeName.toLowerCase().endsWith("boolean")) {
                    String name = VariableUtils.getVariableName(field);
                    if (name != null && name.startsWith("is")) {
                        ViolationUtils.addViolationWithPrecisePosition(
                                this,
                                field,
                                data,
                                I18nResources.getMessage("java.naming.BooleanPropertyShouldNotStartWithIsRule.violation.msg", name)
                        );
                    }
                }
            }
            return super.visit(node, data);
        } catch (JaxenException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public Object visit(ASTRecordDeclaration node, Object data) {
        try {
            List<Node> fields = node.findChildNodesWithXPath(
                    "RecordComponentList/RecordComponent");
            for (Node fieldNode : fields) {
                ASTRecordComponent component = (ASTRecordComponent) fieldNode;
                String typeName = component.getTypeNode().getTypeImage();
                if (typeName.toLowerCase().endsWith("boolean")) {
                    String name = component.getVarId().getName();
                    if (name != null && name.startsWith("is")) {
                        ViolationUtils.addViolationWithPrecisePosition(
                                this,
                                component,
                                data,
                                I18nResources.getMessage("java.naming.BooleanPropertyShouldNotStartWithIsRule.violation.msg", name)
                        );
                    }
                }
            }
            return super.visit(node, data);
        } catch (JaxenException e) {
            throw new RuntimeException(e);
        }
    }
}
