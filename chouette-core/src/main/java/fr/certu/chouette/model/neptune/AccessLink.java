package fr.certu.chouette.model.neptune;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.certu.chouette.model.neptune.type.ConnectionLinkTypeEnum;
import fr.certu.chouette.model.neptune.type.UserNeedEnum;

import lombok.Getter;
import lombok.Setter;

/**
 * Neptune AccessLink  
 * <p/>
 * Note for fields comment : <br/>
 * when readable is added to comment, a implicit getter is available <br/>
 * when writable is added to comment, a implicit setter is available
 * 
 * @author mamadou keira
 */
public class AccessLink extends NeptuneIdentifiedObject{
	private static final long serialVersionUID = 7835556134861322471L;
	/**
	 * Comment 
	 * <br/><i>readable/writable</i>
	 */
	@Getter @Setter private String comment;
	/**
	 * Link Distance in meters (To be confirmed) 
	 * <br/><i>readable/writable</i>
	 */
	@Getter @Setter private BigDecimal linkDistance;
	/**
	 * Neptune Id for Start of Link StopArea 
	 * <br/><i>readable/writable</i>
	 */
	@Getter @Setter private String startOfLinkId;
	/**
	 * StopArea 
	 * <br/><i>readable/writable</i>
	 */
	@Getter @Setter private StopArea stopArea;
	/**
	 * Neptune Id for End of Link StopArea 
	 * <br/><i>readable/writable</i>
	 */
	@Getter @Setter private String endOfLinkId;
	/**
	 * AccessPoint 
	 * <br/><i>readable/writable</i>
	 */
	@Getter @Setter private AccessPoint accessPoint;
	/**
	 * Indicate if a Lift is available 
	 * <br/><i>readable/writable</i>
	 */
	@Getter @Setter boolean liftAvailable;
	/**
	 * indicate if the link is equipped for mobility restricted persons 
	 * <br/><i>readable/writable</i>
	 */
	@Getter @Setter boolean mobilityRestrictedSuitable;
	/**
	 * indicate if stairs are present on the link 
	 * <br/><i>readable/writable</i>
	 */
	@Getter @Setter boolean stairsAvailable;
	/**
	 * give a list of specific User needs available
	 * <br/><i>readable/writable</i>
	 */
	@Getter @Setter List<UserNeedEnum> userNeeds;
	/**
	 * Duration of link 
	 * <br/><i>readable/writable</i>
	 */
	@Getter @Setter private Date defaultDuration;
	/**
	 * Duration of link for frequent travelers 
	 * <br/><i>readable/writable</i>
	 */
	@Getter @Setter private Date frequentTravellerDuration;
	/**
	 * Duration of link for occasional travelers 
	 * <br/><i>readable/writable</i>
	 */
	@Getter @Setter private Date occasionalTravellerDuration;
	/**
	 * Duration of link for mobility restricted travelers 
	 * <br/><i>readable/writable</i>
	 */
	@Getter @Setter private Date mobilityRestrictedTravellerDuration;
	/**
	 * Link type
	 * <br/><i>readable/writable</i>
	 */
	@Getter @Setter private ConnectionLinkTypeEnum linkType; 
	public void addUserNeed(UserNeedEnum userNeed)
	{
		if (userNeeds == null) userNeeds = new ArrayList<UserNeedEnum>();
		userNeeds.add(userNeed);
	}
	
	@Override
	public String toString(String indent, int level) {
		StringBuilder sb = new StringBuilder(super.toString(indent,level));
		sb.append("\n").append(indent).append("  startOfLinkId = ").append(startOfLinkId);
		sb.append("\n").append(indent).append("  endOfLinkId = ").append(endOfLinkId);
		if(linkDistance != null){
			sb.append("\n").append(indent).append("  linkDistance = ").append(linkDistance.toPlainString());			
		}
		sb.append("\n").append(indent).append("  comment = ").append(comment);
		sb.append("\n").append(indent).append("  liftAvailable = ").append(liftAvailable);
		sb.append("\n").append(indent).append("  mobilityRestrictedSuitable = ").append(mobilityRestrictedSuitable);
		sb.append("\n").append(indent).append("  stairsAvailable = ").append(stairsAvailable);
		sb.append("\n").append(indent).append("  defaultDuration = ").append(formatDate(defaultDuration));
		sb.append("\n").append(indent).append("  frequentTravellerDuration = ").append(formatDate(frequentTravellerDuration));
		sb.append("\n").append(indent).append("  occasionalTravellerDuration = ").append(formatDate(occasionalTravellerDuration));
		sb.append("\n").append(indent).append("  mobilityRestrictedTravellerDuration = ").append(formatDate(mobilityRestrictedTravellerDuration));
		sb.append("\n").append(indent).append("  linkType = ").append(linkType);
		
		if(userNeeds != null){
			sb.append("\n").append(indent).append(CHILD_ARROW).append("userNeeds");
			for (UserNeedEnum userNeed : getUserNeeds())
			{
				sb.append("\n").append(indent).append(CHILD_LIST_ARROW).append(userNeed);
			}
		}

		return sb.toString();
	}
	
	private String formatDate(Date date){
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		if(date != null){
			return dateFormat.format(date);
		}
		else{
			return null;
		}
	}
}

