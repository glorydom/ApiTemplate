package com.dto.huiyi.meeting.entity.config;

import java.io.Serializable;

import org.activiti.engine.repository.ProcessDefinition;

public class ProcessDto implements Serializable{
	private static final long serialVersionUID = 4961909416549808188L;

	private String id;
	private String key;
	private String name;
	private int version;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	
	public ProcessDto(ProcessDefinition pd) {
		setId(pd.getId());
		setKey(pd.getKey());
		setName(pd.getName());
		setVersion(pd.getVersion());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ProcessDto that = (ProcessDto) o;

		if (key != null ? !key.equals(that.key) : that.key != null) return false;
		return name != null ? name.equals(that.name) : that.name == null;
	}

	@Override
	public int hashCode() {
		int result = key != null ? key.hashCode() : 0;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		return result;
	}
}
