package com.indonesiapowe.proMET;

import com.indonesiapowe.proMET.Service.PdfServices;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProMetApplicationTests {

	@Autowired
	PdfServices pdfServices;

	@Test
	void contextLoads() {
	}
}
