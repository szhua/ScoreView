package com.szhua.scoreview;


import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;


/**
 * YiBaiEducationExamination
 * Create   2016/12/22 16:19;
 * https://github.com/szhua
 *
 * 显示分数的控件 ；
 * @author sz.hua
 */
public class ScoreView extends View {


    private  static final String LOG_TAG ="Score_View" ;

//    private Paint  firstCirlePaint  ;
//    private Paint secondCirclePaint ;
//    private float circle_width_others =10;

    /*分数的paint*/
    private TextPaint scoreNumberPaint;




    /*整个的圆Paint*/
    private Paint scoreCiclePaint ;
    private float score_circle_width;
    private int total_circle_color ;


   /*实际分数圈*/
    private Paint realScoreCirclePaint;




    /*右下角的绘制*/
    private Paint littleScoreCirlePaint ;
    private Paint littleScoreTextPaint ;
    private static final String fenHanzi ="分";
    private static final int DEFAULT_FEN_TEXT_SIZE =40 ;



    /*总分数*/
    private String totalScore  ;
    /*实际的分数*/
    private String realScore ;
    /*用于动画绘制使用*/
    private float currentScore ;

    private static  final int DEFAULT_SCORE_TEXT_SIZE =100 ;
    private static final int DEFAULT_CIRCLE_WIDTH=12;

    private int width ;
    private int height ;

    private static final float  startAngle =75 ;
    private static final float endAngle =300;




    /*分数的测量数据*/
    private Rect bounds;
    /*分子的测量数据*/
    private Rect boundsLittle;


    public ScoreView(Context context) {
        this(context ,null);
    }
    public ScoreView(Context context, AttributeSet attrs) {
        this(context, attrs ,0);
    }

    public ScoreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context ,attrs) ;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        /*这里以width的测量标准进行分配空间*/
        int width = MeasureSpec.getSize(widthMeasureSpec) ;
        setMeasuredDimension(width,width);

    }

    private void init(Context context , AttributeSet attrSet) {
          TypedArray typeArray =context.obtainStyledAttributes(attrSet,R.styleable.ScoreView);


            realScore=typeArray.getString(R.styleable.ScoreView_score_number);
            totalScore =typeArray.getString(R.styleable.ScoreView_total_score);
            int scoreTextSize = typeArray.getDimensionPixelSize(R.styleable.ScoreView_score_text_size, DEFAULT_SCORE_TEXT_SIZE);
            score_circle_width =typeArray.getDimension(R.styleable.ScoreView_score_circle_width,DEFAULT_CIRCLE_WIDTH);
            int scoreColor = typeArray.getColor(R.styleable.ScoreView_score_text_color, Color.parseColor("#484848"));
            int real_circle_color = typeArray.getColor(R.styleable.ScoreView_score_circl_real_number_color, Color.parseColor("#ed6c44"));
            total_circle_color =typeArray.getColor(R.styleable.ScoreView_score_circle_color, Color.parseColor("#66aade")) ;
            float fenTextSize = typeArray.getDimension(R.styleable.ScoreView_fen_text_size, DEFAULT_FEN_TEXT_SIZE);
            int fenTextColor = typeArray.getColor(R.styleable.ScoreView_fen_text_color, Color.parseColor("#ffffff"));
            int  fenCircleColor = typeArray.getColor(R.styleable.ScoreView_fen_circle_color, Color.parseColor("#01cfd3"));

          typeArray.recycle();




        if(TextUtils.isEmpty(realScore)){
              realScore ="0" ;
          }else{
             try {
                 Float.parseFloat(realScore) ;
             }catch (Exception e){
                 Log.e(LOG_TAG,"Your score must can transform into Float !") ;
             }
          }
         if(TextUtils.isEmpty(totalScore)){
             totalScore ="100" ;
         }else{
             try {
                 Float.parseFloat(totalScore);
             }catch (Exception e){
                 Log.e(LOG_TAG,"Your totalScore must can transform into Float !") ;
             }
         }



        realScoreCirclePaint =new Paint() ;
        realScoreCirclePaint.setColor(real_circle_color);
        realScoreCirclePaint.setAntiAlias(true);
        realScoreCirclePaint.setStrokeWidth(score_circle_width) ;
        realScoreCirclePaint.setStyle(Paint.Style.STROKE);
        realScoreCirclePaint.setStrokeCap(Paint.Cap.ROUND);

        scoreCiclePaint =new Paint() ;
        scoreCiclePaint.setColor(total_circle_color);
        scoreCiclePaint.setAntiAlias(true);
        scoreCiclePaint.setStrokeWidth(score_circle_width) ;
        scoreCiclePaint.setStrokeCap(Paint.Cap.ROUND);
        scoreCiclePaint.setStyle(Paint.Style.STROKE);

        scoreNumberPaint =new TextPaint();
        scoreNumberPaint.setTextSize(scoreTextSize);
        scoreNumberPaint.setColor(scoreColor);
        scoreNumberPaint.setAntiAlias(true);
        scoreNumberPaint.setTypeface(Typeface.SERIF) ;

        littleScoreCirlePaint =new Paint() ;
        littleScoreCirlePaint.setAntiAlias(true);
        littleScoreCirlePaint.setStyle(Paint.Style.FILL);
        littleScoreCirlePaint.setColor(fenCircleColor);

        littleScoreTextPaint=new TextPaint() ;
        littleScoreTextPaint.setAntiAlias(true);
        littleScoreTextPaint.setColor(fenTextColor);
        littleScoreTextPaint.setTextSize(fenTextSize);
        littleScoreTextPaint.setTypeface(Typeface.SERIF) ;



         bounds =new Rect();
        /*绘制右下角的那个文字*/
         boundsLittle =new Rect() ;

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width =getWidth() ;
        height =width;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        /*绘制整体的大圆*/
        RectF rectF =new RectF(score_circle_width,score_circle_width,width-score_circle_width,height-score_circle_width);
        canvas.drawArc(rectF,startAngle,endAngle,false,scoreCiclePaint);

        double total = Double.parseDouble(totalScore);
      //  double real =Double.parseDouble(realScore) ;
        //因为含有STROKE 当为0分的情况下，会进行绘制，所以将0分排除；
        if(currentScore!=0) {
        /*绘制分数圆*/
            canvas.drawArc(rectF, startAngle, (float) (endAngle * currentScore / total), false, realScoreCirclePaint);
        }


       /*绘制中间的分数*/
        scoreNumberPaint.getTextBounds(realScore ,0, realScore.length(), bounds);
        Paint.FontMetricsInt fontMetrics = scoreNumberPaint.getFontMetricsInt();
        int baseline = (height - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        canvas.drawText(realScore,getMeasuredWidth() / 2 - bounds.width() / 2, baseline, scoreNumberPaint);

         /*绘制右下角的那个圆*/

         float rX = (float) ((2+ Math.sqrt(1.5))*width/4);
         float rRadius =(float) ((2- Math.sqrt(2))*width/4);
         canvas.drawCircle(rX,rX,rRadius,littleScoreCirlePaint);

        /*绘制分*/
        littleScoreTextPaint.getTextBounds(fenHanzi ,0, fenHanzi.length(), boundsLittle);
        Paint.FontMetricsInt littleFontMetrics = littleScoreTextPaint.getFontMetricsInt();
        int baselineLittle = (int) ((rX*2 - littleFontMetrics.bottom + littleFontMetrics.top) / 2 - littleFontMetrics.top);
        canvas.drawText(fenHanzi,rX - boundsLittle.width() / 2, baselineLittle, littleScoreTextPaint);

    }

    public void setTotalScore(String totalScore) {
        this.totalScore = totalScore;
     //   invalidate();
    }

    public void setRealScore(String realScore) {
        this.realScore = realScore;
       // invalidate();
    }


    /**
     * 刚进来的时候进行动画绘制；
     */
    public void startAnimation(){
       ValueAnimator valueAnimator =new ValueAnimator() ;
        valueAnimator.setFloatValues(0, Float.parseFloat(realScore));
        AccelerateDecelerateInterpolator bounceInterpolator =new AccelerateDecelerateInterpolator() ;
        valueAnimator.setInterpolator(bounceInterpolator);
        valueAnimator.setDuration(1000) ;
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentScore= (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.start();
    }

}
