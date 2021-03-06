package plakaapp.plakaapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Simulakra on 10.12.2017.
 */

public class tab_sikayet extends Activity {

    JSONArray sikayetler;
    boolean sPulled;
    int sPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_sikayet);

        SikayetDoldur();
        CreateButtonListener();
    }

    private HashMap<String, String> createListItem(String name, String number){
        HashMap<String, String> employeeNameNo = new HashMap<String, String>();
        employeeNameNo.put(name, number);
        return employeeNameNo;
    }

    private void CreateButtonListener() {
        Button btn;

        btn= (Button) findViewById(R.id.s_bt_ban);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                try {
                                    Log.d("ban", new JSONtask().execute(Config.Sikayetban(
                                            sikayetler.getJSONObject(sPosition).getJSONObject("message").getString("ID"),"-250"
                                    )).get());
                                    Toast.makeText(tab_sikayet.this, "Kullanıcı Banlandı", Toast.LENGTH_LONG).show();
                                    SikayetSil();
                                } catch (Exception e) {
                                }

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(tab_sikayet.this);
                builder.setMessage("Kullanıcıyı banlamak istediğinize emin misiniz?\n(Rep değeri -250 olacaktır)")
                        .setPositiveButton("Evet", dialogClickListener)
                        .setNegativeButton("Hayır", dialogClickListener).show();
            }});

        btn= (Button) findViewById(R.id.s_bt_dusus);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                try {
                                   Log.d("engel", new JSONtask().execute(Config.Sikayetban(
                                           sikayetler.getJSONObject(sPosition).getJSONObject("message").getString("ID"),"-150"
                                   )).get());
                                    Toast.makeText(tab_sikayet.this, "Kullanıcı engellendi", Toast.LENGTH_LONG).show();
                                    SikayetSil();
                                } catch (Exception e) {
                                    Log.d("EngelError",e.toString());
                                }

                                Log.d("deneme?","");
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(tab_sikayet.this);
                builder.setMessage("Kullanıcıyı engellemek istediğinize emin misiniz?\n(Rep değeri -150 olacaktır)")
                        .setPositiveButton("Evet", dialogClickListener)
                        .setNegativeButton("Hayır", dialogClickListener).show();
            }});

        btn= (Button) findViewById(R.id.s_bt_sil);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                try {
                                    Toast.makeText(tab_sikayet.this, "Şikayet Silindi", Toast.LENGTH_LONG).show();
                                    SikayetSil();
                                } catch (Exception e) {
                                }

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(tab_sikayet.this);
                builder.setMessage("Şikayeti silmek istediğinize emin misiniz?")
                        .setPositiveButton("Evet", dialogClickListener)
                        .setNegativeButton("Hayır", dialogClickListener).show();
            }});
        }

    private void SikayetSil() {

        try {
            Log.d("SikayetSil", new JSONtask().execute(Config.SikayetSil(sikayetler.getJSONObject(sPosition).getJSONObject("message")
                    .getString("ID"))).get());
            SikayetDoldur();
        }catch (Exception e){}
    }

    private void SikayetDoldur() {
        final ListView listemiz = (ListView) findViewById(R.id.s_listview);
        List<Map<String, String>> itemList = new ArrayList<Map<String, String>>();
        try{
            sikayetler=new JSONArray(new JSONtask().execute(Config.SiLISTELE_URL).get());


            for (int i = 0; i < sikayetler.length(); i++) {
                JSONObject jsTemp = sikayetler.getJSONObject(i).getJSONObject("message");

                String goster="Şikayet ID'si: "+ jsTemp.getString("ID");
                goster += "\nYazı ID'si: "+jsTemp.getString("YaziID");
                String Tarih=jsTemp.getString("Taih");
                Tarih = Tarih.substring(0,Tarih.indexOf('T'));//+" "+Tarih.substring(Tarih.indexOf('T')+1,Tarih.indexOf(".000Z"));
                goster += "\nTarih: " + Tarih;
                itemList.add(createListItem("Üyeler", goster));
            }
            SimpleAdapter simpleAdapter = new SimpleAdapter(tab_sikayet.this, itemList, android.R.layout.simple_list_item_1, new String[]{"Üyeler"}, new int[]{android.R.id.text1});
            listemiz.setAdapter(simpleAdapter);

        }catch (Exception e){
            SimpleAdapter simpleAdapter = new SimpleAdapter(tab_sikayet.this, itemList, android.R.layout.simple_list_item_1, new String[]{"Üyeler"}, new int[]{android.R.id.text1});
            listemiz.setAdapter(simpleAdapter);
        }
        ((TextView)findViewById(R.id.s_yazi)).setText("");
        sPulled=false;
        sPosition=-1;
        listemiz.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                try {
                    sPulled=true;
                    sPosition=position;
                    ((TextView)findViewById(R.id.s_yazi))
                            .setText(sikayetler.getJSONObject(sPosition).getJSONObject("message")
                                    .getString("Nedeni"));

                } catch (Exception e) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(tab_sikayet.this);
                    builder.setMessage(e.getMessage())
                            .setNegativeButton("Tamam", null)
                            .create()
                            .show();
                }
            }
        });
    }
}
