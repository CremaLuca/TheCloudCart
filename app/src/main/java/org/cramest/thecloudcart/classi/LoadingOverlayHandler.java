package org.cramest.thecloudcart.classi;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.admin.SystemUpdatePolicy;
import android.view.View;

import org.cramest.thecloudcart.R;

/**
 * Created by cremaluca on 30/05/2017.
 */

public class LoadingOverlayHandler {

    public static void mostraLoading(Activity a){
        System.out.println("LoadingOverlayHandler - Caricamento");
        animateView(a.findViewById(R.id.progress_overlay), View.VISIBLE, 0.4f, 200);
    }

    public static void nascondiLoading(Activity a){
        System.out.println("LoadingOverlayHandler - Scaricamento");
        animateView(a.findViewById(R.id.progress_overlay), View.GONE, 0, 200);
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
        view.bringToFront();
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
