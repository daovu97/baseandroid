package com.example.hokenbox.data.network

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties

@Keep
data class Response<T>(
    @SerializedName("message")
    var message: String?,

    @SerializedName("status_code")
    var status_code: Int?,

    @SerializedName("data")
    var data: T?
)

data class BaseResponse (val code: String?)

//extension
fun <K, V> Map<K, V?>.filterNotNullValues(): Map<K, V> =
    mapNotNull { (key, value) -> value?.let { key to it } }.toMap()

fun <K, V> Map<K, V>.toHashMap(): HashMap<K, V> = HashMap(this)

fun <T : Any> toMap(obj: T): Map<String, Any?> {
    return (obj::class as KClass<T>).memberProperties.associate { prop ->
        prop.name to prop.get(obj)?.let { value ->
            if (value::class.isData) {
                toMap(value)
            } else {
                value
            }
        }
    }
}