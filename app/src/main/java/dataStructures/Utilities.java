package dataStructures;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class Utilities {
    Context context;

    public Utilities(Context c) {
        context = c;
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
