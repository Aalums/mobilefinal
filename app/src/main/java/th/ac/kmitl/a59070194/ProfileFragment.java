package th.ac.kmitl.a59070194;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    SQLiteDatabase myDB;
    ContentValues _row = new ContentValues();

    SharedPreferences _sp;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        myDB = getActivity().openOrCreateDatabase("my.db", Context.MODE_PRIVATE, null);

        myDB.execSQL(
                "CREATE TABLE IF NOT EXISTS user (_id INTEGER PRIMARY KEY AUTOINCREMENT, userid VARCHAR(12), name VARCHAR(100), age VARCHAR(10), password VARCHAR(12))"
        );
        Log.d("PROFILE", "CREATE TABLE");

        Button _saveBTN = getView().findViewById(R.id.profile_saveBTN);
        _saveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText _userId = getView().findViewById(R.id.profile_userId);
                EditText _name = getView().findViewById(R.id.profile_name);
                EditText _age = getView().findViewById(R.id.profile_age);
                EditText _password = getView().findViewById(R.id.profile_password);
                EditText _quote = getView().findViewById(R.id.profile_quote);
                String _userIdSTR = _userId.getText().toString();
                String _nameSTR = _name.getText().toString();
                String _ageSTR = _age.getText().toString();
                String _passwordSTR = _password.getText().toString();
                String _quoteSTR = _quote.getText().toString();

                _sp = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
                int _idDB = _sp.getInt("id", 0);

                if (checkUserId(_userIdSTR) && checkName(_nameSTR) && checkAge(_ageSTR) && checkPassword(_passwordSTR)) {
                    _row.put("userid", _userIdSTR);
                    _row.put("name", _nameSTR);
                    _row.put("age", _ageSTR);
                    _row.put("password", _passwordSTR);
                    myDB.update("user", _row, "_id="+_idDB, null);
                    Log.d("PROFILE", "UPDATE DATA");
                    Toast.makeText(getActivity(), "UPDATE COMPLETE", Toast.LENGTH_SHORT).show();

                    initFile(_quoteSTR);

                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_view, new HomeFragment())
                            .addToBackStack(null)
                            .commit();
                    Log.d("PROFILE", "GOTO HOME");

                } else {
                    Toast.makeText(getActivity(), "กรอกข้อมูลไม่ถูกต้อง", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    void initFile (String _quoteSTR) {
        try {
            String _fileName = "quote.txt";
            FileOutputStream _fileOut = getActivity().openFileOutput(_fileName, Context.MODE_PRIVATE);
            _fileOut.write(_quoteSTR.getBytes());
            _fileOut.close();
        } catch (FileNotFoundException e) {

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean checkUserId (String _userIdSTR) {
        boolean check = false;
        if (_userIdSTR.length() >= 6 && _userIdSTR.length() <= 12) {
            check = true;
        }
        return check;
    }

    public boolean checkName (String _nameSTR) {
        boolean check = false;
        String[] _name = _nameSTR.split(" ");
        if (_name.length == 2) {
            check = true;
        }
        return check;
    }

    public boolean checkAge (String _ageSTR) {
        boolean check = false;
        try {
            int _age = Integer.valueOf(_ageSTR);
            if (_age >= 10 && _age <= 80 ) {
                check = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return check;
    }

    public boolean checkPassword (String _passwordSTR) {
        boolean check = false;
        if (_passwordSTR.length() > 6) {
            check = true;
        }
        return check;
    }

}
