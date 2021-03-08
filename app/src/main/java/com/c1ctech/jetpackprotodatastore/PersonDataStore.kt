package com.c1ctech.jetpackprotodatastore

import android.content.Context
import androidx.datastore.CorruptionException
import androidx.datastore.DataStore
import androidx.datastore.Serializer
import androidx.datastore.createDataStore
import com.google.protobuf.InvalidProtocolBufferException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.InputStream
import java.io.OutputStream

class PersonDataStore(context: Context) {
    private val dataStore: DataStore<Person>

    init {
        dataStore = context.createDataStore(
            fileName = "data.pb",
            serializer = MySerializer
        )
    }

    val user: Flow<Person?> = dataStore.data
        .map {
            it
        }

    suspend fun storeData(age: Int, fname: String, lname: String, isMale: Boolean) {
        dataStore.updateData { preference ->

            preference.toBuilder().setAge(age).setFirstName(fname).setLastName(lname)
                .setGender(isMale).build()
        }
    }
}

object MySerializer : Serializer<Person> {

    override fun readFrom(input: InputStream): Person {
        try {
            return Person.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override fun writeTo(t: Person, output: OutputStream) {
        t.writeTo(output)
    }

}
