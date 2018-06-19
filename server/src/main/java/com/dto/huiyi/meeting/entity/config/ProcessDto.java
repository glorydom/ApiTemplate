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
}
