package com.telus.credit.profile.sync.pubsub.service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.cloud.gcp.pubsub.support.BasicAcknowledgeablePubsubMessage;
import org.springframework.cloud.gcp.pubsub.support.GcpPubSubHeaders;
import org.springframework.cloud.gcp.pubsub.support.converter.JacksonPubSubMessageConverter;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.telus.credit.profile.sync.base.model.Customer;
import com.telus.credit.profile.sync.base.model.RelatedParty;
import com.telus.credit.profile.sync.base.model.TelusCreditProfile;
import com.telus.credit.profile.sync.exception.ExceptionConstants;
import com.telus.credit.profile.sync.exception.ExceptionHelper;
import com.telus.credit.profile.sync.exception.ReadStoreGenericException;
import com.telus.credit.profile.sync.firestore.data.CustomerCollectionService;
import com.telus.credit.profile.sync.utils.VersionUtils;


@Service
public class CreditProfileMessageReceiver {

	
	private static final Logger LOGGER = LoggerFactory.getLogger(CreditProfileMessageReceiver.class);

    private final CustomerCollectionService readDB;
    private final JacksonPubSubMessageConverter messageConverter;

    public CreditProfileMessageReceiver(PubSubTemplate pubSubTemplate, CustomerCollectionService readDB, ObjectMapper objectMapper) {
        this.readDB = readDB;
        this.messageConverter = new JacksonPubSubMessageConverter(objectMapper);
    }

    @ServiceActivator(inputChannel = "pubSubInputChannel")
    public void messageReceiver(@Header(GcpPubSubHeaders.ORIGINAL_MESSAGE) BasicAcknowledgeablePubsubMessage message) {
    	
    	String custId = null;
    	try {
        	Customer customer = toCustomerModel(message);
        	custId = (customer!=null)?customer.getId():null;

        	MDC.put("debugContext", "CustId=" + custId);
        	long eventReceivedTime = customer.getEventReceivedTime();
        	long submitterEventTime = customer.getSubmitterEventTime();
        	LOGGER.info("CustId={}.BuildDetails:{}",custId,VersionUtils.getBuildDetails());       	
        	LOGGER.info("CustId={}. Start CreditProfileMessageReceiver.messageReceiver. eventReceivedTime={} . submitterEventTime={} . MessageId={}", custId,eventReceivedTime,submitterEventTime,message.getPubsubMessage().getMessageId());    
          	LOGGER.info("CustId={}. Message={}. customer={}", custId,message);    
          	LOGGER.info("CustId={}. customer={}",custId,customer);   
        	
        	processMessage(customer, message);
           
            ackMessage(message.ack(), message.getPubsubMessage().getMessageId());
        } catch (Exception e) {
        	e.printStackTrace();
            LOGGER.error(
            			ExceptionConstants.STACKDRIVER_METRIC  + ":" + 
            			ExceptionConstants.PUBSUB100 + 
            			" Exception processing pubsub message for CustId=. " + custId +
            			" messageId="+ message.getPubsubMessage().getMessageId()+
            			ExceptionHelper.getStackTrace(e));
            ackMessage(message.nack(), message.getPubsubMessage().getMessageId());
        } finally {
            MDC.clear();
        }
        
        LOGGER.info("CustId={}. End CreditProfileMessageReceiver.messageReceiver ", custId);
    }



	private Customer toCustomerModel(BasicAcknowledgeablePubsubMessage message) {
        try {
        	return messageConverter.fromPubSubMessage(message.getPubsubMessage(), Customer.class);
        } catch (Exception e) {
            LOGGER.error(ExceptionConstants.PUBSUB101 + " Invalid pubsub message type. messageId={}", message.getPubsubMessage().getMessageId(), ExceptionHelper.getStackTrace(e));
            throw e;
        }
    }



    private void processMessage(Customer customer, BasicAcknowledgeablePubsubMessage message) throws ReadStoreGenericException {    	
    	
       long publishTime = customer.getEventReceivedTime();
       //long publishTime = customer.getSubmitterEventTime();
       if (publishTime < 1) {
          publishTime = Instant.ofEpochSecond(message.getPubsubMessage().getPublishTime().getSeconds(), message.getPubsubMessage().getPublishTime().getNanos()).toEpochMilli();    
       }
       LOGGER.info("CustId={}, messageId={}, publishTime={} ", customer.getId(), message.getPubsubMessage().getMessageId(), publishTime);
       this.readDB.addorUpdateCustomerCollection(customer,  publishTime);
    }

    private void ackMessage(ListenableFuture<Void> future, String messageId) {
        try {
            future.get();
        } catch (Exception e) {
            LOGGER.error(ExceptionConstants.STACKDRIVER_METRIC  + ":" +ExceptionConstants.PUBSUB103 + "Exception acknowledging pubsub message. messageId={}. {}", messageId, ExceptionHelper.getStackTrace(e));
        }
    }
}
