package com.example.campustaurant;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class FindAccountActivity extends AppCompatActivity {
    EditText searchName, searchStudentNum, searchId, searchNamePw, searchStudentNumPw;
    String tsearchName, tsearchStudentNum, tsearchId, tsearchNamePw, tsearchStudentNumPw;
    Cursor cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_account);

        searchName = (EditText) findViewById(R.id.et_name_id); // 아이디 찾기의 이름 입력 란
        searchStudentNum = (EditText) findViewById(R.id.et_studentId); // 아아디 찾기의 학번 입력 란
        searchId = (EditText) findViewById(R.id.et_id); // 비밀번호 찾기의 아이디 입력 란
        searchNamePw = (EditText) findViewById(R.id.et_name_pwd); // 비밀번호 찾기의 이름 입력 란
        searchStudentNumPw = (EditText) findViewById(R.id.et_student_pwd); // 비밀번호 찾기의 학번 입력 란
        Button searchIdbtn = (Button) findViewById(R.id.btn_findId);
        Button searchPwbtn = (Button) findViewById(R.id.btn_findPwd);
//        searchIdbtn.setOnClickListener(this);
//        searchPwbtn.setOnClickListener(this);
//    }
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.btn_searchid: // 아이디 찾기 버튼
//                tsearchName = searchName.getText().toString();
//                tsearchStudentNum= searchStudentNum.getText().toString();
//
//                if(tsearchName.length() == 0 || tsearchStudentNum.length() == 0){
//                    Toast.makeText(this, "빈칸 없이 모두 입력하세요!", Toast.LENGTH_SHORT).show();
//                    Log.d("minsu", "아이디 찾기 공백 발생");
//                    return;
//                }
//
//                cursor = Database.getInstance().findId(tsearchName, tsearchStudentNum);
//                if(cursor.getCount() != 1){
//                    Toast.makeText(this, "입력한 정보가 존재하지 않습니다!", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                else {
//                    Toast.makeText(this, "아이디는 " + cursor.getString(0) + " 입니다!", Toast.LENGTH_SHORT).show();
//
//                }
//                cursor.close(); // 꼭 닫아주어야 함
//                break;
//
//            case R.id.btn_searchpw: // 비밀번호 찾기 버튼
//                tsearchId = searchId.getText().toString();
//                tsearchNamePw = searchNamePw.getText().toString();
//                tsearchStudentNumPw = searchStudentNumPw.getText().toString();
//
//                if(tsearchId.length() == 0 || tsearchNamePw.length() == 0 || tsearchStudentNumPw.length() == 0){
//                    Toast.makeText(this, "빈칸 없이 모두 입력하세요!", Toast.LENGTH_SHORT).show();
//                    Log.d("minsu", "비밀번호 찾기 공백 발생");
//                    return;
//                }
//
//                cursor = Database.getInstance().findPw(tsearchId, tsearchNamePw, tsearchStudentNumPw);
//                if(cursor.getCount() != 1){
//                    Toast.makeText(this, "입력한 정보가 존재하지 않습니다!", Toast.LENGTH_SHORT).show();
//                    return;
//
//                }
//                else {
//                    Toast.makeText(this, "비밀번호는 " + cursor.getString(0) + " 입니다!", Toast.LENGTH_SHORT).show();
//
//                }
//
//                cursor.close(); // 꼭 닫아주어야 함
//                break;

//        }
//
    }


}