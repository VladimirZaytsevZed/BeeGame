package com.volodia.beegame;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.volodia.beegame.Engine.DRONE_BEE_COUNT;
import static com.volodia.beegame.Engine.WORKER_BEE_COUNT;

public class GameActivity extends AppCompatActivity {

    Engine engine;
    @BindView(R.id.fl_container)
    FrameLayout fl_container;

    @BindView(R.id.ll_drone_bees_container)
    LinearLayout ll_drone_bees_container;

    @BindView(R.id.ll_worker_bees_container)
    LinearLayout ll_worker_bees_container;

    @BindView(R.id.ll_container)
    LinearLayout ll_container;

    @BindView(R.id.ll_message)
    LinearLayout ll_message;

    @BindView(R.id.tv_count_down)
    TextView tv_count_down;

    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        engine = new Engine(this);
        engine.setBees(createBees());
    }

    @OnClick(R.id.bt_start)
    public void onStartClick(){
        if (!engine.gameInProgress()) {
            if (countDownTimer != null) countDownTimer.cancel();
            showGameView();
        }
        engine.onStartClick();
    }

    @OnClick(R.id.bt_hit)
    public void onHitClick(){
        if (!engine.gameInProgress()) {
            Toast.makeText(this, R.string.game_is_not_in_progress, Toast.LENGTH_SHORT).show();
            return;
        }
        engine.onHitClick();
    }

    public List<IBee> createBees() {
        List<IBee> bees = new ArrayList<>();
        for (int i = 0; i < DRONE_BEE_COUNT; i++) {
            BeeView beeView = new BeeView(this, BeeFactory.createBee(BeeType.DRONE));
            ll_drone_bees_container.addView(beeView);
            bees.add(beeView);
        }
        for (int i = 0; i < WORKER_BEE_COUNT; i++) {
            BeeView beeView = new BeeView(this, BeeFactory.createBee(BeeType.WORKER));
            ll_worker_bees_container.addView(beeView);
            bees.add(beeView);
        }

        BeeView beeView = new BeeView(this, BeeFactory.createBee(BeeType.QUEEN));
        ll_container.addView(beeView);
        bees.add(beeView);
        return bees;
    }

    public void showGameView() {
        hideShowViews(ll_message, ll_container);
    }

    public void showMessage() {
        hideShowViews(ll_container, ll_message);
    }

    private void hideShowViews(View toHide, View toShow) {
        toHide.animate().cancel();
        toShow.animate().cancel();
        if (toShow.getAlpha() < 1) toShow.animate().alpha(1).setDuration(300).start();
        if (toHide.getAlpha() > 0) toHide.animate().alpha(0).setDuration(300).start();
    }

    public void gameEnded() {
        showMessage();
        countDownTimer = new CountDownTimer(3300, 1000) {

            public void onTick(long millisUntilFinished) {
                tv_count_down.setText(String.valueOf(millisUntilFinished / 1000));
            }

            public void onFinish() {
                showGameView();
                engine.restartGame();
            }
        }.start();
    }
}
