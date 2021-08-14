package com.example.roomuserinfo

import com.example.roomuserinfo.db.User
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonReader
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import com.squareup.moshi.internal.NullSafeJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {

        val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .add(NULL_TO_EMPTY_STRING_ADAPTER)
                .build()
      val adapter =  moshi.adapter(User::class.java)

       val user  = User("d",1)
      println(adapter.toJson(user))
       val test =  adapter.fromJson("{\"name\":null,\"age\":1,\"id\":0}")
        println(adapter.toJson(test))
    }
}
object NULL_TO_EMPTY_STRING_ADAPTER {
    @FromJson
    fun fromJson(reader: JsonReader): String {
        if (reader.peek() != JsonReader.Token.NULL) {
            return reader.nextString()
        }
        reader.nextNull<Unit>()
        return ""
    }
}
