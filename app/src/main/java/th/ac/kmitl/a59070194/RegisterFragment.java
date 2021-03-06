package th.ac.kmitl.a59070194;

import android.content.ContentValues;
import android.content.Context;
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

public class RegisterFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    SQLiteDatabase myDB;
    ContentValues _row = new ContentValues();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        myDB = getActivity().openOrCreateDatabase("my.db", Context.MODE_PRIVATE, null);

        myDB.execSQL(
                "CREATE TABLE IF NOT EXISTS user (_id INTEGER PRIMARY KEY AUTOINCREMENT, userid VARCHAR(12), name VARCHAR(100), age VARCHAR(10), password VARCHAR(12))"
        );
        Log.d("REGISTER", "CREATE TABLE");

        Button _regBTN = getView().findViewById(R.id.reg_regBTN);
        _regBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText _userId = getView().findViewById(R.id.reg_userId);
                EditText _name = getView().findViewById(R.id.reg_name);
                EditText _age = getView().findViewById(R.id.reg_age);
                EditText _password = getView().findViewById(R.id.reg_password);
                String _userIdSTR = _userId.getText().toString();
                String _nameSTR = _name.getText().toString();
                String _ageSTR = _age.getText().toString();
                String _passwordSTR = _password.getText().toString();

                if (checkUserId(_userIdSTR) && checkName(_nameSTR) && checkAge(_ageSTR) && checkPassword(_passwordSTR)) {
                    _row.put("userid", _userIdSTR);
                    _row.put("name", _nameSTR);
                    _row.put("age", _ageSTR);
                    _row.put("password", _passwordSTR);
                    myDB.insert("user", null, _row);
                    Log.d("REGISTER", "INSERT DATA");
                    Toast.makeText(getActivity(), "REGISTER COMPLETE", Toast.LENGTH_SHORT).show();

                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_view, new LoginFragment())
                            .addToBackStack(null)
                            .commit();
                    Log.d("REGISTER", "GOTO LOGIN");

                } else {
                    Toast.makeText(getActivity(), "กรอกข้อมูลไม่ถูกต้อง", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
