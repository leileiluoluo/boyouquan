package com.boyouquan.helper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class IPInfoHelperTest {

    @Autowired
    private IPInfoHelper ipInfoHelper;

    @Test
    public void testGetIpInfoByDomainName() {
        System.out.println(ipInfoHelper.getIpInfoByDomainName("tonybai.com"));
    }

}
