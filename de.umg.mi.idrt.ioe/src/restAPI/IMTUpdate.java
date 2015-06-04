package restAPI;

import java.util.Date;

import routines.TalendDate;

public class IMTUpdate {
	
	public IMTUpdate(String artifactURI,boolean needsUpdate ) {
	setArtifactURI(artifactURI);
	setNeedsUpdate(needsUpdate);
	}
	
	public IMTUpdate() {
		// TODO Auto-generated constructor stub
	}
	
	private int buildNumber;
	private String artifactURI;
	private boolean needsUpdate;
	private Date buildTime;
	
	public String getArtifactURI() {
		return artifactURI;
	}
	public void setArtifactURI(String artifactURI) {
		this.artifactURI = artifactURI;
	}
	public boolean isNeedsUpdate() {
		return needsUpdate;
	}
	public void setNeedsUpdate(boolean needsUpdate) {
		this.needsUpdate = needsUpdate;
	}

	public int getBuildNumber() {
		return buildNumber;
	}

	public void setBuildNumber(int i) {
		this.buildNumber = i;
	}

	public Date getBuildTime() {
		return buildTime;
	}
	public String getBuildTimeString() {
		return TalendDate.formatDate("yyyy-MM-dd",buildTime);
	}
	public void setBuildTime(Date completedTime) {
		this.buildTime = completedTime;
	}

	public void setBuildTimeString(String buildTime) {
		// TODO Auto-generated method stub
		this.buildTime = TalendDate.parseDate("yyyy-MM-dd", buildTime);
	}
	

}
