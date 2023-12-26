package com.example.bankiapplication.data.localStorage

interface DeepLinkStorage {
    fun doRequest():Array<String>
    fun doWrite(deepLink:List<String>)
}
interface UniqueLinkStorage{
    fun doRequest():Array<String>
    fun doWrite(deepLink:List<String>)
    fun clear()
}