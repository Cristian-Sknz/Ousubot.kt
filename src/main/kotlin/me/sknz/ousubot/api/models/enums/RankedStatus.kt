package me.sknz.ousubot.api.models.enums

enum class RankedStatus(val id: Int, val label: String) {
    Graveyard(-2, "graveyard"),
    WIP(-1, "wip"),
    Pending(0, "pending"),
    Ranked(1, "ranked"),
    Approved(2, "approved"),
    Qualified(3, "qualified"),
    Loved(4, "loved");
}