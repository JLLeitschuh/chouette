package mobi.chouette.exchange.hub.exporter;

import java.io.IOException;
import java.util.Comparator;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Color;
import mobi.chouette.common.Context;
import mobi.chouette.common.chain.Command;
import mobi.chouette.common.chain.CommandFactory;
import mobi.chouette.exchange.CommandCancelledException;
import mobi.chouette.exchange.ProcessingCommands;
import mobi.chouette.exchange.ProcessingCommandsFactory;
import mobi.chouette.exchange.ProgressionCommand;
import mobi.chouette.exchange.exporter.AbstractExporterCommand;
import mobi.chouette.exchange.hub.Constant;
import mobi.chouette.exchange.report.ActionReporter;
import mobi.chouette.exchange.report.ActionReporter.ERROR_CODE;
import mobi.chouette.exchange.report.ReportConstant;
import mobi.chouette.model.Line;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;

@Log4j
@Stateless(name = HubExporterCommand.COMMAND)
public class HubExporterCommand extends AbstractExporterCommand implements Command, Constant, ReportConstant {

	public static final String COMMAND = "HubExporterCommand";

	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean execute(Context context) throws Exception {
		boolean result = ERROR;
		Monitor monitor = MonitorFactory.start(COMMAND);

		InitialContext initialContext = (InitialContext) context.get(INITIAL_CONTEXT);
		ActionReporter reporter = ActionReporter.Factory.getInstance();
		// initialize reporting and progression
		ProgressionCommand progression = (ProgressionCommand) CommandFactory.create(initialContext,
				ProgressionCommand.class.getName());

		try {

			// read parameters
			Object configuration = context.get(CONFIGURATION);
			if (!(configuration instanceof HubExportParameters)) {
				// fatal wrong parameters
				log.error("invalid parameters for hub export " + configuration.getClass().getName());
				reporter.setActionError(context, ERROR_CODE.INVALID_PARAMETERS,"invalid parameters for hub export " + configuration.getClass().getName());
				return ERROR;
			}

			HubExportParameters parameters = (HubExportParameters) configuration;
			if (parameters.getStartDate() != null && parameters.getEndDate() != null) {
				if (parameters.getStartDate().after(parameters.getEndDate())) {
					reporter.setActionError(context, ERROR_CODE.INVALID_PARAMETERS,"end date before start date");
					return ERROR;

				}
			}
			// no validation available for this export
			parameters.setValidateAfterExport(false);
			
			ProcessingCommands commands = ProcessingCommandsFactory.create(HubExporterProcessingCommands.class.getName());
			
			result = process(context, commands, progression, false,Mode.line);

		} catch (CommandCancelledException e) {
			reporter.setActionError(context, ERROR_CODE.INTERNAL_ERROR, "Command cancelled");
			log.error(e.getMessage());
		} catch (Exception e) {
			reporter.setActionError(context, ERROR_CODE.INTERNAL_ERROR,"Fatal :" + e);
			log.error(e.getMessage(), e);
		} finally {
			progression.dispose(context);
			log.info(Color.YELLOW + monitor.stop() + Color.NORMAL);
		}

		return result;
	}

	public class LineSorter implements Comparator<Line> {
		@Override
		public int compare(Line arg0, Line arg1) {

			return arg0.objectIdSuffix().compareTo(arg1.objectIdSuffix());
		}
	}

	public static class DefaultCommandFactory extends CommandFactory {

		@Override
		protected Command create(InitialContext context) throws IOException {
			Command result = null;
			try {
				String name = "java:app/mobi.chouette.exchange.hub/" + COMMAND;
				result = (Command) context.lookup(name);
			} catch (NamingException e) {
				log.error(e);
			}
			return result;
		}
	}

	static {
		CommandFactory.factories.put(HubExporterCommand.class.getName(), new DefaultCommandFactory());
	}
}
