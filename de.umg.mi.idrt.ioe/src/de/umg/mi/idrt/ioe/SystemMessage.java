package de.umg.mi.idrt.ioe;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JTextPane;

public class SystemMessage {

	String messageText = "";
	MessageType messageTypeVar = MessageType.INFO;
	MessageLocation messageLocationVar = MessageLocation.MAIN;
	int messageTime = 0;
	MessageLooks messageLooks = new MessageLooks(MessageType.INFO);
	boolean isDialogMessage = false;

	public SystemMessage() {

		// this.reset();
	}

	public SystemMessage(String messageText, MessageType messageTypeVar,
			MessageLocation messageLocationVar) {
		setMessageText(messageText);
		setMessageType(messageTypeVar);
		setMessageLocation(messageLocationVar);
	}
	
	public SystemMessage(String messageText, MessageType messageTypeVar) {
		setMessageText(messageText);
		setMessageType(messageTypeVar);
		setMessageLocation(SystemMessage.MessageLocation.MAIN);
	}

	public SystemMessage(String messageText, MessageType messageTypeVar,
			MessageLocation messageLocationVar, int messageTime) {
		setMessageText(messageText);
		setMessageType(messageTypeVar);
		setMessageLocation(messageLocationVar);
		setMessageTime(messageTime);
	}

	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}

	public String getMessageText() {
		return this.messageText;
	}

	public void setMessageType(MessageType messageTypeVar) {
		this.messageTypeVar = messageTypeVar;
		// also set the message look
		setMessageLooks(messageTypeVar);
	}

	public MessageLooks getMessageLooks() {
		return this.messageLooks;
	}

	public MessageType getMessageType() {
		return this.messageTypeVar;
	}

	public void setMessageLocation(MessageLocation messageLocationVar) {
		this.messageLocationVar = messageLocationVar;
	}

	public MessageLocation getMessageLocation() {
		return this.messageLocationVar;
	}

	public void setMessageTime(int messageTime) {
		this.messageTime = messageTime;
	}

	public int getMessageTime() {
		return this.messageTime;
	}

	public enum MessageType {
		INFO, SUCCESS, ERROR
	}

	public enum MessageLocation {
		MAIN, NODEEDITOR, PREFERENCES, POPUP
	}

	public enum MessageSize {
		STANDARD, EDITOR, POPUP
	}

	public JTextPane getFormattedMessage(Double width,
			SystemMessage.MessageSize messageSize) {

		int size = 0;
		int size2 = 68;
		if (messageSize.equals(SystemMessage.MessageSize.EDITOR)) {
			size = 16;

		} else if (messageSize.equals(SystemMessage.MessageSize.POPUP)) {
			// standard size
			size = 8;
			size2 = 48;
		} else {
			// standard size
			size = 64;
		}

		JTextPane actionMessagePane = new JTextPane();
		actionMessagePane.setLayout(new FlowLayout());

		actionMessagePane.setContentType("text/html");
		actionMessagePane.setPreferredSize(new Dimension(width.intValue(),
				size2));
		actionMessagePane.setSize(new Dimension(width.intValue(), size2 - 2));
		actionMessagePane.setEditable(false);
		String message = "";
		message = "<table border=\"0\" width=\"" + width.toString() + "\"><tr>";

		message += "<td width=\"" + size + "\" bgcolor=\"#"
				+ this.messageLooks.getColor() + "\"><img src=\"file:"
				+ this.messageLooks.getIcon() + "\" width=\"" + size
				+ "\" height=\"" + size + "\" border=\"0\" alt=\"sign\"></td>";
		message += "<td width=\"" + String.valueOf(width - size)
				+ "px\" bgcolor=\"#" + this.messageLooks.getColorLight()
				+ "\">" + this.getMessageText() + "</td></tr></table>";
		actionMessagePane.setText(message);

		Debug.d("#####################");
		Debug.d("# " + this.getMessageText());
		Debug.d("#####################");

		System.out.println("#####################");
		System.out.println("# " + this.getMessageText());
		System.out.println("#####################");

		return actionMessagePane;
	}

	public boolean isNegativeMessage() {
		if (this.getMessageType().equals(SystemMessage.MessageType.ERROR)) {
			return true;
		}
		return false;
	}

	public JTextPane getFormattedSlim() {
		Debug.d("#getFormattedSlim");

		JTextPane actionMessagePane = new JTextPane();
		actionMessagePane.setContentType("text/html");

		actionMessagePane.setText(this.getMessageText());

		if (this.getMessageType().equals(SystemMessage.MessageType.SUCCESS))
			actionMessagePane.setBackground(Color.GREEN);
		else if (this.getMessageType().equals(SystemMessage.MessageType.ERROR))
			actionMessagePane.setBackground(Color.RED);
		else
			actionMessagePane.setBackground(Color.LIGHT_GRAY);

		// this.reset();

		return actionMessagePane;
	}

	private void setMessageLooks(SystemMessage.MessageType messageType) {
		this.messageLooks = new MessageLooks(messageType);
	}

	public boolean isDialogMessage() {
		return this.isDialogMessage;
	}

	public void setIsDialogMessage(boolean isDialogMessage) {
		this.isDialogMessage = isDialogMessage;
	}

	class MessageLooks {

		String color = "";
		String colorLight = "";
		String iconName = "";

		public MessageLooks(SystemMessage.MessageType messageType) {
			if (messageType.equals(SystemMessage.MessageType.SUCCESS)) {
				// actionMessagePane.setBackground( Color.GREEN );
				color = Resource.COLOR_SUCCESS_HTML;
				colorLight = Resource.COLOR_SUCCESS_HTML_LIGHT;
				iconName = "sign-success.png";
			} else if (messageType.equals(SystemMessage.MessageType.ERROR)) {
				// actionMessagePane.setBackground( Color.RED );
				color = Resource.COLOR_ERROR_HTML;
				colorLight = Resource.COLOR_ERROR_HTML_LIGHT;
				iconName = "sign-error.png";
			} else {
				// actionMessagePane.setBackground( Color.LIGHT_GRAY );
				color = Resource.COLOR_INFO_HTML;
				colorLight = Resource.COLOR_INFO_HTML_LIGHT;
				iconName = "sign-info.png";
			}
		}

		/**
		 * @return the color
		 */
		public String getColor() {
			return color;
		}

		/**
		 * @param color
		 *            the color to set
		 */
		public void setColor(String color) {
			this.color = color;
		}

		/**
		 * @return the colorLight
		 */
		public String getColorLight() {
			return colorLight;
		}

		/**
		 * @param colorLight
		 *            the colorLight to set
		 */
		public void setColorLight(String colorLight) {
			this.colorLight = colorLight;
		}

		/**
		 * @return the icon
		 */
		public String getIconName() {
			return iconName;
		}

		/**
		 * @param icon
		 *            the icon to set
		 */
		public void setIconName(String icon) {
			this.iconName = icon;
		}

		/**
		 * @return the icon
		 */
		public String getIcon() {
			return System.getProperty("user.dir").replace("\\", "/")
					+ "/images/" + this.getIconName();
		}

	}

}
