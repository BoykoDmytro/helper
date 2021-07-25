package com.bo.helper.common.navigation

sealed class Command {
    object Back : Command()
    object Camera : Command()
    object Video : Command()
    object Files : Command()
    class Navigate(val data: Class<*>) : Command()
}