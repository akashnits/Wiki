package com.example.akash.wiki.model;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class Response{

	@SerializedName("batchcomplete")
	private boolean batchcomplete;

	@SerializedName("continue")
	private JsonMemberContinue jsonMemberContinue;

	@SerializedName("query")
	private Query query;

	public void setBatchcomplete(boolean batchcomplete){
		this.batchcomplete = batchcomplete;
	}

	public boolean isBatchcomplete(){
		return batchcomplete;
	}

	public void setJsonMemberContinue(JsonMemberContinue jsonMemberContinue){
		this.jsonMemberContinue = jsonMemberContinue;
	}

	public JsonMemberContinue getJsonMemberContinue(){
		return jsonMemberContinue;
	}

	public void setQuery(Query query){
		this.query = query;
	}

	public Query getQuery(){
		return query;
	}

	@Override
 	public String toString(){
		return 
			"Response{" + 
			"batchcomplete = '" + batchcomplete + '\'' + 
			",continue = '" + jsonMemberContinue + '\'' + 
			",query = '" + query + '\'' + 
			"}";
		}
}