package com.example.gomokuee.Domain

class Exceptions : Exception() {
    class PlayNotAllowedException(message: String) : Exception(message)
    class WrongPlayException(message: String) : Exception(message)
}