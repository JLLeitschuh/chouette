package fr.certu.chouette.exchange.gtfs.refactor.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URL;
import java.util.TimeZone;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import fr.certu.chouette.exchange.gtfs.refactor.exporter.StopExporter;
import fr.certu.chouette.exchange.gtfs.refactor.importer.Context;

// @ToString(callSuper=true)
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class GtfsStop extends GtfsObject implements Serializable
{

   private static final long serialVersionUID = 1L;

   @Getter
   @Setter
   private String stopId;

   @Getter
   @Setter
   private String stopCode;

   @Getter
   @Setter
   private String stopName;

   @Getter
   @Setter
   private String stopDesc;

   @Getter
   @Setter
   private BigDecimal stopLat;

   @Getter
   @Setter
   private BigDecimal stopLon;

   @Getter
   @Setter
   private String zoneId;

   @Getter
   @Setter
   private URL stopUrl;

   @Getter
   @Setter
   private LocationType locationType;

   @Getter
   @Setter
   private String parentStation;

   @Getter
   @Setter
   private TimeZone stopTimezone;

   @Getter
   @Setter
   private WheelchairBoardingType wheelchairBoarding;

   @Override
   public String toString()
   {
      return id + ":" + StopExporter.CONVERTER.to(new Context(),this);
   }

   public enum LocationType implements Serializable
   {
      Stop, Station, Access;

   }

   public enum WheelchairBoardingType implements Serializable
   {
      NoInformation, Allowed, NoAllowed;

   }

}
