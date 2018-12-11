package th.ac.kmitl.a59070194;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    SQLiteDatabase myDB;

    SharedPreferences _sp;
    SharedPreferences.Editor _editor;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        myDB = getActivity().openOrCreateDatabase("my.db", Context.MODE_PRIVATE, null);

        myDB.execSQL(
                "CREATE TABLE IF NOT EXISTS user (_id INTEGER PRIMARY KEY AUTOINCREMENT, userid VARCHAR(12), name VARCHAR(100), age VARCHAR(10), password VARCHAR(12))"
        );
        Log.d("PROFILE", "CREATE TABLE");

        TextView _name = getView().findViewById(R.id.home_name);
        TextView _quote = getView().findViewById(R.id.home_quote);

        final Cursor _cursor = myDB.rawQuery("SELECT * FROM user", null);
        int _idDB = 0;
        while (_cursor.moveToNext()) {
            _idDB = _cursor.getInt(0);
            if (_idDB == _sp.getInt("id", 0)) {
                _name.setText(_sp.getString("name", ""));
                break;
            }
        }

        _sp = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        _editor = _sp.edit();

        try {
            String _fileName = "quote.txt";
            FileInputStream _fileIn = getContext().openFileInput(_fileName);
            String _content = "";
            byte[] _readByte = new byte[_fileIn.available()];
            while(_fileIn.read(_readByte) != -1){
                _content = new String(_readByte);
            }
            _fileIn.close();
            _quote.setText(_content);
        } catch (FileNotFoundException e) {

        } catch (IOException e) {
            e.printStackTrace();
        }

        initProfileSetupBTN();
        initMyFriendBTN();
        initSignoutBTN();

    }

    void initProfileSetupBTN () {
        Button _profileSetupBTN = getView().findViewById(R.id.home_profileSetupBTN);
        _profileSetupBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_view, new ProfileFragment())
                        .addToBackStack(null)
                        .commit();
                Log.d("HOME", "GOTO PROFILESETUP");
            }
        });
    }

    void initMyFriendBTN () {
        Button _myFriendBTN = getView().findViewById(R.id.home_myFriendBTN);
        _myFriendBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_view, new LoginFragment())
                        .addToBackStack(null)
                        .commit();
                Log.d("HOME", "GOTO FRIEND");
            }
        });
    }

    void initSignoutBTN () {
        Button _signoutBTN = getView().findViewById(R.id.home_signoutBTN);
        _signoutBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _sp = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
                _editor = _sp.edit();
                _editor.clear();
                _editor.commit();
                Log.d("HOME", "CLEAR SP");

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_view, new LoginFragment())
                        .addToBackStack(null)
                        .commit();
                Log.d("HOME", "GOTO LOGIN");
            }
        });
    }
}
