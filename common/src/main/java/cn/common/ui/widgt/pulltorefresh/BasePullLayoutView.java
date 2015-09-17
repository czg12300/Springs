package cn.common.ui.widgt.pulltorefresh;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * description：base of pull layout view
 *
 * @author jakechen
 * @since 2015/9/16 18:11
 */
public abstract class BasePullLayoutView extends LinearLayout {
  private final static float OFFSET_RADIO = 1.8f;

  private static final int SCROLL_BACK_HEADER = 0;

  private static final int SCROLL_BACK_HEADER_REFRESH = 2;
  private static final int SCROLL_BACK_FOOTER = 3;

  private static final int SCROLL_BACK_FOOTER_LOAD = 4;

  private int mScrollBack;

  private FrameLayout mFlHeader;

  private FrameLayout mFlFooter;

  private PullEnable mPullEnable;

  private Scroller mScroller;

  private float mLastY;
  private float mPositionY;
  private PullListener mPullListener;

  public BasePullLayoutView(Context context) {
    this(context, null);
  }

  public BasePullLayoutView(Context context, AttributeSet attrs) {
    super(context, attrs);
    mScroller = new Scroller(context, new DecelerateInterpolator());
    mFlHeader = new FrameLayout(context);
    mFlFooter = new FrameLayout(context);
    addView(mFlHeader);
    View view = getContentView();
    if (view instanceof PullEnable) {
      mPullEnable = (PullEnable) view;
    } else {
      throw new IllegalArgumentException("content view is not instance of PullEnable");
    }
    addView(view, new LinearLayout.LayoutParams(-1, -1));
    addView(mFlFooter);
    getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

      @Override
      public void onGlobalLayout() {
        LinearLayout.LayoutParams pHeader = (LayoutParams) mFlHeader.getLayoutParams();
        pHeader.topMargin = -pHeader.height;
        LinearLayout.LayoutParams pFooter = (LayoutParams) mFlFooter.getLayoutParams();
        pHeader.bottomMargin = -pFooter.height;
        ViewTreeObserver observer = getViewTreeObserver();
        if (null != observer) {
          if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            observer.removeGlobalOnLayoutListener(this);
          } else {
            observer.removeOnGlobalLayoutListener(this);
          }
        }
      }
    });
  }

  @Override
  public boolean onTouchEvent(MotionEvent ev) {
    switch (ev.getActionMasked()) {
      case MotionEvent.ACTION_DOWN:
        mLastY = ev.getRawY();
        mPositionY = mLastY;
        break;
      case MotionEvent.ACTION_MOVE:
        mPositionY = ev.getRawY();
        handleMove();
        break;
      default:
        mPositionY = ev.getRawY();
        handleReset();
        mLastY = ev.getRawY();
        break;
    }
    return super.onTouchEvent(ev);
  }

  private void handleReset() {
    if (isPullDown()) {
      LinearLayout.LayoutParams pHeader = (LayoutParams) mFlHeader.getLayoutParams();
      if (mFlHeader.getPaddingTop() > 0) {
        mScrollBack = SCROLL_BACK_HEADER_REFRESH;
        mScroller.startScroll(0, mFlHeader.getPaddingTop(), 0, -mFlHeader.getPaddingTop(), 300);
        if (mPullListener != null) {
          mPullListener.onRefresh();
        }
        handleRefreshing(mFlHeader);
      } else if (pHeader.topMargin > -mFlHeader.getHeight()) {
        mScrollBack = SCROLL_BACK_HEADER;
        mScroller.startScroll(0, pHeader.topMargin, 0, pHeader.topMargin - (-mFlHeader.getHeight()), 300);
      }
    } else if (isPullUp()) {
      LinearLayout.LayoutParams pFooter = (LayoutParams) mFlFooter.getLayoutParams();
      if (mFlFooter.getPaddingBottom() > 0) {
        mScrollBack = SCROLL_BACK_FOOTER_LOAD;
        mScroller.startScroll(0, mFlFooter.getPaddingBottom(), 0, -mFlFooter.getPaddingBottom(), 300);
        if (mPullListener != null) {
          mPullListener.onLoad();
        }
        handleLoading(mFlFooter);
      } else if (pFooter.bottomMargin > -mFlFooter.getHeight()) {
        mScrollBack = SCROLL_BACK_FOOTER;
        mScroller.startScroll(0, pFooter.bottomMargin, 0, pFooter.topMargin - (-mFlFooter.getHeight()), 300);
      }
    }
  }

  @Override
  public void computeScroll() {
    super.computeScroll();
    if (mScroller.computeScrollOffset()) {
      int offset = mScroller.getCurrY();
      switch (mScrollBack) {
        case SCROLL_BACK_HEADER:
          LinearLayout.LayoutParams pHeader = (LinearLayout.LayoutParams) mFlHeader.getLayoutParams();
          pHeader.topMargin = offset;
          break;
        case SCROLL_BACK_HEADER_REFRESH:
          mFlHeader.setPadding(0, offset, 0, 0);
          break;
        case SCROLL_BACK_FOOTER:
          LinearLayout.LayoutParams pFooter = (LinearLayout.LayoutParams) mFlFooter.getLayoutParams();
          pFooter.bottomMargin = offset;
          break;
        case SCROLL_BACK_FOOTER_LOAD:
          mFlFooter.setPadding(0, 0, 0, offset);
          break;
      }
    }
  }

  private boolean isPullUp() {
    return mPositionY - mLastY > 0 && mPullEnable.canPullUp();
  }

  private boolean isPullDown() {
    return mPositionY - mLastY < 0 && mPullEnable.canPullDown();
  }

  private void handleMove() {
    if (isPullDown()) {
      int yDistance = (int) (mLastY / OFFSET_RADIO);
      if (yDistance < mFlHeader.getHeight()) {
        LinearLayout.LayoutParams pFooter = (LayoutParams) mFlHeader.getLayoutParams();
        pFooter.topMargin = -yDistance;
      } else if (yDistance == mFlHeader.getHeight()) {
        handleRefreshAnimation(mFlHeader);
      } else {
        mFlHeader.setPadding(0, yDistance, 0, 0);
      }
    } else if (isPullUp()) {
      int yDistance = (int) (mLastY / OFFSET_RADIO);
      if (yDistance < mFlFooter.getHeight()) {
        LinearLayout.LayoutParams pFooter = (LayoutParams) mFlFooter.getLayoutParams();
        pFooter.bottomMargin = yDistance;
      } else if (yDistance == mFlFooter.getHeight()) {
        handleLoadAnimation(mFlFooter);
      } else {
        mFlFooter.setPadding(0, 0, 0, yDistance);
      }
    }
  }

  protected abstract void handleRefreshing(View header);

  protected abstract void handleLoading(View footer);

  protected void handleRefreshAnimation(View header) {
  }


  protected void handleLoadAnimation(View footer) {
  }


  public void setHeaderView(View header) {
    mFlHeader.addView(header);
  }

  public void setFooterView(View header) {
    mFlHeader.addView(header);
  }

  protected abstract View getContentView();

  public PullListener getPullListener() {
    return mPullListener;
  }

  public void setPullListener(PullListener pullListener) {
    this.mPullListener = pullListener;
  }
}
