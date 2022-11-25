package com.funding.file;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class FileDto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String originName;

	private String newName;
	
	private String 	savePath;
	
	
	public FileDto() {}
	
	public FileDto(String originName, String newName, String savePath) {
		this.originName = originName;
		this.newName = newName;
		this.savePath = savePath;
	}
	

}
