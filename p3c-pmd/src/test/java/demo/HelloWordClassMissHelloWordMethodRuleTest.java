package demo;

import net.sourceforge.pmd.testframework.SimpleAggregatorTst;

public class HelloWordClassMissHelloWordMethodRuleTest extends SimpleAggregatorTst {
    @Override
    protected void setUp() {
//        addRule("java-demo","HelloWordClassMissHelloWordMethodRule");
        addRule("rulesets/java/demo.xml","HelloWordClassMissHelloWordMethodRule");
    }
}