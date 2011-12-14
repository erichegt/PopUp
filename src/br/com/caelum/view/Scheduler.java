package br.com.caelum.view;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.widget.SlidingDrawer;

public class Scheduler {
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	private final SlidingDrawer sliding;
	private final Activity ctx;
	private final ScheduleListener listener;

	public Scheduler(Activity ctx, SlidingDrawer sliding, ScheduleListener listener) {
		this.sliding = sliding;
		this.ctx = ctx;
		this.listener = listener;
	}
	
	public void start() {
		this.start(new SchedulerConfig());
	}
	
	public void start(SchedulerConfig config) {
		
		final Runnable taskToSchedule = new Runnable() {
			@Override
			public void run() {
				ctx.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (sliding.isOpened()) {
							sliding.animateToggle();
						} else {
							listener.beforeOpenPopup(sliding);
							sliding.animateOpen();
						}
					}
				});
			}
		};
		
		final ScheduledFuture<?> schedule = scheduler.scheduleAtFixedRate(
				taskToSchedule, config.initialDelay, config.period, config.timeUnit);

		Runnable executeWhenExpires = new Runnable() {
			public void run() { schedule.cancel(true); }
		};
		
		scheduler.schedule(executeWhenExpires, config.expitionTime, config.timeUnit);
	}
	
	public interface ScheduleListener {
		void beforeOpenPopup(SlidingDrawer sliding);
	}
	
	public static class SchedulerConfig{
		private long initialDelay = 1;
		private long period = 3;
		private TimeUnit timeUnit = TimeUnit.SECONDS;
		private long expitionTime = 60;
		
		public SchedulerConfig initialDelay(long delay) {
			this.initialDelay = delay;
			return this;
		}
		
		public SchedulerConfig period(long period) {
			this.period = period;
			return this;
		}
		
		public SchedulerConfig timeUnit(TimeUnit timeUnit) {
			this.timeUnit = timeUnit;
			return this;
		}
		
		public SchedulerConfig expiresAfter(long expirationTime) {
			this.expitionTime = expirationTime;
			return this;
		}
	}
}
