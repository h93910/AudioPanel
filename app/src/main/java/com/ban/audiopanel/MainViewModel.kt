package com.ban.audiopanel

import android.text.TextUtils
import android.util.ArrayMap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.*
import java.net.Socket

class MainViewModel : ViewModel() {
    private var socket: Socket? = null

    //MutableLiveData是LiveData这是可变 & 线程安全。 setValue() & postValue()
    private val _currentListen = MutableLiveData(0)
    val currentListen: LiveData<Int> = _currentListen //LiveData是不可变的
    private val _currentData = MutableLiveData(mutableListOf<Pair<String, Int>>())
    val currentData: LiveData<MutableList<Pair<String, Int>>> = _currentData //LiveData是不可变的

    init {
        initSocket()
    }

    override fun onCleared() {
        super.onCleared()
        socket?.apply {
            close()
            socket = null
        }
    }

    private fun initSocket() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                socket = Socket("127.0.0.1", 19930)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            while (socket != null) {
                socket?.apply {
                    val br = BufferedReader(InputStreamReader(getInputStream()))
                    if (!isClosed) {
                        val mess: String = br.readLine()
                        println("服务器：$mess")
                        if (mess == "start_listen") {
                            _currentListen.postValue((1..10).random())
                        } else if (mess.startsWith("{")) {//为json数据
                            loadJsonData(mess)
                        }
                        delay(50)
                        //不能close,一close　socket就断开了
                    }
                }
            }
        }
    }

    fun send(s: String) {
        if (TextUtils.isEmpty(s) || s == "。" || s == ".") {
            println("$s 不发送")
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            socket?.apply {
                //构建IO
                val os: OutputStream = getOutputStream()//这个不能关吧
                val bw = BufferedWriter(OutputStreamWriter(os))
                //向服务器端发送一条消息
                bw.write(s)
                bw.flush()
            }
        }
    }

    /**
     * 从python处使用率数据
     * @param d String
     */
    private fun loadJsonData(d: String) {
        val l =
            "{\"code\": \"rate\", \"data\": {\"OK\": 1, \"YES\": 0, \"\\u8c22\\u8c22\": 1, \"\\u542c\\u5230\\u5417\": 2, \"\\u52a0\\u6cb9\": 6, \"\\u5f88\\u5f3a\": 0, \"\\u4eca\\u5929\\u67aa\\u6cd5\\u6709\\u70b9\\u5f3a\": 0, \"\\u4e00\\u6837\": 1, \"\\u51b2\\u8d77\\u6765\": 0, \"\\u52a0\\u6211\\u4e00\\u4e0b\": 0, \"\\u80cc\\u540e\\u6765\\u4e86\": 0, \"\\u6211\\u53d1\\u67aa\": 1, \"\\u6211\\u53bb\\u54ce\": 0, \"\\u6211\\u53bbB\": 1, \"\\u6211\\u53bb\\u4e2d\": 0, \"\\u6211\\u65ad\\u540e\": 0, \"\\u6211\\u8001\\u516d\": 0, \"\\u5185\\u683c\\u592b\\u6218\\u795e\": 1, \"\\u94b1\\u538b\\u4e86\": 0, \"\\u4fdd\\u67aa\": 0, \"\\u4fdd\\u67aa\\u5c0f\\u738b\\u5b50\": 0, \"\\u6ca1\\u94b1\\u6cbb\": 0, \"ECO\": 0, \"\\u6253\\u4e00\\u534a\": 0, \"\\u5927\\u6b8b\": 1, \"\\u5361\": 0, \"\\u6211\\u9a6c\\u67aa\\u4e86\": 1, \"\\u53d1\\u628a\\u67aa\": 1, \"\\u53d1\\u628aP90\": 0, \"\\u53d1\\u4e00\\u628a\\u6b65\\u67aa\": 0, \"\\u53d1\\u4e00\\u628a\\u51b2\\u950b\": 0, \"\\u53d1\\u4e00\\u628a\\u55b7\\u5b50\": 0, \"\\u53d1\\u70b9\\u6295\\u63b7\\u7269\": 0, \"\\u770b\\u5305\": 2, \"\\u6253A\\u4e86\": 1, \"\\u53ef\\u80fd\\u6253A\": 1, \"A\\u5c0f\\u70fd\\u70df\\u4e86\": 0, \"A\\u5c0f\\u4e0a\\u4e86\": 1, \"A\\u5c0f\\u5f88\\u591a\": 0, \"A\\u5927\\u5f88\\u591a\": 0, \"\\u5728A\\u95e8\": 0, \"\\u5230A\\u5e73\\u53f0\\u4e86\": 0, \"\\u659c\\u5761\\u6709\": 0, \"L\\u4e3a\\u53cb\": 1, \"\\u8f66\\u4f4d\\u6709\": 0, \"\\u6253B\\u4e86\": 2, \"\\u53ef\\u80fd\\u6253B\": 1, \"\\u6253B\\u4e86\\u4e00\\u5806\\u4eba\": 1, \"B1\\u697c\\u6709\": 2, \"\\u72d9\\u51fb\\u4f4d\": 1, \"\\u7a97\\u53e3\\u4e0b\": 0, \"B\\u95e8\\u540e\\u6709\": 1, \"B2\\u697c\\u6709\": 0, \"B\\u4e00\\u5f88\\u591a\": 0, \"B2\\u5f88\\u591a\": 0, \"\\u4e2d\\u8def\\u5f88\\u591a\": 0, \"\\u4e2d\\u539f\\u5f88\\u591a\": 2, \"\\u4e2d\\u539f\\u6709\\u53e5\": 3, \"\\u4ed6\\u4eec\\u5bb6\\u6709\\u72d9\": 0, \"\\u5728\\u532a\\u5bb6\\u540e\\u82b1\\u56ed\": 0, \"\\u5728\\u4ed6\\u4eec\\u5bb6\": 0, \"\\u5728\\u6211\\u4eec\\u5bb6\": 0, \"\\u8b66\\u5bb6\\u8fc7\\u6765\\u4e86\": 0, \"\\u4e2d\\u95e8\\u51fa\\u4e86\": 0, \"\\u6253\\u4e2d\\u95e8\\u4e86\": 0, \"\\u5305\\u6389\\u4e86\": 1, \"\\u6253A\": 1, \"\\u6253B\": 0, \"RUSH B\": 1, \"\\u6211\\u4e2d\\u95e8\\u53bbB\": 1, \"\\u6211\\u5148\\u51b2\": 0, \"\\u6211\\u5c01A\\u5c0f\\u9609\\u4e86\": 1, \"\\u6211\\u7d27\\u5939\\u70df\\u6563\": 0, \"\\u84dd\\u8272\": 3, \"\\u9ec4\\u8272\": 1, \"\\u7d2b\\u8272\": 0, \"\\u6a59\\u8272\": 0, \"\\u7eff\\u8272\": 0, \"\\u4e00\\u4e2a\": 1, \"\\u4e24\\u4e2a\": 1, \"\\u4e09\\u4e2a\": 1, \"4\\u4e2a\": 1, \"5\\u4e2a\": 1, \"\\u8bf4\\u9519\": 1, \"\\u6211\\u53ea\\u62a5\\u70b9\": 0, \"\\u592a\\u83dc\\u4e86\\uff0c\\u4e0d\\u6562\\u8bf4\\u8bdd\": 1, \"\\u4e0d\\u5b58\\u5728\\u59b9\\u5b50\": 2, \"\\u770b\\u7a7f\": 0, \"\\u5e7b\\u89c9\": 1, \"\\u65e0\\u60c5\\u7eea\": 1, \"\\u5e73\\u65f6\\u4e0d\\u73a9\": 0}}"
        try {
            val j = Gson().fromJson(d, JsonObject::class.java)
            if (j.get("code").asString == "rate") {
                val data = j.getAsJsonObject("data")
                val result = ArrayMap<String, Int>()
                for (k in data.keySet()) {
                    result[k] = data.get(k).asInt
                }
                val list = result.toList()
                    .sortedWith(compareByDescending<Pair<String, Int>> { it.second })
                    .toMutableList()
                println(list)
//                val list = result.toSortedMap(compareBy<String>().thenByDescending { result[it] })
//                    .toList().toMutableList()//按使用次数倒序排
                _currentData.postValue(list)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _currentData.postValue(mutableListOf())
        }
    }
}