package tools;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class AdvRDAdjuster {
	public static float _scale;
	
	public static void setScale(View wrapper, View device){
	 	_scale = Math.min((float)device.getWidth() / wrapper.getWidth(), (float)device.getHeight() / wrapper.getHeight());
	 	System.out.println(_scale);
	}
	
	public static void adjust(View wrapper){
		
		// adjust layout
	 	ViewGroup.LayoutParams layoutParams = wrapper.getLayoutParams();
	 	layoutParams.width *= layoutParams.width != ViewGroup.LayoutParams.MATCH_PARENT && layoutParams.width != ViewGroup.LayoutParams.WRAP_CONTENT ? _scale : 1;
	 	layoutParams.height *= layoutParams.height != ViewGroup.LayoutParams.MATCH_PARENT && layoutParams.height != ViewGroup.LayoutParams.WRAP_CONTENT ? _scale : 1;
	 	System.out.println("!!!!!!!!!!!!"+layoutParams.height);
	 	
	 	if (layoutParams instanceof ViewGroup.MarginLayoutParams){
	 		ViewGroup.MarginLayoutParams margin = (ViewGroup.MarginLayoutParams)layoutParams;
	 		margin.leftMargin *= _scale;
	 		margin.rightMargin *= _scale;
	 		margin.topMargin *= _scale;
	 		margin.bottomMargin *= _scale;
	 	}	 	
	 	wrapper.setLayoutParams(layoutParams);	
	 	wrapper.setPadding((int)(wrapper.getPaddingLeft() * _scale), (int)(wrapper.getPaddingTop() * _scale), (int)(wrapper.getPaddingRight() * _scale), (int)(wrapper.getPaddingBottom() * _scale));

	 	if (wrapper instanceof TextView){
	 		TextView txt = (TextView)wrapper;
	 		txt.setTextSize(txt.getTextSize() * _scale/2 + txt.getTextSize()/4);
	 	}
	 	
	 	if (wrapper instanceof ViewGroup){
	 		ViewGroup views = (ViewGroup)wrapper;
	 		for (int i = 0; i < views.getChildCount(); ++i){
	 			adjust(views.getChildAt(i));
	 		}	 			
	 	}
	 	
	}

}

