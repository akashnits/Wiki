package com.example.akash.wiki.model;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class PagesItem{

	@SerializedName("ns")
	private int ns;

	@SerializedName("terms")
	private Terms terms;

	@SerializedName("index")
	private int index;

	@SerializedName("pageid")
	private int pageid;

	@SerializedName("title")
	private String title;

	@SerializedName("thumbnail")
	private Thumbnail thumbnail;

	public void setNs(int ns){
		this.ns = ns;
	}

	public int getNs(){
		return ns;
	}

	public void setTerms(Terms terms){
		this.terms = terms;
	}

	public Terms getTerms(){
		return terms;
	}

	public void setIndex(int index){
		this.index = index;
	}

	public int getIndex(){
		return index;
	}

	public void setPageid(int pageid){
		this.pageid = pageid;
	}

	public int getPageid(){
		return pageid;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setThumbnail(Thumbnail thumbnail){
		this.thumbnail = thumbnail;
	}

	public Thumbnail getThumbnail(){
		return thumbnail;
	}

	@Override
 	public String toString(){
		return 
			"PagesItem{" + 
			"ns = '" + ns + '\'' + 
			",terms = '" + terms + '\'' + 
			",index = '" + index + '\'' + 
			",pageid = '" + pageid + '\'' + 
			",title = '" + title + '\'' + 
			",thumbnail = '" + thumbnail + '\'' + 
			"}";
		}
}