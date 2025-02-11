package com.telus.credit.profile.sync.firestore.data;

import java.sql.Array;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import com.telus.credit.profile.sync.base.model.*;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.Transaction;
import com.telus.credit.profile.sync.exception.ExceptionConstants;
import com.telus.credit.profile.sync.exception.ExceptionHelper;
import com.telus.credit.profile.sync.exception.ReadStoreGenericException;
import com.telus.credit.profile.sync.firestore.model.CustomerDocument;
import com.telus.credit.profile.sync.utils.DateTimeUtils;

import javax.swing.text.html.parser.Entity;

@Service
public class CustomerCollectionService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerCollectionService.class);

	@Autowired
	private Firestore firestore;

	@Value("${firestore.collection.prefix}")
	private String collectionPrefix;

	@Value("${cp.sync.firestore.schemaVersion}")
	private String schemaVersion;

	private static final String NAME = "customers";

	public String updateCustomerCollectionSafely(String existingCustFireStoreId, CustomerDocument newCustDataDoc, Customer newCustomer) throws InterruptedException, ExecutionException {
	 String custID = (newCustomer!=null)?newCustomer.getId():null;
	/*
	 * firestore.runTransaction: Executes the given updateFunction and then attempts to commit the changes
	 * applied within the transaction. If any document read within the transaction
	 * has changed, the updateFunction will be retried. If it fails to commit after 5
	 * attempts, the transaction will fail.
	 */
	 ApiFuture<String> futureTransaction;
		try {
			futureTransaction = firestore.runTransaction(transaction -> 
				   {
					 String eventDescription = newCustomer.getEventDescription();
					 LOGGER.info("CustId={}.updateCustomerCollectionSafely. Transaction update start for docId {}.eventDescription{}", custID, existingCustFireStoreId,eventDescription);
	
			          // Async call to get reference to the document
			         DocumentReference docRef = firestore.collection(getCollectionName()).document(existingCustFireStoreId);
	
			         // Read the existing customer document inside transaction
			         DocumentSnapshot documentSnapshot = transaction.get(docRef).get();
			         
			         CustomerDocument existingCustDoc = null;
					try {
						existingCustDoc = documentSnapshot.toObject(CustomerDocument.class);
					} catch (Exception e1) {
				        LOGGER.warn("CustId={}. Failed to convert the contents of the firestore existing document to CustomerDocument POJO . ", custID);	         
				        throw e1;
					}



			         long newCustPublishedTime = newCustDataDoc.getPublishTimeinNanos();
			         long existingCustPublishedTime = existingCustDoc.getPublishTimeinNanos();	         
			         LOGGER.info("CustId={}. updateCustomerCollectionSafely.existingCustPublishedTime:{}, newCustPublishedTime:{}.eventDescription:{}", custID,existingCustPublishedTime, newCustPublishedTime,eventDescription);	         
				     {
					     // do not update the existing doc if it was published after this one
				         if (existingCustPublishedTime > newCustPublishedTime) {
				            LOGGER.warn("CustId={}. CPSYNC_WARN Bypass updating existing doc newCustPublishedTime:{} , existingCustPublishedTime:{} . ", custID, newCustPublishedTime, existingCustPublishedTime);
				            return existingCustDoc.getFireStoreId();
				         }		    	 
				     }
					
					
				     newCustDataDoc.setCustomer(populateAccountInfo(newCustomer, existingCustDoc.getCustomer()));			
			         // Replace the document with the new one.
			         // This will fail if the doc we read has changed.
			         // A failure will trigger a retry of this transaction
					 try {
						transaction.set(docRef, newCustDataDoc);
					} catch (Exception e) {
				         LOGGER.warn("CustId={}. updateCustomerCollectionSafely.transaction.set operation failed.Retry. currentTimeMillis:{},eventDescription:{},newCustDataDoc:{}", custID,System.currentTimeMillis(),eventDescription,newCustDataDoc);	         
				         throw e;
					}	
					 
			         String firestoreId = docRef.getId();
			         LOGGER.info("CustId={}. firestore Transaction commit for: eventDescription:{}, firestoreId:{}, currentTimeMillis:{},newCustDataDoc: {}", custID, eventDescription,firestoreId, System.currentTimeMillis(),newCustDataDoc);
			         return firestoreId;
			       }
				   );
		} catch (Throwable e) {
			String eventDescription = newCustomer.getEventDescription();
	        LOGGER.warn("CustId={}. updateCustomerCollectionSafely.firestore.runTransaction operation failed.Retry. currentTimeMillis:{},eventDescription:{},newCustDataDoc:{}", custID,System.currentTimeMillis(),eventDescription,newCustDataDoc);	         
	        throw e;
		}

      String fireStoreId =(futureTransaction!=null)?futureTransaction.get():null;
      return fireStoreId;
	}

	private void convertEngagedPartyToOrganizationOrIndividual(Customer customer) {
		//get all primary creditprofiles 
		//convert each primary creditprofile's EngagedParty To Organization Or Individual
		List<TelusCreditProfile> creditProfileList = customer.getCreditProfile();
		List<TelusCreditProfile> primaryCreditProfiles = new ArrayList<TelusCreditProfile>();
		if(creditProfileList!=null) {
			for (TelusCreditProfile telusCreditProfile : creditProfileList) {
				if(!"SEC".equalsIgnoreCase( telusCreditProfile.getCustomerCreditProfileRelCd()) ){
					primaryCreditProfiles.add(telusCreditProfile);
				}
			}
		}
		if(primaryCreditProfiles==null || primaryCreditProfiles.isEmpty()) {
			return;
		}		
		
	  for (TelusCreditProfile primaryTelusCreditProfile : primaryCreditProfiles) {	
		int customerRoleRelatedPartyIndex = getIndexOfRelatedPartyWithCustomerRole(primaryTelusCreditProfile);	
		if(primaryTelusCreditProfile.getRelatedParty()!=null && !primaryTelusCreditProfile.getRelatedParty().isEmpty()) {
			RelatedParty customerRoleRelatedParty = primaryTelusCreditProfile.getRelatedParty().get(customerRoleRelatedPartyIndex);
			RelatedPartyInterface indvOrOrgParty = getCustomerRoleEngagedParty(primaryTelusCreditProfile);	
			if(indvOrOrgParty!=null) {
				if (com.telus.credit.profile.sync.base.model.PartyType.INDIVIDUAL.equals(indvOrOrgParty.getRelatedPartyType())) {
					customerRoleRelatedParty.setIndividual( (Individual) indvOrOrgParty );
				}else {
					if (com.telus.credit.profile.sync.base.model.PartyType.ORGANIZATION.equals(indvOrOrgParty.getRelatedPartyType())) {
						customerRoleRelatedParty.setOrganization( (Organization) indvOrOrgParty );
					}
				}
				primaryTelusCreditProfile.getRelatedParty().get(customerRoleRelatedPartyIndex).setEngagedParty(null);
			}			
		}
		
	  }


	}

	  public String addorUpdateCustomerCollection(Customer newCustomer, long publishTime) throws ReadStoreGenericException {
		  //convert Customer to CustomerPubsub 
		 convertEngagedPartyToOrganizationOrIndividual(newCustomer);		  
		  String custID = (newCustomer!=null)?newCustomer.getId():null;
		  LOGGER.info("CustId={} .addorUpdateCustomerCollection  publish time:{}, newCustomer::{}",custID,publishTime,newCustomer);
	      CustomerDocument custNewDocument = null;
	      try {
	         custNewDocument = new CustomerDocument();
	         custNewDocument.setPublishTimeinNanos(publishTime);
	         custNewDocument.setMetaData(populateMetaData(newCustomer));
	         custNewDocument.setCustomer(newCustomer);
	         LOGGER.debug("collectionData to be persisted::{}", custNewDocument);
	         
	         Optional<CustomerDocument> currentDocOpt = getCurrentDocumentId(newCustomer.getId());
	         
	         if (currentDocOpt.isPresent()) {
	            CustomerDocument custExistingDocument = currentDocOpt.get();	            
	            String custExistingDocumentFireStoreId = custExistingDocument.getFireStoreId();
	            LOGGER.info("CustId={} . Update existing docId: {}",custID,custExistingDocumentFireStoreId);

	            //for each incomming creditprofile, find the corresponding existing  creditprofile from firestore
	            //if existing creditprofile from firestore found and incomming creditprofile.attachment is null , add attachment from existing creditprofile from firestore to incomming creditprofile
	            List<TelusCreditProfile> existingCpList = custExistingDocument.getCustomer().getCreditProfile();
	            List<TelusCreditProfile> newCpList 		= custNewDocument.getCustomer().getCreditProfile(); 
	            
	            newCpList.forEach(newCp -> {
	             existingCpList.stream()
	                              .filter(existingCp -> existingCp.getId().equals(newCp.getId())) 
	                              .findFirst()
	                              .ifPresent(existingmatchingCp -> {
	                            	  if( newCp.getAttachments()==null || newCp.getAttachments().isEmpty()) {
	                            		  newCp.setAttachments(existingmatchingCp.getAttachments());
	                            	  }
	                              });
	            });
	            	            
	            String fireStoreId = "";
	            fireStoreId =updateCustomerCollectionSafely(custExistingDocumentFireStoreId, custNewDocument, newCustomer);
	            return fireStoreId;
	            
	         } else {
	            String docId ="";
	            docId = addFireStoreDocument(custNewDocument);
	            LOGGER.info("CustId={} New Document added to Customer collection:{}",custID, docId);
	            return docId;
	         }
	      } catch (InterruptedException | ExecutionException e) {
	           LOGGER.error("{}: {} Write to ReadDB failed for CustId={} . ErrorMsg={} . StackTrace:{}", ExceptionConstants.STACKDRIVER_METRIC ,ExceptionConstants.FIRESTORE100, custID, e.getMessage(), ExceptionHelper.getStackTrace(e));	
	         throw new ReadStoreGenericException(e);
	      }

	   }

	  
//	public String addorUpdateCustomerCollectionOLD(Customer newCustomer, long publishTimeinNanos) throws ReadStoreGenericException {
//		LOGGER.debug("addCustomerCollection input::{}, publish time:{}", newCustomer, publishTimeinNanos);
//
//		String custID = (newCustomer!=null)?newCustomer.getId():null;
//		CustomerDocument customerData = null;
//		try {
//			customerData = new CustomerDocument();
//			customerData.setPublishTimeinNanos(publishTimeinNanos);
//			customerData.setMetaData(populateMetaData(newCustomer));
//			customerData.setCustomer(newCustomer);
//			LOGGER.debug("collectionData to be persisted::{}", customerData);
//
//			Optional<CustomerDocument> currentDocOpt = getCurrentDocumentId(newCustomer.getId());
//			
//			if (currentDocOpt.isPresent()) {
//				CustomerDocument existing = currentDocOpt.get();
//				// do not update the existing doc if it was published after this one
//				long newCustPublishedTime = customerData.getPublishTimeinNanos();
//				long existingCustPublishedTime = existing.getPublishTimeinNanos();
//				if (existingCustPublishedTime > newCustPublishedTime) {
//				   LOGGER.warn("CPSYNC_WARN Bypass updating existing doc newPublished {}  existingPublished {}", newCustPublishedTime, existingCustPublishedTime);
//				   return existing.getFireStoreId();
//				}				
//				customerData.setCustomer(populateAccountInfo(newCustomer, existing.getCustomer()));
//				
//				updateFireStoreDocument(customerData, existing.getFireStoreId());				
//				LOGGER.info("Update existing Customer Document id:{}", currentDocOpt);
//				
//				return existing.getFireStoreId();
//			} else {
//				String docId = addFireStoreDocument(customerData);
//				LOGGER.info("New Document added to Customer collection:{}", docId);
//				return docId;
//			}
//		} catch (InterruptedException | ExecutionException e) {
//	        LOGGER.error("{}: {} Write to ReadDB failed for custId={} . customerData:{}  :{}", ExceptionConstants.STACKDRIVER_METRIC ,ExceptionConstants.FIRESTORE100, custID, customerData, ExceptionHelper.getStackTrace(e));
//	           
//			throw new ReadStoreGenericException(e);
//		}
//
//	}

	
	private Customer populateAccountInfo(Customer newDoc, Customer existing) {
			if(ObjectUtils.isEmpty(newDoc.getAccountInfo())) {
				newDoc.setAccountInfo(existing.getAccountInfo());
				LOGGER.debug("new AccountInfo nullBlock, existing AccountInfo:{}", existing.getAccountInfo());
			} else {
				LOGGER.debug("in Merge Block, existing accountInfo:{}, new accountInfo:{}",existing.getAccountInfo(), newDoc.getAccountInfo());
				if(ObjectUtils.isNotEmpty(existing.getAccountInfo())) {
					BeanUtils.copyProperties(existing.getAccountInfo(), newDoc.getAccountInfo(), newDoc.getAccountInfo().getNotNullFieldNames());
				}
				LOGGER.debug("After Merge Block, new accountInfo:{}",newDoc.getAccountInfo());
			}
			return newDoc;
		}	  
	  
	private RiskLevelRiskAssessment mapRiskLevelRiskAssessment(Customer newDoc, Customer existing) {
		if (ObjectUtils.isEmpty(existing.getCreditProfile().get(0).getRiskLevelRiskAssessment())) {
			return newDoc.getCreditProfile().get(0).getRiskLevelRiskAssessment();
		}

		RiskLevelRiskAssessment existingRlra = existing.getCreditProfile().get(0).getRiskLevelRiskAssessment();
		RiskLevelRiskAssessment newRlra = newDoc.getCreditProfile().get(0).getRiskLevelRiskAssessment();

		String assessmentMessage = existingRlra.getAssessmentMessageTxtEn() != null ? existingRlra.getAssessmentMessageTxtEn() : newRlra.getAssessmentMessageTxtEn();
		newRlra.setAssessmentMessageTxtEn(assessmentMessage);

		return newRlra;
	}

	protected String getCollectionName() {
		return collectionPrefix + NAME; //example of a collectionName = "creditcol_v1.0_dev_customers"
	}

	private Map<String, Object> populateMetaData_DEL(Customer customer) {
		Map<String, Object> metaData = new HashMap<>();
		metaData.put(MetaDataType.CUSTOMER_ID.name(), customer.getId());
		metaData.put(MetaDataType.SCHEMA_VERSION.name(), schemaVersion);

		if (ObjectUtils.isNotEmpty(customer.getCreditProfile())) {
			metaData.putAll(populateCreditProfileIdList(customer.getCreditProfile()));
		}

		LOGGER.debug("metaData generated::{}", metaData);
		return metaData;
	}

	private Map<String, Object> populateMetaData(Customer customer) {
		Map<String, Object> metaData = new HashMap<>();
		metaData.put(MetaDataType.CUSTOMER_ID.name(), customer.getId());
		metaData.put(MetaDataType.SCHEMA_VERSION.name(), schemaVersion);

		if (ObjectUtils.isNotEmpty(customer.getCreditProfile())) 
		{
			//popluate metadata.CREDIT_PROFILE_ID_LIST 
			metaData.putAll(populateCreditProfileIdList(customer.getCreditProfile()));
			
			//popluate metadata. individual or organization credential IDs
			TelusCreditProfile primaryTelusCreditProfile = getPrimayCreditProfile (customer.getCreditProfile());
			RelatedParty relatedPartyCustomer = getCustomerRoleRelatedParty(primaryTelusCreditProfile);
			if(relatedPartyCustomer!=null  && relatedPartyCustomer.getIndividual()!=null ) {
				metaData.putAll(populateIndividualMetaData(relatedPartyCustomer.getIndividual()));
			}
			else 
			{
				if(relatedPartyCustomer!=null  && relatedPartyCustomer.getOrganization()!=null) {
					metaData.putAll(populateCompanyMetaData(relatedPartyCustomer.getOrganization()));
				}
			}

		}
		LOGGER.debug("metaData generated::{}", metaData);
		return metaData;
	}
	
	private Map<String, List> populateCreditProfileIdList(List<TelusCreditProfile> creditProfile) {
		Map<String, List> metaData = new HashMap<>();
		List<String> ids = creditProfile.stream().map(TelusCreditProfile :: getId).collect(Collectors.toList());
		metaData.put(MetaDataType.CREDIT_PROFILE_ID_LIST.name(), ids);
		return metaData;
	}

	private Map<String, String> populateIndividualMetaData(Individual person) {
		Map<String, String> metaData = new HashMap<>();
		if (!CollectionUtils.isEmpty(person.getIndividualIdentification())) {
			for (TelusIndividualIdentification telusIdentity : person.getIndividualIdentification()) {
				if (IdentificationType.getIdentificationType(telusIdentity.getIdentificationType()) != null) {
					metaData.put(
								MetaDataType.getIdentificationType( telusIdentity.getIdentificationType()).name() ,
								telusIdentity.getIdentificationIdHashed() );
				}
			}
		}
		if(!StringUtils.isBlank(person.getBirthDate())) {
			metaData.put(MetaDataType.BIRTH_DATE.name(), person.getBirthDate());
		}
		if(!CollectionUtils.isEmpty(person.getContactMedium())) {
			Optional<MediumCharacteristic> preferedContact = person.getContactMedium().stream().filter(contact ->
					Boolean.TRUE.equals(contact.getPreferred())
					&& ObjectUtils.isNotEmpty(contact.getValidFor()) && ObjectUtils.isNotEmpty(contact.getValidFor().getEndDateTime()) 
					&& DateTimeUtils.toUtcTimestamp(contact.getValidFor().getEndDateTime()).after(Timestamp.from(Instant.now())))
					.map(ContactMedium::getCharacteristic).filter(medium -> !StringUtils.isBlank(medium.getPostCode()))
					.findFirst();
			if(!preferedContact.isPresent()) {
				Optional<MediumCharacteristic> contactExist =person.getContactMedium().stream().filter(contact -> ObjectUtils.isNotEmpty(contact.getValidFor()) 
						&& ObjectUtils.isNotEmpty(contact.getValidFor().getEndDateTime()) 
						&& DateTimeUtils.toUtcTimestamp(contact.getValidFor().getEndDateTime()).after(Timestamp.from(Instant.now())))
						.map(ContactMedium::getCharacteristic).filter(medium -> !StringUtils.isBlank(medium.getPostCode()))
						.findFirst();
				if (contactExist.isPresent()) {
					metaData.put(MetaDataType.POSTAL_CODE.name(), contactExist.get().getPostCode() );
				}
			} else {
				metaData.put(MetaDataType.POSTAL_CODE.name(),preferedContact.get().getPostCode() );
			}
		}
		
		LOGGER.debug("Individual metaData generated::{}", metaData);
		return metaData;
	}

	private Map<String, String> populateCompanyMetaData(Organization org) {
		Map<String, String> metaData = new HashMap<>();
		if (!CollectionUtils.isEmpty(org.getOrganizationIdentification())) {
			for (OrganizationIdentification orgIdentity : org.getOrganizationIdentification()) {
				if (IdentificationType.getIdentificationType(orgIdentity.getIdentificationType()) != null) {
					metaData.put(
							IdentificationType.getIdentificationType(orgIdentity.getIdentificationType()).name(),
							orgIdentity.getIdentificationIdHashed()
							);					
				}
			}
		}
		
		if(!StringUtils.isBlank(org.getBirthDate())) {
			metaData.put(MetaDataType.BIRTH_DATE.name(), org.getBirthDate());
		}		
		
		LOGGER.debug("Organisation metaData generated::{}", metaData);
		return metaData;
	}

	//look up document by MetaDataType.CUSTOMER_ID
	protected Optional<CustomerDocument> getCurrentDocumentId(String custId) throws InterruptedException, ExecutionException {
		QuerySnapshot qSanpshot = firestore.collection(
								getCollectionName())
								.select("customer")
								.whereEqualTo("metadata." + MetaDataType.CUSTOMER_ID.name(), custId)
								.get().get();
		if (!qSanpshot.isEmpty()) {
			List<CustomerDocument> docs = qSanpshot.getDocuments().stream().map(document -> {
				LOGGER.debug("Found Document id:{} for given customer id:{}", document.getId(), custId);
				CustomerDocument doc =document.toObject(CustomerDocument.class);
				doc.setFireStoreId(document.getId());
				return doc;
			}).collect(Collectors.toList());

			if (!CollectionUtils.isEmpty(docs)) {
				return Optional.ofNullable(docs.get(0));
			}
		}
		return Optional.ofNullable(null);
	}

	protected void updateFireStoreDocument(CustomerDocument customerData, String id) {
		firestore.collection(getCollectionName()).document(id).set(customerData);
	}

	protected String addFireStoreDocument(CustomerDocument customerData)
			throws InterruptedException, ExecutionException {
		DocumentReference ref = firestore.collection(getCollectionName()).add(customerData).get();
		return ref.getId();
	}

	private RelatedParty getCustomerRoleRelatedParty(TelusCreditProfile primaryTelusCreditProfile) {
		if(primaryTelusCreditProfile==null) {
			return null;
		}
		List<RelatedParty> relatedPartyCustomerList = primaryTelusCreditProfile.getRelatedParty();
		if(relatedPartyCustomerList!=null ) {
			for (RelatedParty relatedPartyCustomer : relatedPartyCustomerList) {
				if( "customer".equalsIgnoreCase(relatedPartyCustomer.getRole()) ){
					return relatedPartyCustomer;
				}
			}
		}
		return null;
		
	}
	
	private RelatedPartyInterface getCustomerRoleEngagedParty(TelusCreditProfile primaryTelusCreditProfile) {
		if(primaryTelusCreditProfile==null) {
			return null;
		}
		List<RelatedParty> relatedPartyList = primaryTelusCreditProfile.getRelatedParty();
		for (RelatedParty relatedParty : relatedPartyList) {
			if( "customer".equalsIgnoreCase(relatedParty.getRole()) ){
				return relatedParty.getEngagedParty();
			}
		}
		return null;
		
	}

	
	private int getIndexOfRelatedPartyWithCustomerRole(TelusCreditProfile primaryTelusCreditProfile) {
		if(primaryTelusCreditProfile!=null) {
			List<RelatedParty> relatedPartyList = primaryTelusCreditProfile.getRelatedParty();
			if(relatedPartyList!=null && !relatedPartyList.isEmpty()) {
				RelatedParty[] relatedParties = new RelatedParty[relatedPartyList.size()];
				relatedParties = relatedPartyList.toArray(relatedParties);
			    for (int i = 0; i < relatedParties.length; i++) {
			    	if( "customer".equalsIgnoreCase(relatedParties[i].getRole()) ){
			    		 return i;
			    	}
			    }				
			}		    			       
		}
		return 0;
	}

	
	private TelusCreditProfile getPrimayCreditProfile(List<TelusCreditProfile> creditProfile) {
		if(creditProfile!=null) {
			for (TelusCreditProfile telusCreditProfile : creditProfile) {
				if(!"SEC".equalsIgnoreCase( telusCreditProfile.getCustomerCreditProfileRelCd()) ){
					return telusCreditProfile;
				}
			}
		}
		return null;
	}		
}
