package ap.test.com.androidproject;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;

import ap.test.com.androidproject.Decorator.EventDecorator;
import ap.test.com.androidproject.Decorator.HighlightWeekendsDecorator;
import ap.test.com.androidproject.Decorator.OneDayDecorator;
import ap.test.com.androidproject.Decorator.SaturdayDecorator;
import ap.test.com.androidproject.Decorator.SundayDecorator;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by USER on 2017-07-13.
 */

public class StartActivity extends AppCompatActivity{

    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();

    /*@BindView(R.id.calendarView)
    MaterialCalendarView materialCalendarView;*/
    MaterialCalendarView materialCalendarView;

    @Override
    public void onCreate(Bundle saveInstanceState){
        //materialCalendarView = new MaterialCalendarView(this);

        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_start);
        //ButterKnife.bind(this);
        materialCalendarView = (MaterialCalendarView) findViewById(R.id.calendarView);
        materialCalendarView.setShowOtherDates(MaterialCalendarView.SHOW_ALL);

        Calendar instance = Calendar.getInstance();
        materialCalendarView.setSelectedDate(instance.getTime());

        Calendar instance1 = Calendar.getInstance();
        instance1.set(instance1.get(Calendar.YEAR), Calendar.JANUARY, 1);

        Calendar instance2 = Calendar.getInstance();
        instance2.set(instance2.get(Calendar.YEAR), Calendar.DECEMBER, 31);

        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(1900,01,01))
                .setMaximumDate(CalendarDay.from(2200,12,31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                oneDayDecorator.setDate(date.getDate());
                widget.invalidateDecorators();
                //Toast.makeText(StartActivity.this, "" + date, Toast.LENGTH_SHORT).show();
            }
        });
        materialCalendarView.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator(),
                new HighlightWeekendsDecorator(),
                oneDayDecorator
        );

        new ApiSimulator().executeOnExecutor(Executors.newSingleThreadExecutor());

    }

    private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {
        boolean mFinished;
        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -2);
            ArrayList<CalendarDay> dates = new ArrayList<>();
            for (int i = 0; i < 30; i++) {
                CalendarDay day = CalendarDay.from(calendar);
                dates.add(day);
                calendar.add(Calendar.DATE, 5);
            }

            return dates;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            if (isFinishing()) {
                return;
            }

            materialCalendarView.addDecorator(new EventDecorator(Color.RED, calendarDays));
        }

        public boolean isFinishing() {
            return mFinished;
        }
    }
}


