package com.example.roomuserinfo.repository

import android.util.Log
import com.example.roomuserinfo.db.User
import com.example.roomuserinfo.db.dao.UserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import java.io.IOException

class UserRepository(private val userDao: UserDao) {
    val allUsers = userDao.getUserFlow().map { value ->
        Log.d("observe", "repo")
        value.last().name.length
    }

    suspend fun insertUser(user: User) {

        userDao.insert(user)
    }


    fun refresh() = flow {
        kotlin.runCatching {
            Log.d("hjskwon", "1")
            val users = getInfos()
            var mainUser: User? = null
            val otherUser: MutableList<User> = ArrayList()

            users.firstOrNull()?.forEach {
                if (it.id == 1) mainUser = it
                else otherUser.add(it)
            }

            if (mainUser == null && otherUser.isEmpty()) {
                emit(Result.success(otherUser.toList()))
                return@flow
            }
            coroutineScope {
                val mainUserDeffered = mainUser?.run {
                    async {
                        val temp = getMainUser(this@run)
                        Log.d("hjskwon", "2 ${temp.first().isSuccess}")
                        if(temp.first().isFailure){
                            throw temp.first().exceptionOrNull()!!

                        }

                        temp.first().getOrNull()
                    }

                }


                val otherUserDeffered = async {
                    Log.d("hjskwon", "3")

                    fetchNSaveUsers(otherUser).first().getOrNull()
                }

                val main = mainUserDeffered?.await()
                Log.d("hjskwon", "main $main")
                val other = otherUserDeffered.await()
                val value = main?.run {
                    other?.add(this)
                    other?.toList()
                } ?: other?.toList() ?: listOf()
                Log.d("hjskwon", "4")

                emit(Result.success(value))
            }

        }.onFailure { emit(Result.failure(it)) }
        //combine and return

    }.flowOn(Dispatchers.IO)

    fun getInfos(): Flow<List<User>> {
        return userDao.getUserFlow()
    }

    fun getMainUser(mainUser: User) = flow {
        Log.d("hjskwon", "userrepo")
        emit(kotlin.runCatching {
            Log.d("hjskwon", "delay")
            throw IOException()
            delay(3000)
            mainUser.apply {
                name = "changed"
            }
        })

    }.flowOn(Dispatchers.IO)

    fun fetchNSaveUsers(list: List<User>) = flow {
        emit(kotlin.runCatching {
            delay(2000)
            list.toMutableList().apply {
                add(User("custom", 30))
            }
        })

    }
}