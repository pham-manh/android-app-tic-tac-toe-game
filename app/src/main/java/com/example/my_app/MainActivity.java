package com.example.my_app;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    static final int[][] WINNING_POSITIONS = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
            {0, 4, 8}, {2, 4, 6}
    };
    static final Player[] DEFAULT_GAME_STATE = {
            Player.EMPTY, Player.EMPTY, Player.EMPTY,
            Player.EMPTY, Player.EMPTY, Player.EMPTY,
            Player.EMPTY, Player.EMPTY, Player.EMPTY
    };
    Player[] gameState = Arrays.copyOf(DEFAULT_GAME_STATE, DEFAULT_GAME_STATE.length);
    Player activePlayer = Player.X;
    boolean isGameActive = true;

    public boolean isDrawGame(@NonNull Player[] gameState) {
        for (Player position : gameState
        ) {
            if (position == Player.EMPTY) {
                return false;
            }
        }
        return true;
    }

    @SuppressLint("SetTextI18n")
    public void dropIn(View view) {
        TextView notifyTxt = findViewById(R.id.txtWinnerNotif);
        Button btn_resetGame = findViewById(R.id.btnResetGame);
        ImageView counter = (ImageView) view;

        int tappedCounter = Integer.parseInt(counter.getTag().toString());

        if (gameState[tappedCounter] == Player.EMPTY && isGameActive) {
            gameState[tappedCounter] = activePlayer;

            if (activePlayer == Player.X) {
                counter.setImageResource(R.drawable.x);
                activePlayer = Player.O;
                notifyTxt.setText("Turn " + Player.O);
            } else {
                counter.setImageResource(R.drawable.o);
                activePlayer = Player.X;
                notifyTxt.setText("Turn " + Player.X);
            }

            counter.setTranslationY(-1500);
            counter.animate().translationYBy(1500).setDuration(500);

            for (int[] winningPosition : WINNING_POSITIONS
            ) {
                String winner;
                if (gameState[winningPosition[0]] == gameState[winningPosition[1]]
                        && gameState[winningPosition[1]] == gameState[winningPosition[2]]
                        && gameState[winningPosition[0]] != Player.EMPTY
                ) {
                    winner = activePlayer != Player.X ? "X" : "O";
                    notifyTxt.setText(winner + " " + getString(R.string.winner_notification));
                    isGameActive = false;
                    btn_resetGame.setVisibility(View.VISIBLE);
                    break;
                }
            }

            if (isGameActive && isDrawGame(gameState)) {
                btn_resetGame.setVisibility(View.VISIBLE);
                notifyTxt.setText(R.string.draw_notification);
            }
        }

    }

    public void resetNewGame(View view) {
        GridLayout gridLayout = findViewById(R.id.gridLayout);
        Button btn_resetGame = findViewById(R.id.btnResetGame);
        TextView notifyTxt = findViewById(R.id.txtWinnerNotif);
        int childCount = gridLayout.getChildCount();

        for (int i = 0; i < childCount; i++) {
            View childView = gridLayout.getChildAt(i);

            if (childView instanceof ImageView) {
                ImageView imageView = (ImageView) childView;
                imageView.setImageDrawable(null);
            }
        }
        Arrays.fill(gameState, Player.EMPTY);
        notifyTxt.setText("");
        btn_resetGame.setVisibility(View.INVISIBLE);
        isGameActive = true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}