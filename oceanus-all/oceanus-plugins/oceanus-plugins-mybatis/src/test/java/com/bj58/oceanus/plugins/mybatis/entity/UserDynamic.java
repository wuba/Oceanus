package com.bj58.oceanus.plugins.mybatis.entity;

public class UserDynamic {
	
	private long uid;
	
	private String pwds;
	
	private String version;

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public String getPwds() {
		return pwds;
	}

	public void setPwds(String pwds) {
		this.pwds = pwds;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "UserDynamic [uid=" + uid + ", pwds=" + pwds + ", version="
				+ version + "]";
	}

}
