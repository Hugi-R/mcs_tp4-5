package com.example.aliceprobst.mcs;

import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;

public class ResultsActivity extends AppCompatActivity {

    private enum Type {DTW, KppV};
    private String DTW = "DTW";
    private Float DTWrate;
    private String KppV = "KppV";
    private Float KppVrate;

    private Type winner;

    private TextView labelWinner;
    private TextView tauxWinner;

    private TextView labelLooser;
    private TextView tauxLooser;

    //données traitées
    private HashMap<String, HashMap<String, Integer>> confusionMatrixDTW = new HashMap<>();
    private Float rateDTW;
    private HashMap<String, HashMap<String, Integer>> confusionMatrixKppV = new HashMap<>();
    private Float rateKppV;

    public void showWinnerDetail(View view){
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, view, "transition");

        Intent intent = new Intent(this, DetailActivity.class);

        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");

        if (winner == Type.DTW) {
            //TODO: add DTW info a l'intent
            intent.putExtra("name", DTW);
            intent.putExtra("rate", rateDTW);
            intent.putExtra("matrix", confusionMatrixDTW);
        } else {
            //TODO: add KppV info a l'intent
            intent.putExtra("name", KppV);
            intent.putExtra("rate", rateKppV);
            intent.putExtra("matrix", confusionMatrixKppV);
        }
        ActivityCompat.startActivity(this, intent, options.toBundle());
    }

    public void showLooserDetail(View view){
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, view, "transition");

        Intent intent = new Intent(this, DetailActivity.class);

        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");

        if (winner == Type.KppV) {
            //TODO: add DTW info a l'intent
            intent.putExtra("name", DTW);
            intent.putExtra("rate", rateDTW);
            intent.putExtra("matrix", confusionMatrixDTW);
        } else {
            //TODO: add KppV info a l'intent
            intent.putExtra("name", KppV);
            intent.putExtra("rate", rateKppV);
            intent.putExtra("matrix", confusionMatrixKppV);
        }

        ActivityCompat.startActivity(this, intent, options.toBundle());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        labelWinner = findViewById(R.id.labelWinner);
        //labelWinner.setTextSize();
        tauxWinner = findViewById(R.id.tauxWinner);
        labelLooser = findViewById(R.id.labelLooser);
        tauxLooser = findViewById(R.id.tauxLooser);

        //TODO: appeler les fonctions de calcul et init les taux et matrices

        rateDTW = (float) 5;
        confusionMatrixDTW.put("cmd1", new HashMap<String, Integer>());
            confusionMatrixDTW.get("cmd1").put("cmd1", 0);
            confusionMatrixDTW.get("cmd1").put("cmd2", 0);
            confusionMatrixDTW.get("cmd1").put("cmd3", 0);
        confusionMatrixDTW.put("cmd2", new HashMap<String, Integer>());
            confusionMatrixDTW.get("cmd2").put("cmd1", 0);
            confusionMatrixDTW.get("cmd2").put("cmd2", 0);
            confusionMatrixDTW.get("cmd2").put("cmd3", 0);
        confusionMatrixDTW.put("cmd3", new HashMap<String, Integer>());
            confusionMatrixDTW.get("cmd3").put("cmd1", 0);
            confusionMatrixDTW.get("cmd3").put("cmd2", 0);
            confusionMatrixDTW.get("cmd3").put("cmd3", 0);
        rateKppV = (float) 4;
        confusionMatrixKppV = confusionMatrixDTW;


        //comparer taux et definir le winner
        if (rateDTW > rateKppV)
            winner = Type.DTW;
        else
            winner = Type.KppV;

        //afficher resultat dans les parties winner et looser (winner en haut)
        switch (winner){
            case DTW:
                labelWinner.setText(DTW);
                tauxWinner.setText(String.valueOf(DTWrate));
                labelLooser.setText(KppV);
                tauxLooser.setText(String.valueOf(KppVrate));
                break;
            case KppV:
                labelWinner.setText(KppV);
                tauxWinner.setText(String.valueOf(KppVrate));
                labelLooser.setText(DTW);
                tauxLooser.setText(String.valueOf(DTWrate));
                break;
        }
    }
}
