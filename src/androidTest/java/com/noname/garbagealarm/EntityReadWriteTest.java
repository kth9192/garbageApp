package com.noname.garbagealarm;

import android.content.Context;

import com.taehoon.garbagealarm.repository.AppDatabase;
import com.taehoon.garbagealarm.repository.memorepository.MemoDao;
import com.taehoon.garbagealarm.repository.memorepository.MemoRoom;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;


@RunWith(AndroidJUnit4.class)
public class EntityReadWriteTest {
    private MemoDao memoDao;
    private AppDatabase appDatabase;

    @Before
    public void createDB(){
        Context context = ApplicationProvider.getApplicationContext();
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        memoDao = appDatabase.chatDao();
    }

    @After
    public void closeDB() throws IOException{
        appDatabase.close();
    }

    @Test
    public void writeAndRead() throws IOException{

        MemoRoom memoRoom = new MemoRoom(UUID.randomUUID().toString(), "TESTDAY",
                "TESTMEMO" , "0" , "TESTTAG");
        memoDao.insert(memoRoom);
    }
}
