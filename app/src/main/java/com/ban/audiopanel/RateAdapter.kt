package com.ban.audiopanel

import android.content.Context
import android.graphics.Color
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.hjq.shape.view.ShapeTextView

class RateAdapter(data: MutableList<Pair<String, Int>>) :
    BaseQuickAdapter<Pair<String, Int>, QuickViewHolder>(data) {
    override fun onCreateViewHolder(
        context: Context, parent: ViewGroup, viewType: Int
    ): QuickViewHolder {
        return QuickViewHolder(R.layout.item_lot, parent)
    }

    override fun onBindViewHolder(
        holder: QuickViewHolder, position: Int, item: Pair<String, Int>?
    ) {
        item ?: return

        holder.setText(R.id.root, item.first)

        val shape = holder.getView<ShapeTextView>(R.id.root)
        shape.shapeDrawableBuilder.solidColor = getRateColor(item.second)
        shape.shapeDrawableBuilder.buildBackgroundDrawable()//build了之后才生效
    }

    /**
     * 按使用率对按钮进行变色操作
     * @param c Int
     * @return Int
     */
    private fun getRateColor(c: Int): Int {
        var baseColor = intArrayOf(225, 225, 225)
        if (c <= 30) {//     红
            baseColor[0] += c
            baseColor[1] -= (7.5 * c).toInt()
            baseColor[2] -= (7.5 * c).toInt()
        } else if (c in 31..30 + 128) {//橙黄
            baseColor = intArrayOf(255, 0, 0)
            baseColor[1] += (c - 30) * 2
        } else if (c in 159..158 + 128) {//绿
            baseColor = intArrayOf(255, 255, 0)
            baseColor[0] -= (c - 158) * 2
        } else if (c in 287..286 + 64) {//青
            baseColor = intArrayOf(0, 255, 0)
            baseColor[2] += (c - 286) * 4
        } else if (c in 351..350 + 64) {//蓝
            baseColor = intArrayOf(0, 255, 255)
            baseColor[1] -= (c - 350) * 4
        } else if (c in 415..414 + 255) {//紫
            baseColor = intArrayOf(0, 0, 255)
            baseColor[0] += c - 414
        } else {
            baseColor = intArrayOf(240, 192, 255)
        }
        return Color.rgb(baseColor[0], baseColor[1], baseColor[2])
    }
}