package com.camp.bit.todolist;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.camp.bit.todolist.beans.Note;
import com.camp.bit.todolist.beans.State;
import com.camp.bit.todolist.db.TodoContract;
import com.camp.bit.todolist.db.TodoDbHelper;
import com.camp.bit.todolist.debug.DebugActivity;
import com.camp.bit.todolist.ui.NoteListAdapter;

import java.util.Collection;
import java.util.Collections;
import java.util.Currency;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_ADD = 1002;

    private RecyclerView recyclerView;
    private NoteListAdapter notesAdapter;

    //自己新增

    private TodoDbHelper mDbHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDbHelper = new TodoDbHelper(MainActivity.this);
        db = mDbHelper.getWritableDatabase();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(
                        new Intent(MainActivity.this, NoteActivity.class),
                        REQUEST_CODE_ADD);
            }
        });

        recyclerView = findViewById(R.id.list_todo);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        notesAdapter = new NoteListAdapter(new NoteOperator() {
            @Override
            public void deleteNote(Note note) {
                MainActivity.this.deleteNote(note);
            }

            @Override
            public void updateNote(Note note) {
                MainActivity.this.updateNode(note);
            }
        });
        recyclerView.setAdapter(notesAdapter);

        notesAdapter.refresh(loadNotesFromDatabase());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_debug:
                startActivity(new Intent(this, DebugActivity.class));
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD
                && resultCode == Activity.RESULT_OK) {
            notesAdapter.refresh(loadNotesFromDatabase());
        }
    }

    private List<Note> loadNotesFromDatabase() {
        // TODO 从数据库中查询数据，并转换成 JavaBeans
        if(db == null ){
        return Collections.emptyList();
        }
        List<Note> result = new LinkedList<>();
        Cursor cursor =null;
        try{
            cursor =db.query(TodoContract.FeedEntry.TABLE_NAME,
             new String[] {
                     TodoContract.FeedEntry._ID,
                     TodoContract.FeedEntry.COLUMN_DATE,
                     TodoContract.FeedEntry.COLUMN_CONTENT,
                     TodoContract.FeedEntry.COLUMN_STATE},
                    null,
                    null,
                    null,
                    null,
                    TodoContract.FeedEntry.COLUMN_DATE + " DESC");
            while(cursor.moveToNext()){
                int id =cursor.getInt(cursor.getColumnIndex(TodoContract.FeedEntry._ID));
                long dateMs = cursor.getLong(cursor.getColumnIndex(TodoContract.FeedEntry.COLUMN_DATE));
                String content = cursor.getString(cursor.getColumnIndex(TodoContract.FeedEntry.COLUMN_CONTENT));
                int state = cursor.getInt(cursor.getColumnIndex(TodoContract.FeedEntry.COLUMN_STATE));
                Note note = new Note(id);
                note.setContent(content);
                note.setDate(new Date(dateMs));
                note.setState(State.from(state));
                result.add(note);


            }

        }finally {
            if(cursor != null){
                cursor.close();
            }
        }

        return result;
    }

    private void deleteNote(Note note) {
        // TODO 删除数据
    String selection = TodoContract.FeedEntry._ID + " LIKE ?";
    String[] selectionArgs = {note.id+""};
    int deleteRows = db.delete(TodoContract.FeedEntry.TABLE_NAME, selection, selectionArgs);
    notesAdapter.refresh(loadNotesFromDatabase());
    }

    private void updateNode(Note note) {
        // 更新数据
        //String title = "MyNewTitle";
        ContentValues values = new ContentValues();
        values.put(TodoContract.FeedEntry.COLUMN_STATE,note.getState().intValue);  //入口 列名
        String selection = TodoContract.FeedEntry._ID + " LIKE ?";
        String[]  selectionArgs = {note.id+""};
        int count = db.update(
                TodoContract.FeedEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
        notesAdapter.refresh(loadNotesFromDatabase());
    }

}
