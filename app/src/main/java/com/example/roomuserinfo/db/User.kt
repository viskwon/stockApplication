package com.example.roomuserinfo.db

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
class User(
    var name: String,
    val age: Int =0
){
    @PrimaryKey(autoGenerate = true)
    var id: Int =0
}