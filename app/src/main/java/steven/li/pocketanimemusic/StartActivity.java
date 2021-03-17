package steven.li.pocketanimemusic;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import API.AnilistAPI;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    Button getAnilistBtn;
    EditText anilistPseudo;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        getAnilistBtn = findViewById(R.id.getAnilistPseudoBtn);
        getAnilistBtn.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        anilistPseudo = findViewById(R.id.anilistPseudo);
        new AnilistAPI(this).checkUserExist(anilistPseudo.getText().toString());
    }
}