package org.twinone.util;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import wewe.app.moboost.R;


public class UIView extends ListFragment {

    private Presenter presenter;

    public void setPresenter(Presenter p) {
        presenter = p;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.listfragment_main, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setPresenter(new Presenter(this));
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        presenter.listItemClicked(listView, view, position, id);
    }

    public void goBack(){
        presenter.backPressed();
    }




//
//    /* Populate options menu and or action bar with menu from res/menu/menu_main.xml*/
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.menu_main, menu);
//    }
//
//    //Called when an item in the menu, or the home button (if enabled) is selected.
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        switch(id) {
//            case android.R.id.home:
//                presenter.homePressed();
//                break;
//            case R.id.settings:
//                presenter.settings();
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}