package com.example.cryptoapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_coin_detail.*
import kotlinx.android.synthetic.main.item_coin_info.*
import kotlinx.android.synthetic.main.item_coin_info.tvPrice

class CoinDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coin_detail)
        if (!intent.hasExtra(EXTRA_FROM_SYMBOL)) {
            finish()
            return
        }
        val fromSymbol = intent.getStringExtra(EXTRA_FROM_SYMBOL)
        var viewModel:CoinViewModel = ViewModelProvider(this).get(CoinViewModel::class.java)
        fromSymbol?.let {
            viewModel.getDetailInfo(it).observe(this, Observer {
                tvPrice.text=it.price.toString()
                tvMin.text=it.lowday.toString()
                tvMax.text=it.highday.toString()
                tvLastContract.text=it.lastmarket
                tvRefresh.text=it.getFormattedTime()
                tvFromSymbol.text=it.fromsymbol
                tvToSymbol.text=it.tosymbol
                Picasso.get().load(it.getFullImageUrl()).into(ivLogoCoinDetail)
          //      Log.d("DETAIL_INFO",it.toString())
            })
        }
    }
    companion object {
       private const val EXTRA_FROM_SYMBOL="fSym"
        fun newIntent(context:Context, fromSymbol:String):Intent {
            val intent=Intent(context,CoinDetailActivity::class.java)
            intent.putExtra(EXTRA_FROM_SYMBOL,fromSymbol)
            return intent
        }
    }
}