package com.zoomw.zoom.features.chat;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

//메시지 관리, 출력 어떻게 해줄지 관리 BaseAdapter를 상속하고 있음. -어뎁터는 list 보여주기 위해 사용
public class ChatTextAdapter extends BaseAdapter {
    private Context context;
    private List<String> chatTextList = new ArrayList<>();

    public ChatTextAdapter(Context context){
        this.context = context;
    }
    public void addMessage(String message){
        this.chatTextList.add(message);
    }
    @Override
    public int getCount(){
        return this.chatTextList.size();
    }
    @Override
    public Object getItem(int position) {
        return this.chatTextList.get(position);
    }//position은 n번째

    @Override
    public long getItemId(int position){
        return position;
    }//특별한 이름 달라고 하기

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        TextView message = new TextView(this.context);
        message.setText(this.chatTextList.get(position));
        return message;
    }//내가 가지고 있는 데이터를 보여주는 화면 구성
}
