package de.umg.mi.idrt.idrtimporttool.server.Settings;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class I2B2User {

	private String userName;
	private String email;
	private String lastLogin;
	private int loginCount;
	private String lastQuery;
	private int queryCount;
	private boolean admin;

	private HashMap<String, HashSet<String>> userRoles;
	private String fullname;
	private String status;

	public I2B2User(String userName) {
		this.userName = userName;
		email = "";
		lastLogin = "";
		loginCount = 0;
		lastQuery = "";
		queryCount = 0;
		fullname = "";
		status = "";
		admin = false;

		userRoles = new HashMap<String, HashSet<String>>();
	}

	// public HashMap<String, HashSet<String>> getUserRoles() {
	// return userRoles;
	// }
	//
	// public void addUserRoleForProject(String project, String role) {
	// if (this.userRoles.containsKey(project))
	// this.userRoles.get(project).add(role);
	// else {
	// HashSet<String> tmpSet = new HashSet<String>();
	// tmpSet.add(role);
	// this.userRoles.put(project, tmpSet);
	// }
	// }
	//
	// public void removeUserRoleFromProject(String project, String role){
	//
	// }
	@Override
	public String toString() {
		return (userName + " " + lastLogin + " " + lastQuery);
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the roles
	 */
	// public I2B2UserRoles getRoles() {
	// return roles;
	// }

	/**
	 * @param roles
	 *            the roles to set
	 */
	// public void setRoles(I2B2UserRoles roles) {
	// this.roles = roles;
	// }

	/**
	 * @return the lastLogin
	 */
	public String getLastLogin() {
		return lastLogin;
	}

	/**
	 * @param lastLogin
	 *            the lastLogin to set
	 */
	public void setLastLogin(Timestamp date) {
		try {
			if (date != null) {
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				DateFormat iitdf = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
				// System.out.println(date.toString());
				java.util.Date formattedDate = df.parse(date.toString());
				lastLogin = iitdf.format(formattedDate).toString();
			} else {
				lastLogin = "";
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @return the loginCount
	 */
	public int getLoginCount() {
		return loginCount;
	}

	/**
	 * @param loginCount
	 *            the loginCount to set
	 */
	public void setLoginCount(int loginCount) {
		this.loginCount = loginCount;
	}

	/**
	 * @return the lastQuery
	 */
	public String getLastQuery() {
		return lastQuery;
	}

	public int getQueryCount() {
		return queryCount;
	}

	public void setQueryCount(int queryCount) {
		this.queryCount = queryCount;
	}

	/**
	 * @param lastQuery
	 *            the lastQuery to set
	 */
	public void setLastQuery(Timestamp date) {

		try {
			if (date != null) {
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				DateFormat iitdf = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
				// System.out.println(date.toString());
				java.util.Date formattedDate = df.parse(date.toString());
				lastQuery = iitdf.format(formattedDate).toString();
			} else {
				lastQuery = "";
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		if (fullname == null) {
			this.fullname = "";
		} else {
			this.fullname = fullname;
		}
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		if (email == null) {
			this.email = "";
		} else {
			this.email = email;
		}
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		if (status.toLowerCase().equals("a")) {
			this.status = "Active";
		} else {
			this.status = "Inactive";
		}
	}

	public static String getHashedPassword(String pass) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(pass.getBytes());
			return toHex(md5.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String toHex(byte[] digest) {
		StringBuffer buf = new StringBuffer();
		for (byte element : digest) {
			buf.append(Integer.toHexString(element & 0x00FF));
		}
		return buf.toString();
	}

	public HashSet<String> getUserRoles(String project) {
		return userRoles.get(project);
	}

	public void setUserRole(String project, String role) {
		if (userRoles.get(project) == null) {
			HashSet<String> roleSet = new HashSet<String>();
			userRoles.put(project, roleSet);
		}
		userRoles.get(project).add(role);
	}

	public void setAdmin(boolean setAdmin) {
		admin = setAdmin;
	}

	public boolean getAdmin() {
		return admin;
	}

}
