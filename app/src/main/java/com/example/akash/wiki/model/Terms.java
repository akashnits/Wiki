package com.example.akash.wiki.model;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class Terms{

	@SerializedName("description")
	private List<String> description;

	public void setDescription(List<String> description){
		this.description = description;
	}

	public List<String> getDescription(){
		return description;
	}

	@Override
 	public String toString(){
		return 
			"Terms{" + 
			"description = '" + description + '\'' + 
			"}";
		}
}