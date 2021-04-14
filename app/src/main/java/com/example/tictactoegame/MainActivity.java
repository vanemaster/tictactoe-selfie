package com.example.tictactoegame;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    int x=1;
    String winner="";
    int win=0;
    int[][] win_state={{0,1,2},{3,4,5},{6,7,8},{0,4,8},{2,4,6},{0,3,6},{1,4,7},{2,5,8}};
    int[] game_state={2,2,2,2,2,2,2,2,2};
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    Uri currentPhoto1;
    Uri currentPhoto2;
    String currentPlayer = "";
    Uri firstPlayerPicture;
    Uri secondPlayerPicture;
    int currentID;
    public void play(View view)
    {
        winner="";
        win=0;
        x=1;
        Button button=(Button) findViewById(R.id.button2);
        TextView te=(TextView) findViewById(R.id.textView4);
        button.setVisibility(View.INVISIBLE);
        te.setVisibility(View.INVISIBLE);
        ImageView i1=(ImageView) findViewById(R.id.imageView1);
        ImageView i2=(ImageView) findViewById(R.id.imageView2);
        ImageView i3=(ImageView) findViewById(R.id.imageView3);
        ImageView i4=(ImageView) findViewById(R.id.imageView4);
        ImageView i5=(ImageView) findViewById(R.id.imageView5);
        ImageView i6=(ImageView) findViewById(R.id.imageView6);
        ImageView i7=(ImageView) findViewById(R.id.imageView7);
        ImageView i8=(ImageView) findViewById(R.id.imageView8);
        ImageView i9=(ImageView) findViewById(R.id.imageView9);
        i1.setImageDrawable(null);
        i2.setImageDrawable(null);
        i3.setImageDrawable(null);
        i4.setImageDrawable(null);
        i5.setImageDrawable(null);
        i6.setImageDrawable(null);
        i7.setImageDrawable(null);
        i8.setImageDrawable(null);
        i9.setImageDrawable(null);

        for(int i=0; i<9; i++)
        {
            game_state[i]=2;
        }
    }
    public boolean draw()
    {
        boolean d=true;
        for(int i=0;i<9;i++)
        {
            if(game_state[i]==2)
            {
                d=false;
                break;
            }
        }
        return d;
    }

    public void click(View view)
    {
        //X=0; O=1; Empty=2;
        ImageView c = (ImageView) view;

        if(currentPhoto1 == null || currentPhoto2 == null){
            Toast.makeText(getApplicationContext(), "Tire as selfies antes!", Toast.LENGTH_SHORT).show();
        }else if (currentPlayer == ""){
            Toast.makeText(getApplicationContext(), "Quem começa? Clique na sua foto para definir.", Toast.LENGTH_SHORT).show();
        }else if (currentPhoto1 != null && currentPhoto2 != null && currentPlayer != ""){
            int t=Integer.parseInt(c.getTag().toString());
            int a,b,d;

//          Player 1
            if (x==1 && game_state[t]==2) {

                c.setImageURI(firstPlayerPicture);
                Log.i("Player 1", String.valueOf(1));
                c.animate().alpha(1).setDuration(500);
                x=0;
                game_state[t]=0;
            }
//          Player 2
            else if(x==0 && game_state[t]==2)
            {
                c.setImageURI(secondPlayerPicture);
                Log.i("Player 2", String.valueOf(2));
                c.animate().alpha(1).setDuration(500);
                x=1;
                game_state[t]=1;
            }

            else if(game_state[t]==3)
            {
                Toast.makeText(this, "Fim! Jogue novamente!", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this, "Caixa ocupada! Tente outra!", Toast.LENGTH_SHORT).show();
            }
            Log.i("state", Arrays.toString(game_state));

            for(int i=0;i<8;i++)
            {
                a=win_state[i][0];
                b=win_state[i][1];
                d=win_state[i][2];
                if((game_state[a]==0 && game_state[b]==0 && game_state[d]==0) || (game_state[a]==1 && game_state[b]==1 && game_state[d]==1))
                {
                    if (game_state[a]==0) {
                        winner="Player 1";
                    }
                    else{
                        winner="Player 2";
                    }
                    for(int j=0;j<9;j++)
                    {
                        game_state[j]=3;
                    }
                    win=1;
                    Button button=(Button) findViewById(R.id.button2);
                    TextView te=(TextView) findViewById(R.id.textView4);
                    button.setVisibility(View.VISIBLE);
                    winner=winner+" venceu";
                    te.setText(winner);
                    te.setVisibility(View.VISIBLE);
                }
                if(draw() && win==0)
                {
                    Button button=(Button) findViewById(R.id.button2);
                    TextView te=(TextView) findViewById(R.id.textView4);
                    button.setVisibility(View.VISIBLE);
                    winner="Empate! Jogue novamente!";
                    te.setText(winner);
                    te.setVisibility(View.VISIBLE);
                }
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void getFirstPlayer(View view){
        if(currentPhoto1 == null || currentPhoto2 == null){
            dispatchTakePictureIntent();
        }else{
            currentPlayer = view.getResources().getResourceEntryName(view.getId());

            if(currentPlayer.equals("imageButton")){
                firstPlayerPicture = currentPhoto1;
                secondPlayerPicture = currentPhoto2;
            }
            if(currentPlayer.equals("imageButton2")){
                firstPlayerPicture = currentPhoto2;
                secondPlayerPicture = currentPhoto1;
            }

            Log.i("button_click",currentPlayer);

//            Log.i("currentPlayerPicture", String.valueOf(currentPlayerPicture));
//            Log.i("currentPlayerPicture", String.valueOf(currentPlayerPicture==2));
        }

        currentID = view.getId();
    }

    public void dispatchTakePictureIntent(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            if (currentPhoto1 == null){
                currentPhoto1 = getImageUri(getApplicationContext(), imageBitmap);
            }else{
                currentPhoto2 = getImageUri(getApplicationContext(), imageBitmap);
            }

            ImageButton photoButton = (ImageButton) this.findViewById(currentID);
            photoButton.setImageBitmap(imageBitmap);

        }
    }

}