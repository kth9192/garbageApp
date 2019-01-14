package com.noname.garbagealarm;

import android.app.AlarmManager;
import android.content.Context;
import android.test.InstrumentationTestCase;
import android.test.mock.MockContext;

import com.taehoon.garbagealarm.viewmodel.AlarmLogic;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Array;
import java.util.ArrayList;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class AlarmUnitTest {

    @Mock
    Context context;

    @Mock
    AlarmManager alarmManager;
    private AlarmLogic alarmLogic;

    @Before
    public void init() {
        Mockito.when(context.getSystemService(Context.ALARM_SERVICE)).thenReturn(alarmManager);
         alarmLogic = new AlarmLogic(context);
    }

    @Test
    public void convertAlarmTimeTest() {
//        Mockito.when(alarmLogic.convertAlarmTime(12, 0)).thenReturn(Long.valueOf(120000));
        assertEquals(1200, alarmLogic.convertAlarmTime(12, 0));
    }

    @Test
    public void dayConvertToIntTest() {
        ArrayList<String> week = new ArrayList<>();
        week.add("일");
        week.add("월");

        assertEquals(1, alarmLogic.dayConvertToInt(week.get(0)));
    }

    @Test
    public void convertDayOfWeekTest() {
        ArrayList<String> week = new ArrayList<String>();
        week.add("일");
        week.add("월");

        for (int tmp : alarmLogic.convertDayOfWeek(week)) {
            if (week.get(0).equals("일")) {
                assertEquals(1, tmp);
            } else {
                assertEquals(2, tmp);
            }
        }
    }
}