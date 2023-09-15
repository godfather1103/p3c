package io.github.godfather1103.rule.ext

import com.alibaba.p3c.pmd.lang.java.rule.naming.LowerCamelCaseVariableNamingRule
import net.sourceforge.pmd.lang.java.ast.ASTAnnotationTypeDeclaration
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclarator
import net.sourceforge.pmd.lang.java.ast.ASTVariableDeclaratorId
import net.sourceforge.pmd.lang.java.ast.JavaNode
import java.util.regex.Pattern

/**
 * <p>Title:        Godfather1103's Github</p>
 * <p>Copyright:    Copyright (c) 2023</p>
 * <p>Company:      https://github.com/godfather1103</p>
 *
 * @author  作者: Jack Chu E-mail: chuchuanbao@gmail.com
 * @date 创建时间：2023/8/23 13:57
 * @version 1.0
 * @since  1.0
 */
class LowerCamelCaseVariableNamingRuleExt : LowerCamelCaseVariableNamingRule() {

    override fun visit(node: ASTVariableDeclaratorId, data: Any?): Any? {
        if (!(pattern.matcher(node.name)).matches()) {
            return super.visit(node, data)
        }
        return super.visit(node as JavaNode, data)
    }

    override fun visit(node: ASTMethodDeclarator, data: Any?): Any? {
        if (!(pattern.matcher(node.image)).matches()) {
            return super.visit(node, data)
        }
        return super.visit(node as JavaNode, data)
    }

    override fun visit(node: ASTAnnotationTypeDeclaration, data: Any?): Any? {
        return null
    }

    companion object {
        var pattern: Pattern =
            Pattern.compile("^[a-z][a-z0-9]*([A-Z][a-z0-9]+)*(DO|DTO|VO|DAO|BO|DOList|DTOList|VOList|DAOList|BOList|X|Y|Z|UDF|UDAF|[A-Z])?$")
    }

}