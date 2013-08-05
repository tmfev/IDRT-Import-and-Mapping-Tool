package de.umg.mi.idrt.ioe;
import java.awt.Color;

import javax.swing.JTextPane;


public class UserMessage {
	
	private String message = "";
	private int messageType = 0; // 0 = error, 1 = good, 2 = bad, 3 = neutral
	
	
	public UserMessage ( ) {
		
			this.reset();
	}
	
	public UserMessage ( String message, int messageType ) {
		
		if ( message.isEmpty() )
			this.reset();
		else
			this.setUserMessage( message, messageType);
	}
	
	public void setUserMessage ( String message, int messageType ) {
		this.message = message;
		this.messageType = messageType;
	}
	
	public void reset () {
		this.message = "";
		this.messageType = 0;
	}
	
	public void setMessage ( String message ) {
		this.message = message;
	}

	public String getMessage () {
		return this.message;
	}
	
	public void setMessageType ( int messageType ) {
		this.messageType = messageType;
	}

	public int getMessageType () {
		return this.messageType;
	}
	
	public boolean hasMessage () {
		if ( !this.message.isEmpty() && this.messageType != 0 )
			return true;
		else
			return false;
	}
	
	public boolean hasPositivMessage () {
		if ( !this.message.isEmpty() && this.messageType == 1 )
			return true;
		else
			return false;
	}
	
    public JTextPane getFormattedUserMessage () {
    	Debug.d( "#getActionMessage" );
		
    	JTextPane actionMessagePane = new JTextPane();
		actionMessagePane.setContentType( "text/html" );
		
		actionMessagePane.setText( this.getMessage() );
		
		
		if ( this.getMessageType() == 1 )
			actionMessagePane.setBackground( Color.GREEN );
		else if ( this.getMessageType() == 2 )
			actionMessagePane.setBackground( Color.RED );
		else 
			actionMessagePane.setBackground( Color.LIGHT_GRAY );
		
		this.reset();
		
		return actionMessagePane;
    }

}
