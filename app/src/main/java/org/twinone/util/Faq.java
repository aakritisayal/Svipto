package org.twinone.util;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import wewe.app.moboost.R;

/**
 * Created by Android on 6/8/2016.
 */
public class Faq extends Activity {

    RelativeLayout back;
    RelativeLayout listItems0, listItems1, listItems2, listItems3, listItems4, listItems5, listItems6, listItems7, listItems8, listItems9;
    LinearLayout linearQues0,linearQues1, linearQues2, linearQues3, linearQues4, linearQues5, linearQues6, linearQues7, linearQues8, linearQues9;
    ToggleButton idToggle0,idToggle1, idToggle2, idToggle3, idToggle4, idToggle5, idToggle6, idToggle7, idToggle8, idToggle9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faq);

        back = (RelativeLayout) findViewById(R.id.back);

        listItems0 =(RelativeLayout) findViewById(R.id.listItems0);
        listItems1 = (RelativeLayout) findViewById(R.id.listItems1);
        listItems2 = (RelativeLayout) findViewById(R.id.listItems2);
        listItems3 = (RelativeLayout) findViewById(R.id.listItems3);
        listItems4 = (RelativeLayout) findViewById(R.id.listItems4);
        listItems5 = (RelativeLayout) findViewById(R.id.listItems5);
        listItems6 = (RelativeLayout) findViewById(R.id.listItems6);
        listItems7 = (RelativeLayout) findViewById(R.id.listItems7);
        listItems8 = (RelativeLayout) findViewById(R.id.listItems8);
        listItems9 = (RelativeLayout) findViewById(R.id.listItems9);

        linearQues0 = (LinearLayout) findViewById(R.id.linearQues0);
        linearQues1 = (LinearLayout) findViewById(R.id.linearQues1);
        linearQues2 = (LinearLayout) findViewById(R.id.linearQues2);
        linearQues3 = (LinearLayout) findViewById(R.id.linearQues3);
        linearQues4 = (LinearLayout) findViewById(R.id.linearQues4);
        linearQues5 = (LinearLayout) findViewById(R.id.linearQues5);
        linearQues6 = (LinearLayout) findViewById(R.id.linearQues6);
        linearQues7 = (LinearLayout) findViewById(R.id.linearQues7);
        linearQues8 = (LinearLayout) findViewById(R.id.linearQues8);
        linearQues9 = (LinearLayout) findViewById(R.id.linearQues9);

        idToggle0 =(ToggleButton) findViewById(R.id.idToggle0);
        idToggle1 =(ToggleButton) findViewById(R.id.idToggle1);
        idToggle2 =(ToggleButton) findViewById(R.id.idToggle2);
        idToggle3 =(ToggleButton) findViewById(R.id.idToggle3);
        idToggle4 =(ToggleButton) findViewById(R.id.idToggle4);
        idToggle5 =(ToggleButton) findViewById(R.id.idToggle5);
        idToggle6 =(ToggleButton) findViewById(R.id.idToggle6);
        idToggle7 =(ToggleButton) findViewById(R.id.idToggle7);
        idToggle8 =(ToggleButton) findViewById(R.id.idToggle8);
        idToggle9 =(ToggleButton) findViewById(R.id.idToggle9);


        listItems0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linearQues0.getVisibility() == View.VISIBLE) {
                    linearQues0.setVisibility(View.GONE);
                    idToggle0.setChecked(false);
                } else if (linearQues0.getVisibility() != View.VISIBLE) {
                    linearQues0.setVisibility(View.VISIBLE);
                    idToggle0.setChecked(true);
                }

            }
        });


        listItems1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linearQues1.getVisibility() == View.VISIBLE) {
                    linearQues1.setVisibility(View.GONE);
                    idToggle1.setChecked(false);
                } else if (linearQues1.getVisibility() != View.VISIBLE) {
                    linearQues1.setVisibility(View.VISIBLE);
                    idToggle1.setChecked(true);
                }

            }
        });

        listItems2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linearQues2.getVisibility() == View.VISIBLE) {
                    linearQues2.setVisibility(View.GONE);
                    idToggle2.setChecked(false);
                } else if (linearQues2.getVisibility() != View.VISIBLE) {
                    linearQues2.setVisibility(View.VISIBLE);
                    idToggle2.setChecked(true);
                }

            }
        });

        listItems3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linearQues3.getVisibility() == View.VISIBLE) {
                    linearQues3.setVisibility(View.GONE);
                    idToggle3.setChecked(false);
                } else if (linearQues3.getVisibility() != View.VISIBLE) {
                    linearQues3.setVisibility(View.VISIBLE);
                    idToggle3.setChecked(true);
                }

            }
        });

        listItems4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linearQues4.getVisibility() == View.VISIBLE) {
                    linearQues4.setVisibility(View.GONE);
                    idToggle4.setChecked(false);
                } else if (linearQues4.getVisibility() != View.VISIBLE) {
                    linearQues4.setVisibility(View.VISIBLE);
                    idToggle4.setChecked(true);
                }

            }
        });


        listItems5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linearQues5.getVisibility() == View.VISIBLE) {
                    linearQues5.setVisibility(View.GONE);
                    idToggle5.setChecked(false);
                } else if (linearQues5.getVisibility() != View.VISIBLE) {
                    linearQues5.setVisibility(View.VISIBLE);
                    idToggle5.setChecked(true);
                }

            }
        });


        listItems6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linearQues6.getVisibility() == View.VISIBLE) {
                    linearQues6.setVisibility(View.GONE);
                    idToggle6.setChecked(false);
                } else if (linearQues6.getVisibility() != View.VISIBLE) {
                    linearQues6.setVisibility(View.VISIBLE);
                    idToggle6.setChecked(true);
                }

            }
        });

        listItems7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linearQues7.getVisibility() == View.VISIBLE) {
                    linearQues7.setVisibility(View.GONE);
                    idToggle7.setChecked(false);
                } else if (linearQues7.getVisibility() != View.VISIBLE) {
                    linearQues7.setVisibility(View.VISIBLE);
                    idToggle7.setChecked(true);
                }

            }
        });

        listItems8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linearQues8.getVisibility() == View.VISIBLE) {
                    linearQues8.setVisibility(View.GONE);
                    idToggle8.setChecked(false);
                } else if (linearQues8.getVisibility() != View.VISIBLE) {
                    linearQues8.setVisibility(View.VISIBLE);
                    idToggle8.setChecked(true);
                }

            }
        });

        listItems9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linearQues9.getVisibility() == View.VISIBLE) {
                    linearQues9.setVisibility(View.GONE);
                    idToggle9.setChecked(false);
                } else if (linearQues9.getVisibility() != View.VISIBLE) {
                    linearQues9.setVisibility(View.VISIBLE);
                    idToggle9.setChecked(true);
                }

            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
