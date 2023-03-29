package kwok.man.sang.temperatureconversion;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btn_input;
    private EditText et_input, et_cel, et_fah, et_kel;
    private Spinner spinner_unit;

    private String[] unitArray;
    private int unit_index;
    private String input_val;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadPreferences();
    }

    private void init() {
        btn_input = (Button) findViewById(R.id.btn_calc);
        et_input = (EditText) findViewById(R.id.et_input);
        et_cel = (EditText) findViewById(R.id.et_cel);
        et_fah = (EditText) findViewById(R.id.et_fah);
        et_kel = (EditText) findViewById(R.id.et_kel);
        spinner_unit = (Spinner) findViewById(R.id.spinner_unit);

        unitArray = getResources().getStringArray(R.array.unit);

        ArrayAdapter<String> adapterUnit = new ArrayAdapter<String>(this, R.layout.dropdown_item, unitArray);

        spinner_unit.setAdapter(adapterUnit);

        btn_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input_val = et_input.getText().toString();
                savePreferences(unit_index, input_val);
                if (input_val.equals("")) {
                    Toast.makeText(MainActivity.this, R.string.warning, Toast.LENGTH_SHORT).show();
                } else {
                    input_val = et_input.getText().toString();
                    findOutput();
                }
            }
        });

        spinner_unit.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                unit_index = arg0.getSelectedItemPosition();
                input_val = et_input.getText().toString();
                savePreferences(unit_index, input_val);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void findOutput() {
        double input = 0.0;
        double cel = 0.0, fah = 0.0, kel = 0.0;
        String et_str = et_input.getText().toString();
        if (et_str == null) {
            return;
        }
        try {
            input = Double.parseDouble(et_str);
        } catch (NumberFormatException nfe) {
            return;
        }
        switch (unit_index) {
            case 0:
                cel = input;
                fah = 9.0 / 5.0 * input + 32;
                kel = cel + 273.15;
                break;
            case 1:
                fah = input;
                cel = (fah - 32) * (5.0 / 9.0);
                kel = cel + 273.15;
                break;
            case 2:
                kel = input;
                cel = kel - 273.15;
                fah = (9.0 / 5.0) * cel + 32;
                break;
        }
        et_cel.setText(String.format("%.2f", cel));
        et_fah.setText(String.format("%.2f", fah));
        et_kel.setText(String.format("%.2f", kel));
    }

    public void savePreferences(int index, String input) {
        SharedPreferences pref = getSharedPreferences("Temperature_Conversion", MODE_PRIVATE);
        pref.edit().putInt("index", index).commit();
        pref.edit().putString("input", input).commit();
    }

    public void loadPreferences() {
        SharedPreferences pref = getSharedPreferences("Temperature_Conversion", MODE_PRIVATE);
        spinner_unit.setSelection(pref.getInt("index", 0));
        et_input.setText(pref.getString("input", ""));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_about:
                openOptionsDialog();
                return true;
            case R.id.menu_exit:
                finish();
                return true;
        }
        return false;
    }

    public void openOptionsDialog() {
        new AlertDialog.Builder(MainActivity.this)
            .setTitle(R.string.menu_about)
            .setMessage(R.string.about_msg)
            .setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(
                            DialogInterface dialoginterface, int i) {
                    }
                }).show();
    }
}