package br.com.caelum.activity;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import br.com.caelum.view.Scheduler;
import br.com.caelum.view.Scheduler.ScheduleListener;
import br.com.caelum.view.Scheduler.SchedulerConfig;

public class PopUpTest extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        final List<String> banners = Arrays.asList(
        		"Some important stuff", "How are you?", "Got milk?",
        		"If 6 turn out to be 9 I don't mind...", "Just do it... or not");
        final Random random = new Random();
        
        SlidingDrawer sliding = (SlidingDrawer) findViewById(R.id.slidingDrawerPopup);
        
        Scheduler scheduler = new Scheduler(this, sliding, new ScheduleListener() {
			@Override
			public void beforeOpenPopup(SlidingDrawer sliding) {
				TextView banner = (TextView) sliding.findViewById(R.id.bannerText);
				banner.setText(banners.get(random.nextInt(banners.size())));
			}
		});
        
        SchedulerConfig config = new SchedulerConfig().initialDelay(1).period(5).expiresAfter(60);
        
		scheduler.start(config);
    }
}