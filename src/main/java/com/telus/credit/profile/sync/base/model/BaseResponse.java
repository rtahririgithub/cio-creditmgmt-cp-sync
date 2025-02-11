package com.telus.credit.profile.sync.base.model;

import java.io.Serializable;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.cloud.firestore.annotation.Exclude;

public class BaseResponse implements Serializable{

	private static final long serialVersionUID = 6842359356221302736L;

	private int sortRankingRatingVal;

	private Timestamp sortRankingValidForVal;

	@JsonIgnore
	@Exclude
	public Timestamp getSortRankingValidForVal() {
		return sortRankingValidForVal;
	}

	@JsonIgnore
	@Exclude
	public void setSortRankingValidForVal(Timestamp sortRankingValidForVal) {
		this.sortRankingValidForVal = sortRankingValidForVal;
	}

	@JsonIgnore
	@Exclude
	public int getSortRankingRatingVal() {
		return sortRankingRatingVal;
	}

	@JsonIgnore
	@Exclude
	public void setSortRankingRatingVal(int sortRankingRatingVal) {
		this.sortRankingRatingVal = sortRankingRatingVal;
	}
	
}
