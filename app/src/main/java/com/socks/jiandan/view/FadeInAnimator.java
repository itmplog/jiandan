package com.socks.jiandan.view;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Created by zhaokaiqiang on 15/4/24.
 */
public class FadeInAnimator extends RecyclerView.ItemAnimator{

	private ArrayList<RecyclerView.ViewHolder> viewHolders;

	public FadeInAnimator(){
		viewHolders = new ArrayList<>();
	}


	@Override
	public void runPendingAnimations() {

	}



	@Override
	public boolean animateDisappearance(RecyclerView.ViewHolder viewHolder, ItemHolderInfo preLayoutInfo, ItemHolderInfo postLayoutInfo) {
		return false;
	}

	@Override
	public boolean animateAppearance(RecyclerView.ViewHolder viewHolder, ItemHolderInfo preLayoutInfo, ItemHolderInfo postLayoutInfo) {
		return false;
	}

	@Override
	public boolean animatePersistence(RecyclerView.ViewHolder viewHolder, ItemHolderInfo preLayoutInfo, ItemHolderInfo postLayoutInfo) {
		return false;
	}

	@Override
	public boolean animateChange(RecyclerView.ViewHolder oldHolder, RecyclerView.ViewHolder newHolder, ItemHolderInfo preLayoutInfo, ItemHolderInfo postLayoutInfo) {
		return false;
	}

	@Override
	public void endAnimation(RecyclerView.ViewHolder item) {

	}

	@Override
	public void endAnimations() {

	}

	@Override
	public boolean isRunning() {
		return false;
	}
}
