package android.lectcoding.ui.base;

/**
 * Create by LingYan on 2016-10-20
 */

public interface BaseView extends BaseContract.View {
    public void shortToast(String msg);

    public void showProgress();

    public void hideProgress();

    public void showEmpty();

    public void showContent();
}
