package com.maazm7d.termuxhub.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hall_of_fame")
data class HallOfFameEntity(
    @PrimaryKey val id: String,
    val github: String,
    val speciality: String,
    val profileUrl: String,
    val contribution: String
)
