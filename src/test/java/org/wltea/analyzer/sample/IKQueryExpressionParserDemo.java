package org.wltea.analyzer.sample;

import org.apache.lucene.search.Query;
import org.junit.jupiter.api.Test;
import org.wltea.analyzer.query.IKQueryExpressionParser;

public class IKQueryExpressionParserDemo {
    @Test
    public void test() {
        IKQueryExpressionParser parser = new IKQueryExpressionParser();
        String ikQueryExp = "(id='ABcdRf' && date:{'20010101','20110101'} && keyword:'魔兽中国') || (content:'KSHT-KSH-A001-18' || ulr='www.ik.com') - name:'林良益'";
        Query result = parser.parseExp(ikQueryExp, true);
        System.out.println(result);
    }
}