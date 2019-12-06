package dataStructures;

import android.content.Context;
import android.graphics.Color;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.timely.R;

import java.util.ArrayList;

public class Utilities {
    Context context;

    public static final String[] stackedBarChartColors = {
            "#D35400", "#F1C40F", "#27AE60", "#3498DB", "#8E44AD",
            "#34495E", "#BDC3C7", "#E67E22", "#2ECC71", "#16A085",
            "#1ABC9C", "#2980B9", "#9B59B6", "#E74C3C", "#2C3E50"
    };

    public static final String vacantColor = "#edebe9";

    public Utilities(Context c) {
        context = c;
    }

    public void setChart(LinearLayout ll, ArrayList<NamedSegments> values, boolean hasVacant) {
        LayoutInflater inflater = LayoutInflater.from(context);
        for (int i = 0; i < values.size(); i++) {
            NamedSegments currentValue = values.get(i);
            View v = inflater.inflate(R.layout.fragment_viz_segment_view, ll, false);
            v.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, currentValue.getDuration()));
            v.findViewById(R.id.group_viz_segment_color).setBackgroundColor(Color.parseColor(stackedBarChartColors[i % stackedBarChartColors.length]));
            if (hasVacant && i == values.size() - 1) {
                v.findViewById(R.id.group_viz_segment_color).setBackgroundColor(Color.parseColor(vacantColor));
            }
            ((TextView)(v.findViewById(R.id.group_viz_segment_text))).setText(currentValue.getName());
            ll.addView(v);
        }
    }

    public void setChartDynamicLength(LinearLayout ll, ArrayList<NamedSegments> values, int length) {
        String[] colors = {"#FC6451", "#1CBD7D", "#FDD242", "#C056FF"};
        float total = 0;
        for (NamedSegments v : values) {
            total += v.getDuration();
        }
        for (int i = 0; i < values.size(); i++) {
            LinearLayout wrapper = new LinearLayout(this.context);
            wrapper.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            int dp = (int)Math.floor(((values.get(i).getDuration()) / total) * length);
            View v = new View(context);
            v.setLayoutParams(new LinearLayout.LayoutParams(convertDP(dp), convertDP(20)));
            v.setBackgroundColor(Color.parseColor(colors[i % colors.length]));
            TextView txt = new TextView(this.context);
            txt.setText(values.get(i).getName());
            txt.setTextColor(this.context.getColor(R.color.darkMainText));

            wrapper.setOrientation(LinearLayout.VERTICAL);
            wrapper.setGravity(Gravity.START);
            wrapper.addView(v);
            wrapper.addView(txt);
            ll.addView(wrapper);
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

    public void playVibration(VibrationPattern currentVibration) {
        long shortDuration = 300L;
        long longDuration = 700L;
        long delay = 200L;
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = new long[currentVibration.patterns.size()*2];
        pattern[0] = 0;
        for (int i = 1; i <= currentVibration.patterns.size(); i++) {
            VibrationPatternType vp = currentVibration.patterns.get(i-1);
            if (vp == VibrationPatternType.LONG) {
                pattern[2 * i - 1] = longDuration;
                if (i != currentVibration.patterns.size())
                    pattern[2 * i] = delay;
            } else {
                pattern[2 * i - 1] = shortDuration;
                if (i != currentVibration.patterns.size())
                    pattern[2 * i] = delay;
            }
        }
        v.vibrate(VibrationEffect.createWaveform(pattern, -1));
    }
}
