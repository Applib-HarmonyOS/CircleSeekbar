package io.feeeei.circleseekbar;

import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.components.Component.DrawTask;
import ohos.agp.components.Component.EstimateSizeListener;
import ohos.agp.components.Component.TouchEventListener;
import ohos.agp.render.Arc;
import ohos.agp.render.BlurDrawLooper;
import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.render.PixelMapHolder;
import ohos.agp.render.Texture;
import ohos.agp.utils.Color;
import ohos.agp.utils.RectFloat;
import ohos.app.Context;
import ohos.global.resource.NotExistException;
import ohos.global.resource.WrongTypeException;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.media.image.PixelMap;
import ohos.media.image.common.PixelFormat;
import ohos.media.image.common.Size;
import ohos.multimodalinput.event.MmiPoint;
import ohos.multimodalinput.event.TouchEvent;
import ohos.utils.PacMap;
import ohos.utils.Sequenceable;
import com.hmos.compat.utils.AttrUtils;
import java.io.IOException;

/**
 * Created by gaopengfei on 15/11/15.
 */
public class CircleSeekBar extends Component implements EstimateSizeListener, DrawTask, TouchEventListener {
    private static final HiLogLabel HILOG_LABEL = new HiLogLabel(0, 0, "CircleSeekBar");

    private static final double RADIAN = 180 / Math.PI;
    private static final String INSTANCE_MAX_PROCESS = "max_process";
    private static final String INSTANCE_CUR_PROCESS = "cur_process";
    private static final String INSTANCE_REACHED_COLOR = "reached_color";
    private static final String INSTANCE_REACHED_WIDTH = "reached_width";
    private static final String INSTANCE_REACHED_CORNER_ROUND = "reached_corner_round";
    private static final String INSTANCE_UNREACHED_COLOR = "unreached_color";
    private static final String INSTANCE_UNREACHED_WIDTH = "unreached_width";
    private static final String INSTANCE_POINTER_COLOR = "pointer_color";
    private static final String INSTANCE_POINTER_RADIUS = "pointer_radius";
    private static final String INSTANCE_POINTER_SHADOW = "pointer_shadow";
    private static final String INSTANCE_POINTER_SHADOW_RADIUS = "pointer_shadow_radius";
    private static final String INSTANCE_WHEEL_SHADOW = "wheel_shadow";
    private static final String INSTANCE_WHEEL_SHADOW_RADIUS = "wheel_shadow_radius";
    private static final String INSTANCE_WHEEL_HAS_CACHE = "wheel_has_cache";
    private static final String INSTANCE_WHEEL_CAN_TOUCH = "wheel_can_touch";
    private static final String INSTANCE_WHEEL_SCROLL_ONLY_ONE_CIRCLE = "wheel_scroll_only_one_circle";

    private Paint mWheelPaint;
    private Paint mReachedPaint;
    private Paint mReachedEdgePaint;
    private Paint mPointerPaint;
    private int mMaxProcess;
    private int mCurProcess;
    private float mUnreachedRadius;
    private int mReachedColor;
    private int mUnreachedColor;
    private float mReachedWidth;
    private float mUnreachedWidth;
    private boolean isHasReachedCornerRound;
    private int mPointerColor;
    private float mPointerRadius;
    private double mCurAngle;
    private float mWheelCurX;
    private float mWheelCurY;
    private boolean isHasWheelShadow;
    private boolean isHasPointerShadow;
    private float mWheelShadowRadius;
    private float mPointerShadowRadius;
    private boolean isHasCache;
    private Canvas mCacheCanvas;
    private PixelMap mCacheBitmap;
    private boolean isCanTouch;
    private boolean isScrollOneCircle;
    private float mDefShadowOffset;
    private OnSeekBarChangeListener mChangListener;

    /**
     * CircleSeekBar Constructor.
     *
     *  @param context - context for CircleSeekBar constructor
     *
     */
    public CircleSeekBar(Context context) {
        this(context, null);
        setEstimateSizeListener(this);
        addDrawTask(this::onDraw);
        setTouchEventListener(this);
    }

    /**
     * CircleSeekBar Constructor.
     *
     *  @param context - context for CircleSeekBar constructor
     *  @param attrs - attributes
     *
     */
    public CircleSeekBar(Context context, AttrSet attrs) {
        this(context, attrs, 0);
        setEstimateSizeListener(this);
        addDrawTask(this::onDraw);
        setTouchEventListener(this);
    }

    /**
     * CircleSeekBar Constructor.
     *
     *  @param context - context for CircleSeekBar constructor
     *  @param attrs - attributes
     *  @param defStyleAttr - defStyle attribute
     *
     */
    public CircleSeekBar(Context context, AttrSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
        initPadding();
        initPaints();
        setEstimateSizeListener(this);
        addDrawTask(this::onDraw);
        setTouchEventListener(this);
    }

    private void initPaints() {
        mDefShadowOffset = getDimen(ResourceTable.Float_def_shadow_offset);
        //Ring brush
        mWheelPaint = new Paint();
        Color hmosColor = CircleSeekBar.changeParamToColor(mUnreachedColor);
        mWheelPaint.setColor(hmosColor);
        mWheelPaint.setStyle(Paint.Style.STROKE_STYLE);
        mWheelPaint.setStrokeWidth(mUnreachedWidth);
        if (isHasWheelShadow) {
            Color hmosColor1 = CircleSeekBar.changeParamToColor(Color.DKGRAY.getValue());
            BlurDrawLooper blurDrawLooper = new BlurDrawLooper(mWheelShadowRadius, mDefShadowOffset,
                    mDefShadowOffset, hmosColor1);
            mWheelPaint.setBlurDrawLooper(blurDrawLooper);
        }

        //Selected area brush
        mReachedPaint = new Paint();
        Color hmosColor1 = CircleSeekBar.changeParamToColor(mReachedColor);
        mReachedPaint.setColor(hmosColor1);
        mReachedPaint.setStyle(Paint.Style.STROKE_STYLE);
        mReachedPaint.setStrokeWidth(mReachedWidth);
        if (isHasReachedCornerRound) {
            mReachedPaint.setStrokeCap(Paint.StrokeCap.ROUND_CAP);
        }

        //Anchor pen
        mPointerPaint = new Paint();
        Color hmosColor2 = CircleSeekBar.changeParamToColor(mPointerColor);
        mPointerPaint.setColor(hmosColor2);
        mPointerPaint.setStyle(Paint.Style.FILL_STYLE);
        if (isHasPointerShadow) {
            Color hmosColor3 = CircleSeekBar.changeParamToColor(Color.DKGRAY.getValue());
            BlurDrawLooper blurDrawLooper929 = new BlurDrawLooper(mPointerShadowRadius, 
                    mDefShadowOffset, mDefShadowOffset, hmosColor3);
            mPointerPaint.setBlurDrawLooper(blurDrawLooper929);
        }

        //Rounded brushes at both ends of the selected area
        mReachedEdgePaint = new Paint(mReachedPaint);
        mReachedEdgePaint.setStyle(Paint.Style.FILL_STYLE);
    }

    private void initAttrs(AttrSet attrs) {
        mMaxProcess = AttrUtils.getIntFromAttr(attrs, "wheel_max_process", 100);
        mCurProcess = AttrUtils.getIntFromAttr(attrs, "wheel_cur_process", 0);
        if (mCurProcess > mMaxProcess) {
            mCurProcess = mMaxProcess;
        }
        mReachedColor = AttrUtils.getColorFromAttr(attrs, "wheel_reached_color", 
                getColor(ResourceTable.Color_def_reached_color));
        mUnreachedColor = AttrUtils.getColorFromAttr(attrs, "wheel_unreached_color",
                getColor(ResourceTable.Color_def_wheel_color));
        mUnreachedWidth = AttrUtils.getDimensionFromAttr(attrs, "wheel_unreached_width",
                getDimen(ResourceTable.Float_def_wheel_width));
        isHasReachedCornerRound = AttrUtils.getBooleanFromAttr(attrs, "wheel_reached_has_corner_round", true);
        mReachedWidth = AttrUtils.getDimensionFromAttr(attrs, "wheel_reached_width", mUnreachedWidth);
        mPointerColor = AttrUtils.getColorFromAttr(attrs, "wheel_pointer_color",
                getColor(ResourceTable.Color_def_pointer_color));
        mPointerRadius = AttrUtils.getDimensionFromAttr(attrs, "wheel_pointer_radius", mReachedWidth / 2);
        isHasWheelShadow = AttrUtils.getBooleanFromAttr(attrs, "wheel_has_wheel_shadow", false);
        if (isHasWheelShadow) {
            mWheelShadowRadius = AttrUtils.getDimensionFromAttr(attrs, INSTANCE_WHEEL_SHADOW_RADIUS,
                    getDimen(ResourceTable.Float_def_shadow_radius));
        }
        isHasPointerShadow = AttrUtils.getBooleanFromAttr(attrs, "wheel_has_pointer_shadow", false);
        if (isHasPointerShadow) {
            mPointerShadowRadius = AttrUtils.getDimensionFromAttr(attrs, "wheel_pointer_shadow_radius", 
                    getDimen(ResourceTable.Float_def_shadow_radius));
        }
        isHasCache = AttrUtils.getBooleanFromAttr(attrs, INSTANCE_WHEEL_HAS_CACHE, isHasWheelShadow);
        isCanTouch = AttrUtils.getBooleanFromAttr(attrs, INSTANCE_WHEEL_CAN_TOUCH, true);
        isScrollOneCircle = AttrUtils.getBooleanFromAttr(attrs, INSTANCE_WHEEL_SCROLL_ONLY_ONE_CIRCLE, false);
        if (isHasPointerShadow || isHasWheelShadow) {
            setSoftwareLayer();
        }
    }

    private void initPadding() {
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();
        int paddingStart = 0;
        int paddingEnd = 0;
        
        paddingStart = getPaddingStart();
        paddingEnd = getPaddingEnd();
        
        int maxPadding = Math.max(paddingLeft, Math.max(paddingTop, 
                Math.max(paddingRight, Math.max(paddingBottom, Math.max(paddingStart, paddingEnd)))));
        setPadding(maxPadding, maxPadding, maxPadding, maxPadding);
    }

    private int getColor(int colorId) {
        return getContext().getColor(colorId);
    }

    private float getDimen(int dimenId) {
        try {
            return getResourceManager().getElement(dimenId).getFloat();
        } catch (IOException | NotExistException | WrongTypeException e) {
            HiLog.error(HILOG_LABEL, "exception in getDimen");
        }
        return -1;
    }

    private void setSoftwareLayer() {
        //empty
    }

    @Override
    public boolean onEstimateSize(int widthMeasureSpec, int heightMeasureSpec) {
        int height = EstimateSpec.getSize(heightMeasureSpec);
        int width = EstimateSpec.getSize(widthMeasureSpec);
        int min = Math.min(width, height);
        setEstimatedSize(min, min);
        refershPosition();
        refershUnreachedWidth();
        return false;
    }

    @Override
    public void onDraw(Component component, Canvas canvas) {

        int height = getHeight();
        int width = getWidth();

        if (getHeight() > getWidth()) {
            int min = Math.min(width, height);
            setComponentSize(min, min);
        }

        float left = getPaddingLeft() + mUnreachedWidth / 2;
        float top = getPaddingTop() + mUnreachedWidth / 2;
        float right = getWidth() - getPaddingRight() - mUnreachedWidth / 2;
        float bottom = getHeight() - getPaddingBottom() - mUnreachedWidth / 2;
        float centerX = (left + right) / 2;
        float centerY = (top + bottom) / 2;
        float wheelRadius = (getWidth() - getPaddingLeft() - getPaddingRight()) / 2 - mUnreachedWidth / 2;
        if (isHasCache) {
            if (mCacheCanvas == null) {
                buildCache(centerX, centerY, wheelRadius);
            }
            PixelMapHolder pixelMapHolder = CircleSeekBar.changeParamToPixelMapHolder(mCacheBitmap);
            canvas.drawPixelMapHolder(pixelMapHolder, 0, 0, mWheelPaint);
        } else {
            canvas.drawCircle(centerX, centerY, wheelRadius, mWheelPaint);
        }
        Arc arc = CircleSeekBar.changeParamToArc(-90, (float) mCurAngle, false);
        // Draw the selected area
        canvas.drawArc(new RectFloat(left, top, right, bottom), arc, mReachedPaint);
        // Draw anchor
        canvas.drawCircle(mWheelCurX, mWheelCurY, mPointerRadius, mPointerPaint);
    }

    private void buildCache(float centerX, float centerY, float wheelRadius) {
        PixelMap.InitializationOptions opts = new PixelMap.InitializationOptions();
        opts.pixelFormat = PixelFormat.ARGB_8888;
        opts.size = new Size(getEstimatedWidth(), getEstimatedHeight());
        mCacheBitmap = PixelMap.create(opts);
        Texture texture = new Texture(mCacheBitmap);
        mCacheCanvas = new Canvas(texture);
        // Draw ring
        mCacheCanvas.drawCircle(centerX, centerY, wheelRadius, mWheelPaint);
    }

    @Override
    public boolean onTouchEvent(Component component, TouchEvent event) {
        MmiPoint point = event.getPointerPosition(event.getIndex());
        float x = point.getX();
        float y = point.getY();
        if (isCanTouch && (event.getAction() == TouchEvent.POINT_MOVE || isTouch(x, y))) {
            // Get the cos angle value through the current touch point
            float cos = computeCos(x, y);
            // Get the angle value by inverse trigonometric function
            double angle;
            if (x < getWidth() / 2) {
                // Slide more than 180 degrees
                angle = Math.PI * RADIAN + Math.acos(cos) * RADIAN;
            } else {
                // No more than 180 degrees
                angle = Math.PI * RADIAN - Math.acos(cos) * RADIAN;
            }

            cos = isScrollOneCirclefunction(angle, cos);
            mCurProcess = getSelectedValue();
            refershWheelCurPosition(cos);
            if (mChangListener != null && (event.getAction() 
                    & (TouchEvent.POINT_MOVE | TouchEvent.PRIMARY_POINT_UP)) > 0) {
                mChangListener.onChanged(this, mCurProcess);
            }
            invalidate();
        }
        return true;
    }

    /**
     * isScrollOneCirclefunction.
     *
     * @param angle angle value
     * @param cos cos value
     * @return cos value
     */
    public float isScrollOneCirclefunction(double angle, float cos) {
        if (isScrollOneCircle) {
            if (mCurAngle > 270 && angle < 90) {
                mCurAngle = 360;
                cos = -1;
            } else if (mCurAngle < 90 && angle > 270) {
                mCurAngle = 0;
                cos = -1;
            } else {
                mCurAngle = angle;
            }
        } else {
            mCurAngle = angle;
        }
        return cos;
    }

    private boolean isTouch(float x, float y) {
        double radius = (getWidth() - getPaddingLeft() - getPaddingRight() + getCircleWidth()) / 2;
        double centerX = getWidth() / 2;
        double centerY = getHeight() / 2;
        return Math.pow(centerX - x, 2) + Math.pow(centerY - y, 2) < radius * radius;
    }

    private float getCircleWidth() {
        return Math.max(mUnreachedWidth, Math.max(mReachedWidth, mPointerRadius));
    }

    private void refershUnreachedWidth() {
        mUnreachedRadius = (getEstimatedWidth() - getPaddingLeft() - getPaddingRight() - mUnreachedWidth) / 2;
    }

    private void refershWheelCurPosition(double cos) {
        mWheelCurX = calcXlocationInWheel(mCurAngle, cos);
        mWheelCurY = calcYlocationInWheel(cos);
    }

    private void refershPosition() {
        mCurAngle = (double) mCurProcess / mMaxProcess * 360.0;
        double cos = -Math.cos(Math.toRadians(mCurAngle));
        refershWheelCurPosition(cos);
    }

    private float calcXlocationInWheel(double angle, double cos) {
        if (angle < 180) {
            return (float) (getEstimatedWidth() / 2 + Math.sqrt(1 - cos * cos) * mUnreachedRadius);
        } else {
            return (float) (getEstimatedWidth() / 2 - Math.sqrt(1 - cos * cos) * mUnreachedRadius);
        }
    }

    private float calcYlocationInWheel(double cos) {
        return getEstimatedWidth() / 2 + mUnreachedRadius * (float) cos;
    }

    /**
     * Get the cos value of the tilt.
     */
    private float computeCos(float x, float y) {
        float width = x - getWidth() / 2;
        float height = y - getHeight() / 2;
        float slope = (float) Math.sqrt(width * width + height * height);
        return height / slope;
    }

    protected Sequenceable onSaveInstanceState() {
        PacMap bundle = new PacMap();
        bundle.putIntValue(INSTANCE_MAX_PROCESS, mMaxProcess);
        bundle.putIntValue(INSTANCE_CUR_PROCESS, mCurProcess);
        bundle.putIntValue(INSTANCE_REACHED_COLOR, mReachedColor);
        bundle.putFloatValue(INSTANCE_REACHED_WIDTH, mReachedWidth);
        bundle.putBooleanValue(INSTANCE_REACHED_CORNER_ROUND, isHasReachedCornerRound);
        bundle.putIntValue(INSTANCE_UNREACHED_COLOR, mUnreachedColor);
        bundle.putFloatValue(INSTANCE_UNREACHED_WIDTH, mUnreachedWidth);
        bundle.putIntValue(INSTANCE_POINTER_COLOR, mPointerColor);
        bundle.putFloatValue(INSTANCE_POINTER_RADIUS, mPointerRadius);
        bundle.putBooleanValue(INSTANCE_POINTER_SHADOW, isHasPointerShadow);
        bundle.putFloatValue(INSTANCE_POINTER_SHADOW_RADIUS, mPointerShadowRadius);
        bundle.putBooleanValue(INSTANCE_WHEEL_SHADOW, isHasWheelShadow);
        bundle.putFloatValue(INSTANCE_WHEEL_SHADOW_RADIUS, mPointerShadowRadius);
        bundle.putBooleanValue(INSTANCE_WHEEL_HAS_CACHE, isHasCache);
        bundle.putBooleanValue(INSTANCE_WHEEL_CAN_TOUCH, isCanTouch);
        bundle.putBooleanValue(INSTANCE_WHEEL_SCROLL_ONLY_ONE_CIRCLE, isScrollOneCircle);
        return bundle;
    }

    protected void onRestoreInstanceState(Sequenceable state) {
        if (state instanceof PacMap) {
            PacMap bundle = (PacMap) state;
            mMaxProcess = bundle.getIntValue(INSTANCE_MAX_PROCESS);
            mCurProcess = bundle.getIntValue(INSTANCE_CUR_PROCESS);
            mReachedColor = bundle.getIntValue(INSTANCE_REACHED_COLOR);
            mReachedWidth = bundle.getFloatValue(INSTANCE_REACHED_WIDTH);
            isHasReachedCornerRound = bundle.getBooleanValue(INSTANCE_REACHED_CORNER_ROUND);
            mUnreachedColor = bundle.getIntValue(INSTANCE_UNREACHED_COLOR);
            mUnreachedWidth = bundle.getFloatValue(INSTANCE_UNREACHED_WIDTH);
            mPointerColor = bundle.getIntValue(INSTANCE_POINTER_COLOR);
            mPointerRadius = bundle.getFloatValue(INSTANCE_POINTER_RADIUS);
            isHasPointerShadow = bundle.getBooleanValue(INSTANCE_POINTER_SHADOW);
            mPointerShadowRadius = bundle.getFloatValue(INSTANCE_POINTER_SHADOW_RADIUS);
            isHasWheelShadow = bundle.getBooleanValue(INSTANCE_WHEEL_SHADOW);
            mPointerShadowRadius = bundle.getFloatValue(INSTANCE_WHEEL_SHADOW_RADIUS);
            isHasCache = bundle.getBooleanValue(INSTANCE_WHEEL_HAS_CACHE);
            isCanTouch = bundle.getBooleanValue(INSTANCE_WHEEL_CAN_TOUCH);
            isScrollOneCircle = bundle.getBooleanValue(INSTANCE_WHEEL_SCROLL_ONLY_ONE_CIRCLE);
            initPaints();
        } 
        if (mChangListener != null) {
            mChangListener.onChanged(this, mCurProcess);
        }
    }

    private int getSelectedValue() {
        return Math.round(mMaxProcess * ((float) mCurAngle / 360));
    }

    public int getCurProcess() {
        return mCurProcess;
    }

    /**
     * setCurProcess to set current Process value.
     *
     * @param curProcess
     *
     */
    public void setCurProcess(int curProcess) {
        this.mCurProcess = curProcess > mMaxProcess ? mMaxProcess : curProcess;
        if (mChangListener != null) {
            mChangListener.onChanged(this, curProcess);
        }
        refershPosition();
        invalidate();
    }

    public int getMaxProcess() {
        return mMaxProcess;
    }

    /**
     * setMaxProcess to set maximum Process value.
     *
     * @param maxProcess
     *
     */
    public void setMaxProcess(int maxProcess) {
        mMaxProcess = maxProcess;
        refershPosition();
        invalidate();
    }

    public int getReachedColor() {
        return mReachedColor;
    }

    /**
     * setReachedColor to set reached colour.
     *
     * @param reachedColor
     *
     */
    public void setReachedColor(int reachedColor) {
        this.mReachedColor = reachedColor;
        Color hmosColor = CircleSeekBar.changeParamToColor(reachedColor);
        mReachedPaint.setColor(hmosColor);
        Color hmosColor1 = CircleSeekBar.changeParamToColor(reachedColor);
        mReachedEdgePaint.setColor(hmosColor1);
        invalidate();
    }

    public int getUnreachedColor() {
        return mUnreachedColor;
    }

    /**
     * setUnreachedColor to set unreached colour.
     *
     * @param unreachedColor
     *
     */
    public void setUnreachedColor(int unreachedColor) {
        this.mUnreachedColor = unreachedColor;
        Color hmosColor = CircleSeekBar.changeParamToColor(unreachedColor);
        mWheelPaint.setColor(hmosColor);
        invalidate();
    }

    public float getReachedWidth() {
        return mReachedWidth;
    }

    /**
     * setReachedWidth to set reached width.
     *
     * @param reachedWidth
     *
     */
    public void setReachedWidth(float reachedWidth) {
        this.mReachedWidth = reachedWidth;
        mReachedPaint.setStrokeWidth(reachedWidth);
        mReachedEdgePaint.setStrokeWidth(reachedWidth);
        invalidate();
    }

    public boolean isHasReachedCornerRound() {
        return isHasReachedCornerRound;
    }

    /**
     * setHasReachedCornerRound.
     *
     * @param hasReachedCornerRound
     *
     */
    public void setHasReachedCornerRound(boolean hasReachedCornerRound) {
        isHasReachedCornerRound = hasReachedCornerRound;
        mReachedPaint.setStrokeCap(hasReachedCornerRound ? Paint.StrokeCap.ROUND_CAP
                : Paint.StrokeCap.BUTT_CAP);
        invalidate();
    }

    public float getUnreachedWidth() {
        return mUnreachedWidth;
    }

    /**
     * setUnreachedWidth to set unreached width.
     *
     * @param unreachedWidth
     *
     */
    public void setUnreachedWidth(float unreachedWidth) {
        this.mUnreachedWidth = unreachedWidth;
        mWheelPaint.setStrokeWidth(unreachedWidth);
        refershUnreachedWidth();
        invalidate();
    }

    public int getPointerColor() {
        return mPointerColor;
    }

    /**
     * setPointerColor to set colour to pointer.
     *
     * @param pointerColor
     *
     */
    public void setPointerColor(int pointerColor) {
        this.mPointerColor = pointerColor;
        Color hmosColor = CircleSeekBar.changeParamToColor(pointerColor);
        mPointerPaint.setColor(hmosColor);
    }

    public float getPointerRadius() {
        return mPointerRadius;
    }

    /**
     * setPointerRadius to set the Pointer radius.
     *
     * @param pointerRadius
     *
     */
    public void setPointerRadius(float pointerRadius) {
        this.mPointerRadius = pointerRadius;
        mPointerPaint.setStrokeWidth(pointerRadius);
        invalidate();
    }

    public boolean isHasWheelShadow() {
        return isHasWheelShadow;
    }

    /**
     * setWheelShadow to set Shadow under the Wheel.
     *
     * @param wheelShadow
     *
     */
    public void setWheelShadow(float wheelShadow) {
        this.mWheelShadowRadius = wheelShadow;
        if (wheelShadow == 0) {
            isHasWheelShadow = false;
            mWheelPaint.clearBlurDrawLooper();
            mCacheCanvas = null;
            mCacheBitmap.release();
            mCacheBitmap = null;
        } else {
            Color hmosColor = CircleSeekBar.changeParamToColor(Color.DKGRAY.getValue());
            BlurDrawLooper blurDrawLooper = new BlurDrawLooper(mWheelShadowRadius, 
                    mDefShadowOffset, mDefShadowOffset, hmosColor);
            mWheelPaint.setBlurDrawLooper(blurDrawLooper);
            setSoftwareLayer();
        }
        invalidate();
    }

    public float getWheelShadowRadius() {
        return mWheelShadowRadius;
    }

    public boolean isHasPointerShadow() {
        return isHasPointerShadow;
    }

    public float getPointerShadowRadius() {
        return mPointerShadowRadius;
    }

    /**
     * setPointerShadowRadius to set Shadow under the pointer.
     *
     * @param pointerShadowRadius
     *
     */
    public void setPointerShadowRadius(float pointerShadowRadius) {
        this.mPointerShadowRadius = pointerShadowRadius;
        if (mPointerShadowRadius == 0) {
            isHasPointerShadow = false;
            mPointerPaint.clearBlurDrawLooper();
        } else {
            Color hmosColor = CircleSeekBar.changeParamToColor(Color.DKGRAY.getValue());
            BlurDrawLooper blurDrawLooper = new BlurDrawLooper(pointerShadowRadius, 
                    mDefShadowOffset, mDefShadowOffset, hmosColor);
            mPointerPaint.setBlurDrawLooper(blurDrawLooper);
            setSoftwareLayer();
        }
        invalidate();
    }

    public void setOnSeekBarChangeListener(OnSeekBarChangeListener listener) {
        mChangListener = listener;
    }

    /**
     * OnSeekBarChangeListener Interface.
     */
    public interface OnSeekBarChangeListener {

        void onChanged(CircleSeekBar seekbar, int curValue);
    }

    public static Color changeParamToColor(int color) {
        return new Color(color);
    }

    public static PixelMapHolder changeParamToPixelMapHolder(PixelMap pixelMap) {
        return new PixelMapHolder(pixelMap);
    }

    public static Arc changeParamToArc(float startAngle, float sweepAngle, boolean useCenter) {
        return new Arc(startAngle, sweepAngle, useCenter);
    }
}
