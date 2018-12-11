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
import android.widget.Toast;

public class LoginFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
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
        Log.d("REGISTER", "CREATE TABLE");

        _sp = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        _editor = _sp.edit();

        if (!_sp.getString("userId", "").equals("")) {
            initGoHome();
        } else {
            Button _loginBTN = getView().findViewById(R.id.login_loginBTN);
            _loginBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText _userId = getView().findViewById(R.id.login_userId);
                    EditText _password = getView().findViewById(R.id.login_password);
                    String _userIdSTR = _userId.getText().toString();
                    String _passwordSTR = _password.getText().toString();

                    if (_userIdSTR.isEmpty() || _passwordSTR.isEmpty()) {
                        Toast.makeText(getActivity(), "Please fill out this form", Toast.LENGTH_SHORT).show();
                        Log.d("LOGIN", "Please fill out this form");
                    } else {
                        final Cursor _cursor = myDB.rawQuery("SELECT * FROM user", null);
                        boolean check = false;
                        int _idDB = 0;
                        String _userIdDB = "";
                        String _passwordDB = "";
                        String _nameDB = "";
                        while (_cursor.moveToNext()) {
                            _idDB = _cursor.getInt(0);
                            _userIdDB = _cursor.getString(1);
                            _nameDB = _cursor.getString(2);
                            _passwordDB = _cursor.getString(4);
                            if (_userIdDB.equals(_userIdSTR) && _passwordDB.equals(_passwordSTR)) {
                                check = true;
                                break;
                            }
                        }
                        if (check) {
                            _editor.putInt("id", _idDB);
                            _editor.putString("userId", _userIdSTR);
                            _editor.putString("name", _nameDB);
                            _editor.commit();
                            Log.d("LOGIN", "LOGIN COMPLETE");

                            initGoHome();
                        } else {
                            Toast.makeText(getActivity(), "Invalid user or password", Toast.LENGTH_SHORT).show();
                            Log.d("LOGIN", "Invalid user or password");
                        }
                    }

                }
            });
        }

        initRegBTN();

    }

    void initGoHome () {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_view, new HomeFragment())
                .addToBackStack(null)
                .commit();
        Log.d("LOGIN", "GOTO HOME");
    }

    void initRegBTN () {
        TextView _regBTN = getView().findViewById(R.id.login_regBTN);
        _regBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_view, new RegisterFragment())
                        .addToBackStack(null)
                        .commit();
                Log.d("LOGIN", "GOTO REGISTER");
            }
        });
    }
}
