/**
 * Projet CHOUETTE
 *
 * ce projet est sous license libre
 * voir LICENSE.txt pour plus de details
 *
 */

package fr.certu.chouette.plugin.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;
import fr.certu.chouette.plugin.exchange.report.ExchangeReport;
import fr.certu.chouette.plugin.report.Report;
import fr.certu.chouette.plugin.report.ReportItem;

@MappedSuperclass
public abstract class LogMessage extends ActiveRecordObject
{
   private static final long serialVersionUID = 2351542100574918787L;

   @Getter
   @Setter
   @Column(name = "export_id", nullable = false, unique = true)
   private Long parentId;

   @Getter
   @Setter
   @Column(name = "key")
   private String key;

   @Getter
   @Setter
   @Column(name = "arguments", length = 1000)
   private String arguments = null;

   @Getter
   @Setter
   @Column(name = "severity")
   private String severity;

   @Getter
   @Setter
   @Column(name = "position")
   private int position;

   public LogMessage(Long parentId, String format, Report report, int position)
   {
      super();
      this.parentId = parentId;
      if (report instanceof ReportItem)
      {
         ReportItem item = (ReportItem) report;
         init(format, item, null, position);
      }
      else
      {
         this.key = format + report.getOriginKey();
         this.position = position;
         this.severity = report.getStatus().name().toLowerCase();
         if (report instanceof ExchangeReport)
         {
            ExchangeReport eReport = (ExchangeReport) report;
            this.arguments = "{\"0\" : \"" + eReport.getFormat() + "\"}";
         }
      }
   }

   public LogMessage(Long parentId, String format, ReportItem item, String prefix, int position)
   {
      super();
      this.parentId = parentId;
      init(format, item, prefix, position);
   }

   private void init(String format, ReportItem item, String prefix, int position)
   {
      if (prefix != null && !prefix.isEmpty())
         this.key = prefix + "|" + format + item.getMessageKey();
      else
         this.key = format + item.getMessageKey();
      if (!item.getMessageArgs().isEmpty())
      {
         List<Object> args = item.getMessageArgs();
         addArguments(args);
      }
      this.position = position;
      this.severity = item.getStatus().name().toLowerCase();
   }

   private void addArguments(List<Object> args)
   {
      int size = args.size();
      StringBuilder b = new StringBuilder("{");
      for (int i = 0; i < size; i++)
      {
         b.append("\"");
         b.append(i);
         b.append("\" : \"");
         if (args.get(i) != null)
            b.append(args.get(i).toString().replaceAll("\"", "\\\"").replaceAll("\n", " "));
         else
            b.append("???");
         b.append("\"");
         if (i == size - 1)
         {
            b.append("}");
         }
         else
         {
            b.append(",");
         }
      }
      if (b.length() >= 1000)
      {
         // line too long
         b = new StringBuilder("{");
         for (int i = 0; i < size; i++)
         {
            b.append("\"");
            b.append(i);
            b.append("\" : \"");
            if (args.get(i) != null)
            {
               String s = args.get(i).toString();
               if (s.length() > 50)
                  s = s.substring(0, 50);
               b.append(s.replaceAll("\"", "\\\"").replaceAll("\n", " "));
            }
            else
               b.append("???");
            b.append("\"");
            if (i == size - 1)
            {
               b.append("}");
            }
            else
            {
               b.append(",");
            }
         }

      }
      this.arguments = b.toString();
   }

}
