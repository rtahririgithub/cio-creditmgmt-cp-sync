package com.telus.credit.profile.sync;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.telus.credit.profile.sync.utils.VersionUtils;

@SpringBootApplication
public class CioCreditMgmtCPSyncApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(CioCreditMgmtCPSyncApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(CioCreditMgmtCPSyncApplication.class, args);
		System.out.println("cp-sync is alive. Getting git build info...");
		
		LOGGER.info("BuildDetails:{}",VersionUtils.getBuildDetails());

	}
}
