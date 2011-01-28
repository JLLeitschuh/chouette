/**
 * Projet CHOUETTE
 *
 * ce projet est sous license libre
 * voir LICENSE.txt pour plus de details
 *
 */
package fr.certu.chouette.common;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * 
 */
@SuppressWarnings("serial")
public abstract class ChouetteException extends Exception
{
    public abstract String getPrefix();
	
	public abstract String getCode();

	private String[] messageArgs;
	/**
	 * 
	 */
	public ChouetteException() 
	{
		super();
		messageArgs = new String[0];
	}

	/**
	 * @param message
	 */
	public ChouetteException(String message) 
	{
		super();
		messageArgs = new String[]{message};
	}

	/**
	 * @param cause
	 */
	public ChouetteException(Throwable cause) 
	{
		super(cause);
		messageArgs = new String[0];
	}

	/**
	 * @param arg0
	 */
	public ChouetteException(String... args)
	{
		super();
		messageArgs = args;
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public ChouetteException(String message, Throwable cause)
	{
		super(cause);
		messageArgs = new String[]{message};
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public ChouetteException(Throwable cause,String... args)
	{
		super(cause);
		messageArgs = args;
	}

	/* (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */
	@Override
	public final String getMessage()
	{
		Locale locale = new Locale(Locale.ENGLISH.getLanguage(), Locale.UK.getCountry());
		return getLocalizedMessage(locale);
	}
		
	

	/* (non-Javadoc)
	 * @see java.lang.Throwable#getLocalizedMessage()
	 */
	@Override
	public final String getLocalizedMessage() 
	{
		return getLocalizedMessage(Locale.getDefault());
	}

	/**
	 * @param locale
	 * @return
	 */
	public final String getLocalizedMessage(Locale locale)
	{
		try
		{
			String format = "";
			String message = "";
			try
			{
				ResourceBundle bundle = ResourceBundle.getBundle(this.getClass().getName(),locale);
				format = bundle.getString(getCode());
				message = MessageFormat.format(format,(Object[])messageArgs);
			}
			catch (MissingResourceException e1)
			{
				try
				{
					ResourceBundle bundle = ResourceBundle.getBundle(this.getClass().getName());
					format = bundle.getString(getCode());
					message = MessageFormat.format(format,(Object[])messageArgs);
				}
				catch (MissingResourceException e2)
				{
					message = Arrays.toString(messageArgs);
				}
			}

			if (getCause() != null)
			{
				format = getFormat("cause",locale);
				message += "\n"+MessageFormat.format(format,getCause().getLocalizedMessage());
			}
			
			format = getFormat("message",locale);
			return MessageFormat.format(format,getPrefix(),getCode(),message);
		}
		catch (RuntimeException ex)
		{
			throw ex;
			//return this.getClass().getName() + ":"+ code.name()+" "+detail.name();
		}
	}

	private String getFormat(String key,Locale locale) 
	{
		try
		{
			ResourceBundle localBundle = ResourceBundle.getBundle(ChouetteException.class.getName(),locale);
			return localBundle.getString(key);
		}
		catch (MissingResourceException e)
		{
			ResourceBundle localBundle = ResourceBundle.getBundle(ChouetteException.class.getName());
			return localBundle.getString(key);
		}
	}
}