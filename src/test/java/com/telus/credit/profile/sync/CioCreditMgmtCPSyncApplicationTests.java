package com.telus.credit.profile.sync;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.gcp.pubsub.support.BasicAcknowledgeablePubsubMessage;
import org.springframework.cloud.gcp.pubsub.support.converter.JacksonPubSubMessageConverter;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.telus.credit.profile.sync.base.model.CreditProfile;
import com.telus.credit.profile.sync.base.model.Customer;
import com.telus.credit.profile.sync.exception.ReadStoreGenericException;
import com.telus.credit.profile.sync.firestore.data.CustomerCollectionService;

@SpringBootTest
class CioCreditMgmtCPSyncApplicationTests {

    private static final Log LOGGER = LogFactory.getLog(CioCreditMgmtCPSyncApplicationTests.class);
    private static final String TEST_DATA_CUSTOMER_ID = "10000";
    private static final String TEST_DATA_CREATED_BY = "TEST";
    private static final String TEST_DATA_EVENT_TYPE = "assessmentCreate";
    private static final Long TIMEOUT_SECONDS = 30L;
    private static final Long POLL_INTERVAL_SECONDS = 2L;

    @Autowired
     CustomerCollectionService customerCollectionService;
    @Test
    void contextLoads() {
    	 
		/*
		 * Customer newCustomer = new Customer();; newCustomer.setId("71002"); long
		 * publishTime=1; try {
		 * customerCollectionService.addorUpdateCustomerCollection(newCustomer ,
		 * publishTime); } catch (Throwable e) { e.printStackTrace(); }
		 */
  	 
    	String NEW_TEST_FILE="create-new-credit-profile.json";
    	//BasicAcknowledgeablePubsubMessage message = null;
    	try {
    		InputStream  is= this.getClass().getClassLoader().getResourceAsStream(NEW_TEST_FILE);    
    		
    		ObjectMapper objectMapper = new ObjectMapper();
    		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			CreditProfile aCreditProfile = objectMapper.readValue(is, CreditProfile.class);
			
			aCreditProfile.getProductCategoryQualification();
			aCreditProfile.getBoltonInd();
			aCreditProfile.getCharacteristic();
			
			String payload = objectMapper.writeValueAsString(aCreditProfile);
			System.out.println(payload);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  

    	//Customer aCustomer = messageConverter.fromPubSubMessage(message.getPubsubMessage(), Customer.class);
    }

}
