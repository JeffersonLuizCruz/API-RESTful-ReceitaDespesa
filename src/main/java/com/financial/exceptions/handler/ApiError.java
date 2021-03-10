package com.financial.exceptions.handler;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class ApiError implements Serializable{
	private static final long serialVersionUID = 1L;
	
	
	private int status;
	private String message;
	private Date dateTimes;

}
