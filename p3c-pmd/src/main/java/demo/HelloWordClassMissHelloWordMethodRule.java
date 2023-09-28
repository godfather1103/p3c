package demo;

import com.alibaba.p3c.pmd.lang.java.rule.AbstractAliRule;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;

import java.util.List;

/**
 * <p>Title:        Godfather1103's Github</p>
 * <p>Copyright:    Copyright (c) 2023</p>
 * <p>Company:      https://github.com/godfather1103</p>
 * HelloWordClassMissHelloWordMethodRule
 *
 * @author 作者: Jack Chu E-mail: chuchuanbao@gmail.com<BR>
 * 创建时间：2023-09-28 21:33
 * @version 1.0
 * @since 1.0
 */
public class HelloWordClassMissHelloWordMethodRule extends AbstractAliRule {

    private final String hitWord = "HelloWord";

    @Override
    public Object visit(ASTClassOrInterfaceDeclaration node, Object data) {
        String className = node.getSimpleName();
        if (className.contains(hitWord)) {
            List<ASTMethodDeclaration> methods = node.findDescendantsOfType(ASTMethodDeclaration.class);
            if (methods.stream().noneMatch(it -> hitWord.equalsIgnoreCase(it.getName()))) {
                addViolationWithMessage(data, node, "demo.HelloWordClassMissHelloWordMethodRule.violation.msg");
            }
        }
        return super.visit(node, data);
    }
}
