package com.example.cryptoapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.R
import com.example.cryptoapp.pojo.CoinInfo
import com.example.cryptoapp.pojo.CoinPriceInfo
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_coin_info.view.*

class CoinInfoAdapter(private val context:Context):RecyclerView.Adapter<CoinInfoAdapter.CoinInfoViewHolder>() {
    var coinInfoList: List<CoinPriceInfo> = listOf()

    set(value) {
        field=value
        notifyDataSetChanged()
    }
    var onCoinClickListener: OnCoinClickListener?=null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinInfoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_coin_info,parent,false)
        return CoinInfoViewHolder(view)
    }

    override fun onBindViewHolder(holder: CoinInfoViewHolder, position: Int) {
        val coin = coinInfoList[position]
        with(holder) {
            with(coin) {
            val symbolsTemplate=context.resources.getString(R.string.symbols_template)
            val lastUpdateTemplate=context.resources.getString(R.string.last_update_template)
            tvSymbols.text = String.format(symbolsTemplate,fromsymbol,tosymbol)
            tvPrice.text = price.toString()
            tvLastRefresh.text = String.format(lastUpdateTemplate,getFormattedTime())
            Picasso.get().load(getFullImageUrl()).into(ivLogoCoin)
        }
        }
        holder.itemView.setOnClickListener{
            onCoinClickListener?.onCoinClick(coin)
        }
    }

    override fun getItemCount() = coinInfoList.size

    inner class CoinInfoViewHolder(itemView:View): RecyclerView.ViewHolder(itemView){
         val ivLogoCoin: ImageView =itemView.ivLogoCoin
         val tvLastRefresh: TextView =itemView.tvLastRefresh
         val tvPrice: TextView =itemView.tvPrice
         val tvSymbols: TextView =itemView.tvSymbols
     }
    interface OnCoinClickListener {
        fun onCoinClick(coinPriceInfo: CoinPriceInfo)
    }
}