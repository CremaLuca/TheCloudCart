package org.cramest.thecloudcart.classi;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;

/**
 * Created by cremaluca on 30/05/2017.
 */

public class LoadingOverlayHandler {

    private View loadingOverlay;

    public LoadingOverlayHandler(View loadingOverlay){
        this.loadingOverlay = loadingOverlay;
    }

    public void mostraLoading(){
        animateView(loadingOverlay, View.VISIBLE, 0.4f, 200);
    }

    public void nascondiLoading(){
        animateView(loadingOverlay, View.GONE, 0, 200);
    }

    /**
     *  Metodo copiato da internet per animare il loading
     * @param view
     * @param toVisibility
     * @param toAlpha
     * @param duration
     */
    public static void animateView(final View view, final int toVisibility, float toAlpha, int duration) {
        boolean show = toVisibility == View.VISIBLE;
        if (show) {
            view.setAlpha(0);
        }
        view.setVisibility(View.VISIBLE);
        view.animate()
                .setDuration(duration)
                .alpha(show ? toAlpha : 0)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(toVisibility);
                    }
                });
    }

}
