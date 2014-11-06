/*
 * Copyright (C) 2014 Pedro Vicente Gómez Sánchez.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.pedrovgs.transformer;

import android.view.View;
import android.widget.RelativeLayout;

/**
 * Transformer extension created to resize the view instead of scale it as the other
 * implementation does. This implementation is based on change the LayoutParams.
 *
 * @author Pedro Vicente Gómez Sánchez
 */
class ResizeTransformer extends Transformer {

	//this is not needed, I think...
    private int lastHeight;
    private int lastWidth;

    ResizeTransformer(View view, View parent) {
        super(view, parent);
    }

    /**
     * Changes view width using view's LayoutParam.
     *
     * @param verticalDragOffset used to calculate the new width.
     */
    @Override
    public void updateWidth(float verticalDragOffset) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) getView().getLayoutParams();

        params.width = (int) (getOriginalWidth() - (getOriginalWidth() - (getOriginalWidth() / getXScaleFactor())) * verticalDragOffset);
        params.height = (int) (getOriginalHeight() - (getOriginalHeight() - (getOriginalHeight() / getYScaleFactor())) * verticalDragOffset);

        lastHeight = params.height;
        lastWidth = params.width;

        getView().setLayoutParams(params);
    }

    /**
     * Empty implementation.
     *
     * @param verticalDragOffset used to calculate the new width.
     */
    @Override
    public void updateHeight(float verticalDragOffset) {
        //Empty
    }

    /**
     * @return last updated height or the view height if the view hasn't been changed yet.
     */
    @Override
    public int getViewHeight() {
        return lastHeight == 0 ? super.getViewHeight() : lastHeight;
    }
    /**
     * @return last updated width or the view height if the view hasn't been changed yet.
     */
    @Override
    public int getViewWidth() {
        return lastWidth == 0 ? super.getViewWidth() : lastWidth;
    }

    /**
     * Changes X/Y view position using layout() method.
     *
     * @param verticalDragOffset used to calculate the new X/Y position.
     */
    @Override
    public void updateXPosition(float verticalDragOffset) {
        int left, top, right, bottom;

        right = getViewRightPosition(verticalDragOffset);
        left = right-getViewWidth();
        top = getView().getTop();
        bottom = top+getViewHeight();

        setLastLeftPosition(left);
        setLastTopPosition(top);

        getView().layout(left, top, right, bottom);
    }

    /**
     * Empty implementation. ViewDragHelper already changes the Y position.
     *
     * @param verticalDragOffset
     */
    @Override
    public void updateYPosition(float verticalDragOffset) {
        // Empty
    }

    /**
     * @return true if the right position of the view plus the right margin is equals to the parent
     * width.
     */
    @Override
    public boolean isViewAtRight() {
        return getView().getRight() + getMarginRight() == getParentView().getWidth();
    }

    /**
     * @return true if the bottom position of the view plus the margin right is equals to
     * the parent view height.
     */
    @Override
    public boolean isViewAtBottom() {
        return getView().getBottom() + getMarginBottom() == getParentView().getHeight();
    }

    /**
     * @return true if the left position of the view is to the right of the seventy five percent of the
     * parent view width.
     */
    @Override
    public boolean isNextToRightBound() {
        return (getView().getLeft() - getMarginRight()) > getParentView().getWidth() * 0.75;
    }

    /**
     * @return true if the left position of the view is to the left of the twenty five percent of
     * the parent width.
     */
    @Override
    public boolean isNextToLeftBound() {
        return (getView().getLeft() - getMarginRight()) < getParentView().getWidth() * 0.25;
    }

    /**
     * Uses the Y scale factor to calculate the min possible height.
     *
     * @return
     */
    @Override
    public int getMinHeightPlusMargin() {
        return (int) ((getOriginalHeight() / getYScaleFactor()) + getMarginBottom());
    }

    /**
     * Uses the X scale factor to calculate the min possible width.
     *
     * @return
     */
    @Override
    public int getMinWidthPlusMargin() {
        return (int) (getOriginalWidth() / getXScaleFactor()+getMarginRight());
    }

    /**
     * Calculate the current view right position for a given verticalDragOffset.
     *
     * @param verticalDragOffset used to calculate the new right position.
     * @return
     */
    private int getViewRightPosition(float verticalDragOffset) {
        return (int) ((getOriginalWidth()) - getMarginRight() * verticalDragOffset);
    }


}
