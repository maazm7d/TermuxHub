package com.maazm7d.termuxhub.data.mapper

import com.maazm7d.termuxhub.data.local.entities.HallOfFameEntity
import com.maazm7d.termuxhub.data.remote.dto.HallOfFameMemberDto
import com.maazm7d.termuxhub.domain.model.HallOfFameMember

fun HallOfFameMemberDto.toDomain(contribution: String) = HallOfFameMember(
    id = id,
    github = github,
    speciality = speciality,
    profileUrl = profile,
    contribution = contribution
)

fun HallOfFameMember.toEntity() = HallOfFameEntity(
    id = id,
    github = github,
    speciality = speciality,
    profileUrl = profileUrl,
    contribution = contribution
)

fun HallOfFameEntity.toDomain() = HallOfFameMember(
    id = id,
    github = github,
    speciality = speciality,
    profileUrl = profileUrl,
    contribution = contribution
)
