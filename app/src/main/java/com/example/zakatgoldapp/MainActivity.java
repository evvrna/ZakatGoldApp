package com.example.zakatgoldapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText etWeight, etValue;
    RadioButton rbKeep, rbWear;
    TextView tvTotalValue, tvZakatValue, tvTotalZakat;
    Button btnCalculate, btnReset;
    Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Zakat Gold Calculator");
        }

        etWeight = findViewById(R.id.etWeight);
        etValue = findViewById(R.id.etValue);
        rbKeep = findViewById(R.id.rbKeep);
        rbWear = findViewById(R.id.rbWear);
        tvTotalValue = findViewById(R.id.tvTotalValue);
        tvZakatValue = findViewById(R.id.tvZakatValue);
        tvTotalZakat = findViewById(R.id.tvTotalZakat);
        btnCalculate = findViewById(R.id.btnCalculate);
        btnReset = findViewById(R.id.btnReset);

        // Calculate button
        btnCalculate.setOnClickListener(v -> calculateZakat());

        // Reset button
        btnReset.setOnClickListener(v -> {
            etWeight.setText("");
            etValue.setText("");
            rbKeep.setChecked(false);
            rbWear.setChecked(false);
            tvTotalValue.setText("Total Value of Gold");
            tvZakatValue.setText("Zakat Payable Value");
            tvTotalZakat.setText("Total Zakat");
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.item_about) {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
            return true;

        } else if (id == R.id.item_share) {
            String shareText = "https://github.com/evvrnaS/ZakatGoldApp";

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            startActivity(Intent.createChooser(shareIntent, "Share via"));

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void calculateZakat() {
        String weightStr = etWeight.getText().toString().trim();
        String valueStr = etValue.getText().toString().trim();

        if (weightStr.isEmpty() || valueStr.isEmpty()) {
            Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        double weight, valuePerGram;
        try {
            weight = Double.parseDouble(weightStr);
            valuePerGram = Double.parseDouble(valueStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numbers!", Toast.LENGTH_SHORT).show();
            return;
        }

        String type = rbKeep.isChecked() ? "Keep" : "Wear";
        double nisab = type.equals("Keep") ? 85.0 : 200.0;

        // Net weight
        double netWeight = weight - nisab;
        String notice = "";
        if (netWeight <= 0) {
            netWeight = 0;
            notice = " (because " + weight + " − " + nisab + " ≤ 0)";
        }

        // Total value of gold
        double totalGoldValue = weight * valuePerGram;

        // Zakat payable value
        double zakatPayableValue = netWeight * valuePerGram;

        // Total zakat
        double totalZakat = zakatPayableValue * 0.025;

        // Display outputs
        tvTotalValue.setText(String.format("Total Value of Gold: RM %.2f", totalGoldValue));
        tvZakatValue.setText(String.format("Zakat Payable Value: RM %.2f%s", zakatPayableValue, notice));
        tvTotalZakat.setText(String.format("Total Zakat (2.5%%): RM %.2f", totalZakat));
    }
}
