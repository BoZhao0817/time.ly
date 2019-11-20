package dataStructures;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class Utilities {
    Context context;

    public Utilities(Context c) {
        context = c;
    }
    public void setChartPreset(LinearLayout l2, ArrayList<Integer> values) {
        String[] colors = {"#013220", "#037d50","#FFFFFF"};
        //int total = 0;
        //for (int v : values) {
        //    total += v;
        //}
        //Log.v("hey",String.valueOf(values.size()));
        int count=0;
        for (int i = 0; i < values.size(); i++) {
            count++;
            //int dp = (int)Math.floor(((values.get(i)*1.0) / total) * length);
            View v = new View(context);
            if(values.get(i)==10)
            {
                v.setLayoutParams(new LinearLayout.LayoutParams(convertDP(40), convertDP(20)));
                v.setBackgroundColor(Color.parseColor(colors[0]));
            }
            else if(values.get(i)==20) {
                v.setLayoutParams(new LinearLayout.LayoutParams(convertDP(80), convertDP(20)));
                v.setBackgroundColor(Color.parseColor(colors[1]));
            }
            View v2 = new View(context);
            v2.setLayoutParams(new LinearLayout.LayoutParams(convertDP(10), convertDP(20)));
            v2.setBackgroundColor(Color.parseColor(colors[2]));
            //Log.v("my",v.toString());
            l2.addView(v);
            l2.addView(v2);
        }
        Log.v("ct",String.valueOf(count));
    }

    public void setChart(LinearLayout ll, ArrayList<Integer> values, int length) {
        String[] colors = {"#FC6451", "#1CBD7D", "#FDD242", "#C056FF"};
        int total = 0;
        for (int v : values) {
            total += v;
        }
        for (int i = 0; i < values.size(); i++) {
            int dp = (int)Math.floor(((values.get(i)*1.0) / total) * length);
            View v = new View(context);
            v.setLayoutParams(new LinearLayout.LayoutParams(convertDP(dp), convertDP(20)));
            v.setBackgroundColor(Color.parseColor(colors[i % colors.length]));
            ll.addView(v);
        }
    }

    public int convertDP(int dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dp*scale+0.5f);
    }

    public int convertPX(int px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)Math.ceil(px / scale);
    }
}
