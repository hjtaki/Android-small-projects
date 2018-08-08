package ca.hjtaki.dailygoals;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Currency;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class main1 extends AppCompatActivity implements Callback<CurrencyExchange>, CurrencyItemClickListener {

    private ListView lvCurrency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvCurrency = findViewById(R.id.lvCurrency);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadCurrenctExchangeData();
    }

    private void loadCurrenctExchangeData(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://data.fixer.io/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CurrencyExchangeService service = retrofit.create(CurrencyExchangeService.class);
        Call<CurrencyExchange> call =  service.loadCurrencyExchange();
        call.enqueue(this);

    }

    @Override
    public void onResponse(Call<CurrencyExchange> call, Response<CurrencyExchange> response) {
//        Toast.makeText(this, response.body().getBase(), Toast.LENGTH_LONG).show();
        CurrencyExchange currencyExchange = response.body();
        lvCurrency.setAdapter(new CurrencyAdapter(this, currencyExchange.getCurrencyList(), this));

    }

    @Override
    public void onFailure(Call<CurrencyExchange> call, Throwable t) {
        Toast.makeText(this, t.getMessage(), Toast.LENGTH_LONG).show();

    }

    @Override
    public void onCurrencyItemClick(ca.hjtaki.dailygoals.Currency c) {
        Toast.makeText(this, c.getName(), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this,SecondActivity.class);
        intent.putExtra("currency_name", c.getName());
        intent.putExtra("currency_rate", c.getRate());

        startActivity(intent);

    }
}
