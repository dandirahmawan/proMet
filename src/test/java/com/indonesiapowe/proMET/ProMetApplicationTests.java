package com.indonesiapowe.proMET;

import com.indonesiapowe.proMET.Service.PdfServices;
import com.indonesiapowe.proMET.Service.QrCodeServices;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProMetApplicationTests {

	@Autowired
	QrCodeServices qrServices;

	@Test
	void contextLoads() {
	}
}
