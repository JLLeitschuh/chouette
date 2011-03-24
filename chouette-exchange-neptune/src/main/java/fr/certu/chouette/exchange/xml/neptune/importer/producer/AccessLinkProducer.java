package fr.certu.chouette.exchange.xml.neptune.importer.producer;

import java.util.Date;

import chouette.schema.AccessibilitySuitabilityDetailsItem;
import chouette.schema.ConnectionLinkExtension;
import fr.certu.chouette.model.neptune.AccessLink;
import fr.certu.chouette.model.neptune.type.ConnectionLinkTypeEnum;
import fr.certu.chouette.model.neptune.type.UserNeedEnum;
import fr.certu.chouette.plugin.report.ReportItem;
/**
 * 
 * @author mamadou keira
 *
 */
public class AccessLinkProducer extends AbstractModelProducer<AccessLink, chouette.schema.AccessLink>{

	@Override
	public AccessLink produce(chouette.schema.AccessLink xmlAccessLink, ReportItem report) {
		AccessLink accessLink = new AccessLink();
		// objectId, objectVersion, creatorId, creationTime
		populateFromCastorNeptune(accessLink, xmlAccessLink,report);
		// Name optional
		accessLink.setName(getNonEmptyTrimedString(xmlAccessLink.getName()));
		
		// Comment optional
		accessLink.setComment(getNonEmptyTrimedString(xmlAccessLink.getComment()));
		
		//StartOfLink mandatory
		accessLink.setStartOfLinkId(getNonEmptyTrimedString(xmlAccessLink.getStartOfLink()));
		
		//EndOfLink mandatory
		accessLink.setEndOfLinkId(getNonEmptyTrimedString(xmlAccessLink.getEndOfLink()));
		
		//LinkDistance optional
		accessLink.setLinkDistance(xmlAccessLink.getLinkDistance());
		
		// LiftAvailability optional
		accessLink.setLiftAvailable(xmlAccessLink.getLiftAvailability());
		
		// MobilityRestrictedSuitability optional
		accessLink.setMobilityRestrictedSuitable(xmlAccessLink.getMobilityRestrictedSuitability());
		
		// StairsAvailability optional
		accessLink.setStairsAvailable(xmlAccessLink.getStairsAvailability());
		
		// accessLinkExtension optional
		ConnectionLinkExtension xmlConnectionLinkExtension = xmlAccessLink.getConnectionLinkExtension();
		if(xmlConnectionLinkExtension != null){
			if(xmlConnectionLinkExtension.getAccessibilitySuitabilityDetails() != null){
				for(AccessibilitySuitabilityDetailsItem xmlAccessibilitySuitabilityDetailsItem : xmlConnectionLinkExtension.getAccessibilitySuitabilityDetails().getAccessibilitySuitabilityDetailsItem()){
					if(xmlAccessibilitySuitabilityDetailsItem.getUserNeedGroup() != null){
						try{
							accessLink.addUserNeed(UserNeedEnum.fromValue(xmlAccessibilitySuitabilityDetailsItem.getUserNeedGroup().getChoiceValue().toString()));
						}
						catch (IllegalArgumentException e) 
						{
							// TODO: traiter le cas de non correspondance
						}
					}
				}
			}
		}
		
		// DefaultDuration optional
		if(xmlAccessLink.getDefaultDuration() != null){
			accessLink.setDefaultDuration(new Date(xmlAccessLink.getDefaultDuration().toLong()));
		}
		
		// FrequentTravellerDuration optional
		if(xmlAccessLink.getFrequentTravellerDuration() != null){
			accessLink.setFrequentTravellerDuration(new Date(xmlAccessLink.getFrequentTravellerDuration().toLong()));
		}
		
		// OccasionalTravellerDuration optional
		if(xmlAccessLink.getOccasionalTravellerDuration() != null){
			accessLink.setOccasionalTravellerDuration(new Date(xmlAccessLink.getOccasionalTravellerDuration().toLong()));
		}
		
		// MobilityRestrictedTravellerDuration optional
		if(xmlAccessLink.getMobilityRestrictedTravellerDuration() != null){
			accessLink.setMobilityRestrictedTravellerDuration(new Date(xmlAccessLink.getMobilityRestrictedTravellerDuration().toLong()));
		}
		
		// LinkType optional
		if(xmlAccessLink.getLinkType() != null){
			try{
				accessLink.setLinkType(ConnectionLinkTypeEnum.fromValue(xmlAccessLink.getLinkType().value()));
			}
			catch (IllegalArgumentException e) 
			{
				// TODO: traiter le cas de non correspondance
			}
		}
		
		return accessLink;
	}

}
